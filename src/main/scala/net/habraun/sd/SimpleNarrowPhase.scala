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



package net.habraun.sd



import collision._
import math._



/**
 * A narrow phase implementation that uses the collision tests from the sd.collision package by default.
 */

class SimpleNarrowPhase extends NarrowPhase {

	/**
	 * The tests this narrow phase uses can be set here.
	 */

	var testCircleCircle: CircleCircleTest = new ContinuousCircleCircleTest
	var testCircleLineSegment: CircleLineSegmentTest = new ContinuousCircleLineSegmentTest



	/**
	 * Handles circle-circle and circle-line segment collisions.
	 */

	def apply(b1: Body, b2: Body) = {
		val p1 = b1.position
		val p2 = b2.position
		val v1 = b1.position - b1.previousPosition
		val v2 = b2.position - b2.previousPosition

		b1.shape match {
			case s1: Circle =>
				b2.shape match {
					case s2: Circle =>
						for (r <- testCircleCircle(s1, s2, p1, p2, v1, v2)) yield {
							Collision(r.t, Contact(b1, r.contact, r.normal, b2),
									Contact(b2, r.contact, -r.normal, b1))
						}

					case s2: LineSegment =>
						for (r <- testCircleLineSegment(s1, s2, p1, p2, v1, v2)) yield {
							Collision(r.t, Contact(b1, r.contact, r.normal, b2),
									Contact(b2, r.contact, -r.normal, b1))
						}

					case NoShape =>
						None

					case _ =>
						throw new IllegalArgumentException("Unsupported shape: " + b2.shape)
				}

			case s1: LineSegment =>
				b2.shape match {
					case s2: Circle =>
						for (r <- testCircleLineSegment(s2, s1, p2, p1, v2, v1)) yield {
							Collision(r.t, Contact(b1, r.contact, r.normal, b2), null)
						}

					case s2: LineSegment =>
						None

					case NoShape =>
						None

					case _ =>
						throw new IllegalArgumentException("Unsupported shape: " + b2.shape)
				}

			case NoShape =>
				None

			case _ =>
				throw new IllegalArgumentException("Unsupported shape: " + b1.shape)
		}
	}
}
