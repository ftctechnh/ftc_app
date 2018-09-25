package com.acmerobotics.roadrunner.followers

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.control.PIDFController
import com.acmerobotics.roadrunner.drive.Drive
import com.acmerobotics.roadrunner.drive.Kinematics
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
abstract class HolonomicPIDVAFollower @JvmOverloads constructor(
        private val drive: Drive,
        translationalCoeffs: PIDCoefficients,
        headingCoeffs: PIDCoefficients,
        private val kV: Double,
        private val kA: Double,
        private val kStatic: Double,
        clock: NanoClock = NanoClock.system()
) : TrajectoryFollower(clock) {
    private val axialController = PIDFController(translationalCoeffs)
    private val lateralController = PIDFController(translationalCoeffs)
    private val headingController = PIDFController(headingCoeffs)

    init {
        headingController.setInputBounds(-Math.PI, Math.PI)
    }

    override fun update(currentPose: Pose2d) {
        if (!isFollowing()) {
            drive.setVelocity(Pose2d(0.0, 0.0, 0.0))
            return
        }

        val t = elapsedTime()

        val targetPose = trajectory[t]
        val targetPoseVelocity = trajectory.velocity(t)
        val targetPoseAcceleration = trajectory.acceleration(t)

        val targetRobotPose = Kinematics.fieldToRobotPose(targetPose)
        val targetRobotPoseVelocity = Kinematics.fieldToRobotPoseVelocity(targetPose, targetPoseVelocity)
        val targetRobotPoseAcceleration = Kinematics.fieldToRobotPoseAcceleration(targetPose, targetPoseVelocity, targetPoseAcceleration)

        val currentRobotPose = Pose2d(currentPose.pos().rotated(-targetPose.heading), currentPose.heading - targetPose.heading)

        axialController.targetPosition = targetRobotPose.x
        lateralController.targetPosition = targetRobotPose.y
        headingController.targetPosition = targetRobotPose.heading

        val axialCorrection = axialController.update(currentRobotPose.x, targetRobotPoseVelocity.x)
        val lateralCorrection = lateralController.update(currentRobotPose.y, targetRobotPoseVelocity.y)
        val headingCorrection = headingController.update(currentRobotPose.heading, targetRobotPoseVelocity.heading)

        val correctedVelocity = targetRobotPoseVelocity + Pose2d(axialCorrection, lateralCorrection, headingCorrection)

        updateDrive(correctedVelocity, targetRobotPoseAcceleration)
    }

    abstract fun updateDrive(poseVelocity: Pose2d, poseAcceleration: Pose2d)
}