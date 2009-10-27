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
import math.Vec2D

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
			b1.velocity = Vec2D( 5, 5 )
			b2.velocity = Vec2D( -4, -4 )

			val contact = Contact( b1, Vec2D( 0, 0 ), Vec2D( 1, 0 ), b2 )

			b1.addContact( contact )
			b2.addContact( -contact )

			reaction.filterAndStep( 0.5, List( b1, b2 ) )

			b1.velocity must beEqualTo( Vec2D( -4, 5 ) )
			b2.velocity must beEqualTo( Vec2D( 5, -4 ) )
		}

		"take the mass ratio into account when switching velocities of different masses." in {
			val reaction = new ElasticCollisionReaction

			val b1 = new Shape {}
			val b2 = new Shape {}
			b1.mass = 6
			b2.mass = 2
			b1.velocity = Vec2D( 2, 3 )
			b2.velocity = Vec2D( -3, -3 )

			val contact = Contact( b1, Vec2D( 0, 0 ), Vec2D( 1, 0 ), b2 )

			b1.addContact( contact )
			b2.addContact( -contact )

			reaction.filterAndStep( 0.5, List( b1, b2 ) )

			b1.velocity must beEqualTo( Vec2D( -1, 3 ) )
			b2.velocity must beEqualTo( Vec2D( 6, -3 ) )
		}

		"reflect a body off another body with infinite mass." in {
			val reaction = new ElasticCollisionReaction

			val b1 = new Shape {}
			val b2 = new Shape {}
			b1.mass = 2
			b2.mass = Double.PositiveInfinity
			b1.velocity = Vec2D( 3, 3 )
			b2.velocity = Vec2D( 0, 0 )

			val contact = Contact( b1, Vec2D( 0, 0 ), Vec2D( 1, 0 ), b2 )

			b1.addContact( contact )
			b2.addContact( -contact )

			reaction.filterAndStep( 0.5, List( b1, b2 ) )

			b1.velocity must beEqualTo( Vec2D( -3, 3 ) )
			b2.velocity must beEqualTo( Vec2D( 0, 0 ) )
		}
	}
}
