package com.acmerobotics.roadrunner.path

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.path.heading.HeadingInterpolator
import com.acmerobotics.roadrunner.path.heading.TangentInterpolator

/**
 * Easy-to-use builder for creating [Path] instances.
 *
 * @param startPose start pose
 */
class PathBuilder(startPose: Pose2d) {
    private var currentPose: Pose2d = startPose
    private var currentReverse = false

    private var parametricCurves = mutableListOf<ParametricCurve>()
    private var interpolators = mutableListOf<HeadingInterpolator>()
    private var reversed = mutableListOf<Boolean>()

    /**
     * Reverse the direction of robot travel.
     */
    // TODO: is there a better solution?
    fun reverse(): PathBuilder {
        currentReverse = !currentReverse
        return this
    }

    /**
     * Sets the robot travel direction.
     */
    fun setReversed(reversed: Boolean): PathBuilder {
        this.currentReverse = reversed
        return this
    }

    /**
     * Adds a line path segment.
     *
     * @param pos end position
     * @param interpolator heading interpolator
     */
    // TODO: add lineToPose()?
    @JvmOverloads
    fun lineTo(pos: Vector2d, interpolator: HeadingInterpolator = TangentInterpolator()): PathBuilder {
        val line = if (currentReverse) {
            LineSegment(pos, currentPose.pos())
        } else {
            LineSegment(currentPose.pos(), pos)
        }

        parametricCurves.add(line)
        interpolators.add(interpolator)
        reversed.add(currentReverse)

        currentPose = Pose2d(pos, currentPose.heading)

        return this
    }

    /**
     * Adds a line straight forward.
     *
     * @param distance distance to travel forward
     */
    fun forward(distance: Double): PathBuilder {
        return lineTo(currentPose.pos() + Vector2d(
                distance * Math.cos(currentPose.heading),
                distance * Math.sin(currentPose.heading)
        ))
    }

    /**
     * Adds a line straight backward.
     *
     * @param distance distance to travel backward
     */
    fun back(distance: Double): PathBuilder {
        return forward(-distance)
    }

    /**
     * Adds a segment that strafes left in the robot reference frame.
     *
     * @param distance distance to strafe left
     */
    fun strafeLeft(distance: Double): PathBuilder {
        return lineTo(currentPose.pos() + Vector2d(
                distance * Math.cos(currentPose.heading + Math.PI / 2),
                distance * Math.sin(currentPose.heading + Math.PI / 2)
        ))
    }

    /**
     * Adds a segment that strafes right in the robot reference frame.
     *
     * @param distance distance to strafe right
     */
    fun strafeRight(distance: Double): PathBuilder {
        return strafeLeft(-distance)
    }

    /**
     * Adds a spline segment.
     *
     * @param pose end pose
     * @param interpolator heading interpolator
     */
    @JvmOverloads
    fun splineTo(pose: Pose2d, interpolator: HeadingInterpolator = TangentInterpolator()): PathBuilder {
        val derivMag = (currentPose.pos() distanceTo pose.pos())
        val spline = if (currentReverse) {
            QuinticSplineSegment(
                    QuinticSplineSegment.Waypoint(pose.x, pose.y, derivMag * Math.cos(pose.heading), derivMag * Math.sin(pose.heading)),
                    QuinticSplineSegment.Waypoint(currentPose.x, currentPose.y, derivMag * Math.cos(currentPose.heading), derivMag * Math.sin(currentPose.heading))
            )
        } else {
            QuinticSplineSegment(
                    QuinticSplineSegment.Waypoint(currentPose.x, currentPose.y, derivMag * Math.cos(currentPose.heading), derivMag * Math.sin(currentPose.heading)),
                    QuinticSplineSegment.Waypoint(pose.x, pose.y, derivMag * Math.cos(pose.heading), derivMag * Math.sin(pose.heading))
            )
        }

        parametricCurves.add(spline)
        interpolators.add(interpolator)
        reversed.add(currentReverse)

        currentPose = pose

        return this
    }

    /**
     * Constructs the [Path] instance.
     */
    fun build() = Path(parametricCurves, interpolators, reversed)
}
