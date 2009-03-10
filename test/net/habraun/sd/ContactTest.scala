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



import org.junit._
import org.junit.Assert._



class ContactTest {

	@Test
	def verifyHasAttributes {
		val b1 = new Body
		val b2 = new Body
		val normal1 = Vec2D(0, 1)
		val normal2 = Vec2D(0, -1)
		val point = Vec2D(10, 10)
		val contact = Contact(b1, b2, normal1, normal2, point)
		assertEquals(b1, contact.b1)
		assertEquals(b2, contact.b2)
		assertEquals(normal1, contact.normal1)
		assertEquals(normal2, contact.normal2)
		assertEquals(point, contact.point)
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def createContactWithNonInverseNormalsExpectException {
		Contact(new Body, new Body, Vec2D(1, 0), Vec2D(0, -1), Vec2D(0, 0))
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def createContactWithNonUnitNormalsExpectException {
		Contact(new Body, new Body, Vec2D(1, 1), Vec2D(-1, -1), Vec2D(0, 0))
	}



	@Test
	def createContactWithSlightlyOffNonUnitNormalsExpectTolerance {
		Contact(new Body, new Body, Vec2D(1.02, 0), Vec2D(-1.02, 0), Vec2D(0, 0))
		Contact(new Body, new Body, Vec2D(0.98, 0), Vec2D(-0.98, 0), Vec2D(0, 0))
	}
}
