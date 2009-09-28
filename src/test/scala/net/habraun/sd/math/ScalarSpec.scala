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



package net.habraun.sd.math



import Scalar._

import org.specs.Specification
import org.specs.runner.JUnit4



class ScalarTest extends JUnit4(ScalarSpec)



object ScalarSpec extends Specification {

	"Scalar" should {
		"implicitely convert Double to Scalar if the Double is mutliplied with a Vec2D." in {
			val vec = 2.0 * Vec2D(1, 1)
			vec must beEqualTo(Vec2D(2, 2))
		}

		"implicitely convert Int to Scalar if the Int is mutliplied with a Vec2D." in {
			val vec = 2 * Vec2D(1, 1)
			vec must beEqualTo(Vec2D(2, 2))
		}
	}
}
