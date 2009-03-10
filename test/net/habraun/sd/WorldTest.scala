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



class WorldTest {

	@Test
	def addBodyStepExpectBodyMoved {
		val world = new World
		val body = new Body
		body.velocity = Vec2D(1, 0)
		world.add(body)
		world.step(2.0)
		assertEquals(Vec2D(2, 0), body.position)
	}



	@Test
	def addAndRemoveBodyExpectBodyNotMoved {
		val world = new World
		val body = new Body
		body.velocity = Vec2D(1, 0)
		world.add(body)
		world.remove(body)
		world.step(2.0)
		assertEquals(Vec2D(0, 0), body.position)
	}



	@Test
	def addBodyApplyForceCheckVelocity {
		val world = new World
		val body = new Body
		body.mass = 5
		body.applyForce(Vec2D(5, 0))
		world.add(body)
		world.step(2.0)
		assertEquals(Vec2D(2, 0), body.velocity)
	}



	@Test
	def addBodyApplyForceStepTwiceCheckVelocity {
		val world = new World
		val body = new Body
		body.mass = 5
		body.applyForce(Vec2D(5, 0))
		world.add(body)
		world.step(2.0)
		world.step(2.0)
		assertEquals(Vec2D(2, 0), body.velocity)
	}



	@Test
	def addBodyApplyForceStepCheckPosition {
		val world = new World
		val body = new Body
		body.mass = 5
		body.applyForce(Vec2D(5, 0))
		world.add(body)
		world.step(2.0)
		assertEquals(Vec2D(4, 0), body.position)
	}



	@Test
	def addBodyApplyImpulseCheckVelocity {
		val world = new World
		val body = new Body
		body.mass = 5
		body.velocity = Vec2D(3, 0)
		body.applyImpulse(Vec2D(5, 0))
		world.add(body)
		world.step(2.0)
		assertEquals(Vec2D(4, 0), body.velocity)
	}



	@Test
	def addBodyApplyImpulseCheckVelocity2 {
		val world = new World
		val body = new Body
		body.mass = 5
		body.velocity = Vec2D(3, 0)
		body.applyImpulse(Vec2D(5, 0))
		world.add(body)
		world.step(5.0)
		assertEquals(Vec2D(4, 0), body.velocity)
	}



	@Test
	def addBodyApplyImpulseCheckImpulse {
		val world = new World
		val body = new Body
		body.applyImpulse(Vec2D(10, 10))
		world.add(body)
		world.step(2.0)
		assertEquals(Vec2D(0, 0), body.appliedImpulse)
	}



	@Test
	def addBodyDisallowXMovementStepCheckPosition {
		val world = new World
		val body = new Body
		body.allowXMovement(false)
		body.applyForce(Vec2D(1, 1))
		world.add(body)
		world.step(2.0)
		assertEquals(Vec2D(0, 4), body.position)
	}



	@Test
	def addBodyDisallowYMovementStepCheckPosition {
		val world = new World
		val body = new Body
		body.allowYMovement(false)
		body.applyForce(Vec2D(1, 1))
		world.add(body)
		world.step(2.0)
		assertEquals(Vec2D(4, 0), body.position)
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
			def inspectCollision(delta: Double, b1: Body, b2: Body) = { passedPairs = passedPairs:::List((b1, b2)); None }
		}
		world.narrowPhase = narrowPhase

		world.step(2.0)

		assertEquals((b1, b2)::(b3, b4)::Nil, narrowPhase.passedPairs)
	}
	
	
	
	@Test
	def verifyCollisionEffects {
		val world = new World

		val b1 = new Body
		b1.position = Vec2D(0, 1)
		b1.mass = 4
		b1.velocity = Vec2D(-10, -10)
		val b2 = new Body
		b2.position = Vec2D(0, -1)
		b2.mass = 8
		b2.velocity = Vec2D(5, 5)

		world.narrowPhase = new NarrowPhase {
			def inspectCollision(delta: Double, b1: Body, b2: Body) = {
				Some(Collision(1.0, Contact(b1, b2, Vec2D(0, -1), Vec2D(0, 1), Vec2D(0, 0))))
			}
		}

		world.add(b1)
		world.add(b2)
		world.step(2.0)

		assertEquals(Vec2D(0, 80), b1.appliedImpulse)
		assertEquals(Vec2D(0, -80), b2.appliedImpulse)
	}



	@Test
	def verifyCollisionEffectsWithBody1Static {
		val world = new World

		val b1 = new Body
		b1.mass = Double.PositiveInfinity
		val b2 = new Body
		b2.mass = 5
		b2.velocity = Vec2D(1, 1)

		world.narrowPhase = new NarrowPhase {
			def inspectCollision(delta: Double, b1: Body, b2: Body) = {
				Some(Collision(1.0, Contact(b1, b2, Vec2D(0, -1), Vec2D(0, 1), Vec2D(0, 0))))
			}
		}

		world.add(b1)
		world.add(b2)
		world.step(2.0)

		assertEquals(Vec2D(0, 0), b1.appliedImpulse)
		assertEquals(Vec2D(0, -10), b2.appliedImpulse)
	}



	@Test
	def verifyCollisionEffectsWithBody2Static {
		val world = new World

		val b1 = new Body
		b1.mass= 5
		b1.velocity = Vec2D(1, 1)
		val b2 = new Body
		b2.mass = Double.PositiveInfinity

		world.narrowPhase = new NarrowPhase {
			def inspectCollision(delta: Double, b1: Body, b2: Body) = {
				Some(Collision(1.0, Contact(b1, b2, Vec2D(0, 1), Vec2D(0, -1), Vec2D(0, 0))))
			}
		}

		world.add(b1)
		world.add(b2)
		world.step(2.0)

		assertEquals(Vec2D(0, -10), b1.appliedImpulse)
		assertEquals(Vec2D(0, 0), b2.appliedImpulse)
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



	@Test
	def addBodiesThatWouldIntersectAfterMovementVerifyTheyDont {
		val world = new World

		val b1 = new Body
		b1.shape = Circle(2)
		b1.position = Vec2D(0, 0)
		b1.velocity = Vec2D(1, 0)
		val b2 = new Body
		b2.shape = Circle(2)
		b2.position = Vec2D(5, 0)
		b2.velocity = Vec2D(0, 0)
		world.add(b1)
		world.add(b2)

		val broadPhase = new BroadPhase {
			def detectPossibleCollisions(bodies: List[Body]) = {
				(b1, b2)::Nil
			}
		}
		world.broadPhase = broadPhase

		val narrowPhase = new NarrowPhase {
			def inspectCollision(delta: Double, b1: Body, b2: Body) = {
				Some(Collision(0.5, Contact(b1, b2, Vec2D(1, 0), Vec2D(-1, 0), Vec2D(3, 0))))
			}
		}
		world.narrowPhase = narrowPhase

		world.step(2.0)

		val expectedPosition = Vec2D(1, 0)

		assertEquals(expectedPosition.x, b1.position.x, 0.01)
		assertEquals(expectedPosition.y, b1.position.y, 0.01)
	}



	@Test
	def testForIntersectionAtHighPositionValues {
		// This is a real-world example that led to a bug. That's where all the odd numbers come from.

		val world = new World

		val b1 = new Body
		b1.shape = Circle(5)
		b1.position = Vec2D(83.9699958296336, 488.77572653374307)
		b1.velocity = Vec2D(-445.3620529718986, -50.442888201040574)
		val b2 = new Body
		b2.shape = Circle(30)
		b2.position = Vec2D(50.0, 469.8163204364546)
		b2.velocity = Vec2D(0.0, 0.0)
		world.add(b1)
		world.add(b2)

		val broadPhase = new BroadPhase {
			def detectPossibleCollisions(bodies: List[Body]) = {
				(b1, b2)::Nil
			}
		}
		world.broadPhase = broadPhase

		val narrowPhase = new NarrowPhase {
			def inspectCollision(delta: Double, b1: Body, b2: Body) = {
				Some(Collision(0.4766389925763854, Contact(b1, b2,
						Vec2D(-0.8732041733361332, -0.4873545646327327),
						Vec2D(0.8732041733361332, 0.4873545646327327), Vec2D(3, 0))))
			}
		}
		world.narrowPhase = narrowPhase

		world.step(0.02)

		assertTrue((b2.position - b1.position).squaredLength >= 35.0 * 35.0)
	}
}
