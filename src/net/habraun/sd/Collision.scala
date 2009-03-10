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
 * * t is the time of impact, relative to the timeframe that was inspected by the collision solver. A value
 *   of 0.0 means, the collision took place at the beginning of the time frame, 1.0 means at the end. A value
 *   of 0.5 would means, that the collision occured halfway through timeframe.
 * * contact is the point of contact between the two bodies.
 */

case class Collision(t: Double, contact: Contact) {
	if (t < 0.0 || t > 1.0) throw new IllegalArgumentException("Time of impact must be between 0.0 and 1.0.")
	if (contact == null) throw new NullPointerException("contact must not be null.")
}
