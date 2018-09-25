package com.acmerobotics.roadrunner.control

/**
 * This class stores the proportional, integral, and derivative gains used by [PIDFController].
 *
 * @param kP proportional gain
 * @param kI integral gain
 * @param kD derivative gain
 */
class PIDCoefficients(
        @JvmField var kP: Double = 0.0,
        @JvmField var kI: Double = 0.0,
        @JvmField var kD: Double = 0.0
)