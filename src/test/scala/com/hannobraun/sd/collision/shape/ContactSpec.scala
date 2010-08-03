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



package com.hannobraun.sd.collision.shape



import com.hannobraun.sd.math.Vector2

import org.specs.Specification
import org.specs.runner.JUnit4



class ContactTest extends JUnit4( ContactSpec )



object ContactSpec extends Specification {

	"Contact" should {
		"make all its attributes accessible." in {
			val s = new Shape {}
			val other = new Shape {}
			val point = Vector2( 10, 10 )
			val normal = Vector2( 0, 1 )
			val depth = 1
			val t = 0.5

			val contact = Contact( s, other, point, normal, depth, t )

			contact.s must beEqualTo( s )
			contact.other must beEqualTo( other )
			contact.point must beEqualTo( point )
			contact.normal must beEqualTo( normal )
			contact.depth must beEqualTo( depth )
			contact.t must beEqualTo( t )
		}

		"throw an exception if normal is not a unit vector." in {
			Contact( new Shape {}, new Shape {}, Vector2( 0, 0 ), Vector2( 1, 1 ), 1, 0.5 ) must
					throwAn[ IllegalArgumentException ]
		}

		"not throw an exception if normal is only slightly off from being a unit vector." in {
			Contact( new Shape {}, new Shape {}, Vector2( 0, 0 ), Vector2( 1.02, 0 ), 1, 0.5 )
			Contact( new Shape {}, new Shape {}, Vector2( 0, 0 ), Vector2( 0.98, 0 ), 1, 0.5 )
		}

		"throw an exception if a parameter is is null." in {
			Contact( null, new Shape {}, Vector2( 0, 0 ), Vector2( 1, 0 ), 1, 0.5 ) must
					throwA[ NullPointerException ]
			Contact( new Shape {}, null, Vector2( 0, 0 ), Vector2( 1, 0 ), 1, 0.5 ) must
					throwA[ NullPointerException ]
			Contact( new Shape {}, new Shape {}, null, Vector2( 1, 0 ), 1, 0.5 ) must
					throwA[ NullPointerException ]
			Contact( new Shape {}, new Shape {}, Vector2( 0, 0 ), null, 1, 0.5 ) must
					throwA[ NullPointerException ]
			
		}

		"create an inverse Contact." in {
			val contact = Contact( new Shape {}, new Shape {}, Vector2( 10, 10 ), Vector2( 0, 1 ), 1, 0.5 )
			val inverse = -contact

			inverse.s must beEqualTo( contact.other )
			inverse.other must beEqualTo( contact.s )
			inverse.point must beEqualTo( contact.point )
			inverse.normal must beEqualTo( -( contact.normal ) )
			inverse.depth must beEqualTo( contact.depth )
			inverse.t must beEqualTo( contact.t )
		}
	}
}
