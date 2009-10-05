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



import core.StepPhase
import math.Vec2D



class PositionConstraintSolver extends StepPhase[PositionConstraint] {

	def step( dt: Double, constraints: Iterable[PositionConstraint] ) {
		for ( constraint <- constraints ) {
			// Check if the x component is constrained, determine the new x component of the position.
			val xComponent = constraint.xConstraint match {
				case Some( x ) => x
				case None => constraint.position.x
			}

			// Check if the y component is constrained, determine the new y component of the position.
			val yComponent = constraint.yConstraint match {
				case Some( y ) => y
				case None => constraint.position.y
			}

			constraint.position = Vec2D( xComponent, yComponent )
		}
	}
}
