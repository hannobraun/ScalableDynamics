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



import math._

import scala.collection.mutable._



/**
 * The central class for the physics simulation.
 * World is basically a container for objects, whose attributes it updates every simulation step.
 */

class World {

	private[this] val _bodies = new HashSet[Body]



	/**
	 * Returns a copy of the body set as an Iterable.
	 */

	def bodies: Iterable[Body] = _bodies.clone



	/**
	 * The integrator is used to integrate the bodies.
	 */

	private[this] var integrate: Integrator = new EulerIntegrator

	def integrator = integrate

	def integrator_=(i: Integrator) {
		if (i == null) throw new NullPointerException("Integrator must not be null.")
		integrate = i
	}

	

	/**
	 * The broad phase is used for detecting which bodys can possible collide.
	 * This is done to cut down the time spent on doing detailed collision checks.
	 */

	private[this] var _broadPhase: BroadPhase = new SimpleBroadPhase

	def broadPhase = _broadPhase

	def broadPhase_=(bp: BroadPhase) = {
		if (bp == null) throw new NullPointerException
		_broadPhase = bp
	}



	/**
	 * The narrow phase is used to perform detailed (and possibly expensive) collision testing on body pairs
	 * that made it through the broad phase.
	 */

	private[this] var _narrowPhase: NarrowPhase = new SimpleNarrowPhase

	def narrowPhase = _narrowPhase

	def narrowPhase_=(np: NarrowPhase) = {
		if (np == null) throw new NullPointerException
		_narrowPhase = np
	}



	/**
	 * The constraint solver.
	 */

	private[this] var solve: ConstraintSolver = new ImpulseSolver

	def constraintSolver = solve

	def constraintSolver_=(solver: ConstraintSolver) = {
		if (solver == null) throw new NullPointerException
		solve = solver
	}



	/**
	 * Adds a body to the world. The body will be simulated until it is removed.
	 */
	
	def add(body: Body) {
		_bodies.addEntry(body)
	}



	/**
	 * Removes the body from the world. The body will no longer be simulated.
	 */

	def remove(body: Body) {
		_bodies.removeEntry(body)
	}
	


	/**
	 * Steps the physics simulation.
	 * All bodies are moved, according to their velocity and the forces that are applied to them.
	 */
	
	def step(delta: Double) {
		// Check if delta is valid.
		if (delta < 0.0) throw new IllegalArgumentException("Time delta must be 0 or greater.")

		// Integrate bodies.
		_bodies.foreach(integrate(delta, _))

		// Collision detection.
		val possibleCollisionPairs = broadPhase.detectPossibleCollisions(_bodies.toList)
		val possibleCollisions = possibleCollisionPairs.map((pair) => {
			narrowPhase.inspectCollision(pair._1, pair._2)
		})

		// Compute collision effects.
		// This is a tricky construction. The "possibleCollision <- collision" part is like an outer for loop
		// that iterates through all collisions. Collisions is a list of Option[Collision], this means,
		// during each iteration possibleCollision is either Some(collision) or None.
		// The "collision <- possibleCollision" part is technically an inner loop that iterates through the
		// Option[Collision]. This works because because Option is like a collection that contains either one
		// (if if is an instance of Some) or no (if it is None) elements.
		// Despite the long explanation, what this does is actually pretty simple: We loop through the list
		// of possible collisions. We execute the yield stuff only for actual collisions, not for None.
		for ( possibleCollision <- possibleCollisions; collision <- possibleCollision ) yield {
			solve(delta, collision)
		}
	}
}
