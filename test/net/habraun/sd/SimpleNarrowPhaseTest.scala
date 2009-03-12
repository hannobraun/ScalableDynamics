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



import collision._
import math._

import org.junit._
import org.junit.Assert._



class SimpleNarrowPhaseTest {

	var narrowPhase: NarrowPhase = null



	@Before
	def setup {
		narrowPhase = new SimpleNarrowPhase
	}



	@Test
	def verifyUsesContinuousCircleCircleTest {
		val simpleNarrowPhase = new SimpleNarrowPhase
		assertTrue(simpleNarrowPhase.testCircleCircle.isInstanceOf[ContinuousCircleCircleTest])
	}



	@Test
	def verifyUsesContinuousCircleLineSegmentTest {
		val simpleNarrowPhase = new SimpleNarrowPhase
		assertTrue(simpleNarrowPhase.testCircleLineSegment.isInstanceOf[ContinuousCircleLineSegmentTest])
	}



	@Test
	def inspectTwoNoShapesExpectNoCollision {
		val b1 = new Body
		b1.shape = NoShape
		val b2 = new Body
		b2.shape = NoShape

		assertEquals(None, narrowPhase.inspectCollision(0.0, b1, b2))
	}



	@Test
	def inspectCircleAndNoShapeExpectNoCollision {
		val b1 = new Body
		b1.position = Vec2D(0, 0)
		b1.shape = Circle(1)
		val b2 = new Body
		b2.position = Vec2D(0, 0.5)
		b2.shape = NoShape

		assertEquals(None, narrowPhase.inspectCollision(0.0, b1, b2))
	}



	@Test
	def inspectTwoCirclesVerifyParametersArePassed {
		val test = new CircleCircleTest {
			var c1: Circle = null
			var c2: Circle = null
			var p1: Vec2D = null
			var p2: Vec2D = null
			var v1: Vec2D = null
			var v2: Vec2D = null
			def apply (_c1: Circle, _c2: Circle, _p1: Vec2D, _p2: Vec2D, _v1: Vec2D, _v2: Vec2D) = {
				c1 = _c1; c2 = _c2; p1 = _p1; p2 = _p2; v1 = _v1; v2 = _v2
				None
			}
		}

		val simpleNarrowPhase = new SimpleNarrowPhase
		simpleNarrowPhase.testCircleCircle = test

		val b1 = new Body
		val b2 = new Body
		b1.shape = Circle(1)
		b2.shape = Circle(2)
		b1.position = Vec2D(1, 1)
		b2.position = Vec2D(2, 2)
		b1.velocity = Vec2D(3, 3)
		b2.velocity = Vec2D(4, 4)

		val delta = 2.0

		simpleNarrowPhase.inspectCollision(delta, b1, b2)

		assertEquals(b1.shape, test.c1)
		assertEquals(b2.shape, test.c2)
		assertEquals(b1.position, test.p1)
		assertEquals(b2.position, test.p2)
		assertEquals(b1.velocity * delta, test.v1)
		assertEquals(b2.velocity * delta, test.v2)
	}



	@Test
	def inspectTwoCirclesExpectCollisionIsCorrect {
		val simpleNarrowPhase = new SimpleNarrowPhase
		simpleNarrowPhase.testCircleCircle = new CircleCircleTest {
			def apply(c1: Circle, c2: Circle, p1: Vec2D, p2: Vec2D, v1: Vec2D, v2: Vec2D) = {
				Some(TestResult(0.5, Vec2D(5, 5), Vec2D(1, 0)))
			}
		}

		val b1 = new Body
		val b2 = new Body
		b1.shape = Circle(1)
		b2.shape = Circle(1)

		val collision = simpleNarrowPhase.inspectCollision(2.0, b1, b2)

		val expected = Collision(0.5, Contact(b1, Vec2D(5, 5), Vec2D(1, 0), b2),
				Contact(b2, Vec2D(5, 5), Vec2D(-1, 0), b1))

		assertEquals(Some(expected), collision)
	}



	@Test
	def inspectTwoCirclesExpectNone {
		val simpleNarrowPhase = new SimpleNarrowPhase
		simpleNarrowPhase.testCircleCircle = new CircleCircleTest {
			def apply(c1: Circle, c2: Circle, p1: Vec2D, p2: Vec2D, v1: Vec2D, v2: Vec2D) = {
				None
			}
		}

		val b1 = new Body
		val b2 = new Body
		b1.shape = Circle(1)
		b2.shape = Circle(1)

		val collision = simpleNarrowPhase.inspectCollision(2.0, b1, b2)

		assertEquals(None, collision)
	}



	@Test
	def inspectCircleLineSegmentVerifyParametersArePassed {
		val test = new CircleLineSegmentTest {
			var c: Circle = null
			var ls: LineSegment = null
			var pc: Vec2D = null
			var pls: Vec2D = null
			var vc: Vec2D = null
			var vls: Vec2D = null
			def apply (_c: Circle, _ls: LineSegment, _pc: Vec2D, _pls: Vec2D, _vc: Vec2D, _vls: Vec2D) = {
				c = _c; ls = _ls; pc = _pc; pls = _pls; vc = _vc; vls = _vls
				None
			}
		}

		val simpleNarrowPhase = new SimpleNarrowPhase
		simpleNarrowPhase.testCircleLineSegment = test

		val b1 = new Body
		val b2 = new Body
		b1.shape = Circle(1)
		b2.shape = LineSegment(Vec2D(-1, -1), Vec2D(-2, -2))
		b1.position = Vec2D(1, 1)
		b2.position = Vec2D(2, 2)
		b1.velocity = Vec2D(3, 3)
		b2.velocity = Vec2D(4, 4)

		val delta = 2.0

		simpleNarrowPhase.inspectCollision(delta, b1, b2)

		assertEquals(b1.shape, test.c)
		assertEquals(b2.shape, test.ls)
		assertEquals(b1.position, test.pc)
		assertEquals(b2.position, test.pls)
		assertEquals(b1.velocity * delta, test.vc)
		assertEquals(b2.velocity * delta, test.vls)
	}



	@Test
	def inspectLineSegmentCircleVerifyParametersArePassed {
		val test = new CircleLineSegmentTest {
			var c: Circle = null
			var ls: LineSegment = null
			var pc: Vec2D = null
			var pls: Vec2D = null
			var vc: Vec2D = null
			var vls: Vec2D = null
			def apply (_c: Circle, _ls: LineSegment, _pc: Vec2D, _pls: Vec2D, _vc: Vec2D, _vls: Vec2D) = {
				c = _c; ls = _ls; pc = _pc; pls = _pls; vc = _vc; vls = _vls
				None
			}
		}

		val simpleNarrowPhase = new SimpleNarrowPhase
		simpleNarrowPhase.testCircleLineSegment = test

		val b1 = new Body
		val b2 = new Body
		b1.shape = Circle(1)
		b2.shape = LineSegment(Vec2D(-1, -1), Vec2D(-2, -2))
		b1.position = Vec2D(1, 1)
		b2.position = Vec2D(2, 2)
		b1.velocity = Vec2D(3, 3)
		b2.velocity = Vec2D(4, 4)

		val delta = 2.0

		simpleNarrowPhase.inspectCollision(delta, b2, b1)

		assertEquals(b1.shape, test.c)
		assertEquals(b2.shape, test.ls)
		assertEquals(b1.position, test.pc)
		assertEquals(b2.position, test.pls)
		assertEquals(b1.velocity * delta, test.vc)
		assertEquals(b2.velocity * delta, test.vls)
	}



	@Test
	def inspectCircleLineSegmentExpectCollisionIsCorrect {
		val simpleNarrowPhase = new SimpleNarrowPhase
		simpleNarrowPhase.testCircleLineSegment = new CircleLineSegmentTest {
			def apply(c: Circle, ls: LineSegment, pc: Vec2D, pls: Vec2D, vc: Vec2D, vls: Vec2D) = {
				Some(TestResult(0.5, Vec2D(5, 5), Vec2D(1, 0)))
			}
		}

		val b1 = new Body
		val b2 = new Body
		b1.shape = Circle(1)
		b2.shape = LineSegment(Vec2D(1, 1), Vec2D(2, 2))

		val collision = simpleNarrowPhase.inspectCollision(2.0, b1, b2)

		val expected = Collision(0.5, Contact(b1, Vec2D(5, 5), Vec2D(1, 0), b2),
				Contact(b2, Vec2D(5, 5), Vec2D(-1, 0), b1))

		assertEquals(Some(expected), collision)
	}



	@Test
	def inspectCircleLineSegmentExpectNone {
		val simpleNarrowPhase = new SimpleNarrowPhase
		simpleNarrowPhase.testCircleLineSegment = new CircleLineSegmentTest {
			def apply(c: Circle, ls: LineSegment, pc: Vec2D, pls: Vec2D, vc: Vec2D, vls: Vec2D) = {
				None
			}
		}

		val b1 = new Body
		val b2 = new Body
		b1.shape = Circle(1)
		b2.shape = LineSegment(Vec2D(1, 1), Vec2D(2, 2))

		val collision = simpleNarrowPhase.inspectCollision(2.0, b1, b2)

		assertEquals(None, collision)
	}



	@Test
	def inspectTwoLineSegmentsExpectNoCollisionEvenIfTheyCollide {
		val b1 = new Body
		b1.position = Vec2D(0, 1)
		b1.shape = LineSegment(Vec2D(0, 0), Vec2D(2, -2))
		val b2 = new Body
		b2.position = Vec2D(0, -1)
		b2.shape = LineSegment(Vec2D(0, 0), Vec2D(2, 2))

		assertEquals(None, narrowPhase.inspectCollision(2.0, b1, b2))
	}
}
