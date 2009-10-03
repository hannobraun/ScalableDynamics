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



import core.Body
import math.Vec2D

import org.junit.Test
import org.junit.Assert._



class ContactTest {

	@Test
	def verifyHasAttributes {
		val b = new Body
		val point = Vec2D(10, 10)
		val normal = Vec2D(0, 1)
		val other = new Body
		val contact = Contact(b, point, normal, other)
		assertEquals(b, contact.b)
		assertEquals(point, contact.point)
		assertEquals(normal, contact.normal)
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def createContactWithNonUnitNormalExpectException {
		Contact(new Body, Vec2D(0, 0), Vec2D(1, 1), new Body)
	}



	@Test
	def createContactWithSlightlyOffNonUnitNormalsExpectTolerance {
		Contact(new Body, Vec2D(0, 0), Vec2D(1.02, 0), new Body)
		Contact(new Body, Vec2D(0, 0), Vec2D(0.98, 0), new Body)
	}



	@Test { val expected = classOf[NullPointerException]}
	def createContactSetBodyNull {
		Contact(null, Vec2D(0, 0), Vec2D(1, 0), new Body)
	}



	@Test { val expected = classOf[NullPointerException]}
	def createContactSetNormalNull {
		Contact(new Body, Vec2D(0, 0), null, new Body)
	}



	@Test { val expected = classOf[NullPointerException]}
	def createContactSetPointNull {
		Contact(new Body, null, Vec2D(1, 0), new Body)
	}



	@Test { val expected = classOf[NullPointerException]}
	def createContactSetOtherNull {
		Contact(new Body, Vec2D(0, 0), Vec2D(1, 0), null)
	}
}
