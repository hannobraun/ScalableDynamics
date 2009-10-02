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



import collision.shape.Circle
import collision.shape.LineSegment
import collision.shape.NoShape
import collision.shape.Shape
import collision.test.CircleCircleTest
import collision.test.CircleLineSegmentTest
import collision.test.ContinuousCircleCircleTest
import collision.test.ContinuousCircleLineSegmentTest
import collision.test.TestResult
import math.Vec2D

import org.specs.Specification
import org.specs.mock.Mockito
import org.specs.runner.JUnit4



class SimpleNarrowPhaseTest extends JUnit4( SimpleNarrowPhaseSpec )



object SimpleNarrowPhaseSpec extends Specification with Mockito {

	"SimpleNarrowPhase" should {
		"be a NarrowPhase." in {
			val narrowPhase = new SimpleNarrowPhase

			narrowPhase must haveSuperClass[NarrowPhase]
		}

		"use ContinuousCircleCircleTest" in {
			val narrowPhase = new SimpleNarrowPhase

			narrowPhase.testCircleCircle must haveClass[ContinuousCircleCircleTest]
		}

		"use ContinuousCircleLineSegmentTest" in {
			val narrowPhase = new SimpleNarrowPhase

			narrowPhase.testCircleLineSegment must haveClass[ContinuousCircleLineSegmentTest]
		}

		"not show a collision for two NoShapeS." in {
			val narrowPhase = new SimpleNarrowPhase

			val b1 = new Body
			b1.shape = NoShape
			val b2 = new Body
			b2.shape = NoShape

			narrowPhase( b1, b2 ) must beEqualTo( None )
		}

		"not show a collision between a circle and a NoShape that lies in the circle." in {
			val narrowPhase = new SimpleNarrowPhase

			val b1 = new Body
			b1.position = Vec2D( 0, 0 )
			b1.shape = Circle( 1 )
			val b2 = new Body
			b2.position = Vec2D( 0, 0.5 )
			b2.shape = NoShape

			narrowPhase( b1, b2 ) must beEqualTo( None )
		}

		"not show a collision between a line segment and a NoShape that lies on the line segment." in {
			val narrowPhase = new SimpleNarrowPhase

			val b1 = new Body
			b1.position = Vec2D( 0, 0 )
			b1.shape = LineSegment( Vec2D( 0, 0 ), Vec2D( 0, 1 ) )
			val b2 = new Body
			b2.position = Vec2D( 0, 0.5 )
			b2.shape = NoShape

			narrowPhase( b1, b2 ) must beEqualTo( None )
		}

		"throw an exception if an unsupported shape and a circle is passed." in {
			val narrowPhase = new SimpleNarrowPhase

			val b1 = new Body
			b1.shape = new Shape {}
			val b2 = new Body
			b2.shape = Circle( 1 )

			narrowPhase( b1, b2 ) must throwAn[IllegalArgumentException]
		}

		"throw an exception if a circle and an unsupported shape." in {
			val narrowPhase = new SimpleNarrowPhase

			val b1 = new Body
			b1.shape = Circle( 1 )
			val b2 = new Body
			b2.shape = new Shape {}

			narrowPhase( b1, b2 ) must throwAn[IllegalArgumentException]
		}

		"throw an exception if a line segment and an unsupported shape." in {
			val narrowPhase = new SimpleNarrowPhase

			val b1 = new Body
			b1.shape = LineSegment( Vec2D( 0, 0 ), Vec2D( 1, 0 ) )
			val b2 = new Body
			b2.shape = new Shape {}

			narrowPhase( b1, b2 ) must throwAn[IllegalArgumentException]
		}

		"pass the parameters to the circle-circle test if two circles are passed." in {
			val narrowPhase = new SimpleNarrowPhase

			val circle1 = Circle( 1 )
			val circle2 = Circle( 2 )
			val b1 = new Body
			b1.shape = circle1
			b1.position = Vec2D( 1, 1 ) // position before movement
			b1.position = Vec2D( 7, 7 ) // position after movement, previous position is saved
			val b2 = new Body
			b2.shape = circle2
			b2.position = Vec2D( 2, 2 )
			b2.position = Vec2D( 10, 10 )

			val circleCircleTest = mock[CircleCircleTest]
			narrowPhase.testCircleCircle = circleCircleTest
			circleCircleTest( circle1, circle2, b1.position, b2.position, b1.position -
					b1.previousPosition, b2.position - b2.previousPosition ) returns None

			narrowPhase( b1, b2 )

			circleCircleTest( circle1, circle2, b1.position, b2.position, b1.position -
					b1.previousPosition, b2.position - b2.previousPosition ) was called
		}

		"pass the parameters to the circle-line segment test if a circle and a line segment are passed." in {
			val narrowPhase = new SimpleNarrowPhase

			val circle = Circle( 1 )
			val lineSegment = LineSegment( Vec2D( -1, -1 ), Vec2D( -2, -2 ) )
			val b1 = new Body
			b1.shape = circle
			b1.position = Vec2D( 1, 1 ) // position before movement
			b1.position = Vec2D( 7, 7 ) // position after movement, previous position is saved
			val b2 = new Body
			b2.shape = lineSegment
			b2.position = Vec2D( 2, 2 )
			b2.position = Vec2D( 10, 10 )

			val circleLineSegmentTest = mock[CircleLineSegmentTest]
			narrowPhase.testCircleLineSegment = circleLineSegmentTest
			circleLineSegmentTest( circle, lineSegment, b1.position, b2.position, b1.position -
					b1.previousPosition, b2.position - b2.previousPosition ) returns None

			narrowPhase( b1, b2 )

			circleLineSegmentTest( circle, lineSegment, b1.position, b2.position, b1.position -
					b1.previousPosition, b2.position - b2.previousPosition ) was called
		}

		"construct a correct Collision instance from the circle-circle test result." in {
			val narrowPhase = new SimpleNarrowPhase

			val circle1 = Circle( 1 )
			val circle2 = Circle( 2 )
			val b1 = new Body
			b1.shape = circle1
			val b2 = new Body
			b2.shape = circle2

			val circleCircleTest = mock[CircleCircleTest]
			circleCircleTest( circle1, circle2, b1.position, b2.position, b1.position - b1.previousPosition,
					b2.position - b2.previousPosition ) returns
					Some( TestResult( 0.5, Vec2D( 5, 5 ), Vec2D( 1, 0 ) ) )
			narrowPhase.testCircleCircle = circleCircleTest

			val expected = Collision( 0.5, Contact( b1, Vec2D( 5, 5 ), Vec2D( 1, 0 ), b2 ),
					Contact( b2, Vec2D( 5, 5 ), Vec2D( -1, 0 ), b1 ) )

			narrowPhase( b1, b2 ) must beEqualTo( Some( expected ) )
		}

		"construct a correct Collision instance from the cirlce - line segment test result." in {
			val narrowPhase = new SimpleNarrowPhase

			val circle = Circle( 1 )
			val lineSegment = LineSegment( Vec2D( 1, 1 ), Vec2D( 2, 2 ) )
			val b1 = new Body
			b1.shape = circle
			val b2 = new Body
			b2.shape = lineSegment

			val circleLineSegmentTest = mock[CircleLineSegmentTest]
			circleLineSegmentTest( circle, lineSegment, b1.position, b2.position,
					b1.position - b1.previousPosition, b2.position - b2.previousPosition ) returns
					Some( TestResult( 0.5, Vec2D( 5, 5 ), Vec2D( 1, 0 ) ) )
			narrowPhase.testCircleLineSegment = circleLineSegmentTest

			val expected = Collision( 0.5, Contact( b1, Vec2D( 5, 5 ), Vec2D( 1, 0 ), b2 ),
				Contact( b2, Vec2D( 5, 5 ), Vec2D( -1, 0 ), b1 ) )

			narrowPhase( b1, b2 ) must beEqualTo( Some( expected ) )
		}

		"return None if the circle-circle test result is None." in {
			val narrowPhase = new SimpleNarrowPhase

			val circle1 = Circle( 1 )
			val circle2 = Circle( 2 )
			val b1 = new Body
			b1.shape = circle1
			val b2 = new Body
			b2.shape = circle2

			val circleCircleTest = mock[CircleCircleTest]
			circleCircleTest( circle1, circle2, b1.position, b2.position, b1.position - b1.previousPosition,
					b2.position - b2.previousPosition ) returns None
			narrowPhase.testCircleCircle = circleCircleTest

			narrowPhase( b1, b2 ) must beEqualTo( None )
		}

		"return None if the circle - line segment test result is None." in {
			val narrowPhase = new SimpleNarrowPhase

			val circle = Circle( 1 )
			val lineSegment = LineSegment( Vec2D( 1, 1 ), Vec2D( 2, 2 ) )
			val b1 = new Body
			b1.shape = circle
			val b2 = new Body
			b2.shape = lineSegment

			val circleLineSegmentTest = mock[CircleLineSegmentTest]
			circleLineSegmentTest( circle, lineSegment, b1.position, b2.position,
					b1.position - b1.previousPosition, b2.position - b2.previousPosition ) returns None
			narrowPhase.testCircleLineSegment = circleLineSegmentTest

			narrowPhase( b1, b2 ) must beEqualTo( None )
		}

		"return None if two line segments are passed." in {
			val narrowPhase = new SimpleNarrowPhase

			val b1 = new Body
			b1.position = Vec2D( 0, 1 )
			b1.shape = LineSegment( Vec2D( 0, 0 ), Vec2D( 2, -2 ) )
			val b2 = new Body
			b2.position = Vec2D( 0, -1 )
			b2.shape = LineSegment( Vec2D( 0, 0 ), Vec2D( 2, 2 ) )

			narrowPhase( b1, b2 ) must beEqualTo( None )
		}
	}
}
