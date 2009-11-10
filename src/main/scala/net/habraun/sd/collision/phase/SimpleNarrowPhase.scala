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



package net.habraun.sd.collision.phase



import math.Vec2D
import shape.Circle
import shape.Contact
import shape.LineSegment
import shape.Shape
import test.CircleCircleTest
import test.CircleLineSegmentTest
import test.ContinuousCircleCircleTest
import test.ContinuousCircleLineSegmentTest



/**
 * A narrow phase implementation that uses the collision tests from the sd.collision package by default.
 */

class SimpleNarrowPhase( testCircleCircle: CircleCircleTest, testCircleLineSegment: CircleLineSegmentTest)
		extends NarrowPhase {

	/**
	 * Default constructor that provides default test implementations.
	 */

	def this() {
		this( new ContinuousCircleCircleTest, new ContinuousCircleLineSegmentTest )
	}



	/**
	 * Handles circle-circle and circle-line segment collisions.
	 */

	def apply( s1: Shape, s2: Shape ) = {
		s1 match {
			case circle1: Circle =>
				s2 match {
					case circle2: Circle =>
						for ( r <- testCircleCircle( circle1, circle2 ) ) yield {
							val contact = Contact( r.t, circle1, r.contact, r.normal, circle2 )
							Collision( r.t, contact, -contact )
						}

					case lineSegment: LineSegment =>
						for ( contact <- testCircleLineSegment( circle1, lineSegment ) ) yield {
							Collision( contact.t, contact, -contact )
						}

					case _ =>
						throw new IllegalArgumentException( "Unsupported shape: " + s2 )
				}

			case lineSegment1: LineSegment =>
				s2 match {
					case circle: Circle =>
						for ( contact <- testCircleLineSegment( circle, lineSegment1 ) ) yield {
							Collision( contact.t, contact, -contact )
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
