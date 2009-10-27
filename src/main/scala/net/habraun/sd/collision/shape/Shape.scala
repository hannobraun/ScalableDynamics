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



import core.Body

import scala.collection.mutable.HashSet



/**
 * Base trait for all shapes.
 * A body needs a shape in order to collide. To add a shape to a body, just mix a concrete Shape into the
 * body.
 */

trait Shape extends Body {

	/**
	 * The shape's list of contacts. Contacts are usually added when collisions are checked. They may be
	 * referred to later, when collision reaction is computed.
	 */

	private var _contacts = HashSet[ Contact ]()

	def contacts = _contacts

	def addContact( contact: Contact ) {
		if ( contact == null )
			throw new NullPointerException

		_contacts += contact
	}

	def removeContact( contact: Contact ) {
		if ( !contacts.contains( contact ) )
			throw new IllegalArgumentException( "Contact " + contact + " is not in contact set." )

		_contacts -= contact
	}
}
