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



package net.habraun.sd.dynamics



import collision.shape.Contact
import collision.shape.Shape
import core.StepPhase
import math.Vector2

import org.specs.Specification
import org.specs.runner.JUnit4



class ElasticCollisionReactionTest extends JUnit4( ElasticCollisionReactionSpec )



object ElasticCollisionReactionSpec extends Specification {

	"ElasticCollisionReaction" should {
		"be a StepPhase." in {
			val reaction = new ElasticCollisionReaction
			reaction must haveSuperClass[ StepPhase[ Shape ] ]
		}

		"just switch velocities if two objects of equal mass are colliding." in {
			val reaction = new ElasticCollisionReaction

			val b1 = new Shape {}
			val b2 = new Shape {}
			b1.mass = 2
			b2.mass = 2
			b1.velocity = Vector2( 5, 5 )
			b2.velocity = Vector2( -4, -4 )

			val contact = Contact( b1, b2, Vector2( 0, 0 ), Vector2( 1, 0 ), 1, 0.5 )

			b1.addContact( contact )
			b2.addContact( -contact )

			reaction.filterAndStep( 0.5, List( b1, b2 ) )

			b1.velocity must beEqualTo( Vector2( -4, 5 ) )
			b2.velocity must beEqualTo( Vector2( 5, -4 ) )
		}

		"take the mass ratio into account when switching velocities of different masses." in {
			val reaction = new ElasticCollisionReaction

			val b1 = new Shape {}
			val b2 = new Shape {}
			b1.mass = 6
			b2.mass = 2
			b1.velocity = Vector2( 2, 3 )
			b2.velocity = Vector2( -3, -3 )

			val contact = Contact( b1, b2, Vector2( 0, 0 ), Vector2( 1, 0 ), 1, 0.5 )

			b1.addContact( contact )
			b2.addContact( -contact )

			reaction.filterAndStep( 0.5, List( b1, b2 ) )

			b1.velocity must beEqualTo( Vector2( -1, 3 ) )
			b2.velocity must beEqualTo( Vector2( 6, -3 ) )
		}

		"reflect a body off another body with infinite mass." in {
			val reaction = new ElasticCollisionReaction

			val b1 = new Shape {}
			val b2 = new Shape {}
			b1.mass = 2
			b2.mass = Double.PositiveInfinity
			b1.velocity = Vector2( 3, 3 )
			b2.velocity = Vector2( 0, 0 )

			val contact = Contact( b1, b2, Vector2( 0, 0 ), Vector2( 1, 0 ), 1, 0.5 )

			b1.addContact( contact )
			b2.addContact( -contact )

			reaction.filterAndStep( 0.5, List( b1, b2 ) )

			b1.velocity must beEqualTo( Vector2( -3, 3 ) )
			b2.velocity must beEqualTo( Vector2( 0, 0 ) )
		}

		"must not remove the contacts from the shapes." in {
			val reaction = new ElasticCollisionReaction

			val s1 = new Shape {}
			val s2 = new Shape {}

			val contact = Contact( s1, s2, Vector2( 0, 0 ), Vector2( 1, 0 ), 1, 0.0 )
			s1.addContact( contact )
			s2.addContact( -contact )

			reaction.filterAndStep( 0.0, List( s1, s2 ) )

			s1.contacts must beEqualTo( Set( contact ) )
			s2.contacts must beEqualTo( Set( -contact ) )
		}
	}
}
