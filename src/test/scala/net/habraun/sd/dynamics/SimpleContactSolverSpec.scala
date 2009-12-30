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



import collision.shape.Contact
import collision.shape.Shape
import core.StepPhase
import math.Vector2

import org.specs.Specification
import org.specs.mock.Mockito
import org.specs.runner.JUnit4



class SimpleContactSolverTest extends JUnit4( SimpleContactSolverSpec )



object SimpleContactSolverSpec extends Specification with Mockito {

	"SimpleContactSolver" should {
		"be a StepPhase." in {
			val solver = new SimpleContactSolver( 0 )
			
			solver must haveSuperClass[ StepPhase[ Shape, Contact ] ]
		}

		"not change the position of two shapes that are just touching." in {
			val solver = new SimpleContactSolver( 0 )


			val pos1 = Vector2( 2, 2 )
			val pos2 = Vector2( 4, 4 )

			val s1 = new Shape {}
			s1.position = pos1
			val s2 = new Shape {}
			s2.position = pos2

			solver.step( 0.0, List( s1, s2 ), List( Contact( s1, s2, Vector2( 5, 5 ), Vector2( 1, 0 ), 0, 0.0 ) ) )

			s1.position must beEqualTo( pos1 )
			s2.position must beEqualTo( pos2 )
		}

		"move two intersecting shapes of equal mass equally far along the normal, so they don't intersect anymore." in {
			val solver = new SimpleContactSolver( 0 )

			val s1 = new Shape {}
			s1.position = Vector2( 2, 0 )
			s1.mass = 2
			val s2 = new Shape {}
			s2.position = Vector2( 3, 0 )
			s2.mass = 2

			solver.step( 0.0, List( s1, s2 ), List( Contact( s1, s2, Vector2( 2.5, 0 ), Vector2( 1, 0 ), 1, 0.0 ) ) )

			s1.position must beEqualTo( Vector2( 1.5, 0 ) )
			s2.position must beEqualTo( Vector2( 3.5, 0 ) )
		}

		"move an intersecting shape by an amount proportional to its mass ratio to to the other shape." in {
			val solver = new SimpleContactSolver( 0 )

			val s1 = new Shape {}
			s1.position = Vector2( 4, 0 )
			s1.mass = 2
			val s2 = new Shape {}
			s2.position = Vector2( 5, 0 )
			s2.mass = 4

			solver.step( 0.0, List( s1, s2 ), List( Contact( s1, s2, Vector2( 4.5, 0 ), Vector2( 1, 0 ), 3, 0.0 ) ) )

			s1.position must beEqualTo( Vector2( 2, 0 ) )
			s2.position must beEqualTo( Vector2( 6, 0 ) )
		}

		"not move the first shape at all, if it has infinite mass." in {
			val solver = new SimpleContactSolver( 0 )

			val pos1 = Vector2( 2, 0 )

			val s1 = new Shape {}
			s1.position = pos1
			s1.mass = Double.PositiveInfinity
			val s2 = new Shape {}
			s2.position = Vector2( 3, 0 )
			s2.mass = 2

			solver.step( 0.0, List( s1, s2 ), List( Contact( s1, s2, Vector2( 2.5, 0 ), Vector2( 1, 0 ), 1, 0.0 ) ) )

			s1.position must beEqualTo( pos1 )
			s2.position must beEqualTo( Vector2( 4, 0 ) )
		}

		"not move the second shape at all, if it has infinite mass." in {
			val solver = new SimpleContactSolver( 0 )

			val pos2 = Vector2( 3, 0 )

			val s1 = new Shape {}
			s1.position = Vector2( 2, 0 )
			s1.mass = 2
			val s2 = new Shape {}
			s2.position = pos2
			s2.mass = Double.PositiveInfinity

			solver.step( 0.0, List( s1, s2 ), List( Contact( s1, s2, Vector2( 2.5, 0 ), Vector2( 1, 0 ), 1, 0.0 ) ) )

			s1.position must beEqualTo( Vector2( 1, 0 ) )
			s2.position must beEqualTo( pos2 )
		}

		"not move the shapes at all, if both have inifinte mass." in {
			val solver = new SimpleContactSolver( 0 )

			val pos1 = Vector2( 2, 0 )
			val pos2 = Vector2( 3, 0 )

			val s1 = new Shape {}
			s1.position = pos1
			s1.mass = Double.PositiveInfinity
			val s2 = new Shape {}
			s2.position = pos2
			s2.mass = Double.PositiveInfinity

			solver.step( 0.0, List( s1, s2 ), List( Contact( s1, s2, Vector2( 2.5, 0 ), Vector2( 1, 0 ), 1, 0.0 ) ) )

			s1.position must beEqualTo( pos1 )
			s2.position must beEqualTo( pos2 )
		}

		"apply an additional tolerance value when moving shapes." in {
			val solver = new SimpleContactSolver( 0.5 )

			val s1 = new Shape {}
			s1.position = Vector2( 2, 0 )
			s1.mass = 2
			val s2 = new Shape {}
			s2.position = Vector2( 3, 0 )
			s2.mass = 2

			solver.step( 0.0, List( s1, s2 ), List( Contact( s1, s2, Vector2( 2.5, 0 ), Vector2( 1, 0 ), 2, 0.0 ) ) )

			s1.position must beEqualTo( Vector2( 0.75, 0 ) )
			s2.position must beEqualTo( Vector2( 4.25, 0 ) )
		}

		"never move bodies of infinite mass, tolerance notwithstanding." in {
			val solver = new SimpleContactSolver( 0.5 )

			val s1 = new Shape {}
			s1.position = Vector2( 2, 0 )
			s1.mass = Double.PositiveInfinity
			val s2 = new Shape {}
			s2.position = Vector2( 3, 0 )
			s2.mass = Double.PositiveInfinity

			solver.step( 0.0, List( s1, s2 ), List( Contact( s1, s2, Vector2( 2.5, 0 ), Vector2( 1, 0 ), 0, 0.0 ) ) )

			s1.position must beEqualTo( Vector2( 2, 0 ) )
			s2.position must beEqualTo( Vector2( 3, 0 ) )
		}

		"return an empty constraint iterable." in {
			val solver = new SimpleContactSolver

			val s1 = new Shape {}
			s1.position = Vector2( 2, 0 )
			s1.mass = Double.PositiveInfinity
			val s2 = new Shape {}
			s2.position = Vector2( 3, 0 )
			s2.mass = Double.PositiveInfinity

			val contacts = List( Contact( s1, s2, Vector2( 2.5, 0 ), Vector2( 1, 0 ), 0, 0.0 ) )
			val ( updatedBodies, updatedConstraints ) = solver.step( 0.0, List( s1, s2 ), contacts )

			updatedConstraints must beEmpty
		}
	}
}
