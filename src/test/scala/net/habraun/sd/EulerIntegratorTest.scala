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

import org.junit.Before
import org.junit.Test
import org.junit.Assert._



class EulerIntegratorTest {

	val t = 2.0

	var integrate: Integrator = null
	var body: Body = null



	@Before
	def setup {
		integrate = new EulerIntegrator
		body = new Body {}
	}



	@Test
	def integrateCheckPosition {
		body.velocity = Vec2D(1, 0)
		body = integrate(t, body)
		assertEquals(Vec2D(2, 0), body.position)
	}



	@Test
	def applyForceIntegrateCheckVelocity {
		body.mass = 5
		body.applyForce(Vec2D(5, 0))
		body = integrate(t, body)
		assertEquals(Vec2D(2, 0), body.velocity)
	}



	@Test
	def applyForceIntegrateTwiceCheckVelocity {
		body.mass = 5
		body.applyForce(Vec2D(5, 0))
		body = integrate(t, body)
		body = integrate(t, body)
		assertEquals(Vec2D(2, 0), body.velocity)
	}



	@Test
	def applyForceIntegrateCheckPosition {
		body.mass = 5
		body.applyForce(Vec2D(5, 0))
		body = integrate(t, body)
		assertEquals(Vec2D(4, 0), body.position)
	}



	@Test
	def applyImpulseIntegrateCheckVelocity {
		body.mass = 5
		body.velocity = Vec2D(3, 0)
		body.applyImpulse(Vec2D(5, 0))
		body = integrate(t, body)
		assertEquals(Vec2D(4, 0), body.velocity)
	}



	@Test
	def applyImpulseIntegrateCheckVelocity2 {
		body.mass = 5
		body.velocity = Vec2D(3, 0)
		body.applyImpulse(Vec2D(5, 0))
		body = integrate(5.0, body)
		assertEquals(Vec2D(4, 0), body.velocity)
	}



	@Test
	def applyImpulseIntegrateCheckImpulse {
		body.applyImpulse(Vec2D(10, 10))
		body = integrate(t, body)
		assertEquals(Vec2D(0, 0), body.appliedImpulse)
	}



	@Test
	def applyImpulseToStaticBodyIntegrateCheckVelocity {
		body.mass = Double.PositiveInfinity
		body.applyImpulse(Vec2D(2, 0))
		body = integrate(t, body)
		assertEquals(Vec2D(0, 0), body.velocity)
	}
}
