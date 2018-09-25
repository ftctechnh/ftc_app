package com.acmerobotics.roadrunner.gui

import com.acmerobotics.roadrunner.trajectory.Trajectory
import java.awt.GridLayout
import javax.swing.JLabel
import javax.swing.JPanel

class TrajectoryInfoPanel : JPanel() {
    private val durationLabel: JLabel

    init {
        val panel = JPanel()
        panel.layout = GridLayout(0, 1, 5, 5)

        durationLabel = JLabel("Duration: 0.00s")
        panel.add(durationLabel)

        add(panel)
    }

    fun updateTrajectory(trajectory: Trajectory) {
        durationLabel.text = if (trajectory.duration().isInfinite() || trajectory.duration().isNaN()) {
            "Duration: --"
        } else {
            String.format("Duration: %.2fs", trajectory.duration())
        }
    }
}