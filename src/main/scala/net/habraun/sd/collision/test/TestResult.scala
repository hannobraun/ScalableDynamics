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



/**
 * This is the result of a collision test between two shapes.
 * A test result has the following attributes:
 * * t: The time of impact. This is a value between 0.0 (beginning of the movement) and 1.0 (end of the
 *      movement).
 * * contact: The point of contact between the two shapes.
 * * normal: The first shape's surface normal at the point of contact.
 */

case class TestResult( t: Double, contact: Vec2D, normal: Vec2D, depth: Double ) {
	// Check if t is valid.
	if ( t < 0.0 || t > 1.0 )
		throw new IllegalArgumentException( "The time must be between 0.0 and 1.0 (t: " + t + ")." )

	// Check if contact is valid.
	if ( contact == null )
		throw new NullPointerException( "Contact point must not be null." )

	// Check if normal is valid.
	if ( normal == null )
		throw new NullPointerException( "Normal must not be null." )
	if ( !normal.unit )
		throw new IllegalArgumentException( "Normal must be a unit vector (normal: " + normal + ")." )
}
