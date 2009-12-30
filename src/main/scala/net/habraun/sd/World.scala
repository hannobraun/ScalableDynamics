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



import collision.CollisionDetector
import collision.phase.BroadPhase
import collision.phase.NarrowPhase
import collision.phase.SimpleBroadPhase
import collision.phase.SimpleNarrowPhase
import core.Body
import dynamics.ElasticCollisionReaction
import dynamics.PositionConstraintSolver
import dynamics.SimpleContactSolver
import dynamics.VelocityConstraintSolver
import dynamics.VerletIntegrator

import scala.collection.mutable.HashSet
import scala.reflect.Manifest



/**
 * The central class for the physics simulation.
 * World is basically a container for objects, whose attributes it updates every simulation step.
 */

class World[ B <: Body ] {

	val bodies = new HashSet[ B ]



	/**
	 * Step phases.
	 */

	var integrator = new VerletIntegrator
	var velocityConstraintSolver = new VelocityConstraintSolver
	var positionConstraintSolver = new PositionConstraintSolver
	var broadPhase = new SimpleBroadPhase
	var narrowPhase = new SimpleNarrowPhase
	var collisionDetector = new CollisionDetector( broadPhase, narrowPhase )
	var collisionReactor = new ElasticCollisionReaction
	var contactSolver  = new SimpleContactSolver



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
		// Execute step phases.
		integrator.step( dt, filterAndCast( bodies ), Nil )
		velocityConstraintSolver.step( dt, filterAndCast( bodies ), Nil )
		collisionDetector.step( dt, filterAndCast( bodies ), Nil )
		collisionReactor.step( dt, filterAndCast( bodies ), Nil )
		contactSolver.step( dt, filterAndCast( bodies ), Nil )
		positionConstraintSolver.step( dt, filterAndCast( bodies ), Nil )
	}



	private def filterAndCast[ T <: AnyRef ]( iterable: Iterable[ AnyRef ] )( implicit m: Manifest[ T ] ): Iterable[ T ] = {
		val filtered = iterable.filter( ( o ) => m.erasure.isAssignableFrom( o.getClass ) )
		val filteredAndCast = filtered.map( ( o ) => o.asInstanceOf[ T ] )

		filteredAndCast
	}
}
