package com.acmerobotics.roadrunner.path.heading

import com.acmerobotics.roadrunner.path.ParametricCurve

/**
 * Interpolator for specifying the heading for holonomic paths.
 */
abstract class HeadingInterpolator {
    protected lateinit var parametricCurve: ParametricCurve

    /**
     * Initialize the interpolator with a [parametricCurve].
     *
     *  @param parametricCurve parametric curve
     */
    open fun init(parametricCurve: ParametricCurve) {
        this.parametricCurve = parametricCurve
    }

    /**
     * Returns true if the heading interpolator respects derivative continuity at path segment endpoints. That is,
     * the start and end heading derivatives match those of the [TangentInterpolator].
     *
     * @return true if derivatives match [TangentInterpolator]
     */
    abstract fun respectsDerivativeContinuity(): Boolean

    /**
     * Returns the heading at the specified [displacement].
     */
    abstract operator fun get(displacement: Double): Double

    /**
     * Returns the heading derivative at the specified [displacement].
     */
    abstract fun deriv(displacement: Double): Double

    /**
     * Returns the heading second derivative at the specified [displacement].
     */
    abstract fun secondDeriv(displacement: Double): Double

    /**
     * Returns the start heading.
     */
    fun start() = get(0.0)

    /**
     * Returns the start heading derivative.
     */
    fun startDeriv() = deriv(0.0)

    /**
     * Returns the start heading second derivative.
     */
    fun startSecondDeriv() = secondDeriv(0.0)

    /**
     * Returns the end heading.
     */
    fun end() = get(parametricCurve.length())

    /**
     * Returns the end heading derivative.
     */
    fun endDeriv() = deriv(parametricCurve.length())

    /**
     * Returns the end heading second derivative.
     */
    fun endSecondDeriv() = secondDeriv(parametricCurve.length())
}