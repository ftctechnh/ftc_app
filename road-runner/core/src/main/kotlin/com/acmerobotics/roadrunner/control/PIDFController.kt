package com.acmerobotics.roadrunner.control

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

/**
 * PID controller with various feedforward components. [kV], [kA], and [kStatic] are designed for DC motor feedforward
 * control (the most common kind of feedforward in FTC). [kF] provides a custom feedforward term for other plants.
 *
 * @param pid traditional PID coefficients
 * @param kV feedforward velocity gain
 * @param kA feedforward acceleration gain
 * @param kStatic additive feedforward constant
 * @param kF custom, position-dependent feedforward (e.g., a cos term for arms)
 */
class PIDFController @JvmOverloads constructor(
        val pid: PIDCoefficients,
        val kV: Double = 0.0,
        val kA: Double = 0.0,
        val kStatic: Double = 0.0,
        val kF: (Double) -> Double = { 0.0 }
) {
    private var errorSum: Double = 0.0
    private var lastError: Double = 0.0
    private var lastUpdateTimestamp: Double = Double.NaN

    private var inputBounded: Boolean = false
    private var minInput: Double = 0.0
    private var maxInput: Double = 0.0

    private var outputBounded: Boolean = false
    private var minOutput: Double = 0.0
    private var maxOutput: Double = 0.0

    /**
     * Target position (that is, the controller setpoint)
     */
    var targetPosition: Double = 0.0

    /**
     * Sets bound on the input of the controller. The min and max values are considered modularly-equivalent (that is,
     * the input wraps around).
     *
     * @param min minimum input
     * @param max maximum input
     */
    fun setInputBounds(min: Double, max: Double) {
        if (min < max) {
            inputBounded = true
            minInput = min
            maxInput = max
        }
    }

    /**
     * Sets bounds on the output of the controller.
     *
     * @param min minimum output
     * @param max maximum output
     */
    fun setOutputBounds(min: Double, max: Double) {
        if (min < max) {
            outputBounded = true
            minOutput = min
            maxOutput = max
        }
    }

    private fun getError(position: Double): Double {
        var error = targetPosition - position;
        if (inputBounded) {
            val inputRange = maxInput - minInput;
            while (abs(error) > inputRange / 2.0) {
                error -= sign(error) * inputRange
            }
        }
        return error
    }

    /**
     * Run a single iteration of the controller.
     *
     * @param position current measured position (feedback)
     * @param velocity feedforward velocity
     * @param acceleration feedforward acceleration
     * @param currentTimestamp timestamp for the above parameters (intendend for simulation)
     */
    @JvmOverloads
    fun update(position: Double, velocity: Double = 0.0, acceleration: Double = 0.0, currentTimestamp: Double = System.nanoTime() / 1e9): Double {
        val error = getError(position)
        return if (lastUpdateTimestamp.isNaN()) {
            lastError = error
            lastUpdateTimestamp = currentTimestamp
            0.0
        } else {
            val dt = currentTimestamp - lastUpdateTimestamp
            errorSum += 0.5 * (error + lastError) * dt
            val errorDeriv = (error - lastError) / dt

            lastError = error
            lastUpdateTimestamp = currentTimestamp

            // note: we'd like to refactor this with Kinematics.calculateMotorFeedforward() but kF complicates the
            // determination of the sign of kStatic
            val baseOutput = pid.kP * error + pid.kI * errorSum + pid.kD * (errorDeriv - velocity) + kV * velocity + kA * acceleration + kF(position)
            val output = if (abs(baseOutput) > 1e-4) baseOutput + sign(baseOutput) * kStatic else 0.0

            if (outputBounded) {
                max(minOutput, min(output, maxOutput))
            } else {
                output
            }
        }
    }

    /**
     * Reset the controller's integral sum.
     */
    fun reset() {
        errorSum = 0.0
        lastError = 0.0
        lastUpdateTimestamp = Double.NaN
    }
}
