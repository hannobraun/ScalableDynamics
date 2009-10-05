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



import org.specs.Specification
import org.specs.runner.JUnit4



class PositionConstraintTest extends JUnit4( PositionConstraintSpec )



object PositionConstraintSpec extends Specification {

	"PositionConstraint" should {
		"extend Body." in {
			val constraint = new PositionConstraint {}

			constraint must haveSuperClass[Body]
		}

		"have xConstraint and yConstraint attributes." in {
			val constraint = new PositionConstraint {}

			val xConstraint = Some( 0.0 )
			val yConstraint = Some( 0.0 )
			constraint.xConstraint = xConstraint
			constraint.yConstraint = yConstraint

			constraint.xConstraint must beEqualTo( xConstraint )
			constraint.yConstraint must beEqualTo( yConstraint )
		}

		"set xConstraint and yConstraint initially to None." in {
			val constraint = new PositionConstraint {}

			constraint.xConstraint must beEqualTo( None )
			constraint.yConstraint must beEqualTo( None )
		}
	}
}
