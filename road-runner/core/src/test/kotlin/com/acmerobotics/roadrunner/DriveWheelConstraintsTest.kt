package com.acmerobotics.roadrunner

import com.acmerobotics.roadrunner.drive.MecanumKinematics
import com.acmerobotics.roadrunner.drive.SwerveKinematics
import com.acmerobotics.roadrunner.drive.TankKinematics
import com.acmerobotics.roadrunner.path.heading.SplineInterpolator
import com.acmerobotics.roadrunner.path.heading.TangentInterpolator
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumConstraints
import com.acmerobotics.roadrunner.trajectory.constraints.SwerveConstraints
import com.acmerobotics.roadrunner.trajectory.constraints.TankConstraints
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.math.abs

private val BASE_CONSTRAINTS = DriveConstraints(10.0, 25.0, Math.PI / 2, Math.PI / 2)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DriveWheelConstraintsTest {
    @Test
    fun testTankWheelVelocityLimiting() {
        val constraints = TankConstraints(BASE_CONSTRAINTS, 10.0)
        val trajectory = TrajectoryBuilder(Pose2d(0.0, 0.0, 0.0), constraints)
                .splineTo(Pose2d(15.0, 15.0, Math.PI))
                .splineTo(Pose2d(5.0, 35.0, Math.PI / 3))
                .build()

        val dt = trajectory.duration() / 10000.0
        val t = (0..10000).map { it * dt }
        val maxWheelVelocityMag = t.map {
            val velocity = trajectory.velocity(it)
            val heading = trajectory[it].heading
            val robotVelocity = Pose2d(velocity.pos().rotated(-heading), velocity.heading)
            TankKinematics.robotToWheelVelocities(robotVelocity, 10.0)
                    .map(::abs)
                    .max() ?: 0.0
        }.max() ?: 0.0
        assertEquals(BASE_CONSTRAINTS.maximumVelocity, maxWheelVelocityMag, 0.1)
    }

    @Test
    fun testMecanumWheelVelocityLimiting() {
        val constraints = MecanumConstraints(BASE_CONSTRAINTS, 10.0, 5.0)
        val trajectory = TrajectoryBuilder(Pose2d(0.0, 0.0, 0.0), constraints)
                .splineTo(Pose2d(15.0, 15.0, Math.PI), interpolator = SplineInterpolator(0.0, Math.PI / 2))
                .splineTo(Pose2d(5.0, 35.0, Math.PI / 3), interpolator = TangentInterpolator())
                .build()

        val dt = trajectory.duration() / 10000.0
        val t = (0..10000).map { it * dt }
        val maxWheelVelocityMag = t.map {
            val velocity = trajectory.velocity(it)
            val heading = trajectory[it].heading
            val robotVelocity = Pose2d(velocity.pos().rotated(-heading), velocity.heading)
            MecanumKinematics.robotToWheelVelocities(robotVelocity, 10.0, 5.0)
                    .map(::abs)
                    .max() ?: 0.0
        }.max() ?: 0.0
        assertEquals(BASE_CONSTRAINTS.maximumVelocity, maxWheelVelocityMag, 0.1)
    }

    @Test
    fun testSwerveWheelVelocityLimiting() {
        val constraints = SwerveConstraints(BASE_CONSTRAINTS, 10.0, 5.0)
        val trajectory = TrajectoryBuilder(Pose2d(0.0, 0.0, 0.0), constraints)
                .splineTo(Pose2d(15.0, 15.0, Math.PI), interpolator = SplineInterpolator(0.0, Math.PI / 2))
                .splineTo(Pose2d(5.0, 35.0, Math.PI / 3), interpolator = TangentInterpolator())
                .build()

        val dt = trajectory.duration() / 10000.0
        val t = (0..10000).map { it * dt }
        val maxWheelVelocityMag = t.map {
            val velocity = trajectory.velocity(it)
            val heading = trajectory[it].heading
            val robotVelocity = Pose2d(velocity.pos().rotated(-heading), velocity.heading)
            SwerveKinematics.robotToWheelVelocities(robotVelocity, 10.0, 5.0)
                    .map(::abs)
                    .max() ?: 0.0
        }.max() ?: 0.0
        assertEquals(BASE_CONSTRAINTS.maximumVelocity, maxWheelVelocityMag, 0.1)
    }
}