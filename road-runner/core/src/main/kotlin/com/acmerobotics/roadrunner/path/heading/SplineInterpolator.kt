package com.acmerobotics.roadrunner.path.heading

import com.acmerobotics.roadrunner.path.ParametricCurve
import com.acmerobotics.roadrunner.path.QuinticPolynomial

/**
 * Spline heading interpolator for transitioning smoothly between headings without violating continuity (and hence
 * allowing for integration into longer profiles).
 */
class SplineInterpolator(private val startHeading: Double, private val endHeading: Double) : HeadingInterpolator() {
    private val tangentInterpolator = TangentInterpolator()
    private lateinit var headingSpline: QuinticPolynomial

    override fun init(parametricCurve: ParametricCurve) {
        super.init(parametricCurve)

        tangentInterpolator.init(this.parametricCurve)

        headingSpline = QuinticPolynomial(
                startHeading,
                this.parametricCurve.internalTangentAngleDeriv(0.0),
                this.parametricCurve.internalTangentAngleSecondDeriv(0.0),
                endHeading,
                this.parametricCurve.internalTangentAngleDeriv(1.0),
                this.parametricCurve.internalTangentAngleSecondDeriv(1.0)
        )
    }

    override fun respectsDerivativeContinuity() = true

    override operator fun get(displacement: Double): Double {
        val t = parametricCurve.displacementToParameter(displacement)
        return headingSpline[t]
    }

    override fun deriv(displacement: Double): Double {
        val t = parametricCurve.displacementToParameter(displacement)
        return headingSpline.deriv(t) * parametricCurve.parameterDeriv(t)
    }

    override fun secondDeriv(displacement: Double): Double {
        val t = parametricCurve.displacementToParameter(displacement)
        return headingSpline.secondDeriv(t) * parametricCurve.parameterDeriv(t) * parametricCurve.parameterDeriv(t) +
                headingSpline.deriv(t) * parametricCurve.parameterSecondDeriv(t)
    }

}