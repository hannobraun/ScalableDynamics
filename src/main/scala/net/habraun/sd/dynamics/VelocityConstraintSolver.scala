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



import core.StepPhase



class VelocityConstraintSolver extends StepPhase[ VelocityConstraint, Nothing ] {

	def execute( dt: Double, constraints: Iterable[ VelocityConstraint ], c: Iterable[ Nothing ] ) = {
		for ( constraint <- constraints ) {
			// Check if the actual velocity is greater than the maximum velocity.
			if ( constraint.velocity.squaredLength > constraint.maxVelocity * constraint.maxVelocity ) {
				// It is. Set the velocity to the maximum velocity while retaining the direction of the new
				// velocity.
				constraint.velocity = constraint.velocity * ( constraint.maxVelocity / Math.sqrt(
						constraint.velocity * constraint.velocity ) )
			}
		}

		( constraints, c )
	}
}
