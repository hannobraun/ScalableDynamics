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
 * Models a contact of one shape with another.
 * A contact has the following attributes:
 * * s: The shape that is in contact with another.
 * * point: The position vector of the point of contact.
 * * normal: The surface normal of b at the point of contact. This is a unit vector.
 * * other: The other shape that s is in contact to.
 */

case class Contact( t: Double, s: Shape, point: Vec2D, normal: Vec2D, other: Shape ) {
	// Make sure the parameters are not null.
	if ( s == null || point == null || normal == null || other == null )
		throw new NullPointerException
	
	// Check if the normal vector is a unit vector.
	if ( !normal.unit )
		throw new IllegalArgumentException( "Normal must be a unit vectors (normal: " + normal + ")." )



	/**
	 * Returns an inverse contact, which has the following attributes:
	 * * inverse.s == this.other
	 * * inverse.point == this.point
	 * * inverse.normal == -this.normal
	 * * inverse.other == this.s
	 */

	def unary_- = Contact( t, other, point, -normal, s )
}
