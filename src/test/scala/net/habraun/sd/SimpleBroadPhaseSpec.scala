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



package net.habraun.sd



import org.specs.Specification
import org.specs.runner.JUnit4



class SimpleBroadPhaseTest extends JUnit4( SimpleBroadPhaseSpec ) 



object SimpleBroadPhaseSpec extends Specification {

	"SimpleBroadPhase" should {
		"build a list containing only the one possible pair if it is given two bodies." in {
			val broadPhase = new SimpleBroadPhase

			val b1 = new Body
			val b2 = new Body

			broadPhase( b1::b2::Nil ) must beEqualTo( ( b1, b2 )::Nil )
		}

		"build a list of all three possible pairs if it is given three bodies." in {
			val broadPhase = new SimpleBroadPhase

			val b1 = new Body
			val b2 = new Body
			val b3 = new Body

			broadPhase( b1::b2::b3::Nil ) must beEqualTo( ( b1, b2 )::( b1, b3 )::( b2, b3 )::Nil )
		}
	}
}
