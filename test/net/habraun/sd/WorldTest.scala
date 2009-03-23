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



import collision._
import math._

import scala.collection.mutable._

import org.junit._
import org.junit.Assert._



class WorldTest {

	// verify step order: integrate -> broad phase -> narrow phase -> constraint solving



	@Test
	def verifyInitialIntegrator {
		val world = new World
		assertTrue(world.integrator.isInstanceOf[EulerIntegrator])
	}

	

	@Test { val expected = classOf[NullPointerException] }
	def setIntegratorNullExpectException() {
		val world = new World
		world.integrator = null
	}



	@Test
	def addBodyVerifyIsIntegrated {
		val world = new World

		val integrate = new Integrator {
			var _t: Double = Double.NaN
			var _body: Body = null
			def apply(t: Double, body: Body) = {
				_t = t
				_body = body
				body
			}
		}
		world.integrator = integrate

		val body = new Body
		world.add(body)

		val t = 2.0
		world.step(t)

		assertEquals(t, integrate._t, 0.0)
		assertEquals(body, integrate._body)
	}



	@Test
	def addAndRemoveBodyVerifyIsNotIntegrated {
		val world = new World
		val body = new Body

		val integrate = new Integrator {
			var integrated = false
			def apply(t: Double, b: Body) = {
				integrated = b == body
				body
			}
		}
		world.integrator = integrate

		world.add(body)
		world.remove(body)
		world.step(2.0)

		assertFalse(integrate.integrated)
	}



	@Test
	def verifyInitialBroadPhase {
		val world = new World
		assertTrue(world.broadPhase.isInstanceOf[SimpleBroadPhase])
	}



	@Test
	def verifyInitialNarrowPhase {
		val world = new World
		assertTrue(world.narrowPhase.isInstanceOf[SimpleNarrowPhase])
	}



	@Test { val expected = classOf[NullPointerException] }
	def setBroadPhaseNullExpectException {
		val world = new World
		world.broadPhase = null
	}



	@Test { val expected = classOf[NullPointerException] }
	def setNarrowPhaseNullExpectException {
		val world = new World
		world.narrowPhase = null
	}



	@Test
	def verifyInitialConstraintSolver {
		val world = new World
		assertTrue(world.constraintSolver.isInstanceOf[ImpulseSolver])
	}



	@Test { val expected = classOf[NullPointerException] }
	def setConstraintSolverNullExpectException {
		val world = new World
		world.constraintSolver = null
	}



	@Test
	def addBodyVerifyItIsPassedToBroadPhase {
		val world = new World

		val broadPhase = new BroadPhase {
			var passedBodies: List[Body] = null
			def detectPossibleCollisions(bodies: List[Body]) = { passedBodies = bodies; Nil }
		}
		world.broadPhase = broadPhase

		val body = new Body
		world.add(body)
		world.step(2.0)

		assertEquals(body::Nil, broadPhase.passedBodies)
	}



	@Test
	def addBroadPhaseReturningBodyPairsVerifyTheyArePassedToNarrowPhase {
		val world = new World

		val b1 = new Body
		val b2 = new Body
		val b3 = new Body
		val b4 = new Body

		world.broadPhase = new BroadPhase {
			def detectPossibleCollisions(bodies: List[Body]) = (b1, b2)::(b3, b4)::Nil
		}

		val narrowPhase = new NarrowPhase {
			var passedPairs: List[(Body, Body)] = Nil
			def inspectCollision(b1: Body, b2: Body) = {
				passedPairs = passedPairs:::List((b1, b2))
				None
			}
		}
		world.narrowPhase = narrowPhase

		world.step(2.0)

		assertEquals((b1, b2)::(b3, b4)::Nil, narrowPhase.passedPairs)
	}



	@Test
	def verifyForceIsAppliedBeforeCollisionDetection {
		val world = new World

		val broadPhase = new BroadPhase {
			var v: Vec2D = null
			def detectPossibleCollisions(bodies: List[Body]) = {
				v = bodies(0).velocity
				Nil
			}
		}
		world.broadPhase = broadPhase

		val body = new Body
		body.mass = 5
		body.applyForce(Vec2D(10, 0))
		world.add(body)

		world.step(2.0)

		assertEquals(Vec2D(4, 0), broadPhase.v)
	}



	@Test
	def verifyImpulseIsAppliedBeforeCollisionDetection {
		val world = new World

		val broadPhase = new BroadPhase {
			var v: Vec2D = null
			def detectPossibleCollisions(bodies: List[Body]) = {
				v = bodies(0).velocity
				Nil
			}
		}
		world.broadPhase = broadPhase

		val body = new Body
		body.mass = 5
		body.applyImpulse(Vec2D(10, 0))
		world.add(body)

		world.step(2.0)

		assertEquals(Vec2D(2, 0), broadPhase.v)
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def stepPassNegativeDelta {
		val world = new World
		world.step(-1.0)
	}



	@Test
	def verifyBodyIterableIsAccessible {
		val world = new World
		assertTrue(world.bodies.isInstanceOf[Iterable[Body]])
	}



	@Test
	def verifyBodyIterableCantChangeWorld {
		val world = new World
		val body = new Body
		world.bodies.asInstanceOf[HashSet[Body]].addEntry(body)
		assertFalse(world.bodies.exists(_ == body))
	}



	@Test
	def verifyCollisionReturnedByNarrowPhaseIsPassedToSolver {
		val world = new World

		val b1 = new Body
		val b2 = new Body
		world.add(b1)
		world.add(b2)

		val collision = Collision(0.0, Contact(b1, Vec2D(1, 1), Vec2D(1, 0), b2),
				Contact(b2, Vec2D(1, 1), Vec2D(-1, 0), b1))

		world.narrowPhase = new NarrowPhase {
			def inspectCollision(b1: Body, b2: Body) = {
				Some(collision)
			}
		}

		val solver = new ConstraintSolver {
			var collision: Collision = null
			def apply(t: Double, constraint: Collision) {
				collision = constraint
			}
		}
		world.constraintSolver = solver

		world.step(2.0)

		assertEquals(collision, solver.collision)
	}
}
