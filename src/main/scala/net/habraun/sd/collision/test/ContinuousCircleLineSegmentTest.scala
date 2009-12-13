/*
	Copyright (c) 2009 Hanno Braun <hanno@habraun.net>

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/



package net.habraun.sd.collision.test



import math.Scalar._
import math.Vector2
import math.ZeroVector
import shape.Circle
import shape.Contact
import shape.LineSegment



/**
 * An implementation of the circle - line segment test that performs continuous collision detection.
 */

class ContinuousCircleLineSegmentTest extends CircleLineSegmentTest {

	/**
	 * Performs continuous collision detection between a circle and a line segment.
	 */

	def apply( circle: Circle, lineSegment: LineSegment ): Option[ Contact ] = {

		// This algorithm performs collision detection between moving circles and line segments. I assembled this algorithm by shamelessly
		// stealing parts of several algorithms from Christer Ericson's book "Real-Time Collision Detection" and combining them to solve
		// the problem at hand. The following parts of the book where integral in creating this algorithm:
		// * A first attempt at implementing this test was based on "Intersecting Moving Sphere Against Plane", pages 219-222. Some parts
		//   made their way into this current version.
		// * The second attempt was mainly based on "Closest Poins of Two Line Segments", pages 148-151.
		// * "Closest Point on Line Segment to Point", pages 127-130 may have also had some influence.

		// Let's save the shape attributes into short variables, so we have them handy for the long equations.
		val pc = circle.previousPosition
		val rc = circle.radius
		val pls = lineSegment.previousPosition
		val dls = lineSegment.direction

		// We'll also need the velocities of the shapes and their relative velocity. We always treat the circle as moving and the line
		// segment as standing still.
		val vc = ( circle.position - circle.previousPosition )
		val vls = ( lineSegment.position - lineSegment.previousPosition )
		val v =  vc - vls

		// The general approach here is to sort out all the edge cases first. If none of them applies, we can compute the contact with the
		// most general formula.
		// First step: Does the circle intersect the line segment at the beginning of the movement?


		// The vector that points from the line segment's position (one of the endpoints) to the circle center.
		val lsPositionToCircleCenter = pc - pls

		// The vector that points from the line segment position to the nearest point between the circle center and the line defined by the
		// line segment.
		val lsPositionToNearestPointOnLine = lsPositionToCircleCenter.projectOn( dls )

		// The factor that needs to applied to the line segment's direction in order to get the vector that points from the beginning of
		// the line segment to the nearest point between the line defined by the segment and the circle center.
		val s = dls.computeFactorFor( lsPositionToNearestPointOnLine )

		// We're interested in the closest point to the circle center that lies on the line segment. The line segments position, direction
		// and the factor s already give us the closest point on the line. We just have to trim it to the line segment.
		val closestPointOnSegment = trimPointToLineSegment( pls, dls, s )

		// Now we can determine if the circle and the line segment overlap. To do this, we first need to compute the vector that points
		// from the circle center to the closest point on the segment and its length (the distance between the circle center and the
		// segment).
		val circleCenterToClosestPointOnLs = closestPointOnSegment - pc
		val lsDistanceToCircleCenter = ( circleCenterToClosestPointOnLs ).length

		// So, do the circle and the line segment overlap initially?
		if ( lsDistanceToCircleCenter <= rc ) {
			// Yes they do. We define the point of contact as the nearest point we computed before. The rest is pretty obvious from there
			// on.
			val point = closestPointOnSegment
			val normal = circleCenterToClosestPointOnLs.normalize
			val depth = circle.radius - lsDistanceToCircleCenter
			val t = 0.0

			Some ( Contact( circle, lineSegment, point, normal, depth, t ) )
		}
		else if ( v == ZeroVector ) {
			// No, they don't. They also don't move relative to each other. This means there can be no contact.
			None
		}
		else if ( !v.isLinearlyIndependentFrom( dls ) ) {
			// They don't intersect intially and they they move, but it's a special kind of movement: The relative movement of the circle
			// is parallel to the direction of the line segment.

			// Check which endpoint is the nearest to the initial position of the circle center.
			val ep1 = pls
			val ep2 = pls + dls
			val ep = if ( ( pc - ep1 ).squaredLength < ( pc - ep2 ).squaredLength ) ep1 else ep2

			// Compute the t for the moment the distance from the nearest endpoint is equal to the radius. This can be done by solving the
			// equation "| pc + t * v - ep | = rc".
			val a = v.x * v.x + v.y * v.y
			val b = 2 * pc.x * v.x + 2 * pc.y * v.y - 2 * v.x * ep.x - 2 * v.y * ep.y
			val c = pc.x * pc.x + pc.y * pc.y + ep.x * ep.x + ep.y * ep.y - 2 * pc.x * ep.x - 2 * pc.y * ep.y - rc * rc
			val x = solveQuadraticEquation( a, b, c )
			if ( x == Nil ) {
				// No real solution, which means that the circle never reaches the endpoint.
				None
			}
			else {
				// One or two real solutions. Doesn't matter, we're only interested in the earlier time anyway, which is the smaller
				// solution.
				val t = x( 0 )

				// That there's a solution doesn't mean they collide within the timeframe we're looking at. The circle might not move far
				// enough or even in the wrong direction.
				if ( t >= 0 && t <= 1.0 ) {
					// Looks like they do collide. All that's left to do is to compute the contact attributes.
					val normal = ( ep + t * vls - ( pc + t * vc ) ).normalize
					val point = pc + t * vc + normal * rc
					val depth = ( ( 1.0 - t ) * v ).length

					Some( Contact( circle, lineSegment, point, normal, depth, t ) )
				}
				else {
					// No collision within the observed timeframe.
					None
				}
			}
		}
		else {
			// They don't overlap initially, but they move! This makes things interesting, since we now have to determine if the shapes
			// come in contact during the movement.
			// For doing that, we represent the relative movement of the circle center as a line segment and check if there's a point where
			// the distance of that virtual line segment and the actual one equals the circle radius.

			// Let's compute the distance between the line that's defined by the line segment and the circle center. We'll need it for the
			// computation ahead.
			val circleCenterDistanceFromLine = {
				// We need a normal and a distance from the origin, which together define the line on which the line segments lies.
				// The normal is one of two possible line normals. The distance is the distance from the origin in units of the normal, which
				// basically means that the distance is negative if the normal points towards the origin, positive if the normal points away
				// from the origin.
				val lineNormal = dls.orthogonal.normalize
				val lineDistanceFromOrigin = pls * lineNormal

				Math.abs( lineNormal * pc - lineDistanceFromOrigin )
			}

			// Now we'll compute the distance vector, which is orthogonal to the line defined by the line segment and points from the point
			// where the circle would touch the line to the center of the line segment. If the circle is already touching the line, the
			// distance vector will just point from the closest point on the line to the circle center.
			val dist = {
				val length = Math.min( rc, Math.abs( circleCenterDistanceFromLine ) )
				val lineOrthogonal = dls.orthogonal.normalize * length
				if ( lineOrthogonal * ( pc - pls ) >= 0 ) lineOrthogonal else -lineOrthogonal
			}

			// The variables that describe where circle center is when it touches the line (t) and where the circle touches the line
			// (s) can be computed from the equation "pls + s * ls.d + dist = pc + t * v".
			val s = ( v.x * ( pls.y - pc.y + dist.y ) - v.y * ( pls.x - pc.x + dist.x ) ) / ( dls.x * v.y - v.x * dls.y )
			val t = ( dls.x * ( pc.y - pls.y - dist.y ) - dls.y * ( pc.x - pls.x - dist.x ) ) / ( v.x * dls.y - dls.x * v.y )

			// Let's take a closer look at the variables we computed.
			if ( t < 0  || t > 1 ) {
				// If t is not between 0 and 1 (inclusive) the circle won't touch the line within the timeframe we're looking at. If it
				// won't touch the line, it also won't touch the line segment that lies on it.
				None
			}
			else if ( s < 0 || s > 1 ) {
				// If s is not between zero and one (inclusive), the circle won't touch the line where the line segment lies. However,
				// it may still touch the line segment at one of its endpoints, if it moves further.
				val p = if ( s < 0 ) pls else pls + dls

				// The time of contact can be computed using a quadratic euqation.
				val a = v.x * v.x + v.y * v.y
				val b = 2 * v.x * pc.x + 2 * v.y * pc.y - 2 * v.x * p.x - 2 * v.y * p.y
				val c = pc.x * pc.x + pc.y * pc.y + p.x * p.x + p.y * p.y - 2 * pc.x * p.x - 2 * pc.y * p.y - rc * rc
				val x = solveQuadraticEquation( a, b, c )
				if (  x == Nil ) {
					// No real solution.
					None
				}
				else {
					// One or two real solution. Doesn't matter, we're only interested in the earlier time anyway.
					val tls = x( 0 )
					val normal = ( p + tls * vls - ( pc + tls * vc ) ).normalize
					val point = pc + tls * vc + normal * rc

					Some( Contact( circle, lineSegment, point, normal, 0, tls ) )
				}
			}
			else {
				// The cirlce touches the line segment within the observed timeframe.
				val point = pc + vc * t - dist
				val normal = -dist.normalize
				val depth = ( v.projectOn( normal ) * ( 1.0 - t ) ).length

				Some( Contact( circle, lineSegment, point, normal, depth, t ) )
			}

		}
	}



	/**
	 * Given a position vector p and a direction vector d, which define a line and a line segment, and a Double s, which define a point p
	 * on the line according to the equation "p(s) = p + s * d", this method will return the position vector of the point on the line
	 * segment that is closest to p.
	 */

	private def trimPointToLineSegment( p: Vector2, d: Vector2, s: Double ) = {
		// Examine s.
		if ( s < 0 ) {
			// s is smaller than zero, which means the nearest point on the line lies in front of the line segment. This makes the
			// beginning of the line segment the nearest point on the segment.
			p
		}
		else if ( s > 1 ) {
			// s is larger than one, which means the nearest point on the line lies behind the line segment. This makes the end of the line
			// segment the nearest point on the segment.
			p + d
		}
		else {
			// s is in between zero and one (inclusive), which means the nearest point on the line lies on the line segment itself. The
			// position vector is easily computed.
			p + s * d
		}
	}



	private def solveQuadraticEquation( a: Double, b: Double, c: Double ) = {
		val d = b * b - 4 * a * c
		if ( d < 0 ) {
			// No real solution.
			Nil
		}
		else {
			// Normally we would have to check if there's one or two real solutions, but since all the code using this method only cares
			// for the smaller solution, let's just return this one.
			List( ( -b - Math.sqrt( d ) ) / ( 2 * a ) )
		}
	}
}
