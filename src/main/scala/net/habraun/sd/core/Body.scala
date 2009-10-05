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



import math.Vec2D



/**
 * Models a rigid body.
 * Body is mostly a data structure with very little logic. It needs to be added to a world in order to do
 * anything useful.
 */

trait Body {

	/**
	 * The body's position in the world, measured in meters.
	 * After creation, a body has an initial position of (0, 0).
	 * Must not be null.
	 */

	private[this] var _position = Vec2D( 0, 0 )

	def position = _position

	def position_=( p: Vec2D ) {
		if ( p == null ) throw new NullPointerException
		_previousPosition = _position
		_position = p
	}



	/**
	 * The previous position of the body, before the position was set.
	 * This attribute is initialized with the value Vec2D(0, 0).
	 */

	private[this] var _previousPosition = Vec2D( 0, 0 )

	def previousPosition = _previousPosition



	/**
	 * The body's velocity, measured in meters per second.
	 * Velocity vectors with a size greater than the maximum velocity will be shortened, retaining their
	 * direction.
	 * After creation, a body has an initial velocity of (0, 0).
	 * Must not be null.
	 */
	
	private[this] var _velocity = Vec2D( 0, 0 )

	def velocity = _velocity

	def velocity_=( v: Vec2D ) {
		if ( v == null ) throw new NullPointerException

		// It is not, which means we can safely set the velocity to the new velocity.
		_velocity = v
	}



	/**
	 * The body's mass, measured in kilograms.
	 * After creation, a body has an initial mass of 1. The mass must never be negative or zero.
	 * A mass of Double.PositiveInfinity indicates that a body is static. Static bodies don't move. Forces
	 * have no effect on them.
	 */
	
	private[this] var _mass = 1.0

	def mass = _mass

	def mass_=( m: Double ) {
		if ( m <= 0 ) throw new IllegalArgumentException
		_mass = m
	}



	/**
	 * Applies a force to the body.
	 * The effect of applied forces is computed when the world simulation is stepped.
	 * Calling this method several times between steps will simply add the applied forces up. When the
	 * simulation is stepped, the sum of all the forces is applied.
	 * A force must never be null.
	 */

	private[this] var _appliedForce = Vec2D( 0, 0 )

	def appliedForce = _appliedForce

	def applyForce( f: Vec2D ) {
		_appliedForce += f
	}

	def resetForce {
		_appliedForce = Vec2D( 0, 0 )
	}



	/**
	 * Applies an impulse to the body.
	 * The effect of the impulse is computed when the world simulation is stepped.
	 * Valling this method several times between steps will simply add the applied impulses up. When the
	 * simulation is stepped, the sum of all the impulses is applied.
	 * An impulse must never be null.
	 */

	private[this] var _appliedImpulse = Vec2D( 0, 0 )

	def appliedImpulse = _appliedImpulse

	def applyImpulse( impulse: Vec2D ) {
		_appliedImpulse += impulse
	}

	def resetImpulse {
		_appliedImpulse = Vec2D( 0, 0 )
	}



	/**
	 * The movement of an object can be constrained. Currently it is only possible to disallow movement along
	 * the x and y axes.
	 */

	private[this] var _xMovementAllowed = true
	private[this] var _yMovementAllowed = true

	def xMovementAllowed = _xMovementAllowed

	def yMovementAllowed = _yMovementAllowed

	def allowXMovement( allowed: Boolean ) = _xMovementAllowed = allowed

	def allowYMovement( allowed: Boolean ) = _yMovementAllowed = allowed
}