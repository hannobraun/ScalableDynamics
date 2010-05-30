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



package com.hannobraun.sd.core



import com.hannobraun.sd.math.Vector2

import org.specs.Specification
import org.specs.runner.JUnit4



class BodyTest extends JUnit4( BodySpec )



object BodySpec extends Specification {

	"Body" should {
		"haven an initial position of (0, 0)." in {
			val body = new Body {}

			body.position must beEqualTo( Vector2( 0, 0 ) )
		}

		"have its position assignable." in {
			val body = new Body {}

			val position = Vector2( 10, 10 )
			body.position = position

			body.position must beEqualTo( position )
		}

		"throw an exception if null is assigned to position." in {
			val body = new Body {}

			( body.position = null ) must throwA[NullPointerException]
		}

		"remember the previous position if a position is assigned." in {
			val body = new Body {}

			val previousPosition = Vector2( 5, 0 )
			body.position = previousPosition
			body.position = Vector2( 0, 5 )

			body.previousPosition must beEqualTo( previousPosition )
		}

		"have an identical position and previous position." in {
			val body = new Body {}

			body.position must beEqualTo( body.previousPosition )
		}
	}

	"Body's movement part" should {
		"have an initial velocity of (0, 0)." in {
			val body = new Body {}

			body.velocity must beEqualTo( Vector2( 0, 0 ) )
		}

		"have velocity assignable." in {
			val body = new Body {}

			val velocity = new Vector2( 10, 10 )
			body.velocity = velocity

			body.velocity must beEqualTo( velocity )
		}

		"throw an exception if null is assigned to velocity." in {
			val body = new Body {}

			( body.velocity = null ) must throwA[NullPointerException]
		}

		"have an initial acceleration of (0, 0)." in {
			val body = new Body {}

			body.acceleration must beEqualTo( Vector2( 0, 0 ) )
		}

		"have its acceleration assignable." in {
			val body = new Body {}

			val acceleration = new Vector2( 10, 10 )
			body.acceleration = acceleration

			body.acceleration must beEqualTo( acceleration )
		}

		"throw an exception if null is assigned to acceleration." in {
			val body = new Body {}

			( body.acceleration = null ) must throwA[ NullPointerException ]
		}
	}

	"Body's mass code" should {
		"have an initial mass of 1.0." in {
			val body = new Body {}

			body.mass must beEqualTo( 1.0 )
		}

		"have mass assignable." in {
			val body = new Body {}

			val mass = 5.0
			body.mass = mass

			body.mass must beEqualTo( mass )
		}

		"throw an exception if mass is set to a negative value." in {
			val body = new Body {}

			( body.mass = -1.0 ) must throwAn[IllegalArgumentException]
		}

		"throw an exception if mass is set to zero." in {
			val body = new Body {}

			( body.mass = 0.0 ) must throwAn[IllegalArgumentException]
		}
	}

	"Body's force code" should {
		"have no applied forces initially." in {
			val body = new Body {}

			body.appliedForce must beEqualTo( Vector2( 0, 0 ) )
		}

		"remember an applied force." in {
			val body = new Body {}

			val force = Vector2( 10, 10 )
			body.applyForce( force )

			body.appliedForce must beEqualTo( force )
		}

		"add up multiple applied forces." in {
			val body = new Body {}

			body.applyForce( Vector2( 2, 1 ) )
			body.applyForce( Vector2( 1, 2 ) )

			body.appliedForce must beEqualTo( Vector2( 3, 3 ) )
		}

		"throw an exception if an applied force is null." in {
			val body = new Body {}

			body.applyForce( null ) must throwA[NullPointerException]
		}

		"reset all applied forces if resetForce is called." in {
			val body = new Body {}

			body.applyForce( Vector2( 10, 10 ) )
			body.resetForce

			body.appliedForce must beEqualTo( Vector2( 0, 0 ) )
		}
	}
}
