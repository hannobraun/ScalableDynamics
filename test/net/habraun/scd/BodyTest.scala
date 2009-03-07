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



class BodyTest {

	@Test
	def checkInitialPosition {
		val body = new Body
		assertEquals(Vec2D(0, 0), body.position)
	}

	

	@Test
	def setPosition {
		val body = new Body
		val position = Vec2D(10, 10)
		body.position = position
		assertEquals(position, body.position)
	}



	@Test { val expected = classOf[NullPointerException] }
	def setPositionNullExpectException {
		val body = new Body
		body.position = null
	}



	@Test
	def checkInitialVelocity {
		val body = new Body
		assertEquals(Vec2D(0, 0), body.velocity)
	}



	@Test
	def setVelocity {
		val body = new Body
		val velocity = new Vec2D(10, 10)
		body.velocity = velocity
		assertEquals(velocity, body.velocity)
	}



	@Test { val expected = classOf[NullPointerException] }
	def setVelocityNullExpectException {
		val body = new Body
		body.velocity = null
	}



	@Test
	def checkInitialMaxVelocity {
		val body = new Body
		assertEquals(Double.PositiveInfinity, body.maxVelocity, 0.0)
	}



	@Test
	def setMaxVelocity {
		val body = new Body
		val maxVelocity = 10.0
		body.maxVelocity = maxVelocity
		assertEquals(maxVelocity, body.maxVelocity, 0.0)
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def setMaxVelocityNegativeExpectException {
		val body = new Body
		body.maxVelocity = -1.0
	}



	@Test
	def setMaxVelocitySetGreaterVelocity {
		val body = new Body
		body.maxVelocity = 10
		body.velocity = Vec2D(15, 0)
		assertEquals(Vec2D(10, 0), body.velocity)
	}



	@Test
	def checkInitialMass {
		val body = new Body
		assertEquals(1.0, body.mass, 0.0)
	}



	@Test
	def setMass {
		val body = new Body
		val mass = 5.0
		body.mass = mass
		assertEquals(mass, body.mass, 0.0)
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def setMassNegativeExpectException {
		val body = new Body
		body.mass = -1.0
	}



	@Test { val expected = classOf[IllegalArgumentException] }
	def setMassZeroExpectException {
		val body = new Body
		body.mass = 0.0
	}



	@Test
	def checkInitialAppliedForce {
		val body = new Body
		assertEquals(Vec2D(0, 0), body.appliedForce)
	}



	@Test
	def applyForce {
		val body = new Body
		val force = Vec2D(10, 10)
		body.applyForce(force)
		assertEquals(force, body.appliedForce)
	}



	@Test
	def applyTwoForces {
		val body = new Body
		val force = Vec2D(10, 10)
		body.applyForce(force)
		body.applyForce(force)
		assertEquals(Vec2D(20, 20), body.appliedForce)
	}



	@Test { val expected = classOf[NullPointerException] }
	def applyNullForce {
		val body = new Body
		body.applyForce(null)
	}



	@Test
	def applyForceResetForce {
		val body = new Body
		body.applyForce(Vec2D(10, 10))
		body.resetForce
		assertEquals(Vec2D(0, 0), body.appliedForce)
	}



	@Test
	def checkInitialAppliedImpulse {
		val body = new Body
		assertEquals(Vec2D(0, 0), body.appliedImpulse)
	}



	@Test
	def applyImpulse {
		val body = new Body
		val impulse = Vec2D(10, 10)
		body.applyImpulse(impulse)
		assertEquals(impulse, body.appliedImpulse)
	}



	@Test
	def applyTwoImpulses {
		val body = new Body
		body.applyImpulse(Vec2D(2, 1))
		body.applyImpulse(Vec2D(1, 2))
		assertEquals(Vec2D(3, 3), body.appliedImpulse)
	}



	@Test { val expected = classOf[NullPointerException] }
	def applyNullImpulse {
		val body = new Body
		body.applyImpulse(null)
	}



	@Test
	def applyAndResetImpulse {
		val body = new Body
		body.applyImpulse(Vec2D(10, 10))
		body.resetImpulse
		assertEquals(Vec2D(0, 0), body.appliedImpulse)
	}



	@Test { val expected = classOf[NullPointerException] }
	def applyNullForceExpectException {
		val body = new Body
		body.applyForce(null)
	}



	@Test
	def checkInitialShape {
		val body = new Body
		assertEquals(NoShape, body.shape)
	}



	@Test
	def setShape {
		val body = new Body
		val shape = new Shape {}
		body.shape = shape
		assertEquals(shape, body.shape)
	}



	@Test { val expected = classOf[NullPointerException] }
	def setShapeNullExpectException {
		val body = new Body
		body.shape = null
	}



	@Test
	def verifyXMovementInitiallyAllowed {
		val body = new Body
		assertTrue(body.xMovementAllowed)
	}



	@Test
	def verifyYMovementInitiallyAllowed {
		val body = new Body
		assertTrue(body.yMovementAllowed)
	}



	@Test
	def disallowXMovement {
		val body = new Body
		body.allowXMovement(false)
		assertFalse(body.xMovementAllowed)
	}



	@Test
	def dissalowYMovement {
		val body = new Body
		body.allowYMovement(false)
		assertFalse(body.yMovementAllowed)
	}
}
