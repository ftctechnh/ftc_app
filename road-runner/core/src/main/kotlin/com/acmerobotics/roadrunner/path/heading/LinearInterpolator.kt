package com.acmerobotics.roadrunner.path.heading

/**
 * Linear heading interpolator for time-optimal transitions between poses.
 */
class LinearInterpolator(private val startHeading: Double, endHeading: Double) : HeadingInterpolator() {
    private val turnAngle: Double = if (endHeading >= startHeading) {
        endHeading - startHeading
    } else {
        2 * Math.PI - endHeading + startHeading
    }

    override fun respectsDerivativeContinuity() = false

    override fun get(displacement: Double) = (startHeading + displacement / parametricCurve.length() * turnAngle) % (2 * Math.PI)

    override fun deriv(displacement: Double) = turnAngle / parametricCurve.length()

    override fun secondDeriv(displacement: Double) = 0.0
}