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



import math.Vec2D
import shape.Contact
import shape.Shape

import org.specs.Specification
import org.specs.runner.JUnit4



class CollisionTest extends JUnit4( CollisionSpec )



object CollisionSpec extends Specification {

	"Collision" should {
		"have its attributes accessible." in {
			val s1 = new Shape {}
			val s2 = new Shape {}
			val t = 0.5
			val contact = Contact( s1, s2, Vec2D( 5, 5 ), Vec2D( 1, 0 ), t )

			val collision = Collision( t, contact, -contact )

			collision.t must beEqualTo( t )
			collision.contact1 must beEqualTo( contact )
			collision.contact2 must beEqualTo( -contact )
		}

		"throw an exception if t is bigger than 1.0." in {
			val t = 1.1
			val contact = Contact( new Shape {}, new Shape {}, Vec2D( 5, 5 ), Vec2D( 1, 0 ), t )

			Collision( t, contact, -contact ) must throwAn[IllegalArgumentException]
		}

		"throw an exception if t is smaller than 0.0." in {
			val t = -1.0
			val contact = Contact( new Shape {}, new Shape {}, Vec2D( 5, 5 ), Vec2D( 1, 0 ), t )

			Collision( t, contact, -contact ) must throwAn[IllegalArgumentException]
		}

		"throw an exception if contact1 is null." in {
			val t = 0.5
			Collision( t, null, Contact( new Shape {}, new Shape {}, Vec2D( 5, 5 ), Vec2D( 1, 0 ), t ) ) must
					throwA[NullPointerException]
		}

		"throw an exception if contact2 is null." in {
			val t = 0.5
			Collision( t, Contact( new Shape {}, new Shape {}, Vec2D( 5, 5 ), Vec2D( 1, 0 ), t ), null ) must
					throwA[NullPointerException]
		}

		"throw an exception is contact1.s is not equal to contact2.other." in {
			val t = 0.5
			val s1 = new Shape {}
			val s2 = new Shape {}
			val contact1 = Contact( s1, s2, Vec2D( 5, 5 ), Vec2D( 1, 0 ), t )
			val contact2 = Contact( s2, new Shape {}, Vec2D( 5, 5 ), Vec2D( -1, 0 ), t )

			Collision( t, contact1, contact2 ) must throwAn[IllegalArgumentException]
		}

		"throw an exception if contact1.other is not equal to contact2.s." in {
			val t = 0.5
			val s1 = new Shape {}
			val s2 = new Shape {}
			val contact1 = Contact( s1, s2, Vec2D( 5, 5 ), Vec2D( 1, 0 ), t )
			val contact2 = Contact( new Shape {}, s1, Vec2D( 5, 5 ), Vec2D( -1, 0 ), t )

			Collision( t, contact1, contact2 ) must throwAn[IllegalArgumentException]
		}

		"throw an exception if the points of the contacts don't match." in {
			val t = 0.5
			val s1 = new Shape {}
			val s2 = new Shape {}
			val contact1 = Contact( s1, s2, Vec2D( 5, 5 ), Vec2D( 1, 0 ), t )
			val contact2 = Contact( s2, s1, Vec2D( 6, 6 ), Vec2D( -1, 0 ), t )

			Collision( t, contact1, contact2 ) must throwAn[IllegalArgumentException]
		}

		"throw an exception if the contact normals don't match." in {
			val t = 0.5
			val s1 = new Shape {}
			val s2 = new Shape {}
			val contact1 = Contact( s1, s2, Vec2D( 5, 5 ), Vec2D( 1, 0 ), t )
			val contact2 = Contact( s2, s1, Vec2D( 5, 5 ), Vec2D( 0, -1 ), t )

			Collision( t, contact1, contact2 ) must throwAn[IllegalArgumentException]
		}
	}
}
