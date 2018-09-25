package com.acmerobotics.roadrunner.trajectory

import com.acmerobotics.roadrunner.Pose2d
import kotlin.math.max
import kotlin.math.min

/**
 * Time-parametrized trajectory of poses.
 *
 * @param segments trajectory segments
 */
class Trajectory(val segments: List<TrajectorySegment> = listOf()) {
    /**
     * Returns the trajectory duration.
     */
    fun duration() = segments.map { it.duration() }.sum()

    /**
     * Returns the pose at the specified [time].
     */
    operator fun get(time: Double): Pose2d {
        var remainingTime = max(0.0, min(time, duration()))
        for (segment in segments) {
            if (remainingTime <= segment.duration()) {
                return segment[remainingTime]
            }
            remainingTime -= segment.duration()
        }
        return segments.lastOrNull()?.get(segments.last().duration()) ?: Pose2d()
    }

    /**
     * Returns the pose velocity at the specified [time].
     */
    fun velocity(time: Double): Pose2d {
        var remainingTime = max(0.0, min(time, duration()))
        for (segment in segments) {
            if (remainingTime <= segment.duration()) {
                return segment.velocity(remainingTime)
            }
            remainingTime -= segment.duration()
        }
        return segments.lastOrNull()?.velocity(segments.last().duration()) ?: Pose2d()
    }

    /**
     * Returns the pose acceleration at the specified [time].
     */
    fun acceleration(time: Double): Pose2d {
        var remainingTime = max(0.0, min(time, duration()))
        for (segment in segments) {
            if (remainingTime <= segment.duration()) {
                return segment.acceleration(remainingTime)
            }
            remainingTime -= segment.duration()
        }
        return segments.lastOrNull()?.acceleration(segments.last().duration()) ?: Pose2d()
    }

    /**
     * Returns the start pose.
     */
    fun start() = get(0.0)

    /**
     * Returns the start pose velocity.
     */
    fun startVelocity() = velocity(0.0)

    /**
     * Returns the start pose acceleration.
     */
    fun startAcceleration() = acceleration(0.0)

    /**
     * Returns the end pose.
     */
    fun end() = get(duration())

    /**
     * Returns the end pose velocity.
     */
    fun endVelocity() = velocity(duration())

    /**
     * Returns the end pose acceleration.
     */
    fun endAcceleration() = acceleration(duration())
}