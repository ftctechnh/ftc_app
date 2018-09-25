package com.acmerobotics.roadrunner.path

import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.util.InterpolatingTreeMap
import java.lang.Math.pow
import kotlin.math.sqrt

private const val LENGTH_SAMPLES = 1000

/**
 * Combination of two quintic polynomials into a 2D spline.
 *
 * @param start start derivatives starting with the 0th derivative
 * @param end end derivatives starting with the 0th derivative
 */
// TODO: generic base class for numerical arc length parametrization
class NthDegreeSplineSegment(start: List<Vector2d>, end: List<Vector2d>) : ParametricCurve() {
    val x: NthDegreePolynomial = NthDegreePolynomial(start.map(Vector2d::x), end.map(Vector2d::x))
    val y: NthDegreePolynomial = NthDegreePolynomial(start.map(Vector2d::y), end.map(Vector2d::y))

    private val length: Double

    private val arcLengthSamples = InterpolatingTreeMap()

    init {
        arcLengthSamples[0.0] = 0.0
        val dx = 1.0 / LENGTH_SAMPLES
        var sum = 0.0
        var lastIntegrand = 0.0
        for (i in 1..LENGTH_SAMPLES) {
            val t = i * dx
            val deriv = internalDeriv(t)
            val integrand = sqrt(deriv.x * deriv.x + deriv.y * deriv.y) * dx
            sum += (integrand + lastIntegrand) / 2.0
            lastIntegrand = integrand

            arcLengthSamples[sum] = t
        }
        length = sum
    }

    override fun internalGet(t: Double) = Vector2d(x[t], y[t])

    override fun internalDeriv(t: Double) = Vector2d(x.deriv(t), y.deriv(t))

    override fun internalSecondDeriv(t: Double) = Vector2d(x.secondDeriv(t), y.secondDeriv(t))

    override fun internalThirdDeriv(t: Double) = Vector2d(x.thirdDeriv(t), y.thirdDeriv(t))

    override fun displacementToParameter(displacement: Double) = arcLengthSamples.getInterpolated(displacement) ?: 0.0

    override fun parameterDeriv(t: Double): Double {
        val deriv = internalDeriv(t)
        return 1.0 / sqrt(deriv.x * deriv.x + deriv.y * deriv.y)
    }

    override fun parameterSecondDeriv(t: Double): Double {
        val deriv = internalDeriv(t)
        val secondDeriv = internalSecondDeriv(t)
        val numerator = -(deriv.x * secondDeriv.x + deriv.y * secondDeriv.y)
        val denominator = deriv.x * deriv.x + deriv.y * deriv.y
        return numerator / (denominator * denominator)
    }

    override fun parameterThirdDeriv(t: Double): Double {
        val deriv = internalDeriv(t)
        val secondDeriv = internalSecondDeriv(t)
        val thirdDeriv = internalThirdDeriv(t)
        val firstNumerator = -(deriv.x * secondDeriv.x + deriv.y * secondDeriv.y)
        val secondNumeratorFirstTerm = secondDeriv.x * secondDeriv.x + secondDeriv.y * secondDeriv.y +
                deriv.x * thirdDeriv.x + deriv.y * thirdDeriv.y
        val secondNumeratorSecondTerm = -4.0 * firstNumerator
        val denominator = deriv.x * deriv.x + deriv.y * deriv.y
        return (secondNumeratorFirstTerm / pow(denominator, 2.5) +
                secondNumeratorSecondTerm / pow(denominator, 3.5))
    }

    override fun length() = length
}