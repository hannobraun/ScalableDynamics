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



import collision.shape.Shape
import math.Vec2D

import org.specs.Specification
import org.specs.runner.JUnit4



class CollisionTest extends JUnit4( CollisionSpec )



object CollisionSpec extends Specification {

	"Collision" should {
		"have its attributes accessible." in {
			val s1 = new Shape {}
			val s2 = new Shape {}
			val t = 0.5
			val contact1 = Contact( s1, Vec2D( 5, 5 ), Vec2D( 1, 0 ),  s2 )
			val contact2 = Contact( s2, Vec2D( 5, 5 ), Vec2D( -1, 0 ), s1 )

			val collision = Collision( t, contact1, contact2 )

			collision.t must beEqualTo( t )
			collision.contact1 must beEqualTo( contact1 )
			collision.contact2 must beEqualTo( contact2 )
		}

		"throw an exception if t is bigger than 1.0." in {
			val contact = Contact( new Shape {}, Vec2D( 5, 5 ), Vec2D( 1, 0 ), new Shape {} )

			Collision( 1.1, contact, contact ) must throwAn[IllegalArgumentException]
		}

		"throw an exception if t is smaller than 0.0." in {
			val contact = Contact( new Shape {}, Vec2D( 5, 5 ), Vec2D( 1, 0 ), new Shape {} )

			Collision( -1.0, contact, contact ) must throwAn[IllegalArgumentException]
		}

		"throw an exception if contact1 is null." in {
			Collision( 0.5, null, Contact( new Shape {}, Vec2D( 5, 5 ), Vec2D( 1, 0 ), new Shape {} ) ) must
					throwA[NullPointerException]
		}

		"throw an exception if contact2 is null." in {
			Collision( 0.5, Contact( new Shape {}, Vec2D( 5, 5 ), Vec2D( 1, 0 ), new Shape {} ), null ) must
					throwA[NullPointerException]
		}

		"throw an exception is contact1.s is not equal to contact2.other." in {
			val s1 = new Shape {}
			val s2 = new Shape {}
			val contact1 = Contact( s1, Vec2D( 5, 5 ), Vec2D( 1, 0 ), s2 )
			val contact2 = Contact( s2, Vec2D( 5, 5 ), Vec2D( -1, 0 ), new Shape {} )

			Collision( 0.5, contact1, contact2 ) must throwAn[IllegalArgumentException]
		}

		"throw an exception if contact1.other is not equal to contact2.s." in {
			val s1 = new Shape {}
			val s2 = new Shape {}
			val contact1 = Contact( s1, Vec2D( 5, 5 ), Vec2D( 1, 0 ), s2 )
			val contact2 = Contact( new Shape {}, Vec2D( 5, 5 ), Vec2D( -1, 0 ), s1 )

			Collision( 0.5, contact1, contact2 ) must throwAn[IllegalArgumentException]
		}

		"throw an exception if the points of the contacts don't match." in {
			val s1 = new Shape {}
			val s2 = new Shape {}
			val contact1 = Contact( s1, Vec2D( 5, 5 ), Vec2D( 1, 0 ), s2 )
			val contact2 = Contact( s2, Vec2D( 6, 6 ), Vec2D( -1, 0 ), s1 )

			Collision( 0.5, contact1, contact2 ) must throwAn[IllegalArgumentException]
		}

		"throw an exception if the contact normals don't match." in {
			val s1 = new Shape {}
			val s2 = new Shape {}
			val contact1 = Contact( s1, Vec2D( 5, 5 ), Vec2D( 1, 0 ), s2 )
			val contact2 = Contact( s2, Vec2D( 5, 5 ), Vec2D( 0, -1 ), s1 )

			Collision( 0.5, contact1, contact2 ) must throwAn[IllegalArgumentException]
		}
	}
}
