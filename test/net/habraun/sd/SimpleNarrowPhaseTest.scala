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

	@Test
	def verifyIsNarrowPhase {
		val narrowPhase = new SimpleNarrowPhase
		assertTrue(narrowPhase.isInstanceOf[NarrowPhase])
	}



	@Test
	def verifyUsesContinuousCircleCircleTest {
		val narrowPhase = new SimpleNarrowPhase
		assertTrue(narrowPhase.testCircleCircle.isInstanceOf[ContinuousCircleCircleTest])
	}



	@Test
	def inspectTwoNoShapesExpectNoCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.shape = NoShape
		val b2 = new Body
		b2.shape = NoShape

		assertEquals(None, narrowPhase.inspectCollision(0.0, b1, b2))
	}



	@Test
	def inspectCircleAndNoShapeExpectNoCollision {
		val narrowPhase = new SimpleNarrowPhase

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
			override def apply (_c1: Circle, _c2: Circle, _p1: Vec2D, _p2: Vec2D, _v1: Vec2D, _v2: Vec2D) = {
				c1 = _c1; c2 = _c2; p1 = _p1; p2 = _p2; v1 = _v1; v2 = _v2
				None
			}
		}

		val narrowPhase = new SimpleNarrowPhase
		narrowPhase.testCircleCircle = test

		val b1 = new Body
		val b2 = new Body
		b1.shape = Circle(1)
		b2.shape = Circle(2)
		b1.position = Vec2D(1, 1)
		b2.position = Vec2D(2, 2)
		b1.velocity = Vec2D(3, 3)
		b2.velocity = Vec2D(4, 4)

		val delta = 2.0

		narrowPhase.inspectCollision(delta, b1, b2)

		assertEquals(b1.shape, test.c1)
		assertEquals(b2.shape, test.c2)
		assertEquals(b1.position, test.p1)
		assertEquals(b2.position, test.p2)
		assertEquals(b1.velocity * delta, test.v1)
		assertEquals(b2.velocity * delta, test.v2)
	}



	@Test
	def inspectTwoCirclesExpectCollisionIsCorrect {
		val narrowPhase = new SimpleNarrowPhase
		narrowPhase.testCircleCircle = new CircleCircleTest {
			def apply(c1: Circle, c2: Circle, p1: Vec2D, p2: Vec2D, v1: Vec2D, v2: Vec2D) = {
				Some(TestResult(0.5, Vec2D(5, 5), Vec2D(1, 0)))
			}
		}

		val b1 = new Body
		val b2 = new Body
		b1.shape = Circle(1)
		b2.shape = Circle(1)

		val collision = narrowPhase.inspectCollision(2.0, b1, b2)

		val expected = Collision(0.5, Contact(b1, b2, Vec2D(1, 0), Vec2D(-1, 0), Vec2D(5, 5)))

		assertEquals(Some(expected), collision)
	}



	@Test
	def inspectTwoCirclesExpectNone {
		val narrowPhase = new SimpleNarrowPhase
		narrowPhase.testCircleCircle = new CircleCircleTest {
			def apply(c1: Circle, c2: Circle, p1: Vec2D, p2: Vec2D, v1: Vec2D, v2: Vec2D) = {
				None
			}
		}

		val b1 = new Body
		val b2 = new Body
		b1.shape = Circle(1)
		b2.shape = Circle(1)

		val collision = narrowPhase.inspectCollision(2.0, b1, b2)

		assertEquals(None, collision)
	}



	@Test
	def inspectStationaryCircleAndLineSegmentExpectNoCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(0, 0)
		b1.shape = Circle(1)
		val b2 = new Body
		b2.position = Vec2D(0, 2)
		b2.shape = LineSegment(Vec2D(-1, 0), Vec2D(2, 0))

		assertEquals(None, narrowPhase.inspectCollision(2.0, b1, b2))
	}



	@Test
	def inspectStationaryLineSegmentAndCircleExpectNoCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(0, 0)
		b1.shape = Circle(1)
		val b2 = new Body
		b2.position = Vec2D(0, 2)
		b2.shape = LineSegment(Vec2D(-1, 0), Vec2D(2, 0))

		assertEquals(None, narrowPhase.inspectCollision(2.0, b2, b1))
	}



	@Test
	def inspectStationaryCircleAndLineSegmentExpectCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(0, 0)
		b1.shape = Circle(1)
		val b2 = new Body
		b2.position = Vec2D(0, 0.5)
		b2.shape = LineSegment(Vec2D(-1, 0), Vec2D(2, 0))

		val expectedCollision = Collision(0.0, Contact(b1, b2, Vec2D(0, 1), Vec2D(0, -1), Vec2D(0, 0)))

		assertEquals(Some(expectedCollision), narrowPhase.inspectCollision(2.0, b1, b2))
	}



	@Test
	def inspectStationaryCircleAndLineSegmentWithCircleOnLineExpectNoCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(0, 0)
		b1.shape = Circle(1)
		val b2 = new Body
		b2.position = Vec2D(2, 0)
		b2.shape = LineSegment(Vec2D(0, 0), Vec2D(2, 0))

		assertEquals(None, narrowPhase.inspectCollision(2.0, b1, b2))
	}



	@Test
	def inspectStationaryLineSegmentAndCirlceExpectCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(0, 0)
		b1.shape = Circle(1)
		val b2 = new Body
		b2.position = Vec2D(0, 0.5)
		b2.shape = LineSegment(Vec2D(-1, 0), Vec2D(2, 0))

		val expectedCollision = Collision(0.0, Contact(b1, b2, Vec2D(0, 1), Vec2D(0, -1), Vec2D(0, 0)))

		assertEquals(Some(expectedCollision), narrowPhase.inspectCollision(2.0, b2, b1))
	}



	@Test
	def inspectMovingCircleAndStationaryLineSegmentExpectNoCollision1 {
		// Circle moving parallel to the line segment.
		val narrowPhase = new SimpleNarrowPhase

		val circle = new Body
		circle.shape = Circle(1)
		circle.position = Vec2D(0, 0)
		circle.velocity = Vec2D(1, 0)
		val segment = new Body
		segment.shape = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		segment.position = Vec2D(0, 2)

		assertEquals(None, narrowPhase.inspectCollision(2.0, circle, segment))
	}



	@Test
	def inspectMovingCircleAndStationaryLineSegmentExpectNoCollision2 {
		// Circle moving away from the line segment.
		val narrowPhase = new SimpleNarrowPhase

		val circle = new Body
		circle.shape = Circle(1)
		circle.position = Vec2D(0, 0)
		circle.velocity = Vec2D(0, -1)
		val segment = new Body
		segment.shape = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		segment.position = Vec2D(0, 2)

		assertEquals(None, narrowPhase.inspectCollision(2.0, circle, segment))
	}



	@Test
	def inspectMovingCircleAndStationaryLineSegmentExpectNoCollision3 {
		// Circle stopping before reaching the line segment.
		val narrowPhase = new SimpleNarrowPhase

		val circle = new Body
		circle.shape = Circle(1)
		circle.position = Vec2D(0, -5)
		circle.velocity = Vec2D(0, 1)
		val segment = new Body
		segment.shape = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		segment.position = Vec2D(0, 2)

		assertEquals(None, narrowPhase.inspectCollision(2.0, circle, segment))
	}



	@Test
	def inspectMovingCircleAndStationaryLineSegmentExpectNoCollision4 {
		// Circle missing the line segment.
		val narrowPhase = new SimpleNarrowPhase

		val circle = new Body
		circle.shape = Circle(1)
		circle.position = Vec2D(-2, 0)
		circle.velocity = Vec2D(0, 1)
		val segment = new Body
		segment.shape = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		segment.position = Vec2D(0, 2)

		assertEquals(None, narrowPhase.inspectCollision(2.0, circle, segment))
	}



	@Test
	def inspectMovingCircleAndStationaryLineSegmentExpectCollision {
		val narrowPhase = new SimpleNarrowPhase

		val circle = new Body
		circle.shape = Circle(1)
		circle.position = Vec2D(2, 0)
		circle.velocity = Vec2D(0, 1)
		val segment = new Body
		segment.shape = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		segment.position = Vec2D(0, 2)

		val expectedCollision = Collision(0.5, Contact(circle, segment, Vec2D(0, 1), Vec2D(0, -1),
				Vec2D(2, 2)))

		assertEquals(Some(expectedCollision), narrowPhase.inspectCollision(2.0, circle, segment))
	}



	@Test
	def inspectStationaryCircleAndMovingLineSegmentExpectNoCollision {
		val narrowPhase = new SimpleNarrowPhase

		val circle = new Body
		circle.shape = Circle(1)
		circle.position = Vec2D(0, 0)
		val segment = new Body
		segment.shape = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		segment.position = Vec2D(0, 2)
		segment.velocity = Vec2D(0, 1)

		assertEquals(None, narrowPhase.inspectCollision(2.0, circle, segment))
	}



	@Test
	def inspectStationaryCircleAndMovingLineSegmentExpectCollision {
		val narrowPhase = new SimpleNarrowPhase

		val circle = new Body
		circle.shape = Circle(1)
		circle.position = Vec2D(2, 0)
		val segment = new Body
		segment.shape = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		segment.position = Vec2D(0, 2)
		segment.velocity = Vec2D(0, -1)

		val expectedCollision = Collision(0.5, Contact(circle, segment, Vec2D(0, 1), Vec2D(0, -1),
				Vec2D(2, 1)))

		assertEquals(Some(expectedCollision), narrowPhase.inspectCollision(2.0, circle, segment))
	}



	@Test
	def inspectMovingCircleAndLineSegmentExpectNoCollision {
		val narrowPhase = new SimpleNarrowPhase

		val circle = new Body
		circle.shape = Circle(1)
		circle.position = Vec2D(0, 0)
		circle.velocity = Vec2D(1, 0)
		val segment = new Body
		segment.shape = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		segment.position = Vec2D(0, 2)
		segment.velocity = Vec2D(-1, 0)

		assertEquals(None, narrowPhase.inspectCollision(2.0, circle, segment))
	}



	@Test
	def inspectMovingCircleAndLineSegmentExpectCollision {
		val narrowPhase = new SimpleNarrowPhase

		val circle = new Body
		circle.shape = Circle(1)
		circle.position = Vec2D(2, -1)
		circle.velocity = Vec2D(0, 1)
		val segment = new Body
		segment.shape = LineSegment(Vec2D(0, 0), Vec2D(5, 0))
		segment.position = Vec2D(0, 2)
		segment.velocity = Vec2D(0, -1)

		val expectedCollision = Collision(0.5, Contact(circle, segment, Vec2D(0, 1), Vec2D(0, -1),
				Vec2D(2, 1)))

		assertEquals(Some(expectedCollision), narrowPhase.inspectCollision(2.0, circle, segment))
	}



	@Test
	def inspectTwoLineSegmentsExpectNoCollisionEvenIfTheyCollide {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(0, 1)
		b1.shape = LineSegment(Vec2D(0, 0), Vec2D(2, -2))
		val b2 = new Body
		b2.position = Vec2D(0, -1)
		b2.shape = LineSegment(Vec2D(0, 0), Vec2D(2, 2))

		assertEquals(None, narrowPhase.inspectCollision(2.0, b1, b2))
	}
}
