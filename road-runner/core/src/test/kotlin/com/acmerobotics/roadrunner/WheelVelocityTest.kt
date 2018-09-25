package com.acmerobotics.roadrunner

import com.acmerobotics.roadrunner.drive.MecanumKinematics
import com.acmerobotics.roadrunner.path.heading.SplineInterpolator
import com.acmerobotics.roadrunner.path.heading.TangentInterpolator
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumConstraints
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

private const val TRACK_WIDTH = 1.0
private val BASE_CONSTRAINTS = DriveConstraints(50.0, 25.0, Math.PI / 2, Math.PI / 2)
private val CONSTRAINTS = MecanumConstraints(BASE_CONSTRAINTS, TRACK_WIDTH)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WheelVelocityTest {
    @Test
    fun testMecanumWheelVelocityDerivatives() {
        val trajectory = TrajectoryBuilder(Pose2d(0.0, 0.0, 0.0), CONSTRAINTS)
                .splineTo(Pose2d(15.0, 15.0, Math.PI), interpolator = SplineInterpolator(0.0, Math.PI / 2))
                .splineTo(Pose2d(5.0, 35.0, Math.PI / 3), interpolator = TangentInterpolator())
                .build()

        val dt = trajectory.duration() / 10000.0
        val t = (0..10000).map { it * dt }

        val robotVelocities = t.map { Pose2d(trajectory.velocity(it).pos().rotated(-trajectory[it].heading), trajectory.velocity(it).heading) }
        val wheelVelocities = robotVelocities.map { MecanumKinematics.robotToWheelVelocities(it, TRACK_WIDTH) }

        val robotAccelerations = t.map {
            val pose = trajectory[it]
            val poseVel = trajectory.velocity(it)
            val poseAccel = trajectory.acceleration(it)
            Pose2d(
                    poseAccel.x * Math.cos(-pose.heading) + poseVel.x * Math.sin(-pose.heading) * poseVel.heading - poseAccel.y * Math.sin(-pose.heading) + poseVel.y * Math.cos(-pose.heading) * poseVel.heading,
                    poseAccel.x * Math.sin(-pose.heading) - poseVel.x * Math.cos(-pose.heading) * poseVel.heading + poseAccel.y * Math.cos(-pose.heading) + poseVel.y * Math.sin(-pose.heading) * poseVel.heading,
                    poseAccel.heading
            )
        }
        val wheelAccelerations = robotAccelerations.map { MecanumKinematics.robotToWheelAccelerations(it, TRACK_WIDTH) }

        for (i in 0..3) {
            val vel = wheelVelocities.map { it[i] }
            val accel = wheelAccelerations.map { it[i] }

            assertTrue(TestUtil.compareDerivatives(vel, accel, dt, 0.1, 0.02))
        }
    }
}