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
import math.Vector2



class PositionConstraintSolver extends StepPhase[ PositionConstraint, Nothing ] {

	def step( dt: Double, constraints: Iterable[ PositionConstraint ], c: Iterable[ Nothing ] ) {
		for ( constraint <- constraints ) {
			val initialX = constraint.position.x
			val initialY = constraint.position.y

			val xAfterMin = constraint.minX match {
				case Some( minX ) => Math.max( initialX, minX )
				case None => initialX
			}

			val yAfterMin = constraint.minY match {
				case Some( minY ) => Math.max( initialY, minY )
				case None => initialY
			}

			val xAfterMax = constraint.maxX match {
				case Some( maxX ) => Math.min( xAfterMin, maxX )
				case None => xAfterMin
			}

			val yAfterMax = constraint.maxY match {
				case Some( maxY ) => Math.min( yAfterMin, maxY )
				case None => yAfterMin
			}

			constraint.position = Vector2( xAfterMax, yAfterMax )
		}
	}
}
