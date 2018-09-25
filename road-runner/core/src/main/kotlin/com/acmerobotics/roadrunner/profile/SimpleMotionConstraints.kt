package com.acmerobotics.roadrunner.profile

/**
 * Constant velocity and acceleration constraints used by [MotionProfileGenerator.generateSimpleMotionProfile].
 */
class SimpleMotionConstraints(
        @JvmField var maximumVelocity: Double,
        @JvmField var maximumAcceleration: Double
) : MotionConstraints {

    override fun maximumVelocity(displacement: Double) = maximumVelocity

    override fun maximumAcceleration(displacement: Double) = maximumAcceleration
}