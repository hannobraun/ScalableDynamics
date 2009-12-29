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



package net.habraun.sd.core



import scala.reflect.Manifest



/**
 * A single phase of the iteration step that is executed by World.
 */

trait StepPhase[ B <: Body ] {

	/**
	 * A concrete iteration step phase must implement this method. It will be called during each step and the
	 * following parameters will be passed:
	 * * dt: The time that has passed since the last step.
	 * * filteredBodies: All bodies, filtered according to the type parameter of the step phase. For example,
	 *                   if a concrete step phase was concerned with something collision-related, it would
	 *                   extend StepPhase[Shape] and only Bodies with Shape mixed in would be passed to this
	 *                   step method.
	 */

	def step( dt: Double, filteredBodies: Iterable[ B ] )



	/**
	 * This is a helper method that is used by World. It filters the bodies according to the type parameter
	 * of the step phase and passes the filtered bodies to step.
	 */

	def filterAndStep( dt: Double, bodies: Iterable[ Body ] )( implicit m: Manifest[ B ] ) {
		val filtered = bodies.filter( ( body ) => m.erasure.isAssignableFrom( body.getClass ) )
		val result = filtered.map( ( body ) => body.asInstanceOf[ B ] )

		step ( dt, result )
	}
}
