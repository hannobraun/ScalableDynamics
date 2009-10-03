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

import org.junit.Test
import org.junit.Assert._



class CollisionTest {

	@Test
	def verifyHasAttributes {
		val s1 = new Shape {}
		val s2 = new Shape {}

		val t = 0.5
		val contact1 = Contact(s1, Vec2D(5, 5), Vec2D(1, 0),  s2)
		val contact2 = Contact(s2, Vec2D(5, 5), Vec2D(-1, 0), s1)

		val collision = Collision(t, contact1, contact2)
		
		assertEquals(t, collision.t, 0.0)
		assertEquals(contact1, collision.contact1)
		assertEquals(contact2, collision.contact2)
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def createCollisionWithInvalidTime {
		val contact = Contact(new Shape {}, Vec2D(5, 5), Vec2D(1, 0), new Shape {})
		Collision(1.1, contact, contact)
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def createCollisionWithInvalidTime2 {
		val contact = Contact(new Shape {}, Vec2D(5, 5), Vec2D(1, 0), new Shape {})
		Collision(-1.0, contact, contact)
	}



	@Test { val expected = classOf[NullPointerException] }
	def createCollisionWithNullContact1 {
		Collision(0.5, null, Contact(new Shape {}, Vec2D(5, 5), Vec2D(1, 0), new Shape {}))
	}



	@Test { val expected = classOf[NullPointerException] }
	def createCollisionWithNullContact2 {
		Collision(0.5, Contact(new Shape {}, Vec2D(5, 5), Vec2D(1, 0), new Shape {}), null)
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def verifyContactBodiesMatch1 {
		val s1 = new Shape {}
		val s2 = new Shape {}
		val contact1 = Contact(s1, Vec2D(5, 5), Vec2D(1, 0), s2)
		val contact2 = Contact(s2, Vec2D(5, 5), Vec2D(-1, 0), new Shape {})
		Collision(0.5, contact1, contact2)
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def verifyContactBodiesMatch2 {
		val s1 = new Shape {}
		val s2 = new Shape {}
		val contact1 = Contact(s1, Vec2D(5, 5), Vec2D(1, 0), s2)
		val contact2 = Contact(new Shape {}, Vec2D(5, 5), Vec2D(-1, 0), s1)
		Collision(0.5, contact1, contact2)
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def verifyContactPointsMatch {
		val s1 = new Shape {}
		val s2 = new Shape {}
		val contact1 = Contact(s1, Vec2D(5, 5), Vec2D(1, 0), s2)
		val contact2 = Contact(s2, Vec2D(6, 6), Vec2D(-1, 0), s1)
		Collision(0.5, contact1, contact2)
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def verifyContactNormalsMatch {
		val s1 = new Shape {}
		val s2 = new Shape {}
		val contact1 = Contact(s1, Vec2D(5, 5), Vec2D(1, 0), s2)
		val contact2 = Contact(s2, Vec2D(5, 5), Vec2D(0, -1), s1)
		Collision(0.5, contact1, contact2)
	}
}
