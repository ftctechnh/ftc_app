package com.acmerobotics.roadrunner.drive

import com.acmerobotics.roadrunner.Pose2d

/**
 * Abstraction for generic drivetrain motion and localization.
 */
abstract class Drive {
    abstract var localizer: Localizer

    /**
     * The robot's current pose estimate.
     */
    var poseEstimate: Pose2d
        get() = localizer.poseEstimate
        set(value) {
            localizer.poseEstimate = value
        }

    /**
     * Updates [poseEstimate] with the most recent positional change.
     */
    fun updatePoseEstimate() {
        localizer.update()
    }

    /**
     * Sets the [poseVelocity] of the robot.
     */
    abstract fun setVelocity(poseVelocity: Pose2d)
}