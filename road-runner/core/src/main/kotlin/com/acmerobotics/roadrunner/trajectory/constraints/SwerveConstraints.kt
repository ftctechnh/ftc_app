package com.acmerobotics.roadrunner.trajectory.constraints

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.drive.MecanumKinematics
import com.acmerobotics.roadrunner.drive.SwerveKinematics
import com.acmerobotics.roadrunner.drive.TankKinematics
import kotlin.math.abs
import kotlin.math.min

/**
 * Mecanum-specific drive constraints that also limit maximum wheel velocities.
 *
 * @param baseConstraints base drive constraints
 * @param trackWidth track width
 * @param wheelBase wheel base
 */
class SwerveConstraints @JvmOverloads constructor(
        baseConstraints: DriveConstraints,
        private val trackWidth: Double,
        private val wheelBase: Double = trackWidth
) : DriveConstraints(
        baseConstraints.maximumVelocity,
        baseConstraints.maximumAcceleration,
        baseConstraints.maximumAngularVelocity,
        baseConstraints.maximumAngularAcceleration
) {
    override fun maximumVelocity(pose: Pose2d, poseDeriv: Pose2d, poseSecondDeriv: Pose2d): Double {
        val robotPositionDeriv = poseDeriv.pos().rotated(-pose.heading)

        val wheelVelocities = SwerveKinematics.robotToWheelVelocities(Pose2d(robotPositionDeriv, poseDeriv.heading), trackWidth, wheelBase)
        val maxTrajectoryVelocity = wheelVelocities.map { maximumVelocity / it }.map(::abs).min() ?: 0.0

        return min(super.maximumVelocity(pose, poseDeriv, poseSecondDeriv), maxTrajectoryVelocity)
    }

}