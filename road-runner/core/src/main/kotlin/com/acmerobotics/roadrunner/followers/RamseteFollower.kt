package com.acmerobotics.roadrunner.followers

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.drive.Kinematics
import com.acmerobotics.roadrunner.drive.TankDrive
import com.acmerobotics.roadrunner.drive.TankKinematics
import com.acmerobotics.roadrunner.util.NanoClock
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Time-varying, non-linear feedback controller for nonholonomic drives. See equation 5.12 of
 * [Ramsete01.pdf](https://www.dis.uniroma1.it/~labrob/pub/papers/Ramsete01.pdf).
 *
 * @param drive tank drive
 * @param b b parameter (non-negative)
 * @param zeta zeta parameter (on (0, 1))
 * @param kV feedforward velocity gain
 * @param kA feedforward acceleration gain (currently unused)
 * @param kStatic additive feedforward constant (used to overcome static friction)
 * @param clock clock
 */
class RamseteFollower @JvmOverloads constructor(
        private val drive: TankDrive,
        private val b: Double,
        private val zeta: Double,
        private val kV: Double,
        private val kA: Double,
        private val kStatic: Double,
        clock: NanoClock = NanoClock.system()
) : TrajectoryFollower(clock) {
    override fun update(currentPose: Pose2d) {
        if (!isFollowing()) {
            drive.setMotorPowers(0.0, 0.0)
            return
        }

        val t = elapsedTime()

        val targetPose = trajectory[t]
        val targetPoseVelocity = trajectory.velocity(t)

        val targetRobotPose = Pose2d(targetPose.pos().rotated(-targetPose.heading), 0.0)
        val targetRobotPoseVelocity = Pose2d(targetPoseVelocity.pos().rotated(-targetPose.heading), targetPoseVelocity.heading)

        val currentRobotPose = Pose2d(currentPose.pos().rotated(-targetPose.heading), currentPose.heading - targetPose.heading)

        val targetV = targetRobotPoseVelocity.x
        val targetOmega = targetRobotPoseVelocity.heading
        val error = targetRobotPose - currentRobotPose

        val k1 = 2 * zeta * sqrt(targetOmega * targetOmega + b * targetV * targetV)
        val k3 = k1
        val k2 = b

        val v = targetV * cos(error.heading) +
                k1 * (cos(currentPose.heading) * error.x + sin(currentPose.heading) * error.y)
        val omega = targetOmega + k2 * targetV * sin(error.heading) / error.heading *
                (cos(currentPose.heading) * error.y - sin(currentPose.heading) * error.x) +
                k3 * error.heading

        // TODO: is Ramsete acceleration FF worth?
        val targetRobotPoseAcceleration = Pose2d()

        val wheelVelocities = TankKinematics.robotToWheelVelocities(Pose2d(v, 0.0, omega), drive.trackWidth)
        val wheelAccelerations = TankKinematics.robotToWheelAccelerations(targetRobotPoseAcceleration, drive.trackWidth)

        val motorPowers = Kinematics.calculateMotorFeedforward(wheelVelocities, wheelAccelerations, kV, kA, kStatic)

        drive.setMotorPowers(motorPowers[0], motorPowers[1])
    }
}