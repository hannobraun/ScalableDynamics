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



import collision.phase.BroadPhase
import collision.phase.Collision
import collision.phase.NarrowPhase
import collision.phase.SimpleBroadPhase
import collision.phase.SimpleNarrowPhase
import collision.shape.Contact
import collision.shape.Shape
import core.Body
import dynamics.VelocityConstraintSolver
import math.Vec2D

import scala.collection.mutable.HashSet


import org.junit.Before
import org.junit.Test
import org.junit.Assert._



class WorldTest {

	var world: World[Body] = null



	@Before
	def setup {
		world = new World
	}



	@Test
	def verifyBodyIterableIsAccessible {
		val world = new World[Body]
		
		val body = new Body {}
		world.add( body )
		
		assertTrue( world.bodies.contains( body ) )
	}



	@Test
	def verifyInitialVelocityConstraintSolver {
		assertTrue( world.velocityConstraintSolver.isInstanceOf[VelocityConstraintSolver] )
	}



	@Test
	def verifyInitialBroadPhase {
		assertTrue(world.broadPhase.isInstanceOf[SimpleBroadPhase])
	}



	@Test { val expected = classOf[NullPointerException] }
	def setBroadPhaseNullExpectException {
		world.broadPhase = null
	}



	@Test
	def verifyInitialNarrowPhase {
		assertTrue(world.narrowPhase.isInstanceOf[SimpleNarrowPhase])
	}



	@Test { val expected = classOf[NullPointerException] }
	def setNarrowPhaseNullExpectException {
		world.narrowPhase = null
	}



	@Test
	def verifyInitialConstraintSolver {
		assertTrue(world.collisionSolver.isInstanceOf[ImpulseSolver])
	}



	@Test { val expected = classOf[NullPointerException] }
	def setConstraintSolverNullExpectException {
		world.collisionSolver = null
	}



	@Test
	def addBodyVerifyItIsPassedToBroadPhase {
		val broadPhase = new BroadPhase {
			var passedBodies: Iterable[Shape] = null
			def apply(shapes: Iterable[Shape]) = { passedBodies = shapes; Nil }
		}
		world.broadPhase = broadPhase

		val body = new Body with Shape {}
		world.add(body)
		world.step(2.0)

		assertEquals(body::Nil, broadPhase.passedBodies)
	}



	@Test
	def addBroadPhaseReturningBodyPairsVerifyTheyArePassedToNarrowPhase {
		val b1 = new Body with Shape {}
		val b2 = new Body with Shape {}
		val b3 = new Body with Shape {}
		val b4 = new Body with Shape {}

		world.broadPhase = new BroadPhase {
			def apply(shapes: Iterable[Shape]) = (b1, b2)::(b3, b4)::Nil
		}

		val narrowPhase = new NarrowPhase {
			var passedPairs: List[(Body, Body)] = Nil
			def apply(b1: Shape, b2: Shape) = {
				passedPairs = passedPairs:::List((b1, b2))
				None
			}
		}
		world.narrowPhase = narrowPhase

		world.step(2.0)

		assertEquals((b1, b2)::(b3, b4)::Nil, narrowPhase.passedPairs)
	}



	@Test
	def verifyCollisionReturnedByNarrowPhaseIsPassedToSolver {
		val b1 = new Body with Shape {}
		val b2 = new Body with Shape {}
		world.add(b1)
		world.add(b2)

		val collision = Collision(0.0, Contact(0.0, b1, Vec2D(1, 1), Vec2D(1, 0), b2),
				Contact(0.0, b2, Vec2D(1, 1), Vec2D(-1, 0), b1))

		world.narrowPhase = new NarrowPhase {
			def apply(b1: Shape, b2: Shape) = {
				Some(collision)
			}
		}

		val solver = new CollisionSolver {
			var collision: Collision = null
			def apply(t: Double, constraint: Collision) {
				collision = constraint
			}
		}
		world.collisionSolver = solver

		world.step(2.0)

		assertEquals(collision, solver.collision)
	}
}
