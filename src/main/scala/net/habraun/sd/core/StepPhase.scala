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

trait StepPhase[ B <: Body, C <: AnyRef ] {

	/**
	 * A concrete iteration step phase must implement this method. It will be called during each step and the
	 * following parameters will be passed:
	 * * dt: The time that has passed since the last step.
	 * * bodies: All bodies, filtered according to the type parameter of the step phase. For example, if a concrete step phase was
	 *           concerned with something collision-related, it would extend StepPhase[ Shape ] and only Bodies with Shape mixed in would
	 *           be passed to this step method.
	 * * constraints: All constraints, filtered according to the type parameter.
	 */

	def step( dt: Double, bodies: Iterable[ B ], constraints: Iterable[ C ] )
}
