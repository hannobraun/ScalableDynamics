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



object Scalar {
	implicit def doubleToScalar(d: Double) = new Scalar(d)
	implicit def intToScalar(i: Int) = new Scalar(i)
}



/**
 * Represents a scalar number.
 * This class is not meant to be used directly, as Double is used to represent scalar values. The sole
 * purpose of this class is to allow - in combination with the implicit conversions defined in the companion
 * object - to write expressions like "2 * vec", in addition "vec * 2".
 */

class Scalar(value: Double) {

	def * (vector: Vec2D) = vector * value
}
