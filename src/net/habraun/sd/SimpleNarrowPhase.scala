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
					val c1 = s1
					val c2 = s2
					val p1 = b1.position
					val p2 = b2.position
					val v1 = b1.velocity
					val v2 = b2.velocity

					for (r <- testCircleCircle(c1, c2, p1, p2, v1 * delta, v2 * delta)) yield {
						Collision(r.t, Contact(b1, b2, r.normal, -r.normal, r.contact))
					}

				case s2: LineSegment =>
					val c = s1
					val ls = s2
					val pc = b1.position
					val pls = b2.position
					val vc = b1.velocity * delta
					val vls = b2.velocity * delta

					for (r <- testCircleLineSegment(c, ls, pc, pls, vc, vls)) yield {
						Collision(r.t, Contact(b1, b2, r.normal, -r.normal, r.contact))
					}

				case _ =>
					None
			}

		case s1: LineSegment =>
			b2.shape match {
				case s2: Circle =>
					val c = s2
					val ls = s1
					val pc = b2.position
					val pls = b1.position
					val vc = b2.velocity * delta
					val vls = b1.velocity * delta

					for (r <- testCircleLineSegment(c, ls, pc, pls, vc, vls)) yield {
						Collision(r.t, Contact(b1, b2, r.normal, -r.normal, r.contact))
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
