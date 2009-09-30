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



package net.habraun.sd.collision.shape



import math.Vec2D



/**
 * Models a line segment.
 * A line segment is defined by the following attributes:
 * * p: The position vector of the first point of the line segment.
 * * d: The direction vector of the line segment. It points from the first point to the second point. Its
 *      length is equal to the length of the line segment.
 */

case class LineSegment( p: Vec2D, d: Vec2D ) extends Shape {
	if ( d == Vec2D( 0, 0 ) ) throw new IllegalArgumentException( "Direction vector must not be 0." )
	if ( p == null || d == null ) throw new NullPointerException
}
