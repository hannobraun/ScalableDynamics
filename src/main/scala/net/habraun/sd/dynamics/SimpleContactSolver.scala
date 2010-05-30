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



package com.hannobraun.sd.dynamics



import com.hannobraun.sd.collision.shape.Contact
import com.hannobraun.sd.core.StepPhase



class SimpleContactSolver( tolerance: Double ) extends StepPhase[ Nothing, Contact ] {

	/**
	 * Constructor that provides a default tolerance value.
	 */

	def this() {
		this( 0.1 )
	}



	def execute( dt: Double, bodies: Iterable[ Nothing ], contacts: Iterable[ Contact ] ) = {
		for ( contact <- contacts ) {
			// These are the two shapes that are in contact.
			val s1 = contact.s
			val s2 = contact.other

			// Compute how much each shape is moved.
			// A value of 0.0 means that the shape is not moved at all. A value of 1.0 means that the shape is moved by depth units.
			val ( f1, f2 ) = if ( s1.mass != Double.PositiveInfinity && s2.mass != Double.PositiveInfinity ) {
				// None of the shapes has an infinite mass. Both need to be moved by some amount, depending on their mass ratio.
				val f1 = s2.mass / ( s1.mass + s2.mass )
				val f2 = s1.mass / ( s1.mass + s2.mass )

				( f1, f2 )
			}
			else if ( s1.mass == Double.PositiveInfinity && s2.mass == Double.PositiveInfinity ) {
				// Both of the shapes have an infinite mass and must not be moved.
				( 0.0, 0.0 )
			}
			else if ( s1.mass == Double.PositiveInfinity ) {
				// The first shape has an infinite mass and must not be moved.
				( 0.0, 1.0 )
			}
			else if ( s2.mass == Double.PositiveInfinity ) {
				// The second shape has an infinite mass and must not be moved.
				( 1.0, 0.0 )
			}
			else {
				throw new AssertionError( "This should never happen." )
			}
				
			// Adjust the positions.
			s1.position -= contact.normal * ( contact.depth + tolerance ) * f1
			s2.position += contact.normal * ( contact.depth + tolerance ) * f2
		}

		( Nil, Nil )
	}
}
