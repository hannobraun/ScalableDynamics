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



package net.habraun.sd.core



import collision.shape.NoShape
import collision.shape.Shape
import math.Vec2D

import org.specs.Specification
import org.specs.runner.JUnit4



class BodyTest extends JUnit4( BodySpec )



object BodySpec extends Specification {

	"Body" should {
		"haven an initial position of (0, 0)." in {
			val body = new Body

			body.position must beEqualTo( Vec2D( 0, 0 ) )
		}

		"have its position assignable." in {
			val body = new Body

			val position = Vec2D( 10, 10 )
			body.position = position

			body.position must beEqualTo( position )
		}

		"throw an exception if null is assigned to position." in {
			val body = new Body

			( body.position = null ) must throwA[NullPointerException]
		}

		"remember the previous position if a position is assigned." in {
			val body = new Body

			val previousPosition = Vec2D( 5, 0 )
			body.position = previousPosition
			body.position = Vec2D( 0, 5 )

			body.previousPosition must beEqualTo( previousPosition )
		}

		"have an identical position and previous position." in {
			val body = new Body

			body.position must beEqualTo( body.previousPosition )
		}
	}

	"Body's movement part" should {
		"have an initial velocity of (0, 0)." in {
			val body = new Body

			body.velocity must beEqualTo( Vec2D( 0, 0 ) )
		}

		"have velocity assignable." in {
			val body = new Body

			val velocity = new Vec2D( 10, 10 )
			body.velocity = velocity

			body.velocity must beEqualTo( velocity )
		}

		"throw an exception if null is assigned to velocity." in {
			val body = new Body

			( body.velocity = null ) must throwA[NullPointerException]
		}

		"have an infinite maximum velocity initially." in {
			val body = new Body

			body.maxVelocity must beEqualTo( Double.PositiveInfinity )
		}

		"have the maxmimum velocity assigneable." in {
			val body = new Body

			val maxVelocity = 10.0
			body.maxVelocity = maxVelocity

			 body.maxVelocity must beEqualTo( maxVelocity )
		}

		"throw an exception if the maximum velocity is set to a negative value." in {
			val body = new Body

			( body.maxVelocity = -1.0 ) must throwAn[IllegalArgumentException]
		}

		"consider the maximum velocity if a velocity is assigned." in {
			val body = new Body

			body.maxVelocity = 10
			body.velocity = Vec2D( 15, 0 )

			body.velocity must beEqualTo( Vec2D( 10, 0 ) )
		}
	}

	"Body's mass code" should {
		"have an initial mass of 1.0." in {
			val body = new Body

			body.mass must beEqualTo( 1.0 )
		}

		"have mass assignable." in {
			val body = new Body

			val mass = 5.0
			body.mass = mass

			body.mass must beEqualTo( mass )
		}

		"throw an exception if mass is set to a negative value." in {
			val body = new Body

			( body.mass = -1.0 ) must throwAn[IllegalArgumentException]
		}

		"throw an exception if mass is set to zero." in {
			val body = new Body

			( body.mass = 0.0 ) must throwAn[IllegalArgumentException]
		}
	}

	"Body's force code" should {
		"have no applied forces initially." in {
			val body = new Body

			body.appliedForce must beEqualTo( Vec2D( 0, 0 ) )
		}

		"remember an applied force." in {
			val body = new Body

			val force = Vec2D( 10, 10 )
			body.applyForce( force )

			body.appliedForce must beEqualTo( force )
		}

		"add up multiple applied forces." in {
			val body = new Body

			body.applyForce( Vec2D( 2, 1 ) )
			body.applyForce( Vec2D( 1, 2 ) )

			body.appliedForce must beEqualTo( Vec2D( 3, 3 ) )
		}

		"throw an exception if an applied force is null." in {
			val body = new Body

			body.applyForce( null ) must throwA[NullPointerException]
		}

		"reset all applied forces if resetForce is called." in {
			val body = new Body

			body.applyForce( Vec2D( 10, 10 ) )
			body.resetForce

			body.appliedForce must beEqualTo( Vec2D( 0, 0 ) )
		}

		"have no applied impulses initially." in {
			val body = new Body

			body.appliedImpulse must beEqualTo( Vec2D( 0, 0 ) )
		}

		"remember an applied impulse." in {
			val body = new Body

			val impulse = Vec2D( 10, 10 )
			body.applyImpulse( impulse )

			body.appliedImpulse must beEqualTo( impulse )
		}

		"add up multiple applied impulses." in {
			val body = new Body

			body.applyImpulse( Vec2D( 2, 1 ) )
			body.applyImpulse( Vec2D( 1, 2 ) )

			body.appliedImpulse must beEqualTo( Vec2D( 3, 3 ) )
		}

		"throw an exception if an applied impulse is null." in {
			val body = new Body

			body.applyImpulse( null ) must throwA[NullPointerException]
		}

		"reset all applied impulses if resetImpulse is called." in {
			val body = new Body

			body.applyImpulse( Vec2D( 10, 10 ) )
			body.resetImpulse

			body.appliedImpulse must beEqualTo( Vec2D( 0, 0 ) )
		}
	}

	"Body's shape code" should {
		"have no initial shape." in {
			val body = new Body

			body.shape must beEqualTo( NoShape )
		}

		"have shape assignable." in {
			val body = new Body

			val shape = new Shape {}
			body.shape = shape

			body.shape must beEqualTo( shape )
		}

		"throw an exception if null is assigned to shape." in {
			val body = new Body

			( body.shape = null ) must throwA[NullPointerException]
		}
	}

	"Body's movement constraint code" should {
		"allow x movement initially." in {
			val body = new Body

			body.xMovementAllowed must beTrue
		}

		"allow y movement initially." in {
			val body = new Body

			body.yMovementAllowed must beTrue
		}

		"make it possible to disallow x movement." in {
			val body = new Body

			body.allowXMovement( false )

			body.xMovementAllowed must beFalse
		}

		"make it possible to disallow y movement." in {
			val body = new Body

			body.allowYMovement( false )

			body.yMovementAllowed must beFalse
		}
	}
}
