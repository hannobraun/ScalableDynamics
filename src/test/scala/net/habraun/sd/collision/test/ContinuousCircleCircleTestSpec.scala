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
import shape.Circle

import org.specs.Specification
import org.specs.runner.JUnit4



class ContinuousCircleCircleTestTest extends JUnit4(ContinuousCircleCircleTestSpec)



object ContinuousCircleCircleTestSpec extends Specification {

	"ContinuousCircleCircleTest" should {
		"handle non-moving, non-colliding circles." in {
			val test = new ContinuousCircleCircleTest
			
			val c1 = Circle(1)
			val c2 = Circle(1)
			val p1 = Vec2D(0, 0)
			val p2 = Vec2D(3, 0)
			val v1 = Vec2D(0, 0)
			val v2 = Vec2D(0, 0)
			
			test(c1, c2, p1, p2, v1, v2) must be(None)
		}

		"handle non-moving, colliding circles." in {
			val test = new ContinuousCircleCircleTest

			val c1 = Circle(2)
			val c2 = Circle(2)
			val p1 = Vec2D(0, 0)
			val p2 = Vec2D(3, 0)
			val v1 = Vec2D(0, 0)
			val v2 = Vec2D(0, 0)

			test(c1, c2, p1, p2, v1, v2) must beEqualTo(Some(TestResult(0.0, Vec2D(0, 0), Vec2D(1, 0))))
		}

		"handle one circle moving and colliding." in {
			val test = new ContinuousCircleCircleTest

			val c1 = Circle(1)
			val c2 = Circle(1)
			val p1 = Vec2D(-1, 0)
			val p2 = Vec2D(2, 0)
			val v1 = Vec2D(2, 0)
			val v2 = Vec2D(0, 0)

			test(c1, c2, p1, p2, v1, v2) must beEqualTo(Some(TestResult(0.5, Vec2D(1, 0), Vec2D(1, 0))))
		}

		"handle one circle moving and not colliding." in {
			val test = new ContinuousCircleCircleTest

			val c1 = Circle(1)
			val c2 = Circle(1)
			val p1 = Vec2D(-3, 0)
			val p2 = Vec2D(2, 0)
			val v1 = Vec2D(2, 0)
			val v2 = Vec2D(0, 0)

			test(c1, c2, p1, p2, v1, v2) must be(None)
		}

		"handle both circles moving and colliding." in {
			val test = new ContinuousCircleCircleTest

			val c1 = Circle(1)
			val c2 = Circle(1)
			val p1 = Vec2D(-3, 0)
			val p2 = Vec2D(1, 0)
			val v1 = Vec2D(2, 0)
			val v2 = Vec2D(-2, 0)

			test(c1, c2, p1, p2, v1, v2) must beEqualTo(Some(TestResult(0.5, Vec2D(-1, 0), Vec2D(1, 0))))
		}

		"handle both circles moving towards each other, but stopping before collision." in {
			val test = new ContinuousCircleCircleTest

			val c1 = Circle(1)
			val c2 = Circle(1)
			val p1 = Vec2D(-3, 0)
			val p2 = Vec2D(4, 0)
			val v1 = Vec2D(2, 0)
			val v2 = Vec2D(-2, 0)

			test(c1, c2, p1, p2, v1, v2) must be(None)
		}

		"handle both circles not moving towards each other." in {
			val test = new ContinuousCircleCircleTest

			val c1 = Circle(1)
			val c2 = Circle(1)
			val p1 = Vec2D(-2, 0)
			val p2 = Vec2D(2, 0)
			val v1 = Vec2D(-2, 0)
			val v2 = Vec2D(2, 0)

			test(c1, c2, p1, p2, v1, v2) must be(None)
		}

		"handle both circles moving and occupying the same space at different times." in {
			val test = new ContinuousCircleCircleTest

			val c1 = Circle(1)
			val c2 = Circle(1)
			val p1 = Vec2D(0, 0)
			val p2 = Vec2D(5, 0)
			val v1 = Vec2D(0, 5)
			val v2 = Vec2D(-5, 0)

			test(c1, c2, p1, p2, v1, v2) must be(None)
		}

		"handle both circles moving on parallel courses with no intersection." in {
			val test = new ContinuousCircleCircleTest

			val c1 = Circle(1)
			val c2 = Circle(1)
			val p1 = Vec2D(0, 0)
			val p2 = Vec2D(3, 0)
			val v1 = Vec2D(0, 5)
			val v2 = Vec2D(0, 3)

			test(c1, c2, p1, p2, v1, v2) must be(None)
		}
	}
}
