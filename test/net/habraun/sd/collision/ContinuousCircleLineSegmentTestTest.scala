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



class CircleLineSegmentTestTest {

	var test: CircleLineSegmentTest = null



	@Before
	def setup {
		test = new ContinuousCircleLineSegmentTest
	}



	@Test
	def noMovementNoCollision {
		val c = Circle(1)
		val ls = LineSegment(Vec2D(-1, 0), Vec2D(2, 0))
		val pc = Vec2D(0, 0)
		val pls = Vec2D(0, 2)
		val vc = Vec2D(0, 0)
		val vls = Vec2D(0, 0)

		assertEquals(None, test(c, ls, pc, pls, vc, vls))
	}



	@Test
	def noMovementCollision {
		val c = Circle(1)
		val ls = LineSegment(Vec2D(-1, 0), Vec2D(2, 0))
		val pc = Vec2D(0, 0)
		val pls = Vec2D(0, 0.5)
		val vc = Vec2D(0, 0)
		val vls = Vec2D(0, 0)

		val result = TestResult(0.0, Vec2D(0, 0), Vec2D(0, 1))

		assertEquals(Some(result), test(c, ls, pc, pls, vc, vls))
	}



	@Test
	def noMovementCircleOnLineNoCollision {
		val c = Circle(1)
		val ls = LineSegment(Vec2D(0, 0), Vec2D(2, 0))
		val pc = Vec2D(0, 0)
		val pls = Vec2D(0, 2)
		val vc = Vec2D(0, 0)
		val vls = Vec2D(0, 0)

		assertEquals(None, test(c, ls, pc, pls, vc, vls))
	}



	@Test
	def circleMovingParallelyToLineSegmentNoCollision {
		val c = Circle(1)
		val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		val pc = Vec2D(0, 0)
		val pls = Vec2D(0, 2)
		val vc = Vec2D(2, 0)
		val vls = Vec2D(0, 0)

		assertEquals(None, test(c, ls, pc, pls, vc, vls))
	}



	@Test
	def circleMovingAwayFromLineSegmentNoCollision {
		val c = Circle(1)
		val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		val pc = Vec2D(0, 0)
		val pls = Vec2D(0, 2)
		val vc = Vec2D(0, -2)
		val vls = Vec2D(0, 0)

		assertEquals(None, test(c, ls, pc, pls, vc, vls))
	}



	@Test
	def circleMovingToButNotReachingLineSegment {
		val c = Circle(1)
		val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		val pc = Vec2D(0, -5)
		val pls = Vec2D(0, 2)
		val vc = Vec2D(0, 2)
		val vls = Vec2D(0, 0)

		assertEquals(None, test(c, ls, pc, pls, vc, vls))
	}



	@Test
	def circleMovingAndMissingLineSegment {
		val c = Circle(1)
		val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		val pc = Vec2D(-2, 0)
		val pls = Vec2D(0, 2)
		val vc = Vec2D(0, 2)
		val vls = Vec2D(0, 0)

		assertEquals(None, test(c, ls, pc, pls, vc, vls))
	}



	@Test
	def circleMovesCollision {
		val c = Circle(1)
		val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		val pc = Vec2D(2, 0)
		val pls = Vec2D(0, 2)
		val vc = Vec2D(0, 2)
		val vls = Vec2D(0, 0)

		val result = TestResult(0.5, Vec2D(2, 2), Vec2D(0, 1))

		assertEquals(Some(result), test(c, ls, pc, pls, vc, vls))
	}



	@Test
	def lineSegmentMovesNoCollision {
		val c = Circle(1)
		val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		val pc = Vec2D(0, 0)
		val pls = Vec2D(0, 2)
		val vc = Vec2D(0, 0)
		val vls = Vec2D(0, 2)

		assertEquals(None, test(c, ls, pc, pls, vc, vls))
	}



	@Test
	def lineSegmentMovesCollision {
		val c = Circle(1)
		val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		val pc = Vec2D(2, 0)
		val pls = Vec2D(0, 2)
		val vc = Vec2D(0, 0)
		val vls = Vec2D(0, -2)

		val result = TestResult(0.5, Vec2D(2, 1), Vec2D(0, 1))

		assertEquals(Some(result), test(c, ls, pc, pls, vc, vls))
	}



	@Test
	def bothMoveNoCollision {
		val c = Circle(1)
		val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		val pc = Vec2D(0, 0)
		val pls = Vec2D(0, 2)
		val vc = Vec2D(2, 0)
		val vls = Vec2D(-2, 0)

		assertEquals(None, test(c, ls, pc, pls, vc, vls))
	}



	@Test
	def bothMoveCollision {
		val c = Circle(1)
		val ls = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		val pc = Vec2D(2, -1)
		val pls = Vec2D(0, 2)
		val vc = Vec2D(0, 2)
		val vls = Vec2D(0, -2)

		val result = TestResult(0.5, Vec2D(2, 1), Vec2D(0, 1))

		assertEquals(Some(result), test(c, ls, pc, pls, vc, vls))
	}
}
