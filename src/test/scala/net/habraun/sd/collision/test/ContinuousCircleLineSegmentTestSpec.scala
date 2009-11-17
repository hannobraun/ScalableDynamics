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
			c.radius returns 1
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 0 )

			val ls = mock[ LineSegment ]
			ls.p returns Vec2D( -1, 0 )
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 0, 2 )
			ls.position returns Vec2D( 0, 2 )

			test( c, ls ) must beEqualTo( None )
		}

		"handle two non-moving, intersecting shapes." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 1
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 0 )
			
			val ls = mock[ LineSegment ]
			ls.p returns Vec2D( -1, 0 )
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 0, 0.5 )
			ls.position returns Vec2D( 0, 0.5 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( 0, 0 ), Vec2D( 0, 1 ), 0, 0.0 ) ) )
		}

		"handle the circle being on the line described by the vectors, but not on the line segment." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 1
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 0 )

			val ls = mock[ LineSegment ]
			ls.p returns Vec2D( 0, 0 )
			ls.d returns Vec2D( 2, 0 )
			ls.previousPosition returns Vec2D( 0, 2 )
			ls.position returns Vec2D( 0, 2 )

			test( c, ls ) must beEqualTo( None )
		}

		"handle the circle moving on a parallel to the line segment and not colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 1
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 2, 0 )

			val ls = mock[ LineSegment ]
			ls.p returns Vec2D( 0, 0 )
			ls.d returns Vec2D( 5, 0 )
			ls.previousPosition returns Vec2D( 0, 2 )
			ls.position returns Vec2D( 0, 2 )

			test( c, ls ) must beEqualTo( None )
		}

		"handle the circle moving away from the line segment and not colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 1
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, -2 )

			val ls = mock[ LineSegment ]
			ls.p returns Vec2D( 0, 0 )
			ls.d returns Vec2D( 5, 0 )
			ls.previousPosition returns Vec2D( 0, 2 )
			ls.position returns Vec2D( 0, 2 )

			test( c, ls ) must beEqualTo( None )
		}

		"handle the circle moving to, but not reaching the line segment." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 1
			c.previousPosition returns Vec2D( 0, -5 )
			c.position returns Vec2D( 0, -3 )

			val ls = mock[ LineSegment ]
			ls.p returns Vec2D( 0, 0 )
			ls.d returns Vec2D( 5, 0 )
			ls.previousPosition returns Vec2D( 0, 2 )
			ls.position returns Vec2D( 0, 2 )

			test( c, ls ) must beEqualTo( None )
		}

		"handle the circle moving and missing the line segment." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 1
			c.previousPosition returns Vec2D( -2, 0 )
			c.position returns Vec2D( -2, 2 )

			val ls = mock[ LineSegment ]
			ls.p returns Vec2D( 0, 0 )
			ls.d returns Vec2D( 5, 0 )
			ls.previousPosition returns Vec2D( 0, 2 )
			ls.position returns Vec2D( 0, 2 )

			test( c, ls ) must beEqualTo( None )
		}

		"handle the circle moving and colliding with the line segment." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 1
			c.previousPosition returns Vec2D( 2, 0 )
			c.position returns Vec2D( 2, 2 )

			val ls = mock[ LineSegment ]
			ls.p returns Vec2D( 0, 0 )
			ls.d returns Vec2D( 5, 0 )
			ls.previousPosition returns Vec2D( 0, 2 )
			ls.position returns Vec2D( 0, 2 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( 2, 2 ), Vec2D( 0, 1 ), 0, 0.5 ) ) )
		}

		"handle the line segment moving and not colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 1
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 0, 0 )

			val ls = mock[ LineSegment ]
			ls.p returns Vec2D( 0, 0 )
			ls.d returns Vec2D( 5, 0 )
			ls.previousPosition returns Vec2D( 0, 2 )
			ls.position returns Vec2D( 0, 4 )

			test( c, ls ) must beEqualTo( None )
		}

		"handle the line segment moving and colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 1
			c.previousPosition returns Vec2D( 2, 0 )
			c.position returns Vec2D( 2, 0 )

			val ls = mock[ LineSegment ]
			ls.p returns Vec2D( 0, 0 )
			ls.d returns Vec2D( 5, 0 )
			ls.previousPosition returns Vec2D( 0, 2 )
			ls.position returns Vec2D( 0, 0 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( 2, 1 ), Vec2D( 0, 1 ), 0, 0.5 ) ) )
		}

		"handle both shapes moving and not colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[ Circle ]
			c.radius returns 1
			c.previousPosition returns Vec2D( 0, 0 )
			c.position returns Vec2D( 2, 0 )

			val ls = mock[ LineSegment ]
			ls.p returns Vec2D( 0, 0 )
			ls.d returns Vec2D( 5, 0 )
			ls.previousPosition returns Vec2D( 0, 2 )
			ls.position returns Vec2D( -2, 2 )

			test( c, ls ) must beEqualTo( None )
		}

		"handle both shapes moving and colliding." in {
			val test = new ContinuousCircleLineSegmentTest

			val c = mock[Circle]
			c.radius returns 1
			c.previousPosition returns Vec2D( 2, -1 )
			c.position returns Vec2D( 2, 1 )

			val ls = mock[LineSegment]
			ls.p returns Vec2D( 0, 0 )
			ls.d returns Vec2D( 5, 0 )
			ls.previousPosition returns Vec2D( 0, 2 )
			ls.position returns Vec2D( 0, 0 )

			test( c, ls ) must beEqualTo( Some( Contact( c, ls, Vec2D( 2, 1 ), Vec2D( 0, 1 ), 0, 0.5 ) ) )
		}
	}
}
