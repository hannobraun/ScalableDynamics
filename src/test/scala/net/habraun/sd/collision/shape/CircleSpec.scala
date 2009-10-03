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



import org.specs.Specification
import org.specs.runner.JUnit4



class CircleTest extends JUnit4( CircleSpec )



object CircleSpec extends Specification {

	"Circle" should {
		"be a Shape." in {
			val circle = new Circle {}
			
			circle must haveSuperClass[Shape]
		}

		"have an initial radius of 1.0." in {
			val circle = new Circle {}

			circle.radius must beEqualTo( 1.0 )
		}

		"have its radius assignable." in {
			val circle = new Circle {}

			val radius = 1.0
			circle.radius = radius

			circle.radius must beEqualTo( radius )
		}

		"throw an exceptio nif zero is assigned to radius." in {
			val circle = new Circle {}

			( circle.radius = 0.0 ) must throwAn[IllegalArgumentException]
		}

		"throw an exception if a negative radius is assigned." in {
			val circle = new Circle {}

			( circle.radius = -1.0 ) must throwAn[IllegalArgumentException]
		}
	}
}
