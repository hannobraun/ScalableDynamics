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



package net.habraun.sd.collision



import math._



/**
 * An circle-circle collision test implementation that performs continuous collision detection.
 */

class ContinuousCircleCircleTest extends CircleCircleTest {

	/**
	 * Performs continuous collision detection between two circles with relative movement.
	 * Parameters:
	 * * c1: The first circle.
	 * * c2: The second circle.
	 * * p1: Position of the first circle.
	 * * p2: Position of the second circle.
	 * * v1: Movement of the first circle.
	 * * v2: Movement of the second circle.
	 *
	 * Attention: If the circles already overlap at the beginning of the movement, no valid contact point is
	 * reported. In that case, the collision point will always be (0, 0).
	 */

	def apply(c1: Circle, c2: Circle, p1: Vec2D, p2: Vec2D, v1: Vec2D, v2: Vec2D): Option[TestResult] = {
		// This algorithms does continious collision detection between two moving circles. I got this from
		// "Real-Time Collision Detection" by Christer Ericson, page 223/224.

		val s = p2 - p1 // vector between sphere centers
		val r = c1.radius + c2.radius // the sum of both radii
		val v = v2 - v1 // relative movement between the circles.

		// The time of impact is given by the smaller solution of the quadratic equation at^2 + 2bt + c = 0.
		// The following 3 variables are a, b and c from that equation.
		val a = v * v
		val b = v * s
		val c = (s * s) - (r * r)

		// The discriminant of the solution.
		val d = (b * b) - (a * c)

		// Check for several corner cases. If none of these occurs, we can compute t with the general
		// formula.
		if (c < 0.0) {
			// Spheres are initially overlapping.
			val normal = (p2 - p1).normalize
			val point = Vec2D(0, 0)
			Some(TestResult(0.0, point, normal))
		}
		else if (a == 0) {
			// Spheres are not moving relative to each other.
			None
		}
		else if (b >= 0.0) {
			// Spheres are not moving towards each other.
			None
		}
		else if (d < 0.0) {
			// Discriminant is negative, no real solution.
			None
		}
		else {
			// None of the edge cases has occured, so we need to compute the time of contact.
			val t = (-b - Math.sqrt(d)) / a
			if (t <= 1.0) {
				val normal = (p2 - p1).normalize
				val point = p1 + (v1 * t) + (normal * c1.radius)
				Some(TestResult(t, point, normal))
			}
			else {
				None
			}
		}
	}
}
