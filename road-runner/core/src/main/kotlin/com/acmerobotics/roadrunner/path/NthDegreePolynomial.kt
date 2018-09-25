package com.acmerobotics.roadrunner.path

import org.apache.commons.math3.linear.LUDecomposition
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import kotlin.math.max

/**
 * Nth-degree polynomial interpolated according to the provided derivatives. Note that this implementation is less
 * performant than [QuinticPolynomial] as it uses general matrix operations for derivative computations.
 *
 * @param start start derivatives starting with the 0th derivative
 * @param end end derivatives starting with the 0th derivative
 */
class NthDegreePolynomial(start: List<Double>, end: List<Double>) {
    private val size = 2 * start.size
    private val coeff: RealMatrix

    init {
        if (start.size != end.size) {
            throw IllegalArgumentException("Unequal number of start and end derivatives")
        }

        val coeffMatrix = MatrixUtils.createRealMatrix(size, size)
        coeffMatrix.setEntry(0, size - 1, 1.0)
        coeffMatrix.setRow(size / 2, (1..size).map { 1.0 }.toDoubleArray())
        for (i in 1 until size / 2) {
            val row = size / 2 + i
            for (col in 0..(size - i)) {
                val coeff = size - i - col
                coeffMatrix.setEntry(row, col, coeff * coeffMatrix.getEntry(row - 1, col))
            }
            coeffMatrix.setEntry(i, size - i - 1, coeffMatrix.getEntry(row, size - i - 1))
        }

        val target = MatrixUtils.createRealMatrix(arrayOf(
                (start.toMutableList() + end.toMutableList()).toDoubleArray()
        )).transpose()

        val solver = LUDecomposition(coeffMatrix).solver
        coeff = solver.solve(target).transpose()
    }

    private fun getBasisMatrix(t: Double): RealMatrix {
        val basisMatrix = MatrixUtils.createRealMatrix(size, 1)
        basisMatrix.setEntry(size - 1, 0,1.0)
        for (row in size - 2 downTo 0) {
            basisMatrix.setEntry(row, 0, t * basisMatrix.getEntry(row + 1, 0))
        }
        return basisMatrix
    }

    private fun getDerivativeCoeffMatrix(n: Int): RealMatrix {
        val coeffMatrix = coeff.copy()
        for (i in 1..n) {
            for (j in 0 until size) {
                val coeff = max(0, size - j - i)
                coeffMatrix.setEntry(0, j, coeff * coeffMatrix.getEntry(0, j))
            }
        }
        return coeffMatrix
    }

    /**
     * Returns the value of the polynomial at [t].
     */
    operator fun get(t: Double) = nthDeriv(0, t)

    /**
     * Returns the derivative of the polynomial at [t].
     */
    fun deriv(t: Double) = nthDeriv(1, t)

    /**
     * Returns the second derivative of the polynomial at [t].
     */
    fun secondDeriv(t: Double) = nthDeriv(2, t)

    /**
     * Returns the third derivative of the polynomial at [t].
     */
    fun thirdDeriv(t: Double) = nthDeriv(3, t)

    private fun nthDeriv(n: Int, t: Double): Double {
        val coeffMatrix = getDerivativeCoeffMatrix(n)
        val basisMatrix = getBasisMatrix(t)
        return coeffMatrix.multiply(basisMatrix).getEntry(0, 0)
    }
}