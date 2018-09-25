package com.acmerobotics.roadrunner.trajectory.constraints

import com.acmerobotics.roadrunner.Pose2d
import kotlin.math.abs

/**
 * This class describes general robot trajectory constraints. More specifically, for paths, the robot velocity,
 * robot acceleration, and robot angular velocity are limited.  For point turns, the angular velocity and angular
 * acceleration are limited.
 *
 * @param maximumVelocity maximum robot velocity
 * @param maximumAcceleration maximum robot acceleration
 * @param maximumAngularVelocity maximum angular velocity
 * @param maximumAngularAcceleration maximum angular acceleration (ignored by path-based trajectories)
 */
open class DriveConstraints(
        val maximumVelocity: Double,
        val maximumAcceleration: Double,
        val maximumAngularVelocity: Double,
        val maximumAngularAcceleration: Double
) : TrajectoryConstraints {
    override fun maximumVelocity(pose: Pose2d, poseDeriv: Pose2d, poseSecondDeriv: Pose2d): Double {
        val maximumVelocities = mutableListOf(maximumVelocity)

        if (abs(poseDeriv.heading) > 1e-6) {
            maximumVelocities.add(maximumAngularVelocity / Math.abs(poseDeriv.heading))
        }

        return maximumVelocities.min() ?: 0.0
    }

    override fun maximumAcceleration(pose: Pose2d, poseDeriv: Pose2d, poseSecondDeriv: Pose2d) = maximumAcceleration
}
