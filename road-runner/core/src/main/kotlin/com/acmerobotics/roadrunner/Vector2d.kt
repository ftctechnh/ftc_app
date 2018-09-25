package com.acmerobotics.roadrunner

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * Class for representing 2D vectors (x and y).
 */
class Vector2d @JvmOverloads constructor(
        val x: Double = 0.0,
        val y: Double = 0.0
) {
    fun norm() = sqrt(x*x + y*y)

    fun angle() = atan2(y, x)

    operator fun plus(other: Vector2d) = Vector2d(x + other.x, y + other.y)

    operator fun minus(other: Vector2d) = Vector2d(x - other.x, y - other.y)

    operator fun times(scalar: Double) = Vector2d(scalar * x, scalar * y)

    operator fun div(scalar: Double) = Vector2d(x / scalar, y / scalar)

    operator fun unaryMinus() = Vector2d(-x, -y)

    infix fun dot(other: Vector2d) = x * other.x + y * other.y

    infix fun distanceTo(other: Vector2d) = (this - other).norm()

    fun rotated(angle: Double): Vector2d {
        val newX = x * Math.cos(angle) - y * Math.sin(angle)
        val newY = x * Math.sin(angle) + y * Math.cos(angle)
        return Vector2d(newX, newY)
    }

    override fun toString() = String.format("(%.3f, %.3f)", x, y)

    override fun equals(other: Any?): Boolean {
        return if (other is Vector2d) {
            abs(x - other.x) < 1e-4 && abs(y - other.y) < 1e-4
        } else {
            false
        }
    }
}

operator fun Double.times(vector: Vector2d) = vector.times(this)

operator fun Double.div(vector: Vector2d) = vector.div(this)