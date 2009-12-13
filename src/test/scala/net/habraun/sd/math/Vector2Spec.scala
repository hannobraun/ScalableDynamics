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



import org.specs.Specification
import org.specs.runner.JUnit4



class Vector2Test extends JUnit4( Vector2Spec )



object Vector2Spec extends Specification {

	"Vector2" should {
		"compute the sum of itself and another vector." in {
			( Vector2( 1, 0 ) + Vector2( 0, 1 ) ) must beEqualTo( Vector2( 1, 1 ) )
		}

		"compute the difference of itself with another vector." in {
			( Vector2( 5, 5 ) - Vector2( 4, 4 ) ) must beEqualTo( Vector2( 1, 1 ) )
		}

		"compute the scalar product." in {
			( Vector2( 1, 1 ) * 2.0 ) must beEqualTo( Vector2( 2, 2 ) )
		}

		"compute the quotient with a scalar." in {
			( Vector2( 2, 2 ) / 2.0 ) must beEqualTo( Vector2( 1, 1 ) )
		}

		"compute the dot product." in {
			( Vector2( 1, 2 ) * Vector2( 2, 1 ) ) must beEqualTo( 4.0 )
		}

		"compute the inverse of itself." in {
			-Vector2( 1, 1 ) must beEqualTo( Vector2( -1, -1 ) )
		}

		"compute its length." in {
			Vector2( 2, 0 ).length must beEqualTo( 2.0 )
		}

		"compute its length to the power of two." in {
			Vector2( 2, 2 ).squaredLength must beEqualTo( 8.0 )
		}

		"compute the unit vector with the same direction." in {
			Vector2( 2, 0 ).normalize must beEqualTo( Vector2( 1, 0 ) )
		}

		"compute the orthogonal vector that is rotated counter-clockwise." in {
			Vector2( 2, 1 ).orthogonal must beEqualTo( Vector2( -1, 2 ) )
		}

		"compute projection of itself on another vector." in {
			Vector2( 5, 5 ).projectOn( Vector2( 1, 0 ) ) must beEqualTo( Vector2( 5, 0 ) )
		}
	}

	"Vector2.unit" should {
		"return true, if the vector is a unit vector." in {
			Vector2( 1, 0 ).unit must beTrue
		}

		"return false, if the vector is not a unit vector." in {
			Vector2( 2, 0 ).unit must beFalse
		}

		"return true, if the vector's length is within a certain tolerance area around 1." in {
			Vector2( 1 - Vector2.unitTolerance, 0 ).unit must beTrue
			Vector2( 1 + Vector2.unitTolerance, 0 ).unit must beTrue
		}
	}

	"Vector2.isLinearlyIndependentFrom" should {
		"return true if the vector is linearly independent from the other vector." in {
			Vector2( 1, 1 ).isLinearlyIndependentFrom( Vector2( 2, 1 ) ) must beTrue
		}

		"return false if the vector is linearly dependent on the other vector." in {
			Vector2( 1, 1 ).isLinearlyIndependentFrom( Vector2( 2, 2 ) ) must beFalse
		}

		"be able to handle components being zero if the vectors are linearly independent." in {
			Vector2( 1, 1 ).isLinearlyIndependentFrom( Vector2( 2, 0 ) ) must beTrue
			Vector2( 1, 1 ).isLinearlyIndependentFrom( Vector2( 0, 2 ) ) must beTrue
		}

		"be able to handle components being zero if the vectors are linearly dependent." in {
			Vector2( 1, 0 ).isLinearlyIndependentFrom( Vector2( 2, 0 ) ) must beFalse
			Vector2( 0, 1 ).isLinearlyIndependentFrom( Vector2( 0, 2 ) ) must beFalse
		}

		"be able to handle one or both of the vectors being the zero vector."in {
			Vector2( 1, 1 ).isLinearlyIndependentFrom( Vector2( 0, 0 ) ) must beFalse
			Vector2( 0, 0 ).isLinearlyIndependentFrom( Vector2( 2, 2 ) ) must beFalse
			Vector2( 0, 0 ).isLinearlyIndependentFrom( Vector2( 0, 0 ) ) must beFalse
		}
	}

	"Vector2.computeFactorFor" should {
		"return the factor for two linearly dependent vectors." in {
			Vector2( 1, 1 ).computeFactorFor( Vector2( 2, 2 ) ) must beEqualTo( 2 )
			Vector2( 2, 2 ).computeFactorFor( Vector2( 1, 1 ) ) must beEqualTo( 0.5 )
		}

		"throw an IllegalArgumentException if the vectors are not linearly dependent." in {
			Vector2( 1, 2 ).computeFactorFor( Vector2( 2, 1 ) ) must throwAn[ IllegalArgumentException ]
		}

		"throw an IllegalArgumentException if the first vector is the zero vector." in {
			Vector2( 0, 0 ).computeFactorFor( Vector2( 1, 1 ) ) must throwAn[ IllegalArgumentException ]
		}

		"be able to handle a component of the first vector being zero." in {
			Vector2( 0, 1 ).computeFactorFor( Vector2( 0, 2 ) ) must beEqualTo( 2 )
			Vector2( 1, 0 ).computeFactorFor( Vector2( 2, 0 ) ) must beEqualTo( 2 )
		}
	}

	"ZeroVector should be the zero vector." in {
		ZeroVector must beEqualTo( Vector2( 0, 0 ) )
	}

	"InvalidVector should be a vector with both components being Double.NaN." in {
		InvalidVector.x must beEqualTo( Double.NaN )
		InvalidVector.y must beEqualTo( Double.NaN )
	}
}
