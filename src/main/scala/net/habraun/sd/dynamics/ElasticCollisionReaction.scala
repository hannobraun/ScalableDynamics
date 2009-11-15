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



package net.habraun.sd.dynamics



import core.StepPhase
import collision.shape.Shape



/**
 * Performs elastic collision reaction.
 */

class ElasticCollisionReaction extends StepPhase[ Shape ] {

	def step( dt: Double, shapes: Iterable[ Shape ] ) {
		for ( shape <- shapes ) {
			for ( contact <- shape.contacts ) {
				val s1 = contact.s
				val s2 = contact.other

				val v1 = s1.velocity.projectOn( contact.normal )
				val v2 = s2.velocity.projectOn( contact.normal )

				val m1 = s1.mass
				val m2 = s2.mass

				s1.velocity -= v1
				s2.velocity -= v2

				if ( m2 != Double.PositiveInfinity )
					s1.velocity += v2 * m2 / m1
				else
					s1.velocity -= v1

				if ( m1 != Double.PositiveInfinity )
					s2.velocity += v1 * m1 / m2
				else
					s2.velocity -= v2

				s1.removeContact( contact )
				s2.removeContact( -contact )
			}
		}
	}
}