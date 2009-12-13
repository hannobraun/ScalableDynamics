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



import math.Vector2



/**
 * Models a line segment.
 * A line segment has a direction attribute, which points from the first endpoint to the second endpoint. Its length is thus equal to the
 * length of the line segment.
 * The default value for direction is Vector2( 1, 0 ).
 *
 * When you instantiate a LineSegment, you can change its direction by overriding it.
 *
 * Overriding with a constant value works like this:
 * new LineSegment {
 *     override val direction = Vector2( 0, 2 )
 * }
 *
 * If you need to change the radius later, you can do it like this:
 * new LineSegment {
 *     override var direction = Vector2( 0, 2 )
 * }
 *
 * You can even set a dynamic value that depends on outside influences:
 * new LineSegment {
 *     override def direction = /insert fancy computation here\
 * }
 */

trait LineSegment extends Shape {

	/**
	 * The line segment's direction vector.
	 */

	def direction = Vector2( 1, 0 )
}
