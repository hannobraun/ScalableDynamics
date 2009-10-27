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



package net.habraun.sd.collision.shape



import math.Vec2D

import org.specs.Specification
import org.specs.runner.JUnit4



class ContactTest extends JUnit4( ContactSpec )



object ContactSpec extends Specification {

	"Contact" should {
		"make all its attributes accessible." in {
			val s = new Shape {}
			val point = Vec2D( 10, 10 )
			val normal = Vec2D( 0, 1 )
			val other = new Shape {}

			val contact = Contact( s, point, normal, other )

			contact.s must beEqualTo( s )
			contact.point must beEqualTo( point )
			contact.normal must beEqualTo( normal )
			contact.other must beEqualTo( other )
		}

		"throw an exception if normal is not a unit vector." in {
			Contact( new Shape {}, Vec2D( 0, 0 ), Vec2D( 1, 1 ), new Shape {} ) must
					throwAn[ IllegalArgumentException ]
		}

		"not throw an exception if normal is only slightly off from being a unit vector." in {
			Contact( new Shape {}, Vec2D( 0, 0 ), Vec2D( 1.02, 0 ), new Shape {} )
			Contact( new Shape {}, Vec2D( 0, 0 ), Vec2D( 0.98, 0 ), new Shape {} )
		}

		"throw an exception if a parameter is is null." in {
			Contact( null, Vec2D( 0, 0 ), Vec2D( 1, 0 ), new Shape {} ) must throwA[ NullPointerException ]
			Contact( new Shape {}, null, Vec2D( 1, 0 ), new Shape {} ) must throwA[ NullPointerException ]
			Contact( new Shape {}, Vec2D( 0, 0 ), null, new Shape {} ) must throwA[ NullPointerException ]
			Contact( new Shape {}, Vec2D( 0, 0 ), Vec2D( 1, 0 ), null ) must throwA[ NullPointerException ]
		}

		"create an inverse Contact." in {
			val contact = Contact( new Shape {}, Vec2D( 10, 10 ), Vec2D( 0, 1 ), new Shape {} )
			val inverse = -contact

			inverse.s must beEqualTo( contact.other )
			inverse.point must beEqualTo( contact.point )
			inverse.normal must beEqualTo( -contact.normal )
			inverse.other must beEqualTo( contact.s )
		}
	}
}
