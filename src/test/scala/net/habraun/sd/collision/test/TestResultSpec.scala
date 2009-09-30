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



package net.habraun.sd.collision.test



import math.Vec2D

import org.specs.Specification
import org.specs.runner.JUnit4



class TestResultTest extends JUnit4(TheTestResultSpec)



object TheTestResultSpec extends Specification {

	"TestResult" should {
		"return its attributes." in {
			val t = 0.5
			val contact = Vec2D(5, 5)
			val normal = Vec2D(1, 0)

			val result = TestResult(t, contact, normal)

			result.t must beEqualTo(t)
			result.contact must beEqualTo(contact)
			result.normal must beEqualTo(normal)
		}

		"throw an exception if a negative contact time is passed." in {
			TestResult(-1.0, Vec2D(5, 5), Vec2D(1, 0)) must throwA[IllegalArgumentException]
		}

		"throw an exception if a contact time greater than 1.0 is passed." in {
			TestResult(1.1, Vec2D(5, 5), Vec2D(1, 0)) must throwA[IllegalArgumentException]
		}

		"throw an exception if the passed normal is not actually a normal." in {
			TestResult(0.5, Vec2D(5, 5), Vec2D(1, 1)) must throwA[IllegalArgumentException]
		}

		"throw an exception if null is passed as a parameter." in {
			TestResult(0.5, null, Vec2D(1, 0)) must throwA[NullPointerException]
			TestResult(0.5, Vec2D(5, 5), null) must throwA[NullPointerException]
		}
	}
}
