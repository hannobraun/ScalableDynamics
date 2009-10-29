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



package net.habraun.sd.collision.phase



import core.Body
import math.Vec2D
import shape.Circle
import shape.Contact
import shape.LineSegment
import shape.Shape
import test.CircleCircleTest
import test.CircleLineSegmentTest
import test.ContinuousCircleCircleTest
import test.ContinuousCircleLineSegmentTest
import test.TestResult

import org.specs.Specification
import org.specs.mock.Mockito
import org.specs.runner.JUnit4



class SimpleNarrowPhaseTest extends JUnit4( SimpleNarrowPhaseSpec )



object SimpleNarrowPhaseSpec extends Specification with Mockito {

	"SimpleNarrowPhase" should {
		"be a NarrowPhase." in {
			val narrowPhase = new SimpleNarrowPhase

			narrowPhase must haveSuperClass[ NarrowPhase ]
		}

		"use ContinuousCircleCircleTest" in {
			val narrowPhase = new SimpleNarrowPhase

			narrowPhase.testCircleCircle must haveClass[ ContinuousCircleCircleTest ]
		}

		"use ContinuousCircleLineSegmentTest" in {
			val narrowPhase = new SimpleNarrowPhase

			narrowPhase.testCircleLineSegment must haveClass[ ContinuousCircleLineSegmentTest ]
		}

		"throw an exception if an unsupported shape and a circle are passed." in {
			val narrowPhase = new SimpleNarrowPhase

			val circle = new Circle {}

			narrowPhase( new Shape {}, circle ) must throwAn[ IllegalArgumentException ]
		}

		"throw an exception if a circle and an unsupported shape are passed." in {
			val narrowPhase = new SimpleNarrowPhase

			val circle = new Circle {}

			narrowPhase( circle, new Shape {} ) must throwAn[ IllegalArgumentException ]
		}

		"throw an exception if a line segment and an unsupported shape are passed." in {
			val narrowPhase = new SimpleNarrowPhase

			val lineSegment = new LineSegment {}

			narrowPhase( lineSegment, new Shape {} ) must
					throwAn[ IllegalArgumentException ]
		}

		"pass the parameters to the circle-circle test if two circles are passed." in {
			val narrowPhase = new SimpleNarrowPhase

			val s1 = new Circle {}
			s1.radius = 1
			s1.position = Vec2D( 1, 1 ) // position before movement
			s1.position = Vec2D( 7, 7 ) // position after movement, previous position is saved
			val s2 = new Circle {}
			s2.radius = 2
			s2.position = Vec2D( 2, 2 )
			s2.position = Vec2D( 10, 10 )

			val circleCircleTest = mock[ CircleCircleTest ]
			narrowPhase.testCircleCircle = circleCircleTest
			circleCircleTest( s1, s2 ) returns None

			narrowPhase( s1, s2 )

			circleCircleTest( s1, s2 ) was called
		}

		"pass the parameters to the circle-line segment test if a circle and a line segment are passed." in {
			val narrowPhase = new SimpleNarrowPhase

			val s1 = new Circle {}
			s1.radius = 1
			s1.position = Vec2D( 1, 1 ) // position before movement
			s1.position = Vec2D( 7, 7 ) // position after movement, previous position is saved
			val s2 = new LineSegment {}
			s2.p = Vec2D( -1, -1 )
			s2.d = Vec2D( -2, -2 )
			s2.position = Vec2D( 2, 2 )
			s2.position = Vec2D( 10, 10 )

			val circleLineSegmentTest = mock[CircleLineSegmentTest]
			narrowPhase.testCircleLineSegment = circleLineSegmentTest
			circleLineSegmentTest( s1, s2 ) returns None

			narrowPhase( s1, s2 )

			circleLineSegmentTest( s1, s2 ) was called
		}

		"pass the parameters to the circle-line segment test if a line segment and a circle are passed." in {
			val narrowPhase = new SimpleNarrowPhase

			val s1 = new LineSegment {}
			s1.p = Vec2D( -1, -1 )
			s1.d = Vec2D( -2, -2 )
			s1.position = Vec2D( 2, 2 )
			s1.position = Vec2D( 10, 10 )
			val s2 = new Circle {}
			s2.radius = 1
			s2.position = Vec2D( 1, 1 ) // position before movement
			s2.position = Vec2D( 7, 7 ) // position after movement, previous position is saved

			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			narrowPhase.testCircleLineSegment = circleLineSegmentTest
			circleLineSegmentTest( s2, s1 ) returns None

			narrowPhase( s1, s2 )

			circleLineSegmentTest( s2, s1 ) was called
		}

		"construct a correct Collision instance from the circle-circle test result." in {
			val narrowPhase = new SimpleNarrowPhase

			val s1 = new Circle {}
			val s2 = new Circle {}
			s1.radius = 1
			s2.radius = 2

			val circleCircleTest = mock[ CircleCircleTest ]
			circleCircleTest( s1, s2 ) returns
					Some( TestResult( 0.5, Vec2D( 5, 5 ), Vec2D( 1, 0 ), 0 ) )
			narrowPhase.testCircleCircle = circleCircleTest

			val expected = Collision( 0.5, Contact( s1, Vec2D( 5, 5 ), Vec2D( 1, 0 ), s2 ),
					Contact( s2, Vec2D( 5, 5 ), Vec2D( -1, 0 ), s1 ) )

			narrowPhase( s1, s2 ) must beEqualTo( Some( expected ) )
		}

		"construct a correct Collision instance from the cirlce - line segment test result." in {
			val narrowPhase = new SimpleNarrowPhase

			val s1 = new Circle {}
			val s2 = new LineSegment {}
			s1.radius = 1
			s2.p = Vec2D( 1, 1 )
			s2.d = Vec2D( 2, 2 )

			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			circleLineSegmentTest( s1, s2 ) returns
					Some( TestResult( 0.5, Vec2D( 5, 5 ), Vec2D( 1, 0 ), 0 ) )
			narrowPhase.testCircleLineSegment = circleLineSegmentTest

			val expected = Collision( 0.5, Contact( s1, Vec2D( 5, 5 ), Vec2D( 1, 0 ), s2 ),
				Contact( s2, Vec2D( 5, 5 ), Vec2D( -1, 0 ), s1 ) )

			narrowPhase( s1, s2 ) must beEqualTo( Some( expected ) )
		}

		"construct a correct Collision instance from the line segment - circle test result." in {
			val narrowPhase = new SimpleNarrowPhase

			val s1 = new LineSegment {}
			val s2 = new Circle {}
			s1.p = Vec2D( 1, 1 )
			s1.d = Vec2D( 2, 2 )
			s2.radius = 1

			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			circleLineSegmentTest( s2, s1 ) returns
					Some( TestResult( 0.5, Vec2D( 5, 5 ), Vec2D( 1, 0 ), 0 ) )
			narrowPhase.testCircleLineSegment = circleLineSegmentTest

			val expected = Collision( 0.5, Contact( s1, Vec2D( 5, 5 ), Vec2D( 1, 0 ), s2 ),
				Contact( s2, Vec2D( 5, 5 ), Vec2D( -1, 0 ), s1 ) )

			narrowPhase( s1, s2 ) must beEqualTo( Some( expected ) )
		}

		"return None if the circle-circle test result is None." in {
			val narrowPhase = new SimpleNarrowPhase

			val s1 = new Circle {}
			val s2 = new Circle {}
			s1.radius = 1
			s2.radius = 2

			val circleCircleTest = mock[ CircleCircleTest ]
			circleCircleTest( s1, s2 ) returns None
			narrowPhase.testCircleCircle = circleCircleTest

			narrowPhase( s1, s2 ) must beEqualTo( None )
		}

		"return None if the circle - line segment test result is None." in {
			val narrowPhase = new SimpleNarrowPhase

			val s1 = new Circle {}
			val s2 = new LineSegment {}
			s1.radius = 1
			s2.p = Vec2D( 1, 1 )
			s2.d = Vec2D( 2, 2 )

			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			circleLineSegmentTest( s1, s2 ) returns None
			narrowPhase.testCircleLineSegment = circleLineSegmentTest

			narrowPhase( s1, s2 ) must beEqualTo( None )
		}

		"return None if two line segments are passed." in {
			val narrowPhase = new SimpleNarrowPhase

			val s1 = new LineSegment {}
			val s2 = new LineSegment {}
			s1.position = Vec2D( 0, 1 )
			s2.position = Vec2D( 0, -1 )
			s1.p = Vec2D( 0, 0 )
			s2.p = Vec2D( 0, 0 )
			s1.d = Vec2D( 2, -2 )
			s2.d = Vec2D( 2, 2 )

			narrowPhase( s1, s2 ) must beEqualTo( None )
		}
	}
}
