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
 * Models a contact point between two bodies.
 * A contact point has the following attributes:
 * * b1 and b2 are the two bodies that have contact.
 * * normal1 is the surface normal for b1 at the point of contact. This is a unit vector.
 * * normal2 is the surface normal for b2 at the point of contact. This is a unit vector.
 * * point is the position vector of the point of contact.
 */

case class Contact(b1: Body, b2: Body, normal1: Vec2D, normal2: Vec2D, point: Vec2D) {
	// Make sure the parameters are not null.
	if (b1 == null || b2 == null || normal1 == null || point == null)
		throw new NullPointerException
	
	// Check if normal vectors are inverse to each other,
	if (normal1 != -normal2)
		throw new IllegalArgumentException("Both collision normals must be inverse to each other.")

	// Check if vectors are unit vectors.
	if (normal1.squaredLength > 1.05 || normal1.squaredLength < 0.95)
		throw new IllegalArgumentException("Normals must be unit vectors. Normal 1: " + normal1
				+ ", Normal 2: " + normal2)
}
