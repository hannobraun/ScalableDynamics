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



package net.habraun.sd.collision



import math._

import org.junit._
import org.junit.Assert._



class LineSegmentTest {

	@Test
	def verifyIsShape {
		val segment = LineSegment(Vec2D(0, 0), Vec2D(10, 10))
		assertTrue(segment.isInstanceOf[Shape])
	}



	@Test
	def verifyHasAttributes {
		val p = Vec2D(5, 5)
		val d = Vec2D(2, 1)
		val segment = LineSegment(p, d)
		
		assertEquals(p, segment.p)
		assertEquals(d, segment.d)
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def createWithZeroDirectionVectorExpectException {
		LineSegment(Vec2D(2, 2), Vec2D(0, 0))
	}


	
	@Test { val expected = classOf[NullPointerException] }
	def createWithPositionVectorNullExpectException {
		LineSegment(null, Vec2D(1, 1))
	}



	@Test { val expected = classOf[NullPointerException] }
	def createWithDirectionVectorNullExpectException {
		LineSegment(Vec2D(0, 0), null)
	}
}
