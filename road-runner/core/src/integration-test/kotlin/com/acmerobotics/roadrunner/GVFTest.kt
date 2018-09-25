package com.acmerobotics.roadrunner

import com.acmerobotics.roadrunner.followers.GuidingVectorField
import com.acmerobotics.roadrunner.path.Path
import com.acmerobotics.roadrunner.path.QuinticSplineSegment
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import kotlin.math.roundToInt

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GVFTest {
    @Test
    fun testGVF() {
        val spline = QuinticSplineSegment(
                QuinticSplineSegment.Waypoint(0.0, 0.0, 20.0, 20.0),
                QuinticSplineSegment.Waypoint(30.0, 15.0, -30.0, 10.0)
        )
        println(spline)
        val path = Path(spline)
        val gvf = GuidingVectorField(path, 0.5)

        val startX = -2.0
        val startY = -2.0
//        val endX = 32.0
//        val endY = 18.0
        val endX = 45.0
        val endY = 30.0
        val dx = 1.0
        val dy = 1.0

        File(CSVUtil.CSV_DIR).mkdirs()

        File("${CSVUtil.CSV_DIR}gvf.csv").printWriter().use { out ->
            val xres = ((endX - startX) / dx).roundToInt()
            val yres = ((endY - startY) / dy).roundToInt()
            for (i in 0..xres) {
                val x = startX + i * dx
                for (j in 0..yres) {
                    val y = startY + j * dy
                    val v = gvf[x, y]
                    out.println("$x,$y,${v.x},${v.y}")
                }
            }
        }
    }
}