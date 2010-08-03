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



package com.hannobraun.sd.collision.shape



/**
 * A circle, a shape that can be mixed into a body.
 * The default value of a circle's radius is 1.0. When you instantiate a Circle you can change its radius by overriding it.
 *
 * Overriding with a constant value works like this:
 * val circle = new Circle {
 *     override val radius = 2.0
 * }
 *
 * If you need to change the radius later, you can do it like this:
 * val circle = new Circle {
 *     override var radius = 2.0
 * }
 *
 * You can even set a dynamic value that depends on outside influences:
 * val circle = new Circle {
 *     override def radius = /insert fancy computation here\
 * }
 */

trait Circle extends Shape {

	def radius = 1.0
}
