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



package net.habraun.sd.core



import org.specs.Specification
import org.specs.runner.JUnit4



class StepPhaseTest extends JUnit4( WorldPhaseSpec )



object StepPhaseSpec extends Specification {

	"StepPhaseSpec" should {
		"filter the body list according to its type parameter and pass this to step." in {
			trait TestTrait extends Body

			val body1 = new Body {}
			val body2 = new TestTrait {}
			val body3 = new Body {}

			val bodies = body1::body2::body3::Nil

			val stepPhase = new StepPhase[TestTrait] {
				var filtered: Iterable[TestTrait] = Nil

				override def step( dt: Double, filteredBodies: Iterable[TestTrait] ) {
					filtered = filteredBodies
				}
			}

			stepPhase.filterAndStep( 0.0, bodies )

			stepPhase.filtered must beEqualTo( body2::Nil )
		}
	}
}
