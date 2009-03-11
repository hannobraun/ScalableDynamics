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



package net.habraun.sd.math



/**
 * A 2D vector.
 * Important: Vec2D is immutable. This means, all operations that modify the vector return a new vector,
 * leaving the old one unchanged.
 */

case class Vec2D(x: Double, y: Double) {

	/**
	 * Adds another vector.
	 */

	def + (vector: Vec2D) = Vec2D(x + vector.x, y + vector.y)



	/**
	 * Substracts another vector.
	 */

	def - (vector: Vec2D) = Vec2D(x - vector.x, y - vector.y)



	/**
	 * Multiplies the vector with a scalar.
	 */

	def * (scalar: Double) = Vec2D(x * scalar, y * scalar)



	/**
	 * Divides the vector with a scalar.
	 */

	def / (scalar: Double) = Vec2D(x / scalar, y / scalar)



	/**
	 * Computes the dot product of this vector and another vector.
	 */

	def * (vector: Vec2D) = (x * vector.x) + (y * vector.y)



	/**
	 * Returns the inverse vector of this vector.
	 * The inverse vector is the vector with the same length and opposite direction.
	 */

	def unary_- = Vec2D(x * -1, y * -1)



	/**
	 * Returns the length of the vector.
	 * Computing the length of the vector is much more expensive than computing its squared length. If
	 * performance is critical, it is recommended to use squaredLength where applicable.
	 */

	def length = Math.sqrt((x * x) + (y * y))



	/**
	 * Returns the squared length of the vector.
	 * If performance is critical, it is recommended to use this method over the length method where
	 * applicable, as it is much cheaper.
	 */

	def squaredLength = (x * x) + (y * y)



	/**
	 * Returns the unit vector with the same direction as this vector.
	 */

	def normalize = this / this.length



	/**
	 * Returns the orthogonal vector to this vector that is rotated by 90 degrees to the left.
	 */

	def orthogonal = Vec2D(-y, x)



	/**
	 * Projects the vector onto the given vector.
	 */

	def project(vec: Vec2D) = vec * ((this * vec) / (vec * vec))



	/**
	 * Returns true if the vector is a unit vector, false otherwise.
	 */

	def unit = squaredLength < 1.05 && squaredLength > 0.95
}
