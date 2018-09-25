package com.acmerobotics.roadrunner.trajectory.constraints

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.drive.TankKinematics
import kotlin.math.abs
import kotlin.math.min

/**
 * Tank-specific drive constraints that also limit maximum wheel velocities.
 *
 * @param baseConstraints base drive constraints
 * @param trackWidth track width
 */
class TankConstraints(
        baseConstraints: DriveConstraints,
        val trackWidth: Double
) : DriveConstraints(
        baseConstraints.maximumVelocity,
        baseConstraints.maximumAcceleration,
        baseConstraints.maximumAngularVelocity,
        baseConstraints.maximumAngularAcceleration
) {
    override fun maximumVelocity(pose: Pose2d, poseDeriv: Pose2d, poseSecondDeriv: Pose2d): Double {
        val robotPositionDeriv = poseDeriv.pos().rotated(-pose.heading)

        val wheelVelocities = TankKinematics.robotToWheelVelocities(Pose2d(robotPositionDeriv, poseDeriv.heading), trackWidth)
        val maxTrajectoryVelocity = wheelVelocities.map { maximumVelocity / it }.map(::abs).min() ?: 0.0

        return min(super.maximumVelocity(pose, poseDeriv, poseSecondDeriv), maxTrajectoryVelocity)
    }

}