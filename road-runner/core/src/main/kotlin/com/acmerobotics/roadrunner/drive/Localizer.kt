package com.acmerobotics.roadrunner.drive

import com.acmerobotics.roadrunner.Pose2d

/**
 * Generic abstraction for estimating robot pose over time.
 */
interface Localizer {

    /**
     * Current robot pose estimate.
     */
    var poseEstimate: Pose2d

    /**
     * Completes a single localization update.
     */
    fun update()
}