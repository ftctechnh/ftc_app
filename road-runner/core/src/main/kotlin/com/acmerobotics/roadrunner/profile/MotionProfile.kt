package com.acmerobotics.roadrunner.profile

import kotlin.math.max
import kotlin.math.min

/**
 * Trapezoidal motion profile composed of motion segments.
 *
 * @param segments profile motion segments
 */
class MotionProfile(segments: List<MotionSegment>) {
    // TODO: is internal acceptable here?
    internal val segments: MutableList<MotionSegment> = segments.toMutableList()

    /**
     * Returns the [MotionState] at time [t].
     */
    operator fun get(t: Double): MotionState {
        var remainingTime = max(0.0, min(t, duration()))
        for (segment in segments) {
            if (remainingTime <= segment.dt) {
                return segment[remainingTime]
            }
            remainingTime -= segment.dt
        }
        return segments.last().end()
    }

    /**
     * Returns the duration of the motion profile.
     */
    fun duration() = segments.map { it.dt }.sum()

    /**
     * Returns a reversed version of the motion profile.
     */
    fun reversed() = MotionProfile(segments.map { it.reversed() }.reversed())

    /**
     * Returns a flipped version of the motion profile.
     */
    fun flipped() = MotionProfile(segments.map { it.flipped() })

    /**
     * Returns the start [MotionState].
     */
    fun start() = get(0.0)

    /**
     * Returns the end [MotionState].
     */
    fun end() = get(duration())

    operator fun plus(other: MotionProfile): MotionProfile {
        val builder = MotionProfileBuilder(start())
        builder.appendProfile(this)
        builder.appendProfile(other)
        return builder.build()
    }
}