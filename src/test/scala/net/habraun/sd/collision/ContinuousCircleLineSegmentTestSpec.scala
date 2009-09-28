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



package net.habraun.sd.collision



import math.Vec2D

import org.specs.Specification
import org.specs.runner.JUnit4



class ContinuousCircleLineSegmentTestTest extends JUnit4(ContinuousCircleLineSegmentTestSpec)



object ContinuousCircleLineSegmentTestSpec extends Specification {

	"ContinuousCircleLineSegmentTest" should {
		"handle two non-moving, non-intersecting bodies." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = Circle(1)
			val ls = LineSegment(Vec2D(-1, 0), Vec2D(2, 0))
			val pc = Vec2D(0, 0)
			val pls = Vec2D(0, 2)
			val vc = Vec2D(0, 0)
			val vls = Vec2D(0, 0)

			test(c, ls, pc, pls, vc, vls) must be(None)
		}

		"handle two non-moving, intersecting bodies." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = Circle(1)
			val ls = LineSegment(Vec2D(-1, 0), Vec2D(2, 0))
			val pc = Vec2D(0, 0)
			val pls = Vec2D(0, 0.5)
			val vc = Vec2D(0, 0)
			val vls = Vec2D(0, 0)

			test(c, ls, pc, pls, vc, vls) must beEqualTo(Some(TestResult(0.0, Vec2D(0, 0), Vec2D(0, 1))))
		}

		"handle the circle being on the line described by the vectors, but not on the line-segment." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = Circle(1)
			val ls = LineSegment(Vec2D(0, 0), Vec2D(2, 0))
			val pc = Vec2D(0, 0)
			val pls = Vec2D(0, 2)
			val vc = Vec2D(0, 0)
			val vls = Vec2D(0, 0)

			test(c, ls, pc, pls, vc, vls) must be(None)
		}

		"handle the circle moving on a parallel to the line segment and not colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = Circle(1)
			val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
			val pc = Vec2D(0, 0)
			val pls = Vec2D(0, 2)
			val vc = Vec2D(2, 0)
			val vls = Vec2D(0, 0)

			test(c, ls, pc, pls, vc, vls) must be(None)
		}

		"handle the circle moving away from the line segment and not colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = Circle(1)
			val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
			val pc = Vec2D(0, 0)
			val pls = Vec2D(0, 2)
			val vc = Vec2D(0, -2)
			val vls = Vec2D(0, 0)

			test(c, ls, pc, pls, vc, vls) must be(None)
		}

		"handle the circle moving to, but not reaching the line segment." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = Circle(1)
			val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
			val pc = Vec2D(0, -5)
			val pls = Vec2D(0, 2)
			val vc = Vec2D(0, 2)
			val vls = Vec2D(0, 0)

			test(c, ls, pc, pls, vc, vls) must be(None)
		}

		"handle the circle moving and missing the line segment." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = Circle(1)
			val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
			val pc = Vec2D(-2, 0)
			val pls = Vec2D(0, 2)
			val vc = Vec2D(0, 2)
			val vls = Vec2D(0, 0)

			test(c, ls, pc, pls, vc, vls) must be(None)
		}

		"handle the circle moving and colliding with the line segment." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = Circle(1)
			val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
			val pc = Vec2D(2, 0)
			val pls = Vec2D(0, 2)
			val vc = Vec2D(0, 2)
			val vls = Vec2D(0, 0)

			test(c, ls, pc, pls, vc, vls) must beEqualTo(Some(TestResult(0.5, Vec2D(2, 2), Vec2D(0, 1))))
		}

		"handle the line segment moving and not colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = Circle(1)
			val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
			val pc = Vec2D(0, 0)
			val pls = Vec2D(0, 2)
			val vc = Vec2D(0, 0)
			val vls = Vec2D(0, 2)

			test(c, ls, pc, pls, vc, vls) must be(None)
		}

		"handle the line segment moving and colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = Circle(1)
			val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
			val pc = Vec2D(2, 0)
			val pls = Vec2D(0, 2)
			val vc = Vec2D(0, 0)
			val vls = Vec2D(0, -2)

			test(c, ls, pc, pls, vc, vls) must beEqualTo(Some(TestResult(0.5, Vec2D(2, 1), Vec2D(0, 1))))
		}

		"handle both bodies moving and not colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = Circle(1)
			val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
			val pc = Vec2D(0, 0)
			val pls = Vec2D(0, 2)
			val vc = Vec2D(2, 0)
			val vls = Vec2D(-2, 0)

			test(c, ls, pc, pls, vc, vls) must be(None)
		}

		"handle both bodies moving and colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = Circle(1)
			val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
			val pc = Vec2D(2, -1)
			val pls = Vec2D(0, 2)
			val vc = Vec2D(0, 2)
			val vls = Vec2D(0, -2)

			test(c, ls, pc, pls, vc, vls) must beEqualTo(Some(TestResult(0.5, Vec2D(2, 1), Vec2D(0, 1))))
		}
	}
}
