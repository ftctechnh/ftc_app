package com.acmerobotics.roadrunner.followers

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.control.PIDFController
import com.acmerobotics.roadrunner.drive.Kinematics
import com.acmerobotics.roadrunner.drive.TankDrive
import com.acmerobotics.roadrunner.drive.TankKinematics
import com.acmerobotics.roadrunner.util.NanoClock

/**
 * Traditional PID controller with feedforward velocity and acceleration components to follow a trajectory. More
 * specifically, one feedback loop controls the path displacement (that is, x in the robot reference frame), and
 * another feedback loop to minimize cross track (lateral) error via heading correction (overall, very similar to
 * [MecanumPIDVAFollower] except adjusted for the nonholonomic constraint). Feedforward is applied at the wheel level.
 *
 * @param drive tank drive instance
 * @param displacementCoeffs PID coefficients for the robot axial (x) controller
 * @param crossTrackCoeffs PID coefficients for the robot heading controller based on cross track error
 * @param kV feedforward velocity gain
 * @param kA feedforward acceleration gain
 * @param kStatic signed, additive feedforward constant (used to overcome static friction)
 * @param clock clock
 */
class TankPIDVAFollower @JvmOverloads constructor(
        private val drive: TankDrive,
        displacementCoeffs: PIDCoefficients,
        crossTrackCoeffs: PIDCoefficients,
        private val kV: Double,
        private val kA: Double,
        private val kStatic: Double,
        clock: NanoClock = NanoClock.system()
) : TrajectoryFollower(clock) {
    private val displacementController = PIDFController(displacementCoeffs)
    private val crossTrackController = PIDFController(crossTrackCoeffs)

    override fun update(currentPose: Pose2d) {
        if (!isFollowing()) {
            drive.setMotorPowers(0.0, 0.0)
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

        displacementController.targetPosition = targetRobotPose.x
        crossTrackController.targetPosition = targetRobotPose.y

        val axialCorrection = displacementController.update(currentRobotPose.x, targetRobotPoseVelocity.x)
        val headingCorrection = crossTrackController.update(currentRobotPose.y, targetRobotPoseVelocity.heading)

        val correctedVelocity = targetRobotPoseVelocity + Pose2d(axialCorrection, 0.0, headingCorrection)

        val wheelVelocities = TankKinematics.robotToWheelVelocities(correctedVelocity, drive.trackWidth)
        val wheelAccelerations = TankKinematics.robotToWheelAccelerations(targetRobotPoseAcceleration, drive.trackWidth)

        val motorPowers = Kinematics.calculateMotorFeedforward(wheelVelocities, wheelAccelerations, kV, kA, kStatic)

        drive.setMotorPowers(motorPowers[0], motorPowers[1])
    }
}