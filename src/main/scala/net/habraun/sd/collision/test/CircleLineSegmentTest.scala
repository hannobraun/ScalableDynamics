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
import shape.LineSegment



/**
 * The definition for collision tests for circle - line segment collisions.
 */

trait CircleLineSegmentTest	extends Function2[ Circle, LineSegment, Option[ Contact ] ] {

	/**
	 *  Circle - line segment tests take the following parameters:
	 * * c: The circle.
	 * * ls: The line segment.
	 *
	 * If the circle and line segment collide, Some(TestResult) is returned. If not, the function returns
	 * None.
	 */

	def apply( c: Circle, ls: LineSegment ): Option[ Contact ]
}
