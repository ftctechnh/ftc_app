package com.acmerobotics.roadrunner

import com.acmerobotics.roadrunner.path.heading.TangentInterpolator
import com.acmerobotics.roadrunner.path.heading.WiggleInterpolator
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TrajectoryTest {
    @Test
    fun testTrajectoryDerivatives() {
        val cryptoColWidth = 7.5
        val stonePose = Pose2d(48.0, -47.5, Math.PI)
        val trajectory = TrajectoryBuilder(stonePose, DriveConstraints(5.0, 10.0, 2.0, 3.0))
                .lineTo(Vector2d(12 - cryptoColWidth, stonePose.y))
                .turnTo(Math.PI / 2)
                .reverse()
                .lineTo(Vector2d(12 - cryptoColWidth, -56.0))
                // deposit
                .reverse()
                .beginComposite()
                .lineTo(Vector2d(12 - cryptoColWidth, -44.0))
                .splineTo(Pose2d(16.0, -24.0, Math.PI / 3))
                .splineTo(Pose2d(24.0, -10.0, Math.PI / 4),
                        WiggleInterpolator(Math.toRadians(15.0), 6.0, TangentInterpolator()))
                .closeComposite()
                .build()

        val dt = trajectory.duration() / 10000.0
        val t = (0..10000).map { it * dt }

        val x = t.map { trajectory[it].x }
        val velX = t.map { trajectory.velocity(it).x }
        val accelX = t.map { trajectory.acceleration(it).x }

        val y = t.map { trajectory[it].y }
        val velY = t.map { trajectory.velocity(it).y }
        val accelY = t.map { trajectory.acceleration(it).y }

        assert(TestUtil.compareDerivatives(x, velX, dt, 0.01))
        assert(TestUtil.compareDerivatives(velX, accelX, dt, 0.01))

        assert(TestUtil.compareDerivatives(y, velY, dt, 0.01))
        assert(TestUtil.compareDerivatives(velY, accelY, dt, 0.01))
    }
}