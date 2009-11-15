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

import org.specs.Specification
import org.specs.mock.Mockito
import org.specs.runner.JUnit4



class ContinuousCircleCircleTestTest extends JUnit4( ContinuousCircleCircleTestSpec )



object ContinuousCircleCircleTestSpec extends Specification with Mockito {

	"ContinuousCircleCircleTest" should {
		"handle non-moving, non-colliding circles." in {
			val test = new ContinuousCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vec2D( 0, 0 )
			c2.previousPosition returns Vec2D( 3, 0 )
			c1.position returns Vec2D( 0, 0 )
			c2.position returns Vec2D( 3, 0 )
			
			test( c1, c2 ) must beEqualTo( None )
		}

		"handle non-moving circles that touch but don't intersect." in {
			val test = new ContinuousCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vec2D( 1, 0 )
			c2.previousPosition returns Vec2D( 3, 0 )
			c1.position returns Vec2D( 1, 0 )
			c2.position returns Vec2D( 3, 0)

			test( c1, c2 ) must beEqualTo( Some(  Contact( c1, c2, Vec2D( 0, 0 ), Vec2D( 1, 0 ), 0, 0.0 ) ) )
		}

		"handle non-moving, colliding circles." in {
			val test = new ContinuousCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 2
			c2.radius returns 2
			c1.previousPosition returns Vec2D( 0, 0 )
			c2.previousPosition returns Vec2D( 3, 0 )
			c1.position returns Vec2D( 0, 0 )
			c2.position returns Vec2D( 3, 0 )

			test( c1, c2 ) must beEqualTo( Some(  Contact( c1, c2, Vec2D( 0, 0 ), Vec2D( 1, 0 ), 1, 0.0 ) ) )
		}

		"handle one circle moving and colliding." in {
			val test = new ContinuousCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vec2D( -1, 0 )
			c2.previousPosition returns Vec2D( 2, 0 )
			c1.position returns Vec2D( 1, 0 )
			c2.position returns Vec2D( 2, 0 )

			test( c1, c2 ) must beEqualTo( Some( Contact( c1, c2, Vec2D( 1, 0 ), Vec2D( 1, 0 ), 1, 0.5 ) ) )
		}

		"handle one circle moving and touching, but not intersecting the other circle." in {
			val test = new ContinuousCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vec2D( 0, 0 )
			c2.previousPosition returns Vec2D( 3, 0 )
			c1.position returns Vec2D( 1, 0 )
			c2.position returns Vec2D( 3, 0)

			test( c1, c2 ) must beEqualTo( Some(  Contact( c1, c2, Vec2D( 2, 0 ), Vec2D( 1, 0 ), 0, 1.0 ) ) )
		}

		"handle spheres whose centers pass each other during the movement." in {
			val test = new ContinuousCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vec2D( -3, 0 )
			c2.previousPosition returns Vec2D( 0, 0 )
			c1.position returns Vec2D( 1, 0 )
			c2.position returns Vec2D( 0, 0 )

			test( c1, c2 ) must beEqualTo( Some( Contact( c1, c2, Vec2D( -1, 0 ), Vec2D( 1, 0 ), 3, 0.25 ) ) )
		}

		"handle one circle moving and not colliding." in {
			val test = new ContinuousCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vec2D( -3, 0 )
			c2.previousPosition returns Vec2D( 2, 0 )
			c1.position returns Vec2D( -1, 0 )
			c2.position returns Vec2D( 2, 0 )

			test( c1, c2 ) must be( None )
		}

		"handle both circles moving and colliding." in {
			val test = new ContinuousCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vec2D( -3, 0 )
			c2.previousPosition returns Vec2D( 1, 0 )
			c1.position returns Vec2D( -1, 0 )
			c2.position returns Vec2D( -1, 0 )

			test( c1, c2 ) must beEqualTo( Some( Contact( c1, c2, Vec2D( -1, 0 ), Vec2D( 1, 0 ), 2, 0.5 ) ) )
		}

		"handle both circles moving towards each other, but stopping before collision." in {
			val test = new ContinuousCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vec2D( -3, 0 )
			c2.previousPosition returns Vec2D( 4, 0 )
			c1.position returns Vec2D( -1, 0 )
			c2.position returns Vec2D( 2, 0 )

			test( c1, c2 ) must beEqualTo( None )
		}

		"handle both circles not moving towards each other." in {
			val test = new ContinuousCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vec2D( -2, 0 )
			c2.previousPosition returns Vec2D( 2, 0 )
			c1.position returns Vec2D( -4, 0 )
			c2.position returns Vec2D( 4, 0 )

			test( c1, c2 ) must beEqualTo( None )
		}

		"handle both circles moving and occupying the same space at different times." in {
			val test = new ContinuousCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vec2D( 0, 0 )
			c2.previousPosition returns Vec2D( 5, 0 )
			c1.position returns Vec2D( 0, 5 )
			c2.position returns Vec2D( 0, 0 )

			test( c1, c2 ) must beEqualTo( None )
		}

		"handle both circles moving on parallel courses with no intersection." in {
			val test = new ContinuousCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vec2D( 0, 0 )
			c2.previousPosition returns Vec2D( 3, 0 )
			c1.position returns Vec2D( 0, 5 )
			c2.position returns Vec2D( 3, 3 )

			test( c1, c2 ) must beEqualTo( None )
		}
	}
}
