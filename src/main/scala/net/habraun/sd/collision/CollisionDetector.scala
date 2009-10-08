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



package net.habraun.sd.collision



import core.StepPhase
import phase.BroadPhase
import phase.NarrowPhase
import shape.Shape



class CollisionDetector( broadPhase: BroadPhase, narrowPhase: NarrowPhase ) extends StepPhase[Shape] {

	def step( dt: Double, shapes: Iterable[Shape] ) {
		// Broad phase. Checks all shapes in a performant way and returns a list of possible collisions.
		val possiblyCollidingPairs = broadPhase( shapes )

		// Narrow Phase. Performs a detailed and possibly performance-heavy collision check for a pair of
		// shapes.
		val possibleCollisions = for ( possibleCollision <- possiblyCollidingPairs ) yield {
			narrowPhase( possibleCollision._1, possibleCollision._2 )
		}

		// Add all detected contacts to the shapes they belong to.
		for ( possibleCollision <- possibleCollisions; collision <- possibleCollision ) {
			collision.contact1.s.addContact( collision.contact1 )
			collision.contact2.s.addContact( collision.contact2 )
		}
	}
}
