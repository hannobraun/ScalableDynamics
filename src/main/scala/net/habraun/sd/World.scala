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
import collision.phase.NarrowPhase
import collision.phase.SimpleBroadPhase
import collision.phase.SimpleNarrowPhase
import collision.shape.Shape
import core.Body
import dynamics.PositionConstraint
import dynamics.PositionConstraintSolver
import dynamics.VelocityConstraint
import dynamics.VelocityConstraintSolver
import math.Vec2D

import scala.collection.mutable.HashSet



/**
 * The central class for the physics simulation.
 * World is basically a container for objects, whose attributes it updates every simulation step.
 */

class World[B <: Body] {

	val bodies = new HashSet[B]



	/**
	 * Step phases.
	 */

	var velocityConstraintSolver = new VelocityConstraintSolver
	var positionConstraintSolver = new PositionConstraintSolver



	/**
	 * The integrator is used to integrate the bodies.
	 */

	private[this] var _integrator: Integrator = new EulerIntegrator

	def integrator = _integrator

	def integrator_=( newIntegrator: Integrator ) {
		if ( newIntegrator == null )
			throw new NullPointerException( "Integrator must not be null." )

		_integrator = newIntegrator
	}

	

	/**
	 * The broad phase is used for detecting which bodys can possible collide.
	 * This is done to cut down the time spent on doing detailed collision checks.
	 */

	private[this] var _broadPhase: BroadPhase = new SimpleBroadPhase

	def broadPhase = _broadPhase

	def broadPhase_=( newBroadPhase: BroadPhase ) = {
		if ( newBroadPhase == null )
			throw new NullPointerException( "Broad phase must not be null." )

		_broadPhase = newBroadPhase
	}



	/**
	 * The narrow phase is used to perform detailed (and possibly expensive) collision testing on body pairs
	 * that made it through the broad phase.
	 */

	private[this] var _narrowPhase: NarrowPhase = new SimpleNarrowPhase

	def narrowPhase = _narrowPhase

	def narrowPhase_=( newNarrowPhase: NarrowPhase ) = {
		if ( newNarrowPhase == null )
			throw new NullPointerException( "Narrow phase must not be null." )

		_narrowPhase = newNarrowPhase
	}



	/**
	 * The constraint solver.
	 */

	private[this] var _collisionSolver: CollisionSolver = new ImpulseSolver

	def collisionSolver = _collisionSolver

	def collisionSolver_=( newCollisionSolver: CollisionSolver ) = {
		if ( newCollisionSolver == null )
			throw new NullPointerException( "Collision solver must not be null." )

		_collisionSolver = newCollisionSolver
	}



	/**
	 * Adds a body to the world. The body will be simulated until it is removed.
	 */
	
	def add( body: B ) {
		bodies.addEntry( body )
	}



	/**
	 * Removes the body from the world. The body will no longer be simulated.
	 */

	def remove( body: B ) {
		bodies.removeEntry( body )
	}
	


	/**
	 * Steps the physics simulation.
	 * All bodies are moved, according to their velocity and the forces that are applied to them.
	 * The parameter dt is the time delta for this simulation step.
	 */
	
	def step( dt: Double ) {
		// Check if delta is valid.
		if ( dt < 0.0 )
			throw new IllegalArgumentException( "Time delta must be 0 or greater." )

		// Integrate bodies.
		bodies.foreach( integrator( dt, _ ) )

		// Execute step phases.
		velocityConstraintSolver.filterAndStep( dt, bodies )
		positionConstraintSolver.filterAndStep( dt, bodies )

		// Filter all shapes from the body set.
		val shapes = bodies.map( {
			_ match {
				case s: Shape =>
					Some( s )
				case _ =>
					None
			}
		} ).filter( _ != None ).map( _ match { case Some( s ) => s } )

		// Collision detection.
		val possibleCollisionPairs = broadPhase( shapes.toList )
		val possibleCollisions = possibleCollisionPairs.map( ( pair ) => {
			narrowPhase( pair._1, pair._2 )
		} )

		// Compute collision effects.
		// This is a tricky construction. The "possibleCollision <- collision" part is like an outer for loop
		// that iterates through all collisions. Collisions is a list of Option[Collision], this means,
		// during each iteration possibleCollision is either Some(collision) or None.
		// The "collision <- possibleCollision" part is technically an inner loop that iterates through the
		// Option[Collision]. This works because because Option is like a collection that contains either one
		// (if if is an instance of Some) or no (if it is None) elements.
		// Despite the long explanation, what this does is actually pretty simple: We loop through the list
		// of possible collisions. We execute the yield stuff only for actual collisions, not for None.
		for ( possibleCollision <- possibleCollisions; collision <- possibleCollision ) {
			collisionSolver( dt, collision )
		}
	}
}
