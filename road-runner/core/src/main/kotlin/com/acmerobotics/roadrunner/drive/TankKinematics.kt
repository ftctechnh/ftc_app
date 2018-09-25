package com.acmerobotics.roadrunner.drive

import com.acmerobotics.roadrunner.Pose2d
import kotlin.math.abs

/**
 * Tank drive kinematic equations based upon the unicycle model.
 * [This page](http://rossum.sourceforge.net/papers/DiffSteer/) gives a motivated derivation.
 */
object TankKinematics {

    /**
     * Computes the wheel velocities corresponding to [robotPoseVelocity] given [trackWidth].
     *
     * @param robotPoseVelocity velocity of the robot in its reference frame
     * @param trackWidth lateral distance between pairs of wheels on different sides of the robot
     */
    @JvmStatic
    fun robotToWheelVelocities(robotPoseVelocity: Pose2d, trackWidth: Double): List<Double> {
        if (abs(robotPoseVelocity.y) > 1e-2) {
            throw IllegalArgumentException("Lateral (robot y) velocity must be zero for tank drives")
        }

        return listOf(robotPoseVelocity.x - trackWidth / 2 * robotPoseVelocity.heading,
                robotPoseVelocity.x + trackWidth / 2 * robotPoseVelocity.heading)
    }

    /**
     * Computes the wheel accelerations corresponding to [robotPoseAcceleration] given [trackWidth].
     *
     * @param robotPoseAcceleration velocity of the robot in its reference frame
     * @param trackWidth lateral distance between pairs of wheels on different sides of the robot
     */
    // follows from linearity of the derivative
    @JvmStatic
    fun robotToWheelAccelerations(robotPoseAcceleration: Pose2d, trackWidth: Double) =
            robotToWheelVelocities(robotPoseAcceleration, trackWidth)

    /**
     * Computes the robot velocity corresponding to [wheelVelocities] and the given drive parameters.
     *
     * @param wheelVelocities wheel velocities (or wheel position deltas)
     * @param trackWidth lateral distance between pairs of wheels on different sides of the robot
     */
    @JvmStatic
    fun wheelToRobotVelocities(wheelVelocities: List<Double>, trackWidth: Double) =
        Pose2d((wheelVelocities[0] + wheelVelocities[1]) / 2.0,
                0.0,
                (-wheelVelocities[0] + wheelVelocities[1]) / trackWidth)
}