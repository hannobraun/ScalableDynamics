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



class SimpleNarrowPhase extends NarrowPhase {

	var testCircleCircle: CircleCircleTest = new ContinuousCircleCircleTest
	var testCircleLineSegment: CircleLineSegmentTest = new ContinuousCircleLineSegmentTest



	def inspectCollision(delta: Double, b1: Body, b2: Body) = b1.shape match {
		case s1: Circle =>
			b2.shape match {
				case s2: Circle =>
					for (r <- testCircleCircle(s1, s2, b1.position, b2.position, b1.velocity * delta,
							b2.velocity * delta)) yield {
						Collision(r.t, Contact(b1, r.contact, r.normal, b2),
								Contact(b2, r.contact, -r.normal, b1))
					}

				case s2: LineSegment =>
					for (r <- testCircleLineSegment(s1, s2, b1.position, b2.position, b1.velocity * delta,
							b2.velocity * delta)) yield {
						Collision(r.t, Contact(b1, r.contact, r.normal, b2),
								Contact(b2, r.contact, -r.normal, b1))
					}

				case _ =>
					None
			}

		case s1: LineSegment =>
			b2.shape match {
				case s2: Circle =>
					for (r <- testCircleLineSegment(s2, s1, b2.position, b1.position, b2.velocity * delta,
							b1.velocity * delta)) yield {
						Collision(r.t, Contact(b1, r.contact, r.normal, b2),
								null)
					}

				case s2: LineSegment =>
					None

				case _ =>
					None
			}

		case _ =>
			None
	}
}
