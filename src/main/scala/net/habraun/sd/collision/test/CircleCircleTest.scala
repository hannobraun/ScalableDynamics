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



package net.habraun.sd.collision.test



import math.Vec2D
import shape.Circle
import shape.Contact



/**
 * The definition for collision test functions for circle-circle collision.
 */

trait CircleCircleTest extends Function2[ Circle, Circle, Option[ Contact ] ] {

	/**
	 * Circle-circle tests take the
	 * following parameters:
	 * * c1: The first circle.
	 * * c2: The second circle.
	 *
	 * If the circles collide, Some(TestResult) is returned. If not, the function returns None.
	 */

	def apply( c1: Circle, c2: Circle ): Option[ Contact ]
}
