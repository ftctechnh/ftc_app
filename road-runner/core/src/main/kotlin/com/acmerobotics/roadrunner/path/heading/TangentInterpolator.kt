package com.acmerobotics.roadrunner.path.heading

/**
 * Tangent (system) interpolator for tank/differential and other nonholonomic drives.
 */
class TangentInterpolator: HeadingInterpolator() {
    override fun respectsDerivativeContinuity() = true

    override fun get(displacement: Double) = parametricCurve.tangentAngle(displacement)

    override fun deriv(displacement: Double) = parametricCurve.tangentAngleDeriv(displacement)

    override fun secondDeriv(displacement: Double) = parametricCurve.tangentAngleSecondDeriv(displacement)
}