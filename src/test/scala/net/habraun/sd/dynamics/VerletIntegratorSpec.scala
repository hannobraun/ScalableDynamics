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



import core.Body
import core.StepPhase
import math.Vec2D

import org.specs.Specification
import org.specs.runner.JUnit4



class VerletIntegratorTest extends JUnit4( VerletIntegratorSpec )



object VerletIntegratorSpec extends Specification {

	"VerletIntegrator" should {
		"be a StepPhase." in {
			val integrator = new VerletIntegrator

			integrator must haveSuperClass[ StepPhase[ Body ] ]
		}

		"update position and velocity correctly." in {
			val integrator= new VerletIntegrator

			val body = new Body {}

			// Values influencing the new position and velocity.
			body.position = Vec2D( 5, 5 )
			body.velocity = Vec2D( 10, 10 )
			body.acceleration = Vec2D( 8, 8 )

			// Values influencing the new acceleration.
			body.mass = 2
			body.applyForce( Vec2D( 16, 16 ) )

			integrator.filterAndStep( 0.5, List( body ) )

			// Position and velocity must have been correctly integrated.
			body.position must beEqualTo( Vec2D( 11, 11 ) )
			body.velocity must beEqualTo( Vec2D( 14, 14 ) )

			// The new acceleration must have been computed correctly.
			body.acceleration must beEqualTo( Vec2D( 8, 8 ) )
			body.appliedForce must beEqualTo( Vec2D( 0, 0 ) )
		}
	}
}
