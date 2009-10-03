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



class ContactTest {

	@Test
	def verifyHasAttributes {
		val s = new Shape {}
		val point = Vec2D(10, 10)
		val normal = Vec2D(0, 1)
		val other = new Shape {}
		val contact = Contact(s, point, normal, other)
		assertEquals(s, contact.s)
		assertEquals(point, contact.point)
		assertEquals(normal, contact.normal)
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def createContactWithNonUnitNormalExpectException {
		Contact(new Shape {}, Vec2D(0, 0), Vec2D(1, 1), new Shape {})
	}



	@Test
	def createContactWithSlightlyOffNonUnitNormalsExpectTolerance {
		Contact(new Shape {}, Vec2D(0, 0), Vec2D(1.02, 0), new Shape {})
		Contact(new Shape {}, Vec2D(0, 0), Vec2D(0.98, 0), new Shape {})
	}



	@Test { val expected = classOf[NullPointerException]}
	def createContactSetBodyNull {
		Contact(null, Vec2D(0, 0), Vec2D(1, 0), new Shape {})
	}



	@Test { val expected = classOf[NullPointerException]}
	def createContactSetNormalNull {
		Contact(new Shape {}, Vec2D(0, 0), null, new Shape {})
	}



	@Test { val expected = classOf[NullPointerException]}
	def createContactSetPointNull {
		Contact(new Shape {}, null, Vec2D(1, 0), new Shape {})
	}



	@Test { val expected = classOf[NullPointerException]}
	def createContactSetOtherNull {
		Contact(new Shape {}, Vec2D(0, 0), Vec2D(1, 0), null)
	}
}
