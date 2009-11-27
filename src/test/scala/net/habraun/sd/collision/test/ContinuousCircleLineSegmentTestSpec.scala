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
import shape.Contact
import shape.LineSegment

import org.specs.Specification
import org.specs.mock.Mockito
import org.specs.runner.JUnit4



class ContinuousCircleLineSegmentTestTest extends JUnit4( ContinuousCircleLineSegmentTestSpec )



object ContinuousCircleLineSegmentTestSpec extends Specification with Mockito {

	"ContinuousCircleLineSegmentTest" should {
		"handle two non-moving, non-intersecting shapes." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 0 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 0, 3 )
			ls.position returns Vec2D( 0, 3 )

			test( c, ls ) must beEqualTo( None )
		}

		"handle the line segment piercing through the circle (both ends being out of the cirlce) initially." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 0 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 4, 0 )
			ls.previousPosition returns Vec2D( -2, 1.5 )
			ls.position returns Vec2D( -2, 1.5 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( 0, 1.5 ), Vec2D( 0, 1 ), 0.5, 0.0 ) ) )
		}

		"handle the beginning of the line segment piercing the circle initially." in {
			// Important: This example is set up, so that the nearest point to the circle center that lies on the line defined by the
			// segment lies outside the segment.

			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 0 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 1.5, 0 )
			ls.position returns Vec2D( 1.5, 0 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( 1.5, 0 ), Vec2D( 1, 0 ), 0.5, 0.0 ) ) )
		}

		"handle the end of the line segment piercing the circle initially." in {
			// Important: This example is set up, so that the nearest point to the circle center that lies on the line defined by the
			// segment lies outside the segment.

			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 0 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( -3.5, 0 )
			ls.position returns Vec2D( -3.5, 0 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( -1.5, 0 ), Vec2D( -1, 0 ), 0.5, 0.0 ) ) )
		}

		"handle the beginning of the line segment touching the circle initially." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 0 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 2, 0 )
			ls.position returns Vec2D( 2, 0 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( 2, 0 ), Vec2D( 1, 0 ), 0, 0.0 ) ) )
		}

		"handle the end of the line segment touching the circle initially." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 0 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( -4, 0 )
			ls.position returns Vec2D( -4, 0 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( -2, 0 ), Vec2D( -1, 0 ), 0, 0.0 ) ) )
		}

		"handle the circle being on the line described by the vectors, but not on the line segment." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 0 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( -4, 0.5 )
			ls.position returns Vec2D( -4, 0.5 )

			test( c, ls ) must beEqualTo( None )
		}

		"handle the circle moving on a parallel to the line segment and not colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 2, 0 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 0, 3 )
			ls.position returns Vec2D( 0, 3 )

			test( c, ls ) must beEqualTo( None )
		}

		"handle the circle moving on a parallel to the line segment and colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 2, 0 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 3, 0 )
			ls.position returns Vec2D( 3, 0 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( 3, 0 ), Vec2D( 1, 0 ), 1, 0.5 ) ) )
		}

		"handle the circle moving away from the line segment and not colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, -2 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 0, 3 )
			ls.position returns Vec2D( 0, 3 )

			test( c, ls ) must beEqualTo( None )
		}

		"handle the circle moving to, but not reaching the line segment." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, -2 )
			c.position returns Vec2D( 0, 0 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 0, 3 )
			ls.position returns Vec2D( 0, 3 )

			test( c, ls ) must beEqualTo( None )
		}

		"handle the circle moving and missing the line segment." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( -3, 0 )
			c.position returns Vec2D( -3, 4 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 0, 2 )
			ls.position returns Vec2D( 0, 2 )

			test( c, ls ) must beEqualTo( None )
		}

		"handle the circle moving and colliding with the line segment." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 2 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 0, 3 )
			ls.position returns Vec2D( 0, 3 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( 0, 3 ), Vec2D( 0, 1 ), 1, 0.5 ) ) )
		}

		"handle the circle moving against the y axis and colliding with the line segment." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 4 )
			c.position returns Vec2D( 0, 2 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 0, 1 )
			ls.position returns Vec2D( 0, 1 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( 0, 1 ), Vec2D( 0, -1 ), 1, 0.5 ) ) )
		}

		"handle the circle moving and colliding with the line segment, the line segment pointing the same way as the y-axis." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 2, 0 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 0, 2 )
			ls.previousPosition returns Vec2D( 3, 0 )
			ls.position returns Vec2D( 3, 0 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( 3, 0 ), Vec2D( 1, 0 ), 1, 0.5 ) ) )
		}

		"handle the circle moving and touching the beginning of the line segment during the movement." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 4 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 2, 2 )
			ls.position returns Vec2D( 2, 2 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( 2, 2 ), Vec2D( 1, 0 ), 0, 0.5 ) ) )
		}

		"handle the circle moving and touching the end of the line segment during the movement." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 4 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( -4, 2 )
			ls.position returns Vec2D( -4, 2 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( -2, 2 ), Vec2D( -1, 0 ), 0, 0.5 ) ) )
		}

		"handle the circle moving and touching the end of the line segment with the line segment not being parallel to any axis." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 2 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 2 )
			ls.previousPosition returns Vec2D( -4, -1 )
			ls.position returns Vec2D( -4, -1 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( -2, 1 ), Vec2D( -1, 0 ), 0, 0.5 ) ) )
		}

		"handle the line segment moving and touching the circle with its end." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 0 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( -4, 2 )
			ls.position returns Vec2D( -4, -2 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( -2, 0 ), Vec2D( -1, 0 ), 0, 0.5 ) ) )
		}

		"handle the line segment moving and missing the circle." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( -3, 0 )
			c.position returns Vec2D( -3, 0 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 0, 2 )
			ls.position returns Vec2D( 0, -2 )

			test( c, ls ) must beEqualTo( None )
		}

		"handle the line segment moving and colliding with the circle." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 0 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 0, 4 )
			ls.position returns Vec2D( 0, 0 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( 0, 2 ), Vec2D( 0, 1 ), 2, 0.5 ) ) )
		}

		"handle both shapes moving and not colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 2
			c.previousPosition returns Vec2D( -3, 0 )
			c.position returns Vec2D( -3, 4 )

			val ls = mock[ LineSegment ]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 0, 2 )
			ls.position returns Vec2D( 0, 0 )

			test( c, ls ) must beEqualTo( None )
		}

		"handle both shapes moving and colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[Circle]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 2 )

			val ls = mock[LineSegment]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 0, 4 )
			ls.position returns Vec2D( 0, 2 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( 0, 3 ), Vec2D( 0, 1 ), 2, 0.5 ) ) )
		}

		"handle the circle intersecting to the line initially, moving on a non-parallel course and touching the line segment." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[Circle]
			c.radius returns 2
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 2 )

			val ls = mock[LineSegment]
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 2, 1 )
			ls.position returns Vec2D( 2, 1 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( 2, 1 ), Vec2D( 1, 0 ), 0, 0.5 ) ) )
		}
	}
}
