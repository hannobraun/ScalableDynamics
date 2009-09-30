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



/**
 * The definition for collision test functions for circle-circle collision. Circle-circle tests take the
 * following parameters:
 * * c1: The first circle.
 * * c2: The second circle.
 * * p1: Position of the first circle.
 * * p2: Position of the second circle.
 * * v1: Velocity of the first circle.
 * * c2: Velocity of the second circle.
 *
 * If the circles collide, Some(TestResult) is returned. If not, the function returns None.
 */

trait CircleCircleTest extends Function6[Circle, Circle, Vec2D, Vec2D, Vec2D, Vec2D, Option[TestResult]]
