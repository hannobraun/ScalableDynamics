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



package net.habraun.sd.collision.phase



import shape.Shape

import org.specs.Specification
import org.specs.runner.JUnit4



class SimpleBroadPhaseTest extends JUnit4( SimpleBroadPhaseSpec ) 



object SimpleBroadPhaseSpec extends Specification {

	"SimpleBroadPhase" should {
		"build a list containing only the one possible pair if it is given two bodies." in {
			val broadPhase = new SimpleBroadPhase

			val s1 = new Shape {}
			val s2 = new Shape {}

			broadPhase( s1::s2::Nil ) must beEqualTo( ( s1, s2 )::Nil )
		}

		"build a list of all three possible pairs if it is given three bodies." in {
			val broadPhase = new SimpleBroadPhase

			val s1 = new Shape {}
			val s2 = new Shape {}
			val s3 = new Shape {}

			broadPhase( s1::s2::s3::Nil ) must beEqualTo( ( s1, s2 )::( s1, s3 )::( s2, s3 )::Nil )
		}
	}
}
