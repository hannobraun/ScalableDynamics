/*
	Copyright (c) 2009, 2010 Hanno Braun <mail@hannobraun.com>

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



package com.hannobraun.sd.collision.test



import com.hannobraun.sd.math.Vector2
import com.hannobraun.sd.collision.shape.Circle
import com.hannobraun.sd.collision.shape.Contact

import org.specs.Specification
import org.specs.mock.Mockito
import org.specs.runner.JUnit4



class MovingCircleCircleTestTest extends JUnit4( MovingCircleCircleTestSpec )



object MovingCircleCircleTestSpec extends Specification with Mockito {

	"MovingCircleCircleTest" should {
		"handle non-moving, non-colliding circles." in {
			val test = new MovingCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vector2( 0, 0 )
			c2.previousPosition returns Vector2( 3, 0 )
			c1.position returns Vector2( 0, 0 )
			c2.position returns Vector2( 3, 0 )
			
			test( c1, c2 ) must beEqualTo( None )
		}

		"handle non-moving circles that touch but don't intersect." in {
			val test = new MovingCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vector2( 1, 0 )
			c2.previousPosition returns Vector2( 3, 0 )
			c1.position returns Vector2( 1, 0 )
			c2.position returns Vector2( 3, 0)

			test( c1, c2 ) must beEqualTo( Some(  Contact( c1, c2, Vector2( 2, 0 ), Vector2( 1, 0 ), 0, 0.0 ) ) )
		}

		"handle non-moving, colliding circles." in {
			val test = new MovingCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 2
			c2.radius returns 2
			c1.previousPosition returns Vector2( 0, 0 )
			c2.previousPosition returns Vector2( 3, 0 )
			c1.position returns Vector2( 0, 0 )
			c2.position returns Vector2( 3, 0 )

			test( c1, c2 ) must beEqualTo( Some(  Contact( c1, c2, Vector2( 1.5, 0 ), Vector2( 1, 0 ), 1, 0.0 ) ) )
		}

		"handle initially intersecting circles of different sizes." in {
			val test = new MovingCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 2
			c2.radius returns 1
			c1.previousPosition returns Vector2( 0, 0 )
			c2.previousPosition returns Vector2( 2, 0 )
			c1.position returns Vector2( 0, 0 )
			c2.position returns Vector2( 2, 0 )

			test( c1, c2 ) must beEqualTo( Some(  Contact( c1, c2, Vector2( 1.5, 0 ), Vector2( 1, 0 ), 1, 0.0 ) ) )
		}

		"handle one circle moving and colliding." in {
			val test = new MovingCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vector2( -1, 0 )
			c2.previousPosition returns Vector2( 2, 0 )
			c1.position returns Vector2( 1, 0 )
			c2.position returns Vector2( 2, 0 )

			test( c1, c2 ) must beEqualTo( Some( Contact( c1, c2, Vector2( 1, 0 ), Vector2( 1, 0 ), 1, 0.5 ) ) )
		}

		"handle one circle moving and touching, but not intersecting the other circle." in {
			val test = new MovingCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vector2( 0, 0 )
			c2.previousPosition returns Vector2( 3, 0 )
			c1.position returns Vector2( 1, 0 )
			c2.position returns Vector2( 3, 0)

			test( c1, c2 ) must beEqualTo( Some(  Contact( c1, c2, Vector2( 2, 0 ), Vector2( 1, 0 ), 0, 1.0 ) ) )
		}

		"handle circles whose centers pass each other during the movement." in {
			val test = new MovingCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vector2( -3, 0 )
			c2.previousPosition returns Vector2( 0, 0 )
			c1.position returns Vector2( 1, 0 )
			c2.position returns Vector2( 0, 0 )

			test( c1, c2 ) must beEqualTo( Some( Contact( c1, c2, Vector2( -1, 0 ), Vector2( 1, 0 ), 3, 0.25 ) ) )
		}

		"handle colliding circles with their centers moving not directly towards each other." in {
			val test = new MovingCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vector2( 0, 3 )
			c2.previousPosition returns Vector2( 2, -1 )
			c1.position returns Vector2( 1, 2 )
			c2.position returns Vector2( 1, 0)

			test( c1, c2 ) must beEqualTo( Some(  Contact( c1, c2, Vector2( 1, 1 ), Vector2( 0, -1 ), 0, 1.0 ) ) )
		}

		"handle circles with their centers moving not directly towards each other that are colliding in the middle of the movement." in {
			val test = new MovingCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vector2( 0, 2 )
			c2.previousPosition returns Vector2( 1, 0 )
			c1.position returns Vector2( 2, 2 )
			c2.position returns Vector2( 1, 0)

			test( c1, c2 ) must beEqualTo( Some(  Contact( c1, c2, Vector2( 1, 1 ), Vector2( 0, -1 ), 0, 0.5 ) ) )
		}

		"handle one circle moving and not colliding." in {
			val test = new MovingCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vector2( -3, 0 )
			c2.previousPosition returns Vector2( 2, 0 )
			c1.position returns Vector2( -1, 0 )
			c2.position returns Vector2( 2, 0 )

			test( c1, c2 ) must be( None )
		}

		"handle both circles moving and colliding." in {
			val test = new MovingCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 2
			c2.radius returns 2
			c1.previousPosition returns Vector2( -4, 0 )
			c2.previousPosition returns Vector2( 6, 0 )
			c1.position returns Vector2( 0, 0 )
			c2.position returns Vector2( 2, 0 )

			test( c1, c2 ) must beEqualTo( Some( Contact( c1, c2, Vector2( 1, 0 ), Vector2( 1, 0 ), 2, 0.75 ) ) )
		}

		"handle both circles moving towards each other, but stopping before collision." in {
			val test = new MovingCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vector2( -3, 0 )
			c2.previousPosition returns Vector2( 4, 0 )
			c1.position returns Vector2( -1, 0 )
			c2.position returns Vector2( 2, 0 )

			test( c1, c2 ) must beEqualTo( None )
		}

		"handle both circles not moving towards each other." in {
			val test = new MovingCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vector2( -2, 0 )
			c2.previousPosition returns Vector2( 2, 0 )
			c1.position returns Vector2( -4, 0 )
			c2.position returns Vector2( 4, 0 )

			test( c1, c2 ) must beEqualTo( None )
		}

		"handle both circles moving and occupying the same space at different times." in {
			val test = new MovingCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vector2( 0, 0 )
			c2.previousPosition returns Vector2( 5, 0 )
			c1.position returns Vector2( 0, 5 )
			c2.position returns Vector2( 0, 0 )

			test( c1, c2 ) must beEqualTo( None )
		}

		"handle both circles moving on parallel courses with no intersection." in {
			val test = new MovingCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 1
			c1.previousPosition returns Vector2( 0, 0 )
			c2.previousPosition returns Vector2( 3, 0 )
			c1.position returns Vector2( 0, 5 )
			c2.position returns Vector2( 3, 3 )

			test( c1, c2 ) must beEqualTo( None )
		}

		"handle two circles whose centers overlap at the beginning of the movement." in {
			val test = new MovingCircleCircleTest

			val c1 = mock[ Circle ]
			val c2 = mock[ Circle ]
			c1.radius returns 1
			c2.radius returns 2
			c1.previousPosition returns Vector2( 3, 0 )
			c2.previousPosition returns Vector2( 3, 0 )
			c1.position returns Vector2( 3, 0 )
			c2.position returns Vector2( 3, 0 )

			test( c1, c2 ) must beEqualTo( Some( Contact( c1, c2, Vector2( 3, 0 ), Vector2( 1, 0 ), 3, 0.0 ) ) )
		}
	}
}
