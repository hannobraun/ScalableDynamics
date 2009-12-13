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

import org.specs.Specification
import org.specs.mock.Mockito
import org.specs.runner.JUnit4



class SimpleNarrowPhaseTest extends JUnit4( SimpleNarrowPhaseSpec )



object SimpleNarrowPhaseSpec extends Specification with Mockito {

	"SimpleNarrowPhase" should {
		"be a NarrowPhase." in {
			val circleCircleTest = mock[ CircleCircleTest ]
			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			val narrowPhase = new SimpleNarrowPhase( circleCircleTest, circleLineSegmentTest)

			narrowPhase must haveSuperClass[ NarrowPhase ]
		}

		"throw an exception if an unsupported shape and a circle are passed." in {
			val circleCircleTest = mock[ CircleCircleTest ]
			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			val narrowPhase = new SimpleNarrowPhase( circleCircleTest, circleLineSegmentTest)

			narrowPhase( new Shape {}, new Circle {} ) must throwAn[ IllegalArgumentException ]
		}

		"throw an exception if a circle and an unsupported shape are passed." in {
			val circleCircleTest = mock[ CircleCircleTest ]
			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			val narrowPhase = new SimpleNarrowPhase( circleCircleTest, circleLineSegmentTest)

			narrowPhase( new Circle {}, new Shape {} ) must throwAn[ IllegalArgumentException ]
		}

		"throw an exception if a line segment and an unsupported shape are passed." in {
			val circleCircleTest = mock[ CircleCircleTest ]
			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			val narrowPhase = new SimpleNarrowPhase( circleCircleTest, circleLineSegmentTest)

			narrowPhase( new LineSegment {}, new Shape {} ) must throwAn[ IllegalArgumentException ]
		}

		"pass the parameters to the circle-circle test if two circles are passed." in {
			val circleCircleTest = mock[ CircleCircleTest ]
			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			val narrowPhase = new SimpleNarrowPhase( circleCircleTest, circleLineSegmentTest)

			val s1 = new Circle {}
			val s2 = new Circle {}
			s1.radius = 1
			s2.radius = 2
			s1.position = Vec2D( 1, 1 ) // position before movement
			s2.position = Vec2D( 2, 2 )
			s1.position = Vec2D( 7, 7 ) // position after movement, previous position is saved
			s2.position = Vec2D( 10, 10 )
			
			circleCircleTest( s1, s2 ) returns None

			narrowPhase( s1, s2 )

			circleCircleTest( s1, s2 ) was called
		}

		"pass the parameters to the circle-line segment test if a circle and a line segment are passed." in {
			val circleCircleTest = mock[ CircleCircleTest ]
			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			val narrowPhase = new SimpleNarrowPhase( circleCircleTest, circleLineSegmentTest)

			val s1 = new Circle {}
			val s2 = new LineSegment {}
			s1.radius = 1
			s2.direction = Vec2D( -2, -2 )
			s1.position = Vec2D( 1, 1 ) // position before movement
			s2.position = Vec2D( 2, 2 )
			s1.position = Vec2D( 7, 7 ) // position after movement, previous position is saved
			s2.position = Vec2D( 10, 10 )

			circleLineSegmentTest( s1, s2 ) returns None

			narrowPhase( s1, s2 )

			circleLineSegmentTest( s1, s2 ) was called
		}

		"pass the parameters to the circle-line segment test if a line segment and a circle are passed." in {
			val circleCircleTest = mock[ CircleCircleTest ]
			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			val narrowPhase = new SimpleNarrowPhase( circleCircleTest, circleLineSegmentTest)

			val s1 = new LineSegment {}
			val s2 = new Circle {}
			s1.direction = Vec2D( -2, -2 )
			s2.radius = 1
			s1.position = Vec2D( 2, 2 ) // position before movement
			s2.position = Vec2D( 1, 1 )
			s1.position = Vec2D( 10, 10 ) // position after movement, previous position is saved
			s2.position = Vec2D( 7, 7 )

			circleLineSegmentTest( s2, s1 ) returns None

			narrowPhase( s1, s2 )

			circleLineSegmentTest( s2, s1 ) was called
		}

		"pass on the Contact returned by the circle-circle test." in {
			val circleCircleTest = mock[ CircleCircleTest ]
			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			val narrowPhase = new SimpleNarrowPhase( circleCircleTest, circleLineSegmentTest)

			val t = 0.5
			val s1 = new Circle {}
			val s2 = new Circle {}
			s1.radius = 1
			s2.radius = 2
			val contact = Contact( s1, s2, Vec2D( 5, 5 ), Vec2D( 1, 0 ), 1, t )

			circleCircleTest( s1, s2 ) returns Some( contact )

			narrowPhase( s1, s2 ) must beEqualTo( Some( contact ) )
		}

		"pass on the Contact returned by the cirlce - line segment test." in {
			val circleCircleTest = mock[ CircleCircleTest ]
			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			val narrowPhase = new SimpleNarrowPhase( circleCircleTest, circleLineSegmentTest)

			val t = 0.5
			val s1 = new Circle {}
			val s2 = new LineSegment {}
			s1.radius = 1
			s2.direction = Vec2D( 2, 2 )
			val contact = Contact( s1, s2, Vec2D( 5, 5 ), Vec2D( 1, 0 ), 1, t )

			circleLineSegmentTest( s1, s2 ) returns Some( contact )

			narrowPhase( s1, s2 ) must beEqualTo( Some( contact ) )
		}

		"pass on the inverse of the Contact returned by the cirlce - line segment test." in {
			val circleCircleTest = mock[ CircleCircleTest ]
			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			val narrowPhase = new SimpleNarrowPhase( circleCircleTest, circleLineSegmentTest)

			val t = 0.5
			val s1 = new LineSegment {}
			val s2 = new Circle {}
			s1.direction = Vec2D( 2, 2 )
			s2.radius = 1
			val contact = Contact( s1, s2, Vec2D( 5, 5 ), Vec2D( 1, 0 ), 1, t )

			circleLineSegmentTest( s2, s1 ) returns Some( contact )

			narrowPhase( s1, s2 ) must beEqualTo( Some( -contact ) )
		}

		"return None if the circle-circle test result is None." in {
			val circleCircleTest = mock[ CircleCircleTest ]
			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			val narrowPhase = new SimpleNarrowPhase( circleCircleTest, circleLineSegmentTest)

			val s1 = new Circle {}
			val s2 = new Circle {}
			s1.radius = 1
			s2.radius = 2

			circleCircleTest( s1, s2 ) returns None

			narrowPhase( s1, s2 ) must beEqualTo( None )
		}

		"return None if the circle - line segment test result is None." in {
			val circleCircleTest = mock[ CircleCircleTest ]
			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			val narrowPhase = new SimpleNarrowPhase( circleCircleTest, circleLineSegmentTest)

			val s1 = new Circle {}
			val s2 = new LineSegment {}
			s1.radius = 1
			s2.direction = Vec2D( 2, 2 )

			circleLineSegmentTest( s1, s2 ) returns None

			narrowPhase( s1, s2 ) must beEqualTo( None )
		}

		"return None if two line segments are passed." in {
			val circleCircleTest = mock[ CircleCircleTest ]
			val circleLineSegmentTest = mock[ CircleLineSegmentTest ]
			val narrowPhase = new SimpleNarrowPhase( circleCircleTest, circleLineSegmentTest)

			val s1 = new LineSegment {}
			val s2 = new LineSegment {}
			s1.position = Vec2D( 0, 1 )
			s2.position = Vec2D( 0, -1 )
			s1.direction = Vec2D( 2, -2 )
			s2.direction = Vec2D( 2, 2 )

			narrowPhase( s1, s2 ) must beEqualTo( None )
		}
	}
}
