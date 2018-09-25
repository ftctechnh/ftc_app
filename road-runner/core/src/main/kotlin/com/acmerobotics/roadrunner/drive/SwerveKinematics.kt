package com.acmerobotics.roadrunner.drive

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Vector2d
import kotlin.math.cos
import kotlin.math.sin

/**
 * Swerve drive kinematic equations.
 */
object SwerveKinematics {

    /**
     * Computes the wheel velocity vectors corresponding to [robotPoseVelocity] given the provided [trackWidth] and
     * [wheelBase].
     *
     * @param robotPoseVelocity velocity of the robot in its reference frame
     * @param trackWidth lateral distance between pairs of wheels on different sides of the robot
     * @param wheelBase distance between pairs of wheels on the same side of the robot
     */
    @JvmStatic
    @JvmOverloads
    fun robotToModuleVelocityVectors(robotPoseVelocity: Pose2d, trackWidth: Double, wheelBase: Double = trackWidth): List<Vector2d> {
        val x = wheelBase / 2
        val y = trackWidth / 2

        val vx = robotPoseVelocity.x
        val vy = robotPoseVelocity.y
        val omega = robotPoseVelocity.heading

        return listOf(
                Vector2d(vx - omega * y, vy + omega * x),
                Vector2d(vx - omega * y, vy - omega * x),
                Vector2d(vx + omega * y, vy - omega * x),
                Vector2d(vx + omega * y, vy + omega * x)
        )
    }

    /**
     * Computes the wheel velocities corresponding to [robotPoseVelocity] given the provided [trackWidth] and
     * [wheelBase].
     *
     * @param robotPoseVelocity velocity of the robot in its reference frame
     * @param trackWidth lateral distance between pairs of wheels on different sides of the robot
     * @param wheelBase distance between pairs of wheels on the same side of the robot
     */
    @JvmStatic
    @JvmOverloads
    fun robotToWheelVelocities(robotPoseVelocity: Pose2d, trackWidth: Double, wheelBase: Double = trackWidth) =
            robotToModuleVelocityVectors(robotPoseVelocity, trackWidth, wheelBase).map(Vector2d::norm)

    /**
     * Computes the module orientations (in radians) corresponding to [robotPoseVelocity] given the provided
     * [trackWidth] and [wheelBase].
     *
     * @param robotPoseVelocity velocity of the robot in its reference frame
     * @param trackWidth lateral distance between pairs of wheels on different sides of the robot
     * @param wheelBase distance between pairs of wheels on the same side of the robot
     */
    @JvmStatic
    @JvmOverloads
    fun robotToModuleOrientations(robotPoseVelocity: Pose2d, trackWidth: Double, wheelBase: Double = trackWidth) =
            robotToModuleVelocityVectors(robotPoseVelocity, trackWidth, wheelBase).map(Vector2d::angle)

    /**
     * Computes the acceleration vectors corresponding to [robotPoseAcceleration] given the provided [trackWidth] and
     * [wheelBase].
     *
     * @param robotPoseAcceleration velocity of the robot in its reference frame
     * @param trackWidth lateral distance between pairs of wheels on different sides of the robot
     * @param wheelBase distance between pairs of wheels on the same side of the robot
     */
    @JvmStatic
    @JvmOverloads
    fun robotToModuleAccelerationVectors(robotPoseAcceleration: Pose2d, trackWidth: Double, wheelBase: Double = trackWidth): List<Vector2d> {
        val x = wheelBase / 2
        val y = trackWidth / 2

        val ax = robotPoseAcceleration.x
        val ay = robotPoseAcceleration.y
        val alpha = robotPoseAcceleration.heading

        return listOf(
                Vector2d(ax - alpha * y, ay + alpha * x),
                Vector2d(ax - alpha * y, ay - alpha * x),
                Vector2d(ax + alpha * y, ay - alpha * x),
                Vector2d(ax + alpha * y, ay + alpha * x)
        )
    }

    /**
     * Computes the wheel accelerations corresponding to [robotPoseAcceleration] given the provided [trackWidth] and
     * [wheelBase].
     *
     * @param robotPoseAcceleration velocity of the robot in its reference frame
     * @param trackWidth lateral distance between pairs of wheels on different sides of the robot
     * @param wheelBase distance between pairs of wheels on the same side of the robot
     */
    @JvmStatic
    @JvmOverloads
    fun robotToWheelAccelerations(robotPoseVelocity: Pose2d, robotPoseAcceleration: Pose2d, trackWidth: Double, wheelBase: Double = trackWidth) =
            robotToModuleVelocityVectors(robotPoseVelocity, trackWidth, wheelBase)
                    .zip(robotToModuleAccelerationVectors(robotPoseAcceleration, trackWidth, wheelBase))
                    .map { (it.first.x * it.second.x + it.first.y * it.second.y) / it.first.norm() }

    /**
     * Computes the module angular velocities corresponding to [robotPoseAcceleration] given the provided [trackWidth]
     * and [wheelBase].
     *
     * @param robotPoseAcceleration velocity of the robot in its reference frame
     * @param trackWidth lateral distance between pairs of wheels on different sides of the robot
     * @param wheelBase distance between pairs of wheels on the same side of the robot
     */
    @JvmStatic
    @JvmOverloads
    fun robotToModuleAngularVelocities(robotPoseVelocity: Pose2d, robotPoseAcceleration: Pose2d, trackWidth: Double, wheelBase: Double = trackWidth) =
            robotToModuleVelocityVectors(robotPoseVelocity, trackWidth, wheelBase)
                    .zip(robotToModuleAccelerationVectors(robotPoseAcceleration, trackWidth, wheelBase))
                    .map { (it.first.x * it.second.y - it.first.y * it.second.x) / (it.first.x * it.first.x + it.first.y * it.first.y) }

    /**
     * Computes the robot velocities corresponding to [wheelVelocities], [moduleOrientations], and the drive parameters.
     *
     * @param wheelVelocities wheel velocities (or wheel position deltas)
     * @param moduleOrientations wheel orientations (in radians)
     * @param trackWidth lateral distance between pairs of wheels on different sides of the robot
     * @param wheelBase distance between pairs of wheels on the same side of the robot
     */
    @JvmStatic
    @JvmOverloads
    fun wheelToRobotVelocities(wheelVelocities: List<Double>, moduleOrientations: List<Double>, trackWidth: Double, wheelBase: Double = trackWidth): Pose2d {
        val x = wheelBase / 2
        val y = trackWidth / 2

        val vectors = wheelVelocities
                .zip(moduleOrientations)
                .map { Vector2d(it.first * cos(it.second), it.first * sin(it.second)) }

        val vx = vectors.sumByDouble { it.x } / 4
        val vy = vectors.sumByDouble { it.y } / 4
        val omega = (y * (vectors[2].x + vectors[3].x - vectors[0].x - vectors[1].x)
                + x * (vectors[0].y + vectors[3].y - vectors[1].y - vectors[2].y)) / (4 * (x * x + y * y))

        return Pose2d(vx, vy, omega)
    }
}