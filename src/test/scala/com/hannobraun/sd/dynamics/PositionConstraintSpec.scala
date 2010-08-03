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



import org.specs.Specification
import org.specs.runner.JUnit4



class PositionConstraintTest extends JUnit4( PositionConstraintSpec )



object PositionConstraintSpec extends Specification {

	"PositionConstraint" should {
		"extend Body." in {
			val constraint = new PositionConstraint {}

			constraint must haveSuperClass[ Body ]
		}

		"have xConstraint and yConstraint attributes." in {
			val constraint = new PositionConstraint {}

			val minX = Some( 0.0 )
			val maxX = Some( 0.0 )
			val minY = Some( 0.0 )
			val maxY = Some( 0.0 )
			constraint.minX = minX
			constraint.maxX = maxX
			constraint.minY = minY
			constraint.maxY = maxY

			constraint.minX must beEqualTo( minX )
			constraint.maxX must beEqualTo( maxX )
			constraint.minY must beEqualTo( minY )
			constraint.maxY must beEqualTo( maxY )
		}

		"set xConstraint and yConstraint initially to None." in {
			val constraint = new PositionConstraint {}

			constraint.minX must beEqualTo( None )
			constraint.maxX must beEqualTo( None )
			constraint.minY must beEqualTo( None )
			constraint.maxY must beEqualTo( None )
		}
	}
}
