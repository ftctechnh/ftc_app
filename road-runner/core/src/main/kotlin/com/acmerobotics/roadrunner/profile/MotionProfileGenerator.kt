package com.acmerobotics.roadrunner.profile

import com.acmerobotics.roadrunner.util.MathUtil.solveQuadratic
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Motion profile generator with arbitrary start and end motion states and either dynamic constraints or jerk limiting.
 */
object MotionProfileGenerator {

    /**
     * Generates a simple motion profile with constant [maximumVelocity], [maximumAcceleration], and [maximumJerk]. If
     * constraints can't be obeyed, there are two possible fallbacks. If [overshoot] is true, then two profiles will be
     * concatenated (the first one overshoots the goal and the second one reverses back to reach the goal). Otherwise,
     * The highest order constraint (e.g., max jerk for jerk-limited profiles) is repeatedly violated until the goal is
     * satisfiable.
     *
     * @param start start motion state
     * @param goal goal motion state
     * @param maximumVelocity maximum velocity
     * @param maximumAcceleration maximum acceleration
     * @param maximumJerk maximum jerk
     * @param overshoot
     */
    @JvmStatic
    @JvmOverloads
    fun generateSimpleMotionProfile(
        start: MotionState,
        goal: MotionState,
        maximumVelocity: Double,
        maximumAcceleration: Double,
        maximumJerk: Double = Double.NaN,
        overshoot: Boolean = false
    ): MotionProfile {
        // ensure the goal is always after the start; plan the flipped profile otherwise
        if (goal.x < start.x) {
            return generateSimpleMotionProfile(
                    start.flipped(),
                    goal.flipped(),
                    maximumVelocity,
                    maximumAcceleration,
                    maximumJerk
            ).flipped()
        }

        if (maximumJerk.isNaN()) {
            // acceleration-limited profile (trapezoidal)
            val requiredAccel = (goal.v * goal.v - start.v * start.v) / (2 * (goal.x - start.x))

            val accelProfile = generateAccelProfile(start, maximumVelocity, maximumAcceleration)
            val decelProfile = generateAccelProfile(
                    MotionState(goal.x, goal.v, -goal.a, goal.j
                    ), maximumVelocity, maximumAcceleration, maximumJerk)
                    .reversed()

            val noCoastProfile = accelProfile + decelProfile
            val remainingDistance = goal.x - noCoastProfile.end().x

            if (remainingDistance >= 0.0) {
                // normal 3-segment profile works
                val deltaT2 = remainingDistance / maximumVelocity

                return MotionProfileBuilder(start)
                        .appendProfile(accelProfile)
                        .appendAccelerationControl(0.0, deltaT2)
                        .appendProfile(decelProfile)
                        .build()
            } else if (abs(requiredAccel) > maximumAcceleration) {
                return if (overshoot) {
                    // TODO: is this most efficient? (do we care?)
                    noCoastProfile + generateSimpleMotionProfile(
                            noCoastProfile.end(),
                            goal,
                            maximumVelocity,
                            maximumAcceleration,
                            overshoot = true
                    )
                } else {
                    // single segment profile
                    val dt = (goal.v - start.v) / requiredAccel
                    MotionProfileBuilder(start)
                            .appendAccelerationControl(requiredAccel, dt)
                            .build()
                }
            } else if (start.v > maximumVelocity && goal.v > maximumVelocity) {
                // decel, accel
                val roots = solveQuadratic(-maximumAcceleration, 2 * start.v,
                        (goal.v * goal.v - start.v * start.v) / (2 * maximumAcceleration) - goal.x + start.x)
                val deltaT1 = roots.filter { it >= 0.0 }.min()!!
                val deltaT3 = abs(start.v - goal.v) / maximumAcceleration + deltaT1

                return MotionProfileBuilder(start)
                        .appendAccelerationControl(-maximumAcceleration, deltaT1)
                        .appendAccelerationControl(maximumAcceleration, deltaT3)
                        .build()
            } else {
                // accel, decel
                val roots = solveQuadratic(maximumAcceleration, 2 * start.v,
                        (start.v * start.v - goal.v * goal.v) / (2 * maximumAcceleration) - goal.x + start.x)
                val deltaT1 = roots.filter { it >= 0.0 }.min()!!
                val deltaT3 = abs(start.v - goal.v) / maximumAcceleration + deltaT1

                return MotionProfileBuilder(start)
                        .appendAccelerationControl(maximumAcceleration, deltaT1)
                        .appendAccelerationControl(-maximumAcceleration, deltaT3)
                        .build()
            }
        } else {
            // jerk-limited profile (S-curve)
            val accelerationProfile = generateAccelProfile(start, maximumVelocity, maximumAcceleration, maximumJerk)
            // we leverage symmetry here; deceleration profiles are just reversed acceleration ones with the goal
            // acceleration flipped
            val decelerationProfile = generateAccelProfile(
                    MotionState(goal.x, goal.v, -goal.a, goal.j
                ), maximumVelocity, maximumAcceleration, maximumJerk)
                    .reversed()

            val noCoastProfile = accelerationProfile + decelerationProfile
            val remainingDistance = goal.x - noCoastProfile.end().x

            if (remainingDistance >= 0.0) {
                // we just need to add a coast segment of appropriate duration
                val deltaT4 = remainingDistance / maximumVelocity

                return MotionProfileBuilder(start)
                        .appendProfile(accelerationProfile)
                        .appendJerkControl(0.0, deltaT4)
                        .appendProfile(decelerationProfile)
                        .build()
            } else {
                // the profile never reaches maxV
                // thus, we need to compute the peak velocity (0 < peak vel < max vel)
                // we *could* construct a large polynomial expression (i.e., a nasty cubic) and solve it using Cardano's
                // method, some kind of inclusion method like modified Anderson-Bjorck-King, or a host of other methods
                // (see https://link.springer.com/content/pdf/bbm%3A978-3-642-05175-3%2F1.pdf for modified ABK)
                // instead, however, we conduct a binary search as it's sufficiently performant for this use case, requires
                // less code, and is overall significantly more comprehensible
                var upperBound = maximumVelocity
                var lowerBound = 0.0
                var iterations = 0
                while (iterations < 1000) {
                    val peakVel = (upperBound + lowerBound) / 2

                    val searchAccelProfile = generateAccelProfile(start, maximumVelocity, maximumAcceleration, maximumJerk)
                    val searchDecelProfile = generateAccelProfile(goal, maximumVelocity, maximumAcceleration, maximumJerk)
                            .reversed()

                    val searchProfile = searchAccelProfile + searchDecelProfile

                    val error = goal.x - searchProfile.end().x

                    if (abs(error) < 1e-10) {
                        return searchProfile
                    }

                    if (error > 0.0) {
                        // we undershot so shift the lower bound up
                        lowerBound = peakVel
                    } else {
                        // we overshot so shift the upper bound down
                        upperBound = peakVel
                    }

                    iterations++
                }

                // constraints are not satisfiable
                return if (overshoot) {
                    noCoastProfile + generateSimpleMotionProfile(
                            noCoastProfile.end(),
                            goal,
                            maximumVelocity,
                            maximumAcceleration,
                            maximumJerk,
                            overshoot = true
                    )
                } else {
                    // violate max jerk first
                    generateSimpleMotionProfile(
                            start,
                            goal,
                            maximumVelocity,
                            maximumAcceleration,
                            overshoot = false
                    )
                }
            }
        }
    }

    private fun generateAccelProfile(
            start: MotionState,
            maxVel: Double,
            maxAccel: Double,
            maxJerk: Double = Double.NaN
    ): MotionProfile =
        if (maxJerk.isNaN()) {
            // acceleration-limited
            val deltaT1 = abs(start.v - maxVel) / maxAccel
            val builder = MotionProfileBuilder(start)
            if (start.v > maxVel) {
                // we need to decelerate
                builder.appendAccelerationControl(-maxAccel, deltaT1)
            } else {
                builder.appendAccelerationControl(maxAccel, deltaT1)
            }
            builder.build()
        } else {
            // jerk-limited
            // compute the duration and velocity of the first segment
            val (deltaT1, deltaV1) = if (start.a > maxAccel) {
                // slow down and see where we are
                val deltaT1 = (start.a - maxAccel) / maxJerk
                val deltaV1 = start.a * deltaT1 - 0.5 * maxJerk * deltaT1 * deltaT1
                Pair(deltaT1, deltaV1)
            } else {
                // otherwise accelerate
                val deltaT1 = (maxAccel - start.a) / maxJerk
                val deltaV1 = start.a * deltaT1 + 0.5 * maxJerk * deltaT1 * deltaT1
                Pair(deltaT1, deltaV1)
            }

            // compute the duration and velocity of the third segment
            val deltaT3 = maxAccel / maxJerk
            val deltaV3 = maxAccel * deltaT3 - 0.5 * maxJerk * deltaT3 * deltaT3

            // compute the velocity change required in the second segment
            val deltaV2 = maxVel - start.v - deltaV1 - deltaV3

            if (deltaV2 < 0.0) {
                // there is no constant acceleration phase
                // the second case checks if we're going to exceed max vel
                if (start.a > maxAccel || (start.v - maxVel) > (start.a * start.a) / (2 * maxJerk)) {
                    // problem: we need to cut down on our acceleration but we can't cut our initial decel
                    // solution: we'll lengthen our initial decel to -max accel and similarly with our final accel
                    // if this results in an over correction, decel instead to a good accel
                    val newDeltaT1 = (start.a + maxAccel) / maxJerk
                    val newDeltaV1 = start.a * newDeltaT1 - 0.5 * maxJerk * newDeltaT1 * newDeltaT1

                    val newDeltaV2 = maxVel - start.v - newDeltaV1 + deltaV3

                    if (newDeltaV2 > 0.0) {
                        // we decelerated too much
                        val roots = solveQuadratic(-maxJerk, 2 * start.a,
                                start.v - maxVel - start.a * start.a / (2 * maxJerk))
                        val finalDeltaT1 = roots.filter { it >= 0.0 }.min()!!
                        val finalDeltaT3 = finalDeltaT1 - start.a / maxJerk

                        MotionProfileBuilder(start)
                                .appendJerkControl(-maxJerk, finalDeltaT1)
                                .appendJerkControl(maxJerk, finalDeltaT3)
                                .build()
                    } else {
                        // we're almost good
                        val newDeltaT2 = newDeltaV2 / -maxAccel

                        MotionProfileBuilder(start)
                                .appendJerkControl(-maxJerk, newDeltaT1)
                                .appendJerkControl(0.0, newDeltaT2)
                                .appendJerkControl(maxJerk, deltaT3)
                                .build()
                    }
                } else {
                    // cut out the constant accel phase and find a shorter delta t1 and delta t3
                    val roots = solveQuadratic(maxJerk, 2 * start.a,
                            start.v - maxVel + start.a * start.a / (2 * maxJerk))
                    val newDeltaT1 = roots.filter { it >= 0.0 }.min()!!
                    val newDeltaT3 = newDeltaT1 + start.a / maxJerk

                    MotionProfileBuilder(start)
                            .appendJerkControl(maxJerk, newDeltaT1)
                            .appendJerkControl(-maxJerk, newDeltaT3)
                            .build()
                }
            } else {
                // there is a constant acceleration phase
                val deltaT2 = deltaV2 / maxAccel

                val builder = MotionProfileBuilder(start)
                if (start.a > maxAccel) {
                    builder.appendJerkControl(-maxJerk, deltaT1)
                } else {
                    builder.appendJerkControl(maxJerk, deltaT1)
                }
                builder.appendJerkControl(0.0, deltaT2)
                        .appendJerkControl(-maxJerk, deltaT3)
                        .build()
            }
        }

    /**
     * Generates a motion profile with dynamic maximum velocity and acceleration. Uses the algorithm described in
     * section 3.2 of [Sprunk2008.pdf](http://www2.informatik.uni-freiburg.de/~lau/students/Sprunk2008.pdf). Warning:
     * Profiles may be generated incorrectly if the endpoint velocity/acceleration values preclude the obedience of the
     * motion constraints. To protect against this, verify the continuity of the generated profile or keep the start and
     * goal velocities at 0.
     *
     * @param start start motion state
     * @param goal goal motion state
     * @param constraints motion constraints
     * @param resolution number of constraint samples
     */
    @JvmStatic
    fun generateMotionProfile(
        start: MotionState,
        goal: MotionState,
        constraints: MotionConstraints,
        resolution: Int = 250
    ): MotionProfile {
        if (goal.x < start.x) {
            return generateMotionProfile(
                start.flipped(),
                goal.flipped(),
                object : MotionConstraints {
                    override fun maximumVelocity(displacement: Double) =
                            constraints.maximumVelocity(-displacement)
                    override fun maximumAcceleration(displacement: Double) =
                            constraints.maximumAcceleration(-displacement)
                },
                resolution
            ).flipped()
        }

        val length = goal.x - start.x
        val dx = length / resolution

        val forwardStates = forwardPass(
            MotionState(0.0, start.v, start.a),
            { constraints.maximumVelocity(start.x + it) },
            { constraints.maximumAcceleration(start.x + it) },
            resolution,
            dx
        ).map { (motionState, dx) -> Pair(
            MotionState(
                motionState.x + start.x,
                motionState.v,
                motionState.a
            ), dx) }
            .toMutableList()

        val backwardStates = forwardPass(
            MotionState(0.0, goal.v, goal.a),
            { constraints.maximumVelocity(goal.x - it) },
            { constraints.maximumAcceleration(goal.x - it) },
            resolution,
            dx
        ).map { (motionState, dx) -> Pair(afterDisplacement(motionState, dx), dx) }.map { (motionState, dx) ->
            Pair(
                MotionState(
                    goal.x - motionState.x,
                    motionState.v,
                    -motionState.a
                ), dx
            )
        }.reversed().toMutableList()

        val finalStates = mutableListOf<Pair<MotionState, Double>>()

        var i = 0
        var j = 0
        while (i < forwardStates.size && i < backwardStates.size) {
            var (forwardStartState, forwardDx) = forwardStates[i]
            var (backwardStartState, backwardDx) = backwardStates[j]

            if (abs(forwardDx - backwardDx) > 1e-6) {
                if (forwardDx < backwardDx) {
                    backwardStates.add(
                        j + 1,
                        Pair(afterDisplacement(backwardStartState, forwardDx), backwardDx - forwardDx)
                    )
                    backwardDx = forwardDx
                } else {
                    forwardStates.add(
                        i + 1,
                        Pair(afterDisplacement(forwardStartState, backwardDx), forwardDx - backwardDx)
                    )
                    forwardDx = backwardDx
                }
            }

            val forwardEndState = afterDisplacement(forwardStartState, forwardDx)
            val backwardEndState = afterDisplacement(backwardStartState, backwardDx)

            if (forwardStartState.v <= backwardStartState.v) {
                if (forwardEndState.v <= backwardEndState.v) {
                    finalStates.add(Pair(forwardStartState, forwardDx))
                } else {
                    val intersection = intersection(
                        forwardStartState,
                        backwardStartState
                    )
                    finalStates.add(Pair(forwardStartState, intersection))
                    finalStates.add(
                        Pair(
                            afterDisplacement(backwardStartState, intersection),
                            backwardDx - intersection
                        )
                    )
                }
            } else {
                if (forwardEndState.v >= backwardEndState.v) {
                    finalStates.add(Pair(backwardStartState, backwardDx))
                } else {
                    val intersection = intersection(
                        forwardStartState,
                        backwardStartState
                    )
                    finalStates.add(Pair(backwardStartState, intersection))
                    finalStates.add(
                        Pair(
                            afterDisplacement(forwardStartState, intersection),
                            forwardDx - intersection
                        )
                    )
                }
            }
            i++
            j++
        }

        val motionSegments = mutableListOf<MotionSegment>()
        for ((state, stateDx) in finalStates) {
            val dt = if (abs(state.a) > 1e-6) {
                val discriminant = state.v * state.v + 2 * state.a * stateDx
                ((if (abs(discriminant) < 1e-6) 0.0 else sqrt(discriminant)) - state.v) / state.a
            } else {
                stateDx / state.v
            }
            motionSegments.add(MotionSegment(state, dt))
        }

        return MotionProfile(motionSegments)
    }

    private fun forwardPass(
        start: MotionState,
        maximumVelocity: (displacement: Double) -> Double,
        maximumAcceleration: (displacement: Double) -> Double,
        resolution: Int,
        dx: Double
    ): List<Pair<MotionState, Double>> {
        val forwardStates = mutableListOf<Pair<MotionState, Double>>()

        val displacements = (0 until resolution).map { it * dx + start.x }

        var lastState = start
        for (displacement in displacements) {
            val maxVel = maximumVelocity(displacement)
            val maxAccel = maximumAcceleration(displacement)

            lastState = if (lastState.v >= maxVel) {
                val state = MotionState(displacement, maxVel, 0.0)
                forwardStates.add(Pair(state, dx))
                afterDisplacement(state, dx)
            } else {
                val desiredVelocity = sqrt(lastState.v * lastState.v + 2 * maxAccel * dx)
                if (desiredVelocity <= maxVel) {
                    val state = MotionState(displacement, lastState.v, maxAccel)
                    forwardStates.add(Pair(state, dx))
                    afterDisplacement(state, dx)
                } else {
                    val accelDx =
                        (maxVel * maxVel - lastState.v * lastState.v) / (2 * maxAccel)
                    val accelState = MotionState(displacement, lastState.v, maxAccel)
                    val coastState = MotionState(displacement + accelDx, maxVel, 0.0)
                    forwardStates.add(Pair(accelState, accelDx))
                    forwardStates.add(Pair(coastState, dx - accelDx))
                    afterDisplacement(coastState, dx - accelDx)
                }
            }
        }

        return forwardStates
    }

    private fun afterDisplacement(state: MotionState, dx: Double): MotionState {
        val discriminant = state.v * state.v + 2 * state.a * dx
        return if (abs(discriminant) < 1e-6) {
            MotionState(state.x + dx, 0.0, state.a)
        } else {
            MotionState(state.x + dx, sqrt(discriminant), state.a)
        }
    }

    private fun intersection(state1: MotionState, state2: MotionState): Double {
        return (state1.v * state1.v - state2.v * state2.v) / (2 * state2.a - 2 * state1.a)
    }
}