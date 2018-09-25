package com.acmerobotics.roadrunner.trajectory

import com.acmerobotics.roadrunner.util.Angle
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.profile.MotionConstraints
import com.acmerobotics.roadrunner.profile.MotionProfile
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator
import com.acmerobotics.roadrunner.profile.MotionState
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints

/**
 * Point turn trajectory segment.
 *
 * @param start start pose
 * @param endHeading end heading
 * @param constraints drive constraints
 */
class PointTurn(val start: Pose2d, endHeading: Double, val constraints: DriveConstraints): TrajectorySegment {
    val profile: MotionProfile

    init {
        val ccwTurnAngle = Angle.norm(endHeading - start.heading)
        val turnAngle = if (ccwTurnAngle <= Math.PI) {
            ccwTurnAngle
        } else {
            Angle.norm(start.heading - endHeading)
        }
        val start = MotionState(0.0, 0.0, 0.0)
        val goal = MotionState(turnAngle, 0.0, 0.0)
        profile = MotionProfileGenerator.generateMotionProfile(start, goal, object : MotionConstraints {
            override fun maximumVelocity(displacement: Double) = constraints.maximumAngularVelocity
            override fun maximumAcceleration(displacement: Double) = constraints.maximumAngularAcceleration
        })
    }

    override fun duration() = profile.duration()

    override fun get(time: Double) = Pose2d(start.x, start.y, Angle.norm(start.heading + profile[time].x))

    override fun velocity(time: Double) = Pose2d(0.0, 0.0, profile[time].v)

    override fun acceleration(time: Double) = Pose2d(0.0, 0.0, profile[time].a)

}