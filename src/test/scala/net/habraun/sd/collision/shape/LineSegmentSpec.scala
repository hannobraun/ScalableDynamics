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



package net.habraun.sd.collision.shape



import math.Vec2D

import org.specs.Specification
import org.specs.runner.JUnit4



class LineSegmentTest extends JUnit4( LineSegmentSpec )



object LineSegmentSpec extends Specification {

	"LineSegment" should {
		"be a shape." in {
			val lineSegment = new LineSegment {}

			lineSegment must haveSuperClass[ Shape ]
		}

		"have the correct initial values for its attributes." in {
			val lineSegment = new LineSegment {}

			lineSegment.p must beEqualTo( Vec2D( 0, 0 ) )
			lineSegment.d must beEqualTo( Vec2D( 1, 0 ) )
		}

		"have its attributes assignable." in {
			val lineSegment = new LineSegment {}

			val p = Vec2D( 5, 5 )
			val d = Vec2D( 2, 1 )
			lineSegment.p = p
			lineSegment.d = d

			lineSegment.p must beEqualTo( p )
			lineSegment.d must beEqualTo( d )
		}

		"throw an exception if the direction vector is zero." in {
			val lineSegment = new LineSegment {}
			
			( lineSegment.d = Vec2D( 0, 0 ) ) must throwAn[ IllegalArgumentException ]
		}

		"throw an exception if null is assigned to an attribute." in {
			val lineSegment = new LineSegment {}

			( lineSegment.p = null ) must throwA[ NullPointerException ]
			( lineSegment.d = null ) must throwA[ NullPointerException ]
		}
	}
}
