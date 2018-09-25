package com.acmerobotics.roadrunner.profile

/**
 * Motion profile motion constraints.
 */
interface MotionConstraints {

    /**
     * Returns the maximum velocity [displacement] units along the profile.
     */
    fun maximumVelocity(displacement: Double): Double

    /**
     * Returns the maximum acceleration [displacement] units along the profile.
     */
    fun maximumAcceleration(displacement: Double): Double
}