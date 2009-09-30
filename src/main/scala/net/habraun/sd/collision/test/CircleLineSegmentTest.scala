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
import shape.LineSegment



/**
 * The definition for collision tests for circle - line segment collisions. Circle - line segment tests take
 * the following parameters:
 * * c: The circle.
 * * ls: The line segment.
 * * pc: The position of the circle.
 * * pls: The position of the line segment.
 * * vc: The velocity of the circle.
 * * vls: The velocity of the line segment.
 */

trait CircleLineSegmentTest
		extends Function6[Circle, LineSegment, Vec2D, Vec2D, Vec2D, Vec2D, Option[TestResult]]