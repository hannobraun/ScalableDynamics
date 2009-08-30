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



import math._



/**
 * Models a contact of one body to another.
 * A contact has the following attributes:
 * * b: The body that has contact to another.
 * * point: The position vector of the point of contact.
 * * normal: The surface normal of b at the point of contact. This is a unit vector.
 * * other: The other body that b has contact to.
 */

case class Contact(b: Body, point: Vec2D, normal: Vec2D, other: Body) {
	// Make sure the parameters are not null.
	if (b == null || point == null || normal == null || other == null)
		throw new NullPointerException
	
	// Check if vectors are unit vectors.
	if (normal.squaredLength < 0.95 || normal.squaredLength > 1.05)
		throw new IllegalArgumentException("Normal must be a unit vectors (normal: " + normal + ").")
}