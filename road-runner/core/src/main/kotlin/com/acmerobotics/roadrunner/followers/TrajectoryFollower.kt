package com.acmerobotics.roadrunner.followers

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.util.NanoClock

/**
 * Generic [Trajectory] follower for time-based pose reference tracking.
 *
 * @param clock clock
 */
abstract class TrajectoryFollower @JvmOverloads constructor(protected val clock: NanoClock = NanoClock.system()) {
    private var startTimestamp: Double = 0.0
    protected var trajectory: Trajectory = Trajectory()

    /**
     * Follow the given [trajectory].
     */
    fun followTrajectory(trajectory: Trajectory) {
        this.startTimestamp = clock.seconds()
        this.trajectory = trajectory
    }

    /**
     * Returns true if the current trajectory has finished executing.
     */
    fun isFollowing(): Boolean {
        return elapsedTime() <= trajectory.duration()
    }

    /**
     * Returns the elapsed time since the last [followTrajectory] calls.
     */
    protected fun elapsedTime() = clock.seconds() - startTimestamp

    /**
     * Run a single iteration of the trajectory follower.
     *
     * @param currentPose current robot pose
     */
    abstract fun update(currentPose: Pose2d)
}