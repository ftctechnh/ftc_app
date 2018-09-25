package com.acmerobotics.roadrunner.path.heading

import com.acmerobotics.roadrunner.path.ParametricCurve
import com.acmerobotics.roadrunner.path.QuinticPolynomial
import kotlin.math.cos
import kotlin.math.sin

private const val K = 0.5  // fraction of a period replaced by a path on either side

/**
 * Heading interpolator that wraps another interpolator and adds sinusoidal oscillations ("wiggles") while preserving
 * continuity. More specifically, the wiggle function is composed of a sine wave with a quintic spline on either end.
 */
class WiggleInterpolator(
        private val amplitude: Double,
        private val desiredPeriod: Double,
        private val baseInterpolator: HeadingInterpolator = TangentInterpolator()
) : HeadingInterpolator() {
    
    private var period: Double = 0.0
    private lateinit var beginSpline: QuinticPolynomial
    private lateinit var endSpline: QuinticPolynomial

    override fun init(parametricCurve: ParametricCurve) {
        super.init(parametricCurve)

        baseInterpolator.init(parametricCurve)

        val n = (parametricCurve.length() / desiredPeriod).toInt()
        period = 1.0 / n

        val t1 = K * period
        val t2 = 1.0 - t1

        beginSpline = QuinticPolynomial(
                0.0,
                0.0,
                0.0,
                internalGet(t1),
                internalDeriv(t1) * (K * period),
                internalSecondDeriv(t1) * (K * K * period * period)
        )

        endSpline = QuinticPolynomial(
                internalGet(t2),
                internalDeriv(t2) * (1 - K * period),
                internalSecondDeriv(t2) * ((1 - K * period) * (1 - K * period)),
                0.0,
                0.0,
                0.0
        )
    }

    override fun respectsDerivativeContinuity() = baseInterpolator.respectsDerivativeContinuity()

    private fun internalGet(t: Double) = amplitude * sin(2.0 * Math.PI * t / period)

    private fun internalDeriv(t: Double) = 2.0 * Math.PI * amplitude / period * cos(2.0 * Math.PI * t / period)

    private fun internalSecondDeriv(t: Double) = 4.0 * Math.PI * Math.PI * amplitude / (period * period) *
            sin(2.0 * Math.PI * t / period)

    override operator fun get(displacement: Double): Double {
        val t = parametricCurve.displacementToParameter(displacement)

        val heading = when {
            t < K * period -> beginSpline[t / (K * period)]
            t > (1.0 - K * period) -> endSpline[t / (1 - K * period) - 1.0]
            else -> internalGet(t)
        }

        return heading + baseInterpolator[displacement]
    }

    override fun deriv(displacement: Double): Double {
        val t = parametricCurve.displacementToParameter(displacement)

        val headingDeriv = when {
            t < K * period -> beginSpline.deriv(t / (K * period)) / (K * period)
            t > (1.0 - K * period) -> endSpline.deriv(t / (1 - K * period) - 1.0) / (1 - K * period)
            else -> internalDeriv(t)
        }

        return headingDeriv * parametricCurve.parameterDeriv(t) + baseInterpolator.deriv(displacement)
    }

    override fun secondDeriv(displacement: Double): Double {
        val t = parametricCurve.displacementToParameter(displacement)

        val headingDeriv = when {
            t < K * period -> beginSpline.deriv(t / (K * period)) / (K * period)
            t > (1.0 - K * period) -> endSpline.deriv(t / (1 - K * period) - 1.0) / (1 - K * period)
            else -> internalDeriv(t)
        }

        val headingSecondDeriv = when {
            t < K * period -> beginSpline.secondDeriv(t / (K * period)) / (K * K * period * period)
            t > (1.0 - K * period) -> endSpline.secondDeriv(t / (1 - K * period) - 1.0) / ((1 - K * period) * (1 - K * period))
            else -> internalSecondDeriv(t)
        }

        return headingSecondDeriv * parametricCurve.parameterDeriv(t) * parametricCurve.parameterDeriv(t) +
                headingDeriv * parametricCurve.parameterSecondDeriv(t) + baseInterpolator.secondDeriv(displacement)
    }
}