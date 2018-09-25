package com.acmerobotics.roadrunner

import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.drive.TankDrive
import com.acmerobotics.roadrunner.followers.GVFFollower
import com.acmerobotics.roadrunner.followers.RamseteFollower
import com.acmerobotics.roadrunner.followers.TankPIDVAFollower
import com.acmerobotics.roadrunner.path.PathBuilder
import com.acmerobotics.roadrunner.profile.SimpleMotionConstraints
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints
import com.acmerobotics.roadrunner.trajectory.constraints.TankConstraints
import com.acmerobotics.roadrunner.util.NanoClock
import org.apache.commons.math3.distribution.NormalDistribution
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.knowm.xchart.XYChart
import org.knowm.xchart.style.MatlabTheme
import org.knowm.xchart.style.markers.None
import kotlin.math.atan
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

private const val kV = 1.0 / 60.0
private const val SIMULATION_HZ = 25
private const val TRACK_WIDTH = 3.0

private val BASE_CONSTRAINTS = DriveConstraints(50.0, 25.0, Math.PI / 2, Math.PI / 2)
private val CONSTRAINTS = TankConstraints(BASE_CONSTRAINTS, TRACK_WIDTH)

private val VOLTAGE_NOISE_DIST = NormalDistribution(1.0, 0.05)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TankFollowerTest {

    private class SimulatedTankDrive(
            private val dt: Double,
            private val kV: Double,
            trackWidth: Double,
            clock: NanoClock
    ) : TankDrive(trackWidth, clock) {
        var powers = listOf(0.0, 0.0)
        var positions = listOf(0.0, 0.0)

        override fun setMotorPowers(left: Double, right: Double) {
            powers = listOf(left, right)
                    .map { it * VOLTAGE_NOISE_DIST.sample() }
                    .map { max(0.0, min(it, 1.0)) }
            positions = positions.zip(powers)
                    .map { it.first + it.second / kV * dt }
        }

        override fun getWheelPositions() = positions
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

        val clock = SimulatedClock()
        val drive = SimulatedTankDrive(dt, kV, TRACK_WIDTH, clock)
        val follower = TankPIDVAFollower(drive, PIDCoefficients(1.0), PIDCoefficients(kP = 1.0), kV, 0.0, 0.0, clock)
        follower.followTrajectory(trajectory)

        val targetPositions = mutableListOf<Vector2d>()
        val actualPositions = mutableListOf<Vector2d>()

        drive.poseEstimate = trajectory.start()
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
        graph.title = "Tank PIDVA Follower Sim"
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
        GraphUtil.saveGraph("tankPIDVASim", graph)
    }

    @Test
    fun simulateRamseteFollower() {
        val dt = 1.0 / SIMULATION_HZ

        val trajectory = TrajectoryBuilder(Pose2d(0.0, 0.0, 0.0), CONSTRAINTS)
                .beginComposite()
                .splineTo(Pose2d(15.0, 15.0, Math.PI))
                .splineTo(Pose2d(5.0, 35.0, Math.PI / 3))
                .closeComposite()
                .waitFor(0.5)
                .build()

        val clock = SimulatedClock()
        val drive = SimulatedTankDrive(dt, kV, TRACK_WIDTH, clock)
        val follower = RamseteFollower(drive, 0.0008, 0.5, kV, 0.0, 0.0, clock)
        follower.followTrajectory(trajectory)

        val targetPositions = mutableListOf<Vector2d>()
        val actualPositions = mutableListOf<Vector2d>()

        drive.poseEstimate = trajectory.start()
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
        graph.title = "Tank Ramsete Follower Sim"
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
        GraphUtil.saveGraph("tankRamseteSim", graph)
    }

    @Test
    fun simulateGVFFollower() {
        val dt = 1.0 / SIMULATION_HZ

        val path = PathBuilder(Pose2d(0.0, 0.0, 0.0))
                .splineTo(Pose2d(15.0, 15.0, 0.0))
                .lineTo(Vector2d(30.0, 15.0))
                .build()

        val clock = SimulatedClock()
        val drive = SimulatedTankDrive(dt, kV, TRACK_WIDTH, clock)
        val follower = GVFFollower(
                drive,
                SimpleMotionConstraints(5.0, 25.0),
                3.0,
                5.0,
                kV,
                0.0,
                0.0,
                ::atan,
                clock)
        follower.followPath(path)

        val actualPositions = mutableListOf<Vector2d>()

        drive.poseEstimate = Pose2d(0.0, 10.0, -Math.PI / 2)
        var t = 0.0
        while (follower.isFollowing()) {
            t += dt
            clock.time = t
            follower.update(drive.poseEstimate)
            drive.updatePoseEstimate()

            actualPositions.add(drive.poseEstimate.pos())
        }

        val pathPoints = (0..10000)
                .map { it / 10000.0 * path.length() }
                .map { path[it] }
        val graph = XYChart(600, 400)
        graph.title = "Tank GVF Follower Sim"
        graph.addSeries(
                "Target Trajectory",
                pathPoints.map { it.x }.toDoubleArray(),
                pathPoints.map { it.y }.toDoubleArray())
        graph.addSeries(
                "Actual Trajectory",
                actualPositions.map { it.x }.toDoubleArray(),
                actualPositions.map { it.y }.toDoubleArray())
        graph.seriesMap.values.forEach { it.marker = None() }
        graph.styler.theme = MatlabTheme()
        GraphUtil.saveGraph("tankGVFSim", graph)
    }
}
