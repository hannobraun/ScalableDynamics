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



import math.Vec2D
import shape.Circle
import shape.LineSegment



/**
 * An implementation of the circle - line segment test that performs continuous collision detection.
 */

class ContinuousCircleLineSegmentTest extends CircleLineSegmentTest {

	/**
	 * Performs continuous collision detection between a circle and a line segment.
	 * Attention: If the circle and the line segment overlap initially, the reported contact point is
	 * invalid. It will always be (0, 0).
	 */

	def apply( c: Circle, ls: LineSegment ): Option[TestResult] = {
		// Extract position and velocity from the shapes.
		val pc = c.previousPosition
		val pls = ls.previousPosition
		val vc = c.position - c.previousPosition
		val vls = ls.position - ls.previousPosition

		// This algorithms does continious collision detection between a moving circle and a moving line
		// segment. I got this from "Real-Time Collision Detection" by Christer Ericson, page 219-222.

		// For this algorithm, we need the normal and the distance from the origin, which together define the
		// line on which the line segments lies.
		// The normal is one of two possible line normals. The distance is the distance from the origin in
		// units of the normal, which basically means that the distance is negative if the normal points
		// towards the origin, positive if the normal points away from the origin.
		val lineNormal = ls.d.orthogonal.normalize
		val lineDistance = ( pls + ls.p ) * lineNormal

		// Compute the distance between the line and the circle. The distance is positive if the line normal
		// points towards the circle (the circle lies in front of the line), negative otherwise.
		val distance = lineNormal * pc - lineDistance

		// Check if the circle and the line are already intersecting.
		if ( Math.abs( distance ) <= c.radius ) {
			// Circle and line are already intersecting.

			// The surface normal of the circle at the point of impact.
			val normal = if ( distance > 0 ) -lineNormal else lineNormal

			// The point on the line that lies nearest to the circle center.
			val lambda = ( pc - pls - ls.p ) * ls.d / ls.d.squaredLength
			if ( lambda >= 0 && lambda <= ls.d.length ) {
				Some( TestResult( 0.0, Vec2D( 0, 0 ), normal, 0 ) )
			}
			else {
				None
			}
		}
		else {
			// Compute the relative velocity between the two bodies. No matter which of the two bodies
			// actually moves, we will model this as a moving sphere and a stationary line segment.
			val v = vc - vls

			// Compute the direction of the circle's movement relative to the line normal. A positive value
			// denotes movment in the direction of the line normal, a negative value the opposite.
			// A value of zero means, that the sphere moves parallel to the line.
			val direction = lineNormal * v

			// Check if the circle moves towards the line. This is the case if the direction multiplied with
			// the distance between the line and the circle is negative.
			// If both are positive, the circle lies in front of the circle (line normal points towards it)
			// and moves in the direction of the normal. If both are negative, it lies behind theline (normal
			// points away from the circle) and it moves against the direction of the normal.
			// The value can only be zero if the circle moves parallel to the line. The direction can't be
			// zero because that has already been ruled out before.
			if ( direction * distance < 0.0 ) {
				// The circle moves towards the line. What's left to do is compute the time of impact, check
				// if it lies within the current timeframe and check if the point of impact lies on the line
				// segment.

				// For the following computation, we need the radius of the circle as a positive value if it
				// lies in front of the plane, as a negative value otherwise.
				val radius = if ( distance > 0.0 ) c.radius else -c.radius

				// Compute the time of impact.
				val t = ( radius - distance ) / direction

				// Check if the time is within the movement interval.
				if ( t >= 0.0 && t <= 1.0 ) {
					// The time is within the movement interval, which  means the circle will hit the line.
					// Compute the point of impact and the normals.

					// The surface normal of the circle at the point of impact.
					val normal = if ( distance > 0 ) -lineNormal else lineNormal

					// Point of impact.
					val point = pc + ( normal * c.radius ) + ( vc * t )

					// We now have the point of impact between the circle and the line. But does this point
					// lie on the line segment?
					val pt = ( point.x - ( pls.x + ls.p.x ) ) / ls.d.x
					if ( pt >= 0.0 && pt <= 1.0 ) {
						// Yes it does.
						Some( TestResult( t, point, normal, 0 ) )
					}
					else {
						// No, it doesn't. No collision.
						None
					}
				}
				else {
					None
				}
			}
			else {
				None
			}
		}
	}
}
