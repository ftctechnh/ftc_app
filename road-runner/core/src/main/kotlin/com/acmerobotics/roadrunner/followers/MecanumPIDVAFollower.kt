package com.acmerobotics.roadrunner.followers

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.drive.Kinematics
import com.acmerobotics.roadrunner.drive.MecanumDrive
import com.acmerobotics.roadrunner.drive.MecanumKinematics
import com.acmerobotics.roadrunner.util.NanoClock

/**
 * Traditional PID controller with feedforward velocity and acceleration components to follow a trajectory. More
 * specifically, the feedback is applied to the components of the robot's pose (x position, y position, and heading) to
 * determine the velocity correction. The feedforward components are instead applied at the wheel level.
 *
 * @param drive mecanum drive instance
 * @param translationalCoeffs PID coefficients for the robot axial and lateral (x and y, respectively) controllers
 * @param headingCoeffs PID coefficients for the robot heading controller
 * @param kV feedforward velocity gain
 * @param kA feedforward acceleration gain
 * @param kStatic signed, additive feedforward constant (used to overcome static friction)
 * @param clock clock
 */
class MecanumPIDVAFollower @JvmOverloads constructor(
        private val drive: MecanumDrive,
        translationalCoeffs: PIDCoefficients,
        headingCoeffs: PIDCoefficients,
        private val kV: Double,
        private val kA: Double,
        private val kStatic: Double,
        clock: NanoClock = NanoClock.system()
) : HolonomicPIDVAFollower(drive, translationalCoeffs, headingCoeffs, kV, kA, kStatic, clock) {
    override fun updateDrive(poseVelocity: Pose2d, poseAcceleration: Pose2d) {
        val wheelVelocities = MecanumKinematics.robotToWheelVelocities(poseVelocity, drive.trackWidth, drive.wheelBase)
        val wheelAccelerations = MecanumKinematics.robotToWheelAccelerations(poseAcceleration, drive.trackWidth, drive.wheelBase)

        val motorPowers = Kinematics.calculateMotorFeedforward(wheelVelocities, wheelAccelerations, kV, kA, kStatic)

        drive.setMotorPowers(motorPowers[0], motorPowers[1], motorPowers[2], motorPowers[3])
    }
}