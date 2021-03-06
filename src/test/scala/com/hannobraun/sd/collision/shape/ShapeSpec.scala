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



package com.hannobraun.sd.collision.shape



import com.hannobraun.sd.core.Body

import org.specs.Specification
import org.specs.mock.Mockito
import org.specs.runner.JUnit4



class ShapeTest extends JUnit4( ShapeSpec )



object ShapeSpec extends Specification with Mockito {

	"Shape" should {
		"extend Body." in {
			val shape = new Shape {}

			shape must haveSuperClass[ Body ]
		}
	}
}
