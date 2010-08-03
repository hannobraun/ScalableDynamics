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



import com.hannobraun.sd.core.StepPhase
import com.hannobraun.sd.math.Vector2

import scala.math._



class PositionConstraintSolver extends StepPhase[ PositionConstraint, Nothing ] {

	def execute( dt: Double, constraints: Iterable[ PositionConstraint ], c: Iterable[ Nothing ] ) = {
		for ( constraint <- constraints ) {
			val initialX = constraint.position.x
			val initialY = constraint.position.y

			val xAfterMin = constraint.minX match {
				case Some( minX ) => max( initialX, minX )
				case None => initialX
			}

			val yAfterMin = constraint.minY match {
				case Some( minY ) => max( initialY, minY )
				case None => initialY
			}

			val xAfterMax = constraint.maxX match {
				case Some( maxX ) => min( xAfterMin, maxX )
				case None => xAfterMin
			}

			val yAfterMax = constraint.maxY match {
				case Some( maxY ) => min( yAfterMin, maxY )
				case None => yAfterMin
			}

			constraint.position = Vector2( xAfterMax, yAfterMax )
		}

		( constraints, c )
	}
}
