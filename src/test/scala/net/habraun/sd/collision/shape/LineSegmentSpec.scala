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



import math.Vector2

import org.specs.Specification
import org.specs.runner.JUnit4



class LineSegmentTest extends JUnit4( LineSegmentSpec )



object LineSegmentSpec extends Specification {

	"LineSegment" should {
		"be a shape." in {
			val lineSegment = new LineSegment {}

			lineSegment must haveSuperClass[ Shape ]
		}

		"have a default value for its direction." in {
			val lineSegment = new LineSegment {}

			lineSegment.direction must beEqualTo( Vector2( 1, 0 ) )
		}

		"allow reassigment of its direction." in {
			val lineSegment = new LineSegment {}

			val direction = Vector2( 2, 1 )
			lineSegment.direction = direction

			lineSegment.direction must beEqualTo( direction )
		}

		"throw an exception if the direction vector is zero." in {
			val lineSegment = new LineSegment {}
			
			( lineSegment.direction = Vector2( 0, 0 ) ) must throwAn[ IllegalArgumentException ]
		}

		"throw an exception if null is assigned to its direction." in {
			val lineSegment = new LineSegment {}

			( lineSegment.direction = null ) must throwA[ NullPointerException ]
		}
	}
}
