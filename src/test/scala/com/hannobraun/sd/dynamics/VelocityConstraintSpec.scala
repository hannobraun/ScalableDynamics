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



class VelocityConstraintTest extends JUnit4( VelocityConstraintSpec )



object VelocityConstraintSpec extends Specification {

	"VelocityConstraint" should {
		"extends Body." in {
			val constraint = new VelocityConstraint {}

			constraint must haveSuperClass[Body]
		}

		"have the attribute maximumVelocity." in {
			val constraint = new VelocityConstraint {}

			val maxVelocity = 5
			constraint.maxVelocity = maxVelocity

			constraint.maxVelocity must beEqualTo( maxVelocity )
		}

		"have an initial maximum velocity of Double.PositiveInfinity." in{
			val constraint = new VelocityConstraint {}

			constraint.maxVelocity must beEqualTo( Double.PositiveInfinity )
		}

		"must throw an exception if a negative value is assigned to maximumVelocity." in {
			val constraint = new VelocityConstraint {}

			( constraint.maxVelocity = -1 ) must throwAn[IllegalArgumentException]
		}
	}
}
