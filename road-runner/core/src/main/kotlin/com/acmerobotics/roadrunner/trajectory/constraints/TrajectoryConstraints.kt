package com.acmerobotics.roadrunner.trajectory.constraints

import com.acmerobotics.roadrunner.Pose2d

/**
 * Trajectory-specific constraints for motion profile generation.
 */
interface TrajectoryConstraints {

    /**
     * Returns the maximum velocity for the given pose derivatives.
     *
     * @param pose pose
     * @param poseDeriv pose derivative
     * @param poseSecondDeriv pose second derivative
     */
    fun maximumVelocity(pose: Pose2d, poseDeriv: Pose2d, poseSecondDeriv: Pose2d): Double

    /**
     * Returns the maximum acceleration for the given pose derivatives.
     *
     * @param pose pose
     * @param poseDeriv pose derivative
     * @param poseSecondDeriv pose second derivative
     */
    fun maximumAcceleration(pose: Pose2d, poseDeriv: Pose2d, poseSecondDeriv: Pose2d): Double
}