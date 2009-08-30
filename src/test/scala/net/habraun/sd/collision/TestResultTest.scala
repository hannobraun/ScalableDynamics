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



class TestResultTest {

	@Test
	def verifyHasAttributes {
		TestResult(0.5, Vec2D(5, 5), Vec2D(1, 0))
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def createWithNegativeT {
		TestResult(-1.0, Vec2D(5, 5), Vec2D(1, 0))
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def createWithTGreater1 {
		TestResult(1.1, Vec2D(5, 5), Vec2D(1, 0))
	}



	@Test { val expected = classOf[NullPointerException] }
	def createWithContactNull {
		TestResult(0.5, null, Vec2D(1, 0))
	}



	@Test { val expected = classOf[NullPointerException] }
	def createWithNormalNull {
		TestResult(0.5, Vec2D(5, 5), null)
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def createWithNonNormalNormal {
		TestResult(0.5, Vec2D(5, 5), Vec2D(1, 1))
	}



	@Test
	def createWithNonNormalNormalExpectTolerance {
		TestResult(0.5, Vec2D(5, 5), Vec2D(1.02, 0))
		TestResult(0.5, Vec2D(5, 5), Vec2D(0.98, 0))
	}
}
