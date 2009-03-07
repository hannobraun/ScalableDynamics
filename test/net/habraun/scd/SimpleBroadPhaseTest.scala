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



package net.habraun.scd



import org.junit._
import org.junit.Assert._



class SimpleBroadPhaseTest {

	@Test
	def verifyIsBroadPhase {
		val broadPhase = new SimpleBroadPhase
		assertTrue(broadPhase.isInstanceOf[BroadPhase])
	}



	@Test
	def buildPairsFromTwoBodies {
		val b1 = new Body
		val b2 = new Body
		val broadPhase: BroadPhase = new SimpleBroadPhase
		assertEquals((b1, b2)::Nil, broadPhase.detectPossibleCollisions(b1::b2::Nil))
	}



	@Test
	def buildPairsFromThreeBodies {
		val b1 = new Body
		val b2 = new Body
		val b3 = new Body
		val broadPhase: BroadPhase = new SimpleBroadPhase
		val expectedPairs = (b1, b2)::(b1, b3)::(b2, b3)::Nil
		val actualPairs = broadPhase.detectPossibleCollisions(b1::b2::b3::Nil)
		assertEquals(expectedPairs, actualPairs)
	}
}
