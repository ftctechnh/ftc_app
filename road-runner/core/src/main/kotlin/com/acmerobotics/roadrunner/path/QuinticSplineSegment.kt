package com.acmerobotics.roadrunner.path

import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.util.InterpolatingTreeMap
import java.lang.Math.pow
import kotlin.math.sqrt

private const val LENGTH_SAMPLES = 1000

/**
 * Combination of two quintic polynomials into a 2D quintic spline. See
 * [this short paper](https://github.com/acmerobotics/road-runner/blob/master/doc/pdf/Quintic_Splines_for_FTC.pdf) for
 * some motivation and implementation details.
 *
 * @param start start waypoint
 * @param end end waypoint
 */
class QuinticSplineSegment(start: Waypoint, end: Waypoint) : ParametricCurve() {
    val x: QuinticPolynomial = QuinticPolynomial(start.x, start.dx, start.d2x, end.x, end.dx, end.d2x)
    val y: QuinticPolynomial = QuinticPolynomial(start.y, start.dy, start.d2y, end.y, end.dy, end.d2y)

    /**
     * Class for representing the end points of interpolated quintic splines.
     */
    class Waypoint @JvmOverloads constructor(
            val x: Double,
            val y: Double,
            val dx: Double = 0.0,
            val dy: Double = 0.0,
            val d2x: Double = 0.0,
            val d2y: Double = 0.0
    ) {
        fun pos() = Vector2d(x, y)

        fun deriv() = Vector2d(dx, dy)

        fun secondDeriv() = Vector2d(d2x, d2y)
    }

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

    override fun toString() = "($x,$y)"
}