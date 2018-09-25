package com.acmerobotics.roadrunner

import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.drive.MecanumDrive
import com.acmerobotics.roadrunner.followers.MecanumPIDVAFollower
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumConstraints
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import org.apache.commons.math3.distribution.NormalDistribution
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.knowm.xchart.XYChart
import org.knowm.xchart.style.MatlabTheme
import org.knowm.xchart.style.markers.None
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

private const val kV = 1.0 / 60.0
private const val SIMULATION_HZ = 25
private const val TRACK_WIDTH = 3.0

private val BASE_CONSTRAINTS = DriveConstraints(50.0, 25.0, Math.PI / 2, Math.PI / 2)
private val CONSTRAINTS = MecanumConstraints(BASE_CONSTRAINTS, TRACK_WIDTH)

private val VOLTAGE_NOISE_DIST = NormalDistribution(1.0, 0.05)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MecanumFollowerTest {

    private class SimulatedMecanumDrive(
            private val dt: Double,
            private val kV: Double,
            trackWidth: Double,
            wheelBase: Double = trackWidth
    ) : MecanumDrive(trackWidth, wheelBase) {
        var powers = listOf(0.0, 0.0, 0.0, 0.0)
        var positions = listOf(0.0, 0.0, 0.0, 0.0)

        override fun setMotorPowers(frontLeft: Double, rearLeft: Double, rearRight: Double, frontRight: Double) {
            powers = listOf(frontLeft, rearLeft, rearRight, frontRight)
                    .map { it * VOLTAGE_NOISE_DIST.sample() }
                    .map { max(0.0, min(it, 1.0)) }
            positions = positions.zip(powers)
                    .map { it.first + it.second / kV * dt }
        }

        override fun getWheelPositions(): List<Double> = positions
    }

    @Test
    fun simulatePIDVAFollower() {
        val dt = 1.0 / SIMULATION_HZ

        val trajectory = TrajectoryBuilder(Pose2d(0.0, 0.0, 0.0), CONSTRAINTS)
                .beginComposite()
                .splineTo(Pose2d(15.0, 15.0, Math.PI))
                .splineTo(Pose2d(5.0, 35.0, Math.PI / 3))
                .closeComposite()
                .waitFor(0.5)
                .build()

        val drive = SimulatedMecanumDrive(dt, kV, TRACK_WIDTH)
        val clock = SimulatedClock()
        val follower = MecanumPIDVAFollower(drive, PIDCoefficients(1.0), PIDCoefficients(5.0), kV, 0.0, 0.0, clock)
        follower.followTrajectory(trajectory)

        val targetPositions = mutableListOf<Vector2d>()
        val actualPositions = mutableListOf<Vector2d>()

        val samples = ceil(trajectory.duration() / dt).toInt()
        for (sample in 1..samples) {
            val t = sample * dt
            clock.time = t
            follower.update(drive.poseEstimate)
            drive.updatePoseEstimate()

            targetPositions.add(trajectory[t].pos())
            actualPositions.add(drive.poseEstimate.pos())
        }

        val graph = XYChart(600, 400)
        graph.title = "Mecanum PIDVA Follower Sim"
        graph.addSeries(
                "Target Trajectory",
                targetPositions.map { it.x }.toDoubleArray(),
                targetPositions.map { it.y }.toDoubleArray())
        graph.addSeries(
                "Actual Trajectory",
                actualPositions.map { it.x }.toDoubleArray(),
                actualPositions.map { it.y }.toDoubleArray())
        graph.seriesMap.values.forEach { it.marker = None() }
        graph.styler.theme = MatlabTheme()
        GraphUtil.saveGraph("mecanumSim", graph)
    }
}
