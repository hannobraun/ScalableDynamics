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
import math.Vec2D

import scala.collection.immutable.HashSet

import org.specs.Specification
import org.specs.runner.JUnit4



class VelocityConstraintSolverTest extends JUnit4( VelocityConstraintSolverSpec )



object VelocityConstraintSolverSpec extends Specification {

	"VelocityConstraintSolver" should {
		"not modify the velocity of a passed Body if it is below the maximum velocity." in {
			val solver = new VelocityConstraintSolver

			val velocity = Vec2D( 3, 0 )
			val body = new Body with VelocityConstraint {}
			body.velocity = velocity
			body.maxVelocity = 5
			solver.solve( HashSet( body ) )

			body.velocity must beEqualTo( velocity )
		}

		"set the velocity to the maximum velocity if it is above maximum velocity." in {
			val solver = new VelocityConstraintSolver

			val body = new Body with VelocityConstraint {}
			body.velocity = Vec2D( 6, 0 )
			body.maxVelocity = 5
			solver.solve( HashSet( body ) )

			body.velocity must beEqualTo( Vec2D( 5, 0 ) )
		}
	}
}