package com.acmerobotics.roadrunner.trajectory

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.path.Path
import com.acmerobotics.roadrunner.profile.MotionConstraints
import com.acmerobotics.roadrunner.profile.MotionProfile
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator
import com.acmerobotics.roadrunner.profile.MotionState
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryConstraints
import kotlin.math.max
import kotlin.math.min

/**
 * Trajectory segment backed by a list of [Path] objects.
 *
 * @param paths paths
 * @param trajectoryConstraintsList motion constraints for each respective path
 * @param resolution resolution used for the motion profile (see [MotionProfileGenerator.generateMotionProfile])
 */
class PathTrajectorySegment @JvmOverloads constructor(
        val paths: List<Path> = listOf(),
        val trajectoryConstraintsList: List<TrajectoryConstraints> = listOf(),
        resolution: Int = 250
) : TrajectorySegment {
    @JvmOverloads constructor(
            path: Path,
            trajectoryConstraints: TrajectoryConstraints,
            resolution: Int = 250
    ) : this(listOf(path), listOf(trajectoryConstraints), resolution)

    val profile: MotionProfile

    init {
        val length = paths.sumByDouble { it.length() }
        val compositeConstraints = object : MotionConstraints {
            override fun maximumVelocity(displacement: Double): Double {
                var remainingDisplacement = max(0.0, min(displacement, length))
                for ((path, motionConstraints) in paths.zip(trajectoryConstraintsList)) {
                    if (remainingDisplacement <= path.length()) {
                        return motionConstraints.maximumVelocity(
                            path[remainingDisplacement],
                            path.deriv(remainingDisplacement),
                            path.secondDeriv(remainingDisplacement)
                        )
                    }
                    remainingDisplacement -= path.length()
                }
                return trajectoryConstraintsList.last()
                    .maximumVelocity(paths.last().end(), paths.last().endDeriv(), paths.last().endSecondDeriv())
            }

            override fun maximumAcceleration(displacement: Double): Double {
                var remainingDisplacement = max(0.0, min(displacement, length))
                for ((path, motionConstraints) in paths.zip(trajectoryConstraintsList)) {
                    if (remainingDisplacement <= path.length()) {
                        return motionConstraints.maximumAcceleration(
                            path[remainingDisplacement],
                            path.deriv(remainingDisplacement),
                            path.secondDeriv(remainingDisplacement)
                        )
                    }
                    remainingDisplacement -= path.length()
                }
                return trajectoryConstraintsList.last()
                    .maximumAcceleration(paths.last().end(), paths.last().endDeriv(), paths.last().endSecondDeriv())
            }
        }

        val start = MotionState(0.0, 0.0, 0.0)
        val goal = MotionState(length, 0.0, 0.0)
        profile = MotionProfileGenerator.generateMotionProfile(start, goal, compositeConstraints, resolution)
    }

    override fun duration() = profile.duration()

    override operator fun get(time: Double): Pose2d {
        var remainingDisplacement = profile[time].x
        for (path in paths) {
            if (remainingDisplacement <= path.length()) {
                return path[remainingDisplacement]
            }
            remainingDisplacement -= path.length()
        }
        return paths.lastOrNull()?.end() ?: Pose2d()
    }

    override fun velocity(time: Double): Pose2d {
        val motionState = profile[time]
        var remainingDisplacement = motionState.x
        for (path in paths) {
            if (remainingDisplacement <= path.length()) {
                return path.deriv(remainingDisplacement) * motionState.v
            }
            remainingDisplacement -= path.length()
        }
        return (paths.lastOrNull()?.endDeriv() ?: Pose2d()) * profile.end().v
    }

    override fun acceleration(time: Double): Pose2d {
        val motionState = profile[time]
        var remainingDisplacement = motionState.x
        for (path in paths) {
            if (remainingDisplacement <= path.length()) {
                return path.secondDeriv(remainingDisplacement) * motionState.v * motionState.v +
                        path.deriv(remainingDisplacement) * motionState.a
            }
            remainingDisplacement -= path.length()
        }
        return (paths.lastOrNull()?.endSecondDeriv() ?: Pose2d()) * profile.end().v * profile.end().v +
                (paths.lastOrNull()?.endDeriv() ?: Pose2d()) * profile.end().a
    }
}