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



/**
 * Models a collision between two bodies.
 * A collision has the following attributes:
 * * t: The time of impact, relative to the timeframe that was inspected by the collision solver. A value of
 *      0.0 means, the collision took place at the beginning of the time frame, 1.0 means at the end. A value
 *      of 0.5 would means, that the collision occured halfway through timeframe.
 * * contact1, contact2: The contact objects that represent the collision from the viewpoint of each of the
 *                       colliding bodies.
 */

case class Collision( t: Double, contact1: Contact, contact2: Contact ) {
	// Verify time of impact is valid.
	if ( t < 0.0 || t > 1.0 )
		throw new IllegalArgumentException( "Time of impact must be between 0.0 and 1.0." )

	// Verify contacts are not null.
	if ( contact1 == null || contact2 == null )
		throw new NullPointerException( "Contact must not be null." )

	// Verify contacts match each other.
	if ( contact1.b != contact2.other || contact1.other != contact2.b || contact1.point != contact2.point
			|| contact1.normal != -contact2.normal )
		throw new IllegalArgumentException( "Contacts do not match each other (contact1: " + contact1 + ","
				+ " contact2: " + contact2 + ")." )
}
