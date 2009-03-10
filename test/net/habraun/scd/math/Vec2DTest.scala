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



package net.habraun.scd.math



import org.junit._
import org.junit.Assert._



class Vec2DTest {

	@Test
	def addition {
		val vec1 = Vec2D(1, 0)
		val vec2 = Vec2D(0, 1)
		assertEquals(Vec2D(1, 1), vec1 + vec2)
	}



	@Test
	def substraction {
		val vec1 = Vec2D(5, 5)
		val vec2 = Vec2D(4, 4)
		assertEquals(Vec2D(1, 1), vec1 - vec2)
	}



	@Test
	def scalarMultiplication {
		val vec = Vec2D(1, 1)
		assertEquals(Vec2D(2, 2), vec * 2)
	}



	@Test
	def scalarDivision {
		val vec = Vec2D(2, 2)
		assertEquals(Vec2D(1, 1), vec / 2.0)
	}



	@Test
	def dotProduct {
		val vec1 = Vec2D(1, 2)
		val vec2 = Vec2D(2, 1)
		assertEquals(4.0, vec1 * vec2, 0.0)
	}



	@Test
	def inverse {
		val vec = Vec2D(1, 1)
		assertEquals(Vec2D(-1, -1), -vec)
	}



	@Test
	def length {
		val vec = Vec2D(2, 0)
		assertEquals(2.0, vec.length, 0.0)
	}



	@Test
	def squaredLength {
		val vec = Vec2D(2, 2)
		assertEquals(8.0, vec.squaredLength, 0.0)
	}



	@Test
	def normalize {
		val vec = Vec2D(2, 0)
		assertEquals(Vec2D(1, 0), vec.normalize)
	}



	@Test
	def orthogonal {
		val vec = Vec2D(2, 1)
		assertEquals(Vec2D(-1, 2), vec.orthogonal)
	}



	@Test
	def project {
		val vec1 = Vec2D(5, 5)
		val vec2 = Vec2D(1, 0)
		assertEquals(Vec2D(5, 0), vec1.project(vec2))
	}
}
