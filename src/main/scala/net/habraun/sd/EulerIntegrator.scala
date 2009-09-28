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



import math.Vec2D



/**
 * A simple integrator using Euler integration.
 * Euler integration is simple, cheap and inaccurate. If accuracy is needed, another integration method
 * should be used, especially since the inaccuracies add up over time.
 */

class EulerIntegrator extends Integrator {

	def apply(t: Double, body: Body) = {
		var velocity = body.velocity

		// Apply forces.
		velocity += body.appliedForce / body.mass * t
		body.resetForce

		// Apply impulses.
		velocity += body.appliedImpulse / body.mass
		body.resetImpulse

		// Solve movement constraints.
		val constrainedXVelocity = if (body.xMovementAllowed) velocity.x else 0.0
		val constrainedYVelocity = if (body.yMovementAllowed) velocity.y else 0.0
		val constrainedVelocity = Vec2D(constrainedXVelocity, constrainedYVelocity)

		// Set new velocity.
		body.velocity = constrainedVelocity

		// Set new position.
		body.position = body.position + (body.velocity * t)

		body
	}
}
