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



package net.habraun.sd



class ImpulseSolver extends ConstraintSolver {

	def apply(t: Double, constraint: Collision) {
		// Get the bodies out of the contact, so we can access them easier.
		val b1 = constraint.contact1.b
		val b2 = constraint.contact2.b

		// Compute the part of the velocities that points in the direction of the collision normals.
		val v1 = b1.velocity.project(constraint.contact1.normal)
		val v2 = b2.velocity.project(constraint.contact2.normal)

		// Apply impulses along the collision normals.
		val m1 = b1.mass
		val m2 = b2.mass
		if (m1 == Double.PositiveInfinity) {
			val impulse = (v1 - v2) * 2 * m2
			b2.applyImpulse(impulse)
		}
		else if (m2 == Double.PositiveInfinity) {
			val impulse = (v2 - v1) * 2 * m1
			b1.applyImpulse(impulse)
		}
		else {
			val impulse = (v2 - v1) * 2 * m1 * m2 / (m1 + m2)
			b1.applyImpulse(impulse)
			b2.applyImpulse(-impulse)
		}

		// If the time of impact given by the collision is smaller than 1.0, the bodies would overlap
		// after the movement has been carried out. We don't want that, we want the bodies to stop right
		// at the point of impact. Let's set them back, so the regular movement will put them right where
		// we want them.
		b1.position -= b1.velocity * t * (1.0 - constraint.t + 0.0001)
		b2.position -= b2.velocity * t * (1.0 - constraint.t + 0.0001)
	}
}
