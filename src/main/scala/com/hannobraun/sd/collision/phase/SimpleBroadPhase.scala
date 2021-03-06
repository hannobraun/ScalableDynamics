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



package com.hannobraun.sd.collision.phase



import com.hannobraun.sd.collision.shape.Shape



/**
 * SimpleBroadPhase is the simplest possible implementation of a broad phase. It just compiles all possible
 * pairs of the bodies it is given and returns those.
 * If any other broad phase implementation is available, it should probably be used in place of this one.
 */

class SimpleBroadPhase extends BroadPhase {

	/**
	 * Returns all possible pairs of the given bodies.
	 */

	def apply( shapes: Iterable[Shape] ) = {
		def buildPairs( list: List[Shape], pairs: List[( Shape, Shape )]): List[( Shape, Shape )] = {
			if ( list.isEmpty )
				pairs
			else
				buildPairs( list.tail, pairs:::list.tail.map( ( list.head, _ ) ) )
		}

		buildPairs( shapes.toList, Nil )
	}
}
