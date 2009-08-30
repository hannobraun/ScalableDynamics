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



import math._

import org.junit._
import org.junit.Assert._



class ContinuousCircleCircleTestTest {

	var test: CircleCircleTest = null



	@Before
	def setup {
		test = new ContinuousCircleCircleTest
	}

	

	@Test
	def noMovementNoCollision {
		val c1 = Circle(1)
		val c2 = Circle(1)
		val p1 = Vec2D(0, 0)
		val p2 = Vec2D(3, 0)
		val v1 = Vec2D(0, 0)
		val v2 = Vec2D(0, 0)
		
		assertEquals(None, test(c1, c2, p1, p2, v1, v2))
	}



	@Test
	def noMovementCollision {
		val c1 = Circle(2)
		val c2 = Circle(2)
		val p1 = Vec2D(0, 0)
		val p2 = Vec2D(3, 0)
		val v1 = Vec2D(0, 0)
		val v2 = Vec2D(0, 0)

		val result = TestResult(0.0, Vec2D(0, 0), Vec2D(1, 0))

		assertEquals(Some(result), test(c1, c2, p1, p2, v1, v2))
	}



	@Test
	def firstCircleMovingCollision {
		val c1 = Circle(1)
		val c2 = Circle(1)
		val p1 = Vec2D(-1, 0)
		val p2 = Vec2D(2, 0)
		val v1 = Vec2D(2, 0)
		val v2 = Vec2D(0, 0)

		val result = TestResult(0.5, Vec2D(1, 0), Vec2D(1, 0))

		assertEquals(Some(result), test(c1, c2, p1, p2, v1, v2))
	}



	@Test
	def firstCircleMovingNoCollision {
		val c1 = Circle(1)
		val c2 = Circle(1)
		val p1 = Vec2D(-3, 0)
		val p2 = Vec2D(2, 0)
		val v1 = Vec2D(2, 0)
		val v2 = Vec2D(0, 0)

		assertEquals(None, test(c1, c2, p1, p2, v1, v2))
	}



	@Test
	def bothCirclesMovingCollision {
		val c1 = Circle(1)
		val c2 = Circle(1)
		val p1 = Vec2D(-3, 0)
		val p2 = Vec2D(1, 0)
		val v1 = Vec2D(2, 0)
		val v2 = Vec2D(-2, 0)

		val result = TestResult(0.5, Vec2D(-1, 0), Vec2D(1, 0))

		assertEquals(Some(result), test(c1, c2, p1, p2, v1, v2))
	}



	@Test
	def bothCirclesMovingStopBeforeCollision {
		val c1 = Circle(1)
		val c2 = Circle(1)
		val p1 = Vec2D(-3, 0)
		val p2 = Vec2D(4, 0)
		val v1 = Vec2D(2, 0)
		val v2 = Vec2D(-2, 0)

		assertEquals(None, test(c1, c2, p1, p2, v1, v2))
	}



	@Test
	def bothCirclesMovingNotMovingTowardsEachOther {
		val c1 = Circle(1)
		val c2 = Circle(1)
		val p1 = Vec2D(-2, 0)
		val p2 = Vec2D(2, 0)
		val v1 = Vec2D(-2, 0)
		val v2 = Vec2D(2, 0)

		assertEquals(None, test(c1, c2, p1, p2, v1, v2))
	}



	@Test
	def bothCirclesMovingIntersectingCoursesNoCollision {
		val c1 = Circle(1)
		val c2 = Circle(1)
		val p1 = Vec2D(0, 0)
		val p2 = Vec2D(5, 0)
		val v1 = Vec2D(0, 5)
		val v2 = Vec2D(-5, 0)

		assertEquals(None, test(c1, c2, p1, p2, v1, v2))
	}



	@Test
	def bothCirclesMovingParallelyNoCollision {
		val c1 = Circle(1)
		val c2 = Circle(1)
		val p1 = Vec2D(0, 0)
		val p2 = Vec2D(3, 0)
		val v1 = Vec2D(0, 5)
		val v2 = Vec2D(0, 3)

		assertEquals(None, test(c1, c2, p1, p2, v1, v2))
	}
}
