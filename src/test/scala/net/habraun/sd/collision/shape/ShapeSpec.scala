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
import math.Vec2D

import scala.collection.mutable.HashSet

import org.specs.Specification
import org.specs.mock.Mockito
import org.specs.runner.JUnit4



class ShapeTest extends JUnit4( ShapeSpec )



object ShapeSpec extends Specification with Mockito {

	"Shape" should {
		"extend Body." in {
			val shape = new Shape {}

			shape must haveSuperClass[ Body ]
		}

		"have no contacts initially." in {
			val shape = new Shape {}

			shape.contacts must beEqualTo( HashSet[ Contact ]() )
		}

		"add contacts to the contact set." in {
			val shape = new Shape {}

			val contact = mock[ Contact ]

			shape.addContact( contact )

			shape.contacts must beEqualTo( HashSet( contact ) )
		}

		"throw an exception if a null contact is added." in {
			val shape = new Shape {}

			shape.addContact( null ) must throwA[ NullPointerException ]
		}

		"remove a contact from the contact set." in {
			val shape = new Shape {}
			val contact = mock[ Contact ]

			shape.addContact( contact )
			shape.removeContact( contact )

			shape.contacts must beEqualTo( HashSet[ Contact ]() )
		}

		"should throw an exception if a to-be-removed contact has not been added." in {
			val shape = new Shape {}
			
			val contact = mock[ Contact ]

			shape.removeContact( contact ) must throwAn[ IllegalArgumentException ]
		}
	}
}
