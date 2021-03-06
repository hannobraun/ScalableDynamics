/*
	Copyright (c) 2009, 2010 Hanno Braun <mail@hannobraun.com>

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



package com.hannobraun.sd.dynamics



import com.hannobraun.sd.core.Body
import com.hannobraun.sd.core.StepPhase
import com.hannobraun.sd.math.Vector2

import org.specs.Specification
import org.specs.runner.JUnit4



class PositionConstraintSolverTest extends JUnit4( PositionConstraintSolverSpec )



object PositionConstraintSolverSpec extends Specification {

	"PositionConstraintSolverSpec" should {
		"be a StepPhase." in {
			val solver = new PositionConstraintSolver

			solver must haveSuperClass[ StepPhase[ PositionConstraint, Nothing ] ]
		}

		"set the x component of the position vector to minX, if it is below minX." in {
			val solver = new PositionConstraintSolver

			val body = new Body with PositionConstraint {}
			body.position = Vector2( 1, 2 )
			body.minX = Some( 2 )

			solver.execute( 0.0, body::Nil, Nil )

			body.position must beEqualTo( Vector2( 2, 2 ) )
		}

		"set the y component of the position vector to minY, if it is below minY." in {
			val solver = new PositionConstraintSolver

			val body = new Body with PositionConstraint {}
			body.position = Vector2( 2, 1 )
			body.minY = Some( 2 )

			solver.execute( 0.0, body::Nil, Nil )

			body.position must beEqualTo( Vector2( 2, 2 ) )
		}

		"set the x component of the position vector to maxX, if it is above maxX." in {
			val solver = new PositionConstraintSolver

			val body = new Body with PositionConstraint {}
			body.position = Vector2( 3, 2 )
			body.maxX = Some( 2 )

			solver.execute( 0.0, body::Nil, Nil )

			body.position must beEqualTo( Vector2( 2, 2 ) )
		}

		"set the y component of the position vector to maxY, if it is above maxY." in {
			val solver = new PositionConstraintSolver

			val body = new Body with PositionConstraint {}
			body.position = Vector2( 2, 3 )
			body.maxY = Some( 2 )

			solver.execute( 0.0, body::Nil, Nil )

			body.position must beEqualTo( Vector2( 2, 2 ) )
		}
	}
}
