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



import net.habraun.sd.collision.phase.BroadPhase
import net.habraun.sd.collision.phase.NarrowPhase
import net.habraun.sd.collision.shape.Contact
import net.habraun.sd.collision.shape.Shape
import net.habraun.sd.core.StepPhase

import org.specs.Specification
import org.specs.mock.Mockito
import org.specs.runner.JUnit4



class CollisionDetectorTest extends JUnit4( CollisionDetectorSpec )




object CollisionDetectorSpec extends Specification with Mockito {

	"CollisionDetector" should {
		"take the collision phases as parameters." in {
			val broadPhase = mock[ BroadPhase ]
			val narrowPhase = mock[ NarrowPhase ]

			new CollisionDetector( broadPhase, narrowPhase )
		}

		"be a StepPhase." in {
			val broadPhase = mock[ BroadPhase ]
			val narrowPhase = mock[ NarrowPhase ]
			val detector = new CollisionDetector( broadPhase, narrowPhase )

			detector must haveSuperClass[ StepPhase[ Shape, Nothing ] ]
		}

		"return the body iterable unchanged." in {
			val broadPhase = mock[ BroadPhase ]
			val narrowPhase = mock[ NarrowPhase ]
			val detector = new CollisionDetector( broadPhase, narrowPhase )

			val shape1 = mock[ Shape ]
			val shape2 = mock[ Shape ]
			val shapes = List( shape1, shape2 )

			broadPhase( shapes ) returns Nil

			val ( updatedShapes, updatedConstraints ) = detector.execute( 0.0, shapes, Nil )

			updatedShapes must beEqualTo( shapes )
		}

		"pass a list of all objects to the broad phase." in {
			val broadPhase = mock[ BroadPhase ]
			val narrowPhase = mock[ NarrowPhase ]
			val detector = new CollisionDetector( broadPhase, narrowPhase )
			
			val shape1 = mock[ Shape ]
			val shape2 = mock[ Shape ]
			val shapes = List( shape1, shape2 )

			broadPhase( shapes ) returns Nil

			detector.execute( 0.0, shapes, Nil )

			broadPhase( shapes ) was called
		}

		"pass the pairs returned by the broad phase to the narrow phase." in {
			val broadPhase = mock[ BroadPhase ]
			val narrowPhase = mock[ NarrowPhase ]
			val detector = new CollisionDetector( broadPhase, narrowPhase )

			val shape1 = mock[ Shape ]
			val shape2 = mock[ Shape ]
			val shapes = List( shape1, shape2 )

			broadPhase( shapes ) returns List( ( shape1, shape2 ) )
			narrowPhase( shape1, shape2 ) returns None
			
			detector.execute( 0.0, shapes, Nil )

			narrowPhase( shape1, shape2 ) was called
		}

		"return the contacts returned by the narrow phase in the constraints iterable." in {
			val broadPhase = mock[ BroadPhase ]
			val narrowPhase = mock[ NarrowPhase ]
			val detector = new CollisionDetector( broadPhase, narrowPhase )

			val shape1 = mock[ Shape ]
			val shape2 = mock[ Shape ]
			val shapes = List( shape1, shape2 )

			val contact = mock[ Contact ]

			broadPhase( shapes ) returns List( ( shape1, shape2 ) )
			narrowPhase( shape1, shape2 ) returns Some( contact )

			val ( updatedShapes, updatedConstraints ) = detector.execute( 0.0, shapes, Nil )

			updatedConstraints must haveTheSameElementsAs( List( contact ) )
		}
	}
}
