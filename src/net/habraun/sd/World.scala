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

		// Apply forces and impulses, make sure speed constraints are fulfilled.
		_bodies.foreach((body) => {
			var velocity = body.velocity

			// Apply forces.
			velocity += body.appliedForce / body.mass * delta
			body.resetForce

			// Apply impulses.
			velocity += body.appliedImpulse / body.mass
			body.resetImpulse

			// Solve movement constraints.
			val constrainedXVelocity = if (body.xMovementAllowed) velocity.x else 0.0
			val constrainedYVelocity = if (body.yMovementAllowed) velocity.y else 0.0
			val constrainedVelocity = Vec2D(constrainedXVelocity, constrainedYVelocity)

			// Set new velocity.
			body.velocity = constrainedVelocity
		})

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
			// Get the bodies out of the contact, so we can access them easier.
			val b1 = collision.contact1.b
			val b2 = collision.contact2.b

			// Compute the part of the velocities that points in the direction of the collision normals.
			val v1 = b1.velocity.project(collision.contact1.normal)
			val v2 = b2.velocity.project(collision.contact2.normal)

			// Apply impulses along the collision normals.
			val m1 = b1.mass
			val m2 = b2.mass
			if (m1 == Double.PositiveInfinity) {
				val impulse = (v1 - v2) * 2 * m2
				b2.applyImpulse(impulse)
			}
			else if (m2 == Double.PositiveInfinity) {
				val impulse = (v2 - v1) * 2 * m1
				b1.applyImpulse(impulse)
			}
			else {
				val impulse = (v2 - v1) * 2 * m1 * m2 / (m1 + m2)
				b1.applyImpulse(impulse)
				b2.applyImpulse(-impulse)
			}

			// If the time of impact given by the collision is smaller than 1.0, the bodies would overlap
			// after the movement has been carried out. We don't want that, we want the bodies to stop right
			// at the point of impact. Let's set them back, so the regular movement will put them right where
			// we want them.
			b1.position -= b1.velocity * delta * (1.0 - collision.t + 0.0001)
			b2.position -= b2.velocity * delta * (1.0 - collision.t + 0.0001)
		}

		// Apply speed.
		_bodies.foreach((body) => {
			// Move bodies.
			body.position = body.position + (body.velocity * delta)
		})
	}
}
