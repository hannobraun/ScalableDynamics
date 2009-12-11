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
}
