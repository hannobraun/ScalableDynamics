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



/**
 * The narrow phase is the second of two phases of collision detection.
 * It determines if two bodies collide, and if they do, the properties of that collision. This operation is
 * potentially expensive, so the narrow phase only tests bodies that were deemed likely candidates for
 * collision by the broad phase.
 */

trait NarrowPhase {

	/**
	 * Determines if the two bodies collide, and if they do, returns a Collision object that describes the
	 * collision. If the bodies do not collide, this method returns None.
	 */

	def inspectCollision(delta: Double, b1: Body, b2: Body): Option[Collision]
}
