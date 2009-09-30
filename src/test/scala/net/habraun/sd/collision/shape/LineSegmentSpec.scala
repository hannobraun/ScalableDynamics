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



package net.habraun.sd.collision.shape



import math.Vec2D

import org.specs.Specification
import org.specs.runner.JUnit4



class LineSegmentTest extends JUnit4(LineSegmentSpec)



object LineSegmentSpec extends Specification {

	"LineSegment" should {
		"be a shape." in {
			LineSegment(Vec2D(0, 0), Vec2D(10, 10)) must haveSuperClass[Shape]
		}

		"return its attributes." in {
			val p = Vec2D(5, 5)
			val d = Vec2D(2, 1)
			
			val segment = LineSegment(p, d)

			segment.p must beEqualTo(p)
			segment.d must beEqualTo(d)
		}

		"throw an exception if the direction vector is zero." in {
			LineSegment(Vec2D(2, 2), Vec2D(0, 0)) must throwA[IllegalArgumentException]
		}

		"throw an exception if null is passed as an argument." in {
			LineSegment(null, Vec2D(1, 1)) must throwA[NullPointerException]
			LineSegment(Vec2D(0, 0), null) must throwA[NullPointerException]
		}
	}
}
