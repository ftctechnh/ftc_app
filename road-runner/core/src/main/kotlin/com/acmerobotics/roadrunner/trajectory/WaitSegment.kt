package com.acmerobotics.roadrunner.trajectory

import com.acmerobotics.roadrunner.Pose2d

/**
 * Static trajectory segment that holds a constant pose. Used for giving trajectories extra time to settle.
 *
 * @param pose pose to hold
 * @param duration duration
 */
class WaitSegment(private val pose: Pose2d, private val duration: Double) : TrajectorySegment {
    override fun duration() = duration

    override fun get(time: Double) = pose

    override fun velocity(time: Double) = Pose2d(0.0, 0.0, 0.0)

    override fun acceleration(time: Double) = Pose2d(0.0, 0.0, 0.0)
}