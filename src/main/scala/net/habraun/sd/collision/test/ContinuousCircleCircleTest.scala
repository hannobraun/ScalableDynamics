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
import math.Vec2D
import shape.Circle
import shape.Contact



/**
 * An circle-circle collision test implementation that performs continuous collision detection.
 */

class ContinuousCircleCircleTest extends CircleCircleTest {

	/**
	 * Performs continuous collision detection between two circles with relative movement.
	 * Parameters:
	 * * c1: The first circle.
	 * * c2: The second circle.
	 */

	def apply( c1: Circle, c2: Circle ): Option[ Contact ] = {
		// Extract position and velocity from the circles.
		val p1 = c1.previousPosition
		val p2 = c2.previousPosition
		val v1 = c1.position - c1.previousPosition
		val v2 = c2.position - c2.previousPosition

		// This algorithm does continuous collision detection between two moving circles. I got this from "Real-Time Collision Detection"
		// by Christer Ericson, page 223/224.

		val s = p2 - p1 // vector between sphere centers
		val r = c1.radius + c2.radius // the sum of both radii
		val v = v2 - v1 // relative movement between the circles.

		// The time of impact is given by the smaller solution of the quadratic equation at^2 + 2bt + c = 0.
		// The following 3 variables are a, b and c from that equation.
		val a = v * v
		val b = v * s
		val c = ( s * s ) - ( r * r )

		// The discriminant of the solution.
		val d = ( b * b ) - ( a * c )

		// Check for several corner cases. If none of these occurs, we can compute t with the general formula.
		if ( c <= 0.0 ) {
			// Spheres are initially overlapping. Test if the circle centers are also overlapping.
			if ( s != Vec2D( 0, 0 ) ) {
				// Sphere centers are now overlapping.

				val normal = s.normalize // the direction of the normal is given by the vector between the sphere centers
				val depth = r - s.length // penetration depth is the sum of the radii minus the distance between the sphere centers

				// If the circles just touch, the contact point is where they touch. If they overlap, the point is on the line between
				// their centers, in the middle of the intersection. We can compute this point by going to the center of one circle, moving
				// along the normal to its radius and going back for half the depth.
				val point = p1 + normal * ( c1.radius -  depth / 2 )

				Some( Contact( c1, c2, point, normal, depth, 0.0 ) )
			}
			else {
				// The sphere centers are overlapping.

				// The contact normal is undefined, but the way Contact is currently defined, we need to put something here. Collision
				// reaction code should just check if the circle centers overlap.
				val normal = Vec2D( 1, 0 )

				// Penetration depth is ambigious in this case, but the main goal of having a Contact is to use it to resolve any
				// contstraints. In this sense, let's assume the smaller circle has penetrated the bigger one from any direction. This
				// would put the depth at 2 times the smaller radius plus big minus small (note: this term has been simplified below).
				val smallRadius = if ( c1.radius < c2.radius ) c1.radius else c2.radius
				val bigRadius = if ( c1.radius > c2.radius ) c1.radius else c2.radius
				val depth = smallRadius + bigRadius

				// Just like the normal, the contact point is undefined. I'll use the center of the spheres here, mainly because I can't
				// think of anything sensible.
				val point = p1

				Some( Contact( c1, c2, point, normal, depth, 0.0 ) )
			}
		}
		else if ( a == 0 ) {
			// Spheres are not moving relative to each other.
			None
		}
		else if ( b >= 0.0 ) {
			// Spheres are not moving towards each other.
			None
		}
		else if ( d < 0.0 ) {
			// Discriminant is negative, no real solution.
			None
		}
		else {
			// None of the edge cases has occured, so we need to compute the time of contact.
			val t = ( -b - Math.sqrt( d ) ) / a
			if ( t <= 1.0 ) {
				// Time of contact is within the timeframe we're checking. Let's compute the other attributes of the contact.

				// The contact normal always points from c1's center to c2's center at the time of impact.
				val normal = ( p2 + ( v * t ) - p1 ).normalize

				// The point of contact can be computed by looking at the position of circle 1 at the time of impact and go along the
				// contact normal (which points to the center of circle 2).
				val point = p1 + ( v1 * t ) + ( normal * c1.radius )

				// The depth is computed by taking the time that is left after the contact is made and looking how much relative movement
				// there is left, in the direction of the contact normal.
				val depth = ( 1.0 - t ) * v.projectOn( normal ).length

				// All attributes have been computed, let's create the Contact.
				Some( Contact( c1, c2, point, normal, depth, t ) )
			}
			else {
				// Contact would occur, but only after the timeframe we're looking at.
				None
			}
		}
	}
}
