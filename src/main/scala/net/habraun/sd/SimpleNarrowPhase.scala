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



import collision.shape.Circle
import collision.shape.LineSegment
import collision.shape.Shape
import collision.test.CircleCircleTest
import collision.test.CircleLineSegmentTest
import collision.test.ContinuousCircleCircleTest
import collision.test.ContinuousCircleLineSegmentTest
import math.Vec2D



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

	def apply( s1: Shape, s2: Shape ) = {
		val p1 = s1.position
		val p2 = s2.position
		val v1 = s1.position - s1.previousPosition
		val v2 = s2.position - s2.previousPosition

		s1 match {
			case circle1: Circle =>
				s2 match {
					case circle2: Circle =>
						for ( r <- testCircleCircle( circle1, circle2, p1, p2, v1, v2 ) ) yield {
							Collision( r.t, Contact( circle1, r.contact, r.normal, circle2 ),
									Contact( circle2, r.contact, -r.normal, circle1 ) )
						}

					case lineSegment: LineSegment =>
						for ( r <- testCircleLineSegment( circle1, lineSegment, p1, p2, v1, v2 ) ) yield {
							Collision( r.t, Contact( circle1, r.contact, r.normal, lineSegment ),
									Contact( lineSegment, r.contact, -r.normal, circle1 ) )
						}

					case _ =>
						throw new IllegalArgumentException( "Unsupported shape: " + s2 )
				}

			case lineSegment1: LineSegment =>
				s2 match {
					case circle: Circle =>
						for ( r <- testCircleLineSegment( circle, lineSegment1, p2, p1, v2, v1 ) ) yield {
							Collision( r.t, Contact( lineSegment1, r.contact, r.normal, circle ), null )
						}

					case lineSegment2: LineSegment =>
						None

					case _ =>
						throw new IllegalArgumentException( "Unsupported shape: " + s2 )
				}

			case _ =>
				throw new IllegalArgumentException( "Unsupported shape: " + s1 )
		}
	}
}
