package com.acmerobotics.roadrunner

import com.acmerobotics.roadrunner.path.NthDegreeSplineSegment
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SplineComparisonTest {
    @Test
    fun testSplines() {
        val startBase = mutableListOf(Vector2d(0.0, 0.0), Vector2d(20.0, -20.0))
        val endBase = mutableListOf(Vector2d(20.0, 20.0), Vector2d(20.0, -20.0))
        for (i in 0..10) {
            val start = startBase + (0 until i).map { Vector2d(0.0, 0.0) }
            val end = endBase + (0 until i).map { Vector2d(0.0, 0.0) }
            val spline = NthDegreeSplineSegment(start, end)
            GraphUtil.saveParametricCurve("splineComparison/$i", spline)
        }
    }
}