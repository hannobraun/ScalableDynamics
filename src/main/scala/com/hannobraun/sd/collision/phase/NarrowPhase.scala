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



package com.hannobraun.sd.collision.phase



import com.hannobraun.sd.collision.shape.Contact
import com.hannobraun.sd.collision.shape.Shape



/**
 * The narrow phase is the second of two phases of collision detection.
 * It determines if two shapes collide, and if they do, the properties of that collision. This operation is
 * potentially expensive, so a narrow phase should only test shapes that were deemed likely candidates for
 * collision by a broad phase.
 */

trait NarrowPhase extends Function2[ Shape, Shape, Option[ Contact ] ] {

	/**
	 * Determines if the two shapes collide, and if they do, returns a Collision object that describes the
	 * collision. If the shapes do not collide, this method returns None.
	 */

	def apply( s1: Shape, s2: Shape ): Option[ Contact ]
}
