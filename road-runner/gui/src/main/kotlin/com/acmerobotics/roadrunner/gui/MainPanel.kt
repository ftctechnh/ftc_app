package com.acmerobotics.roadrunner.gui

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.trajectory.TrajectoryConfig
import com.acmerobotics.roadrunner.trajectory.TrajectoryLoader
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints
import java.io.File
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JTabbedPane

private val DEFAULT_CONSTRAINTS = DriveConstraints(25.0, 40.0, Math.toRadians(180.0), Math.toRadians(360.0))

class MainPanel : JPanel() {

    var onTrajectoryUpdateListener: (() -> Unit)? = null

    private val fieldPanel = FieldPanel()
    private val trajectoryGraphPanel = TrajectoryGraphPanel()
    private val trajectoryInfoPanel = TrajectoryInfoPanel()
    private val poseEditorPanel = PoseEditorPanel()
    private val constraintsPanel = ConstraintsPanel()

    private var poses = listOf<Pose2d>()
    private var constraints = DEFAULT_CONSTRAINTS

    init {
        poseEditorPanel.onPosesUpdateListener = { updateTrajectory(it, constraints) }
        constraintsPanel.onConstraintsUpdateListener = { updateTrajectory(poses, it) }

        constraintsPanel.updateConstraints(DEFAULT_CONSTRAINTS)

        val upperTabbedPane = JTabbedPane()
        upperTabbedPane.addTab("Field", fieldPanel)
        upperTabbedPane.addTab("Trajectory", trajectoryGraphPanel)

        val lowerTabbedPane = JTabbedPane()
        val panel = JPanel()
        panel.add(poseEditorPanel)
        lowerTabbedPane.addTab("Poses", panel)
        lowerTabbedPane.addTab("Constraints", constraintsPanel)

        layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
        add(upperTabbedPane)
        add(trajectoryInfoPanel)
        add(lowerTabbedPane)
    }

    fun updateTrajectory(poses: List<Pose2d>, constraints: DriveConstraints) {
        this.poses = poses
        this.constraints = constraints

        val trajectory = TrajectoryConfig(poses, constraints).toTrajectory()

        fieldPanel.updateTrajectoryAndPoses(trajectory, poses)
        trajectoryInfoPanel.updateTrajectory(trajectory)
        trajectoryGraphPanel.updateTrajectory(trajectory)

        onTrajectoryUpdateListener?.invoke()
    }

    fun clearTrajectory() {
        updateTrajectory(listOf(), DEFAULT_CONSTRAINTS)
    }

    fun save(file: File) {
        TrajectoryLoader.saveConfig(TrajectoryConfig(poses, constraints), file)
    }

    fun load(file: File) {
        val trajectoryConfig = TrajectoryLoader.loadConfig(file)
        updateTrajectory(trajectoryConfig.poses, trajectoryConfig.constraints)
        poseEditorPanel.updatePoses(poses)
        constraintsPanel.updateConstraints(constraints)
    }
}