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



package net.habraun.scd



import org.junit._
import org.junit.Assert._



class SimpleNarrowPhaseTest {

	@Test
	def verifyIsNarrowPhase {
		val narrowPhase = new SimpleNarrowPhase
		assertTrue(narrowPhase.isInstanceOf[NarrowPhase])
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
	def inspectTwoStationaryCirclesExpectNoCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(0, 0)
		b1.shape = Circle(1)
		val b2 = new Body
		b2.position = Vec2D(3, 0)
		b2.shape = Circle(1)

		assertEquals(None, narrowPhase.inspectCollision(2.0, b1, b2))
	}



	@Test
	def inspectTwoStationaryCirclesExpectCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(0, 0)
		b1.shape = Circle(2)
		val b2 = new Body
		b2.position = Vec2D(3, 0)
		b2.shape = Circle(2)

		val expectedCollision = Collision(0.0, Contact(b1, b2, Vec2D(1, 0), Vec2D(-1, 0), Vec2D(0, 0)))

		assertEquals(Some(expectedCollision), narrowPhase.inspectCollision(2.0, b1, b2))
	}



	@Test
	def inspectTwoCirclesOneMovingExpectCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(-1, 0)
		b1.velocity = Vec2D(1, 0)
		b1.shape = Circle(1)
		val b2 = new Body
		b2.position = Vec2D(2, 0)
		b2.shape = Circle(1)

		val expectedCollision = Collision(0.5, Contact(b1, b2, Vec2D(1, 0), Vec2D(-1, 0), Vec2D(1, 0)))

		assertEquals(Some(expectedCollision), narrowPhase.inspectCollision(2.0, b1, b2))
	}



	@Test
	def inspectTwoCirclesOneMovingExpectNoCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.position = Vec2D(-3, 0)
		b1.velocity = Vec2D(1, 0)
		b1.shape = Circle(1)
		val b2 = new Body
		b2.position = Vec2D(2, 0)
		b2.shape = Circle(1)

		assertEquals(None, narrowPhase.inspectCollision(2.0, b1, b2))
	}



	@Test
	def inspectTwoMovingCirclesExpectCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.shape = Circle(1)
		b1.position = Vec2D(-3, 0)
		b1.velocity = Vec2D(1, 0)
		val b2 = new Body
		b2.shape = Circle(1)
		b2.position = Vec2D(1, 0)
		b2.velocity = Vec2D(-1, 0)

		val expectedCollision = Collision(0.5, Contact(b1, b2, Vec2D(1, 0), Vec2D(-1, 0), Vec2D(-1, 0)))

		assertEquals(Some(expectedCollision), narrowPhase.inspectCollision(2.0, b1, b2))
	}



	@Test
	def inspectTwoMovingCirclesExpectNoCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.shape = Circle(1)
		b1.position = Vec2D(-3, 0)
		b1.velocity = Vec2D(1, 0)
		val b2 = new Body
		b2.shape = Circle(1)
		b2.position = Vec2D(4, 0)
		b2.velocity = Vec2D(-1, 0)

		assertEquals(None, narrowPhase.inspectCollision(2.0, b1, b2))
	}



	@Test
	def inspectTwoMovingCirclesExpectNoCollision2 {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.shape = Circle(1)
		b1.position = Vec2D(-2, 0)
		b1.velocity = Vec2D(-1, 0)
		val b2 = new Body
		b2.shape = Circle(1)
		b2.position = Vec2D(2, 0)
		b2.velocity = Vec2D(1, 0)

		assertEquals(None, narrowPhase.inspectCollision(2.0, b1, b2))
	}



	@Test
	def inspectTwoMovingCirclesWithIntersectingCoursesExpectNoCollision {
		val narrowPhase = new SimpleNarrowPhase

		val b1 = new Body
		b1.shape = Circle(1)
		b1.position = Vec2D(0, 0)
		b1.velocity = Vec2D(0, 5)
		val b2 = new Body
		b2.shape = Circle(1)
		b2.position = Vec2D(5, 0)
		b2.velocity = Vec2D(-5, 0)

		assertEquals(None, narrowPhase.inspectCollision(2.0, b1, b2))
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
