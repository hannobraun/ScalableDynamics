/*
	Copyright (c) 2009, 2010 Hanno Braun <mail@hannobraun.com>

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



package com.hannobraun.sd



import com.hannobraun.sd.collision.CollisionDetector
import com.hannobraun.sd.collision.phase.SimpleBroadPhase
import com.hannobraun.sd.collision.phase.SimpleNarrowPhase
import com.hannobraun.sd.collision.shape.Shape
import com.hannobraun.sd.core.Body
import com.hannobraun.sd.dynamics.ElasticCollisionReaction
import com.hannobraun.sd.dynamics.PositionConstraint
import com.hannobraun.sd.dynamics.PositionConstraintSolver
import com.hannobraun.sd.dynamics.SimpleContactSolver
import com.hannobraun.sd.dynamics.VelocityConstraint
import com.hannobraun.sd.dynamics.VelocityConstraintSolver
import com.hannobraun.sd.dynamics.VerletIntegrator

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
		integrator.execute( dt, filterAndCast[ Body ]( bodies ), Nil )
		velocityConstraintSolver.execute( dt, filterAndCast[ VelocityConstraint ]( bodies ), Nil )
		val ( updatedBodies, updatedConstraints ) = collisionDetector.execute( dt, filterAndCast[ Shape ]( bodies ), Nil )
		collisionReactor.execute( dt, Nil, updatedConstraints )
		contactSolver.execute( dt, Nil, updatedConstraints )
		positionConstraintSolver.execute( dt, filterAndCast[ PositionConstraint ]( bodies ), Nil )
	}



	private def filterAndCast[ T <: AnyRef ]( iterable: Iterable[ AnyRef ] )( implicit m: Manifest[ T ] ): Iterable[ T ] = {
		val filtered = iterable.filter( ( o ) => m.erasure.isAssignableFrom( o.getClass ) )
		val filteredAndCast = filtered.map( ( o ) => o.asInstanceOf[ T ] )

		filteredAndCast
	}
}
