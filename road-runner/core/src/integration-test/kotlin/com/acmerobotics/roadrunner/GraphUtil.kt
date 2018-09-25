package com.acmerobotics.roadrunner

import com.acmerobotics.roadrunner.path.ParametricCurve
import com.acmerobotics.roadrunner.profile.MotionProfile
import com.acmerobotics.roadrunner.trajectory.Trajectory
import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.QuickChart
import org.knowm.xchart.XYChart
import org.knowm.xchart.style.MatlabTheme
import java.io.File
import java.nio.file.Paths

object GraphUtil {
    const val GRAPH_DIR = "./graphs/"
    private const val GRAPH_DPI = 300

    fun saveGraph(name: String, graph: XYChart) {
        val file = File(Paths.get(GRAPH_DIR, name).toString())
        file.parentFile.mkdirs()

        BitmapEncoder.saveBitmapWithDPI(graph, file.absolutePath, BitmapEncoder.BitmapFormat.PNG, GRAPH_DPI)
    }

    fun saveMotionProfile(name: String, profile: MotionProfile, includeAcceleration: Boolean = true, resolution: Int = 1000) {
        val timeData = (0..resolution).map { it / resolution.toDouble() * profile.duration() }.toDoubleArray()
        val positionData = timeData.map { profile[it].x }.toDoubleArray()
        val velocityData = timeData.map { profile[it].v }.toDoubleArray()

        val labels = mutableListOf("x(t)", "v(t)")
        val data = mutableListOf(positionData, velocityData)

        if (includeAcceleration) {
            val accelerationData = timeData.map { profile[it].a }.toDoubleArray()

            labels.add("a(t)")
            data.add(accelerationData)
        }

        val graph = QuickChart.getChart(
                name,
                "time (sec)",
                "",
                labels.toTypedArray(),
                timeData,
                data.toTypedArray()
        )
        graph.styler.theme = MatlabTheme()

        saveGraph(name, graph)
    }

    fun saveParametricCurve(name: String, parametricCurve: ParametricCurve, resolution: Int = 1000) {
        val displacementData = (0..resolution).map { it / resolution.toDouble() * parametricCurve.length() }
        val points = displacementData.map { parametricCurve[it] }
        val xData = points.map { it.x }.toDoubleArray()
        val yData = points.map { it.y }.toDoubleArray()

        val graph = QuickChart.getChart(name, "x", "y", name, xData, yData)
        graph.styler.isLegendVisible = false
        graph.styler.theme = MatlabTheme()
        saveGraph(name, graph)
    }

    fun saveTrajectory(name: String, trajectory: Trajectory, resolution: Int = 1000) {
        val timeData = (0..resolution).map { it / resolution.toDouble() * trajectory.duration() }.toDoubleArray()
        val velocityData = timeData.map { trajectory.velocity(it) }
        val xVelocityData = velocityData.map { it.x }.toDoubleArray()
        val yVelocityData = velocityData.map { it.y }.toDoubleArray()
        val omegaData = velocityData.map { it.heading }.toDoubleArray()

        val labels = listOf("vx(t)", "vy(t)", "ω(t)")
        val data = listOf(xVelocityData, yVelocityData, omegaData)

        val graph = QuickChart.getChart(
                name,
                "time (sec)",
                "",
                labels.toTypedArray(),
                timeData,
                data.toTypedArray()
        )
        graph.styler.theme = MatlabTheme()

        saveGraph(name, graph)
    }
}