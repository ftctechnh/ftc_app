package com.acmerobotics.roadrunner.trajectory

import com.acmerobotics.roadrunner.Pose2d

/**
 * Generic trajectory segment.
 */
interface TrajectorySegment {

    /**
     * Returns the duration of the segment.
     */
    fun duration(): Double

    /**
     * Returns the pose at the given [time].
     */
    operator fun get(time: Double): Pose2d

    /**
     * Returns the pose velocity at the given [time].
     */
    fun velocity(time: Double): Pose2d

    /**
     * Returns the pose acceleration at the given [time].
     */
    fun acceleration(time: Double): Pose2d
}