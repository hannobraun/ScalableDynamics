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
import math.Vec2D
import phase.BroadPhase
import phase.Collision
import phase.NarrowPhase
import shape.Contact
import shape.Shape

import org.specs.Specification
import org.specs.mock.Mockito
import org.specs.runner.JUnit4



class CollisionDetectorTest extends JUnit4( CollisionDetectorSpec )




object CollisionDetectorSpec extends Specification with Mockito {

	"CollisionDetector" should {
		"take the collision phases as parameters." in {
			val broadPhase = mock[BroadPhase]
			val narrowPhase = mock[NarrowPhase]

			new CollisionDetector( broadPhase, narrowPhase )
		}

		"be a StepPhase." in {
			val broadPhase = mock[BroadPhase]
			val narrowPhase = mock[NarrowPhase]
			val detector = new CollisionDetector( broadPhase, narrowPhase )

			detector must haveSuperClass[StepPhase[Shape]]
		}

		"pass a list of all objects to the broad phase." in {
			val broadPhase = mock[BroadPhase]
			val narrowPhase = mock[NarrowPhase]
			val detector = new CollisionDetector( broadPhase, narrowPhase )
			
			val shape1 = mock[Shape]
			val shape2 = mock[Shape]
			val shapes = shape1::shape2::Nil

			broadPhase( shape1::shape2::Nil ) returns Nil

			detector.filterAndStep( 0.0, shapes )

			broadPhase( shapes ) was called
		}

		"pass the pairs returned by the broad phase to the narrow phase." in {
			val broadPhase = mock[BroadPhase]
			val narrowPhase = mock[NarrowPhase]
			val detector = new CollisionDetector( broadPhase, narrowPhase )

			val shape1 = mock[Shape]
			val shape2 = mock[Shape]
			val shapes = shape1::shape2::Nil

			broadPhase( shapes ) returns ( shape1, shape2 )::Nil
			narrowPhase( shape1, shape2 ) returns None
			
			detector.filterAndStep( 0.0, shapes )

			narrowPhase( shape1, shape2 ) was called
		}

		"add the contacts returned by the narrow phase to the shape." in {
			val broadPhase = mock[BroadPhase]
			val narrowPhase = mock[NarrowPhase]
			val detector = new CollisionDetector( broadPhase, narrowPhase )

			val t = 0.5
			val shape1 = mock[Shape]
			val shape2 = mock[Shape]
			val shapes = shape1::shape2::Nil
			val contact1 = mock[ Contact ]
			val contact2 = mock[ Contact ]

			-contact1 returns contact2
			-contact2 returns contact1
			contact1.s returns shape1
			contact1.other returns shape2
			contact2.s returns shape2
			contact2.other returns shape1

			broadPhase( shapes ) returns ( shape1, shape2 )::Nil
			narrowPhase( shape1, shape2 ) returns Some( contact1 )

			detector.filterAndStep( 0.0, shapes )

			shape1.addContact( contact1 ) was called
			shape2.addContact( contact2 ) was called
		}
	}
}
