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



package net.habraun.sd.dynamics



import net.habraun.sd.core.Body
import net.habraun.sd.core.StepPhase
import net.habraun.sd.math.Scalar._



/**
 * Integrates the position and velocity of bodies using the Velocity Verlet algorithm.
 */

class VerletIntegrator extends StepPhase[ Body, Nothing ] {
	
	def execute( dt: Double, bodies: Iterable[ Body ], constraints: Iterable[ Nothing ] ) = {
		for ( body <- bodies ) {
			// Integrate position.
			body.position += body.velocity * dt + 0.5 * body.acceleration * dt * dt

			// Integrate velocity and acceleration.
			val halfTimeVelocity = body.velocity + 0.5 * body.acceleration * dt
			body.acceleration = body.appliedForce / body.mass
			body.velocity = halfTimeVelocity + 0.5 * body.acceleration * dt

			// Reset the forces, since they were already applied to the new velocity.
			body.resetForce
		}

		( bodies, constraints )
	}
}
