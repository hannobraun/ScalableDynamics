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



	def inspectCollision(delta: Double, b1: Body, b2: Body) = {
		if (b1.shape.isInstanceOf[Circle] && b2.shape.isInstanceOf[Circle]) {
			val c1 = b1.shape.asInstanceOf[Circle]
			val c2 = b2.shape.asInstanceOf[Circle]
			val p1 = b1.position
			val p2 = b2.position
			val v1 = b1.velocity
			val v2 = b2.velocity 

			for (r <- testCircleCircle(c1, c2, p1, p2, v1 * delta, v2 * delta)) yield {
				Collision(r.t, Contact(b1, b2, r.normal, -r.normal, r.contact))
			}
		}
		else if ((b1.shape.isInstanceOf[Circle] && b2.shape.isInstanceOf[LineSegment])
				|| (b1.shape.isInstanceOf[LineSegment] && b2.shape.isInstanceOf[Circle])) {
			val circle = if (b1.shape.isInstanceOf[Circle]) b1 else b2
			val segment = if (b1.shape.isInstanceOf[Circle]) b2 else b1
			
			val c = circle.shape.asInstanceOf[Circle]
			val ls = segment.shape.asInstanceOf[LineSegment]
			val pc = circle.position
			val pls = segment.position
			val vc = circle.velocity * delta
			val vls = segment.velocity * delta

			for (r <- testCircleLineSegment(c, ls, pc, pls, vc, vls)) yield {
				Collision(r.t, Contact(circle, segment, r.normal, -r.normal, r.contact))
			}
		}
		else {
			None
		}
	}
}
