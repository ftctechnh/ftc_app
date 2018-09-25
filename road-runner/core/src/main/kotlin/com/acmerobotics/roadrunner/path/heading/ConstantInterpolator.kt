package com.acmerobotics.roadrunner.path.heading

/**
 * Constant heading interpolator used for arbitrary holonomic translations.
 */
class ConstantInterpolator(val heading: Double) : HeadingInterpolator() {
    override fun respectsDerivativeContinuity() = false

    override fun get(displacement: Double) = heading

    override fun deriv(displacement: Double) = 0.0

    override fun secondDeriv(displacement: Double) = 0.0

}