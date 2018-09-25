package com.acmerobotics.roadrunner.gui

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.geom.Point2D
import javax.imageio.ImageIO
import javax.swing.JPanel
import kotlin.math.min
import kotlin.math.roundToInt

private const val RESOLUTION = 1000

class FieldPanel : JPanel() {

    private var poses = listOf<Pose2d>()
    private var trajectory = Trajectory()

    init {
        preferredSize = Dimension(500, 500)
    }

    fun updateTrajectoryAndPoses(trajectory: Trajectory, poses: List<Pose2d>) {
        this.poses = poses
        this.trajectory = trajectory
        repaint()
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        val g2d = g as Graphics2D

        // antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        // transform coordinate frame
        val fieldSize = min(width, height)
        val offsetX = (width - fieldSize) / 2.0
        val offsetY = (height - fieldSize) / 2.0

        val transform = AffineTransform()
        transform.translate(width / 2.0, height / 2.0)
        transform.scale(fieldSize / 144.0, fieldSize / 144.0)
        transform.rotate(Math.PI / 2)
        transform.scale(-1.0, 1.0)

        // draw field
        val fieldImage = ImageIO.read(javaClass.getResource("/field.png"));
        g2d.drawImage(fieldImage, offsetX.roundToInt(), offsetY.roundToInt(), fieldSize, fieldSize, null)

        g2d.color = Color(76, 175, 80)

        // draw poses
        for (pose in poses) {
            val point = Point2D.Double()
            transform.transform(Point2D.Double(pose.x, pose.y), point)
            g2d.fillArc(point.x.roundToInt() - 5, point.y.roundToInt() - 5, 10, 10, 0, 360)
        }

        // draw trajectory
        g2d.stroke = BasicStroke(3F)

        if (trajectory.duration() == 0.0) {
            return
        }

        val displacements = (0..RESOLUTION).map { it / RESOLUTION.toDouble() * trajectory.duration() }
        for (i in 1..RESOLUTION) {
            val firstPose = trajectory[displacements[i-1]]
            val secondPose = trajectory[displacements[i]]
            val firstPoint = Point2D.Double()
            val secondPoint = Point2D.Double()
            transform.transform(Point2D.Double(firstPose.x, firstPose.y), firstPoint)
            transform.transform(Point2D.Double(secondPose.x, secondPose.y), secondPoint)
            g2d.drawLine(firstPoint.x.roundToInt(), firstPoint.y.roundToInt(), secondPoint.x.roundToInt(), secondPoint.y.roundToInt())
        }
    }
}