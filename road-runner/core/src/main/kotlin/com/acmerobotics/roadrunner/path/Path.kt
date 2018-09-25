package com.acmerobotics.roadrunner.path

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.path.heading.HeadingInterpolator
import com.acmerobotics.roadrunner.path.heading.TangentInterpolator
import org.apache.commons.math3.exception.ConvergenceException
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import org.apache.commons.math3.linear.RealVector


/**
 * Path composed of a parametric curve and a heading interpolator.
 *
 * @param parametricCurve parametric curve
 * @param interpolator heading interpolator
 * @param reversed whether or not to travel along the path in reverse
 */
class Path @JvmOverloads constructor(
        val parametricCurves: List<ParametricCurve>,
        val interpolators: List<HeadingInterpolator> = parametricCurves.map { TangentInterpolator() },
        val reversed: List<Boolean> = parametricCurves.map { false }
) {
    constructor(
            parametricCurve: ParametricCurve,
            interpolator: HeadingInterpolator = TangentInterpolator(),
            reversed: Boolean = false
    ) : this(listOf(parametricCurve), listOf(interpolator), listOf(reversed))

    class ProjectionResult(val displacement: Double, val distance: Double)

    init {
        interpolators.zip(parametricCurves).forEach { it.first.init(it.second) }
    }

    /**
     * Returns the length of the path.
     */
    fun length() = parametricCurves.sumByDouble { it.length() }

    /**
     * Returns the pose [displacement] units along the path.
     */
    operator fun get(displacement: Double): Pose2d {
        var remainingDisplacement = displacement
        for (i in parametricCurves.indices) {
            val parametricCurve = parametricCurves[i]
            if (remainingDisplacement <= parametricCurve.length()) {
                return segmentGet(i, remainingDisplacement)
            }
            remainingDisplacement -= parametricCurve.length()
        }
        val finalVector = parametricCurves.lastOrNull()?.end() ?: return Pose2d()
        return Pose2d(finalVector, interpolators.last().end())
    }

    /**
     * Returns the pose derivative [displacement] units along the path.
     */
    fun deriv(displacement: Double): Pose2d {
        var remainingDisplacement = displacement
        for (i in parametricCurves.indices) {
            val parametricCurve = parametricCurves[i]
            if (remainingDisplacement <= parametricCurve.length()) {
                return segmentDeriv(i, remainingDisplacement)
            }
            remainingDisplacement -= parametricCurve.length()
        }
        val finalVector = parametricCurves.lastOrNull()?.end() ?: return Pose2d()
        return Pose2d(finalVector, interpolators.last().end())
    }

    /**
     * Returns the pose second derivative [displacement] units along the path.
     */
    fun secondDeriv(displacement: Double): Pose2d {
        var remainingDisplacement = displacement
        for (i in parametricCurves.indices) {
            val parametricCurve = parametricCurves[i]
            if (remainingDisplacement <= parametricCurve.length()) {
                return segmentSecondDeriv(i, remainingDisplacement)
            }
            remainingDisplacement -= parametricCurve.length()
        }
        val finalVector = parametricCurves.lastOrNull()?.end() ?: return Pose2d()
        return Pose2d(finalVector, interpolators.last().end())
    }

    /**
     * Returns the pose [displacement] units along the path.
     */
    private fun segmentGet(i: Int, displacement: Double): Pose2d {
        val parametricCurve = parametricCurves[i]
        val interpolator = interpolators[i]
        val reversed = reversed[i]
        val point = if (reversed) {
            parametricCurve[length() - displacement]
        } else {
            parametricCurve[displacement]
        }
        val heading = if (reversed) {
            interpolator[length() - displacement]
        } else {
            interpolator[displacement]
        }
        return Pose2d(point.x, point.y, heading)
    }

    /**
     * Returns the pose derivative [displacement] units along the path.
     */
    private fun segmentDeriv(i: Int, displacement: Double): Pose2d {
        val parametricCurve = parametricCurves[i]
        val interpolator = interpolators[i]
        val reversed = reversed[i]
        val deriv = if (reversed) {
            -parametricCurve.deriv(length() - displacement)
        } else {
            parametricCurve.deriv(displacement)
        }
        val headingDeriv = if (reversed) {
            -interpolator.deriv(length() - displacement)
        } else {
            interpolator.deriv(displacement)
        }
        return Pose2d(deriv.x, deriv.y, headingDeriv)
    }

    /**
     * Returns the pose second derivative [displacement] units along the path.
     */
    private fun segmentSecondDeriv(i: Int, displacement: Double): Pose2d {
        val parametricCurve = parametricCurves[i]
        val interpolator = interpolators[i]
        val reversed = reversed[i]
        val secondDeriv = if (reversed) {
            parametricCurve.secondDeriv(length() - displacement)
        } else {
            parametricCurve.secondDeriv(displacement)
        }
        val headingSecondDeriv = if (reversed) {
            interpolator.secondDeriv(length() - displacement)
        } else {
            interpolator.secondDeriv(displacement)
        }
        return Pose2d(secondDeriv.x, secondDeriv.y, headingSecondDeriv)
    }

    /**
     * Project [point] onto the current path.
     *
     * @param point query point
     * @param projectGuess guess for the projected point's displacement along the path
     */
    fun project(point: Vector2d, projectGuess: Double = length() / 2.0): ProjectionResult {
        val problem = LeastSquaresBuilder()
                .start(doubleArrayOf(projectGuess))
                .model {
                    val pathPoint = this[it.getEntry(0)].pos()
                    val pathDerivative = deriv(it.getEntry(0)).pos()

                    val diff = pathPoint - point
                    val distance = diff.norm()

                    val value = ArrayRealVector(doubleArrayOf(distance))

                    val derivative = (diff.x * pathDerivative.x + diff.y * pathDerivative.y) / distance
                    val jacobian = MatrixUtils.createRealMatrix(arrayOf(doubleArrayOf(derivative)))

                    org.apache.commons.math3.util.Pair<RealVector, RealMatrix>(value, jacobian)
                }
                .target(doubleArrayOf(0.0))
                .lazyEvaluation(false)
                .maxEvaluations(1000)
                .maxIterations(1000)
                .build()

        val displacement = try {
            val optimum = LevenbergMarquardtOptimizer().optimize(problem)
            optimum.point.getEntry(0)
        } catch (e: ConvergenceException) {
            0.0
        }
        val optimumPoint = this[displacement].pos()

        return ProjectionResult(displacement, point distanceTo optimumPoint)
    }

    /**
     * Returns the start pose.
     */
    fun start() = get(0.0)

    /**
     * Returns the start pose derivative.
     */
    fun startDeriv() = deriv(0.0)

    /**
     * Returns the start pose second derivative.
     */
    fun startSecondDeriv() = secondDeriv(0.0)

    /**
     * Returns the end pose.
     */
    fun end() = get(length())

    /**
     * Returns the end pose derivative.
     */
    fun endDeriv() = deriv(length())

    /**
     * Returns the end pose second derivative.
     */
    fun endSecondDeriv() = secondDeriv(length())
}