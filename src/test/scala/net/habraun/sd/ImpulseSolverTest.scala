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



import collision.phase.Collision
import collision.shape.Contact
import collision.shape.Shape
import core.Body
import math.Vec2D

import org.junit.Before
import org.junit.Test
import org.junit.Assert._



class ImpulseSolverTest {

	val t = 2.0

	var solve: CollisionSolver = null



	@Before
	def setup {
		solve = new ImpulseSolver
	}



	@Test
	def verifyCollisionEffects {
		val b1 = new Body with Shape {}
		b1.position = Vec2D(-20, -21)
		b1.mass = 4
		b1.velocity = Vec2D(-10, -10)
		val b2 = new Body with Shape {}
		b2.position = Vec2D(10, 9)
		b2.mass = 8
		b2.velocity = Vec2D(5, 5)
		val collision = Collision(1.0, Contact(b1, Vec2D(0, 0), Vec2D(0, -1), b2),
				Contact(b2, Vec2D(0, 0), Vec2D(0, 1), b1))

		solve(t, collision)

		assertEquals(Vec2D(0, 80), b1.appliedImpulse)
		assertEquals(Vec2D(0, -80), b2.appliedImpulse)
	}



	@Test
	def verifyCollisionEffectsWithBody1Static {
		val b1 = new Body with Shape {}
		b1.mass = Double.PositiveInfinity
		val b2 = new Body with Shape {}
		b2.mass = 5
		b2.position = Vec2D(2, 2)
		b2.velocity = Vec2D(1, 1)
		val collision = Collision(1.0, Contact(b1, Vec2D(0, 0), Vec2D(0, -1), b2),
				Contact(b2, Vec2D(0, 0), Vec2D(0, 1), b1))

		solve(t, collision)

		assertEquals(Vec2D(0, 0), b1.appliedImpulse)
		assertEquals(Vec2D(0, -10), b2.appliedImpulse)
	}



	@Test
	def verifyCollisionEffectsWithBody2Static {
		val b1 = new Body with Shape {}
		b1.mass= 5
		b1.position = Vec2D(2, 2)
		b1.velocity = Vec2D(1, 1)
		val b2 = new Body with Shape {}
		b2.mass = Double.PositiveInfinity
		val collision = Collision(1.0, Contact(b1, Vec2D(0, 0), Vec2D(0, 1), b2),
				Contact(b2, Vec2D(0, 0), Vec2D(0, -1), b1))

		solve(t, collision)

		assertEquals(Vec2D(0, -10), b1.appliedImpulse)
		assertEquals(Vec2D(0, 0), b2.appliedImpulse)
	}



	@Test
	def addBodiesThatWouldIntersectAfterMovementVerifyTheyDont {
		val b1 = new Body with Shape {}
		b1.position = Vec2D(2, 0)
		b1.velocity = Vec2D(1, 0)
		val b2 = new Body with Shape {}
		b2.position = Vec2D(5, 0)
		b2.velocity = Vec2D(0, 0)
		val collision = Collision(0.5, Contact(b1, Vec2D(3, 0), Vec2D(1, 0), b2),
				Contact(b2, Vec2D(3, 0), Vec2D(-1, 0), b1))
		
		solve(t, collision)

		val expectedPosition = Vec2D(1, 0)

		assertEquals(expectedPosition.x, b1.position.x, 0.01)
		assertEquals(expectedPosition.y, b1.position.y, 0.01)
	}



	@Test
	def testForIntersectionAtHighPositionValues {
		// This is a real-world example that led to a bug. That's where all the odd numbers come from.
		val b1 = new Body with Shape {}
		b1.position = Vec2D(-806.75411, 387.8899501)
		b1.velocity = Vec2D(-445.3620529718986, -50.442888201040574)
		val b2 = new Body with Shape {}
		b2.position = Vec2D(50.0, 469.8163204364546)
		b2.velocity = Vec2D(0.0, 0.0)
		val collision = Collision(0.4766389925763854,
						Contact(b1, Vec2D(3, 0), Vec2D(-0.8732041733361332, -0.4873545646327327), b2),
						Contact(b2, Vec2D(3, 0), Vec2D(0.8732041733361332, 0.4873545646327327), b1))

		solve(t, collision)

		assertTrue((b2.position - b1.position).squaredLength >= 35.0 * 35.0)
	}
}
