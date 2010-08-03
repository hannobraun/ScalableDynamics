/*
	Copyright (c) 2009, 2010 Hanno Braun <mail@hannobraun.com>

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



package com.hannobraun.sd.dynamics



import com.hannobraun.sd.collision.shape.Contact
import com.hannobraun.sd.collision.shape.Shape
import com.hannobraun.sd.core.StepPhase
import com.hannobraun.sd.math.Vector2

import org.specs.Specification
import org.specs.mock.Mockito
import org.specs.runner.JUnit4



class ElasticCollisionReactionTest extends JUnit4( ElasticCollisionReactionSpec )



object ElasticCollisionReactionSpec extends Specification with Mockito {

	"ElasticCollisionReaction" should {
		"be a StepPhase." in {
			val reaction = new ElasticCollisionReaction
			reaction must haveSuperClass[ StepPhase[ Nothing, Contact ] ]
		}

		"return the passed constraints unchanged." in {
			val reaction = new ElasticCollisionReaction

			val s1 = new Shape {}
			val s2 = new Shape {}
			s1.mass = 2
			s2.mass = 2
			s1.velocity = Vector2( 5, 5 )
			s2.velocity = Vector2( -4, -4 )

			val contacts = List( Contact( s1, s2, Vector2( 0, 0 ), Vector2( 1, 0 ), 1, 0.5 ) )

			val ( updatedBodies, updatedContacts ) = reaction.execute( 0.5, Nil, contacts )

			updatedContacts must haveSameElementsAs( contacts )
		}

		"just switch velocities if two objects of equal mass are colliding." in {
			val reaction = new ElasticCollisionReaction

			val s1 = new Shape {}
			val s2 = new Shape {}
			s1.mass = 2
			s2.mass = 2
			s1.velocity = Vector2( 5, 5 )
			s2.velocity = Vector2( -4, -4 )

			reaction.execute( 0.5, Nil, List( Contact( s1, s2, Vector2( 0, 0 ), Vector2( 1, 0 ), 1, 0.5 ) ) )

			s1.velocity must beEqualTo( Vector2( -4, 5 ) )
			s2.velocity must beEqualTo( Vector2( 5, -4 ) )
		}

		"take the mass ratio into account when switching velocities of different shapes." in {
			val reaction = new ElasticCollisionReaction

			val s1 = new Shape {}
			val s2 = new Shape {}
			s1.mass = 6
			s2.mass = 2
			s1.velocity = Vector2( 2, 3 )
			s2.velocity = Vector2( -3, -3 )

			reaction.execute( 0.5, Nil, List( Contact( s1, s2, Vector2( 0, 0 ), Vector2( 1, 0 ), 1, 0.5 ) ) )

			s1.velocity must beEqualTo( Vector2( -1, 3 ) )
			s2.velocity must beEqualTo( Vector2( 6, -3 ) )
		}

		"just reflect the first body off a second, non-moving body with infinite mass." in {
			val reaction = new ElasticCollisionReaction

			val s1 = new Shape {}
			val s2 = new Shape {}
			s1.mass = 2
			s2.mass = Double.PositiveInfinity
			s1.velocity = Vector2( 3, 3 )
			s2.velocity = Vector2( 0, 0 )

			reaction.execute( 0.5, Nil, List( Contact( s1, s2, Vector2( 0, 0 ), Vector2( 1, 0 ), 1, 0.5 ) ) )

			s1.velocity must beEqualTo( Vector2( -3, 3 ) )
			s2.velocity must beEqualTo( Vector2( 0, 0 ) )
		}

		"just reflect the second body off a first, non-moving body with infinite mass." in {
			val reaction = new ElasticCollisionReaction

			val s1 = new Shape {}
			s1.mass = Double.PositiveInfinity
			s1.velocity = Vector2( 0, 0 )
			val s2 = new Shape {}
			s2.mass = 2
			s2.velocity = Vector2( -3, 3 )

			reaction.execute( 0.5, Nil, List( Contact( s1, s2, Vector2( 0, 0 ), Vector2( 1, 0 ), 1, 0.5 ) ) )

			s1.velocity must beEqualTo( Vector2( 0, 0 ) )
			s2.velocity must beEqualTo( Vector2( 3, 3 ) )
		}

		"reflect the first body off a second, moving body with infinite mass, additionally adding its velocity." in {
			val reaction = new ElasticCollisionReaction

			val s1 = new Shape {}
			s1.mass = 2
			s1.velocity = Vector2( 3, 3 )
			val s2 = new Shape {}
			s2.mass = Double.PositiveInfinity
			s2.velocity = Vector2( -1, 0 )

			reaction.execute( 0.5, Nil, List( Contact( s1, s2, Vector2( 0, 0 ), Vector2( 1, 0 ), 1, 0.5 ) ) )

			s1.velocity must beEqualTo( Vector2( -4, 3 ) )
			s2.velocity must beEqualTo( Vector2( -1, 0 ) )
		}

		"reflect the second body off a first, moving body with infinite mass, additionally adding its velocity." in {
			val reaction = new ElasticCollisionReaction

			val s1 = new Shape {}
			s1.mass = Double.PositiveInfinity
			s1.velocity = Vector2( 1, 0 )
			val s2 = new Shape {}
			s2.mass = 2
			s2.velocity = Vector2( -3, 3 )

			reaction.execute( 0.5, Nil, List( Contact( s1, s2, Vector2( 0, 0 ), Vector2( 1, 0 ), 1, 0.5 ) ) )

			s1.velocity must beEqualTo( Vector2( 1, 0 ) )
			s2.velocity must beEqualTo( Vector2( 4, 3 ) )
		}

		"not change the speed at all if both bodies have infinite mass." in {
			val reaction = new ElasticCollisionReaction

			val s1 = new Shape {}
			s1.mass = Double.PositiveInfinity
			s1.velocity = Vector2( 1, 1 )
			val s2 = new Shape {}
			s2.mass = Double.PositiveInfinity
			s2.velocity = Vector2( -1, -1 )

			reaction.execute( 0.5, Nil, List( Contact( s1, s2, Vector2( 0, 0 ), Vector2( 1, 0 ), 1, 0.5 ) ) )

			s1.velocity must beEqualTo( Vector2( 1, 1 ) )
			s2.velocity must beEqualTo( Vector2( -1, -1 ) )
		}
	}
}
