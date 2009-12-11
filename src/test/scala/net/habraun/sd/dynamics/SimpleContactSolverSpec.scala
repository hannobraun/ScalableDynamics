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
import math.Vec2D

import org.specs.Specification
import org.specs.mock.Mockito
import org.specs.runner.JUnit4



class SimpleContactSolverTest extends JUnit4( SimpleContactSolverSpec )



object SimpleContactSolverSpec extends Specification with Mockito {

	"SimpleContactSolver" should {
		"be a StepPhase." in {
			val solver = new SimpleContactSolver
			
			solver must haveSuperClass[ StepPhase[ Shape ] ]
		}

		"remove the contact from two touching shapes and otherwise not change their position." in {
			val solver = new SimpleContactSolver


			val pos1 = Vec2D( 2, 2 )
			val pos2 = Vec2D( 4, 4 )

			val s1 = new Shape {}
			s1.position = pos1
			val s2 = new Shape {}
			s2.position = pos2

			val contact = Contact( s1, s2, Vec2D( 5, 5 ), Vec2D( 1, 0 ), 0, 0.0 )
			s1.addContact( contact )
			s2.addContact( -contact )

			solver.filterAndStep( 0.0, List( s1, s2 ) )

			s1.position must beEqualTo( pos1 )
			s2.position must beEqualTo( pos2 )

			s1.contacts must beEmpty
			s2.contacts must beEmpty
		}

		"move two intersecting shapes of equal mass equally far along the normal, so they don't intersect anymore." in {
			val solver = new SimpleContactSolver

			val s1 = new Shape {}
			s1.position = Vec2D( 2, 0 )
			s1.mass = 2
			val s2 = new Shape {}
			s2.position = Vec2D( 3, 0 )
			s2.mass = 2

			val contact = Contact( s1, s2, Vec2D( 2.5, 0 ), Vec2D( 1, 0 ), 1, 0.0 )
			s1.addContact( contact )
			s2.addContact( -contact )

			solver.filterAndStep( 0.0, List( s1, s2 ) )

			s1.position must beEqualTo( Vec2D( 1.5, 0 ) )
			s2.position must beEqualTo( Vec2D( 3.5, 0 ) )
		}

		"move an intersecting shape by an amount proportional to its mass ratio to to the other shape." in {
			val solver = new SimpleContactSolver

			val s1 = new Shape {}
			s1.position = Vec2D( 4, 0 )
			s1.mass = 2
			val s2 = new Shape {}
			s2.position = Vec2D( 5, 0 )
			s2.mass = 4

			val contact = Contact( s1, s2, Vec2D( 4.5, 0 ), Vec2D( 1, 0 ), 3, 0.0 )
			s1.addContact( contact )
			s2.addContact( -contact )

			solver.filterAndStep( 0.0, List( s1, s2 ) )

			s1.position must beEqualTo( Vec2D( 2, 0 ) )
			s2.position must beEqualTo( Vec2D( 6, 0 ) )
		}

		"not move the first shape at all, if it has infinite mass." in {
			val solver = new SimpleContactSolver

			val pos1 = Vec2D( 2, 0 )

			val s1 = new Shape {}
			s1.position = pos1
			s1.mass = Double.PositiveInfinity
			val s2 = new Shape {}
			s2.position = Vec2D( 3, 0 )
			s2.mass = 2

			val contact = Contact( s1, s2, Vec2D( 2.5, 0 ), Vec2D( 1, 0 ), 1, 0.0 )
			s1.addContact( contact )
			s2.addContact( -contact )

			solver.filterAndStep( 0.0, List( s1, s2 ) )

			s1.position must beEqualTo( pos1 )
			s2.position must beEqualTo( Vec2D( 4, 0 ) )
		}

		"not move the second shape at all, if it has infinite mass." in {
			val solver = new SimpleContactSolver

			val pos2 = Vec2D( 3, 0 )

			val s1 = new Shape {}
			s1.position = Vec2D( 2, 0 )
			s1.mass = 2
			val s2 = new Shape {}
			s2.position = pos2
			s2.mass = Double.PositiveInfinity

			val contact = Contact( s1, s2, Vec2D( 2.5, 0 ), Vec2D( 1, 0 ), 1, 0.0 )
			s1.addContact( contact )
			s2.addContact( -contact )

			solver.filterAndStep( 0.0, List( s1, s2 ) )

			s1.position must beEqualTo( Vec2D( 1, 0 ) )
			s2.position must beEqualTo( pos2 )
		}

		"not move the shapes at all, if both have inifinte mass." in {
			val solver = new SimpleContactSolver

			val pos1 = Vec2D( 2, 0 )
			val pos2 = Vec2D( 3, 0 )

			val s1 = new Shape {}
			s1.position = pos1
			s1.mass = Double.PositiveInfinity
			val s2 = new Shape {}
			s2.position = pos2
			s2.mass = Double.PositiveInfinity

			val contact = Contact( s1, s2, Vec2D( 2.5, 0 ), Vec2D( 1, 0 ), 1, 0.0 )
			s1.addContact( contact )
			s2.addContact( -contact )

			solver.filterAndStep( 0.0, List( s1, s2 ) )

			s1.position must beEqualTo( pos1 )
			s2.position must beEqualTo( pos2 )
		}
	}
}
