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



class PositionConstraintSolverTest extends JUnit4( PositionConstraintSolverSpec )



object PositionConstraintSolverSpec extends Specification {

	"PositionConstraintSolverSpec" should {
		"be a StepPhase." in {
			val solver = new PositionConstraintSolver

			solver must haveSuperClass[StepPhase[PositionConstraint]]
		}

		"reset the x component of the position vector if a x constraint is set." in {
			val solver = new PositionConstraintSolver

			val body = new Body with PositionConstraint {}
			body.position = Vec2D( 2, 2 )
			body.xConstraint = Some( 1.0 )

			solver.filterAndStep( 0.0, body::Nil )

			body.position must beEqualTo( Vec2D( 1, 2 ) )
		}

		"reset the y component of the position vector if a y constraint is set." in {
			val solver = new PositionConstraintSolver

			val body = new Body with PositionConstraint {}
			body.position = Vec2D( 2, 2 )
			body.yConstraint = Some( 1.0 )

			solver.filterAndStep( 0.0, body::Nil )

			body.position must beEqualTo( Vec2D( 2, 1 ) )
		}
	}
}
