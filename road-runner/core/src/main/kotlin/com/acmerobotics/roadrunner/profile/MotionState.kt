package com.acmerobotics.roadrunner.profile

/**
 * Kinematic state of a motion profile at any given time.
 */
class MotionState(val x: Double, val v: Double, val a: Double = Double.NaN, val j: Double = Double.NaN) {

    /**
     * Returns the [MotionState] at time [t].
     */
    operator fun get(t: Double) =
            when {
                a.isNaN() -> MotionState(
                        x + v * t,
                        v
                )
                j.isNaN() -> MotionState(
                        x + v * t + a / 2 * t * t,
                        v + a * t,
                        a
                )
                else -> MotionState(
                        x + v * t + a / 2 * t * t + j / 6 * t * t * t,
                        v + a * t + j / 2 * t * t,
                        a + j * t,
                        j
                )
            }

    /**
     * Returns a flipped (negated) version of the state.
     */
    fun flipped() = MotionState(-x, -v, -a, -j)

    override fun toString() = String.format("(x=%.3f, v=%.3f, a=%.3f, j=%.3f)", x, v, a, j)
}