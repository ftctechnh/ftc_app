package com.acmerobotics.roadrunner

import com.acmerobotics.roadrunner.path.ParametricCurve
import com.acmerobotics.roadrunner.path.Path
import java.io.File

object CSVUtil {
    const val CSV_DIR = "./csv/"

    fun saveCurve(name: String, parametricCurve: ParametricCurve, resolution: Int = 1000) {
        File(CSV_DIR).mkdirs()

        File("$CSV_DIR$name.csv").printWriter().use { out ->
            out.println("t,x,y,dx,dy,d2x,d2y")
            val dx = parametricCurve.length() / resolution
            (0..resolution)
                .map { it * dx }
                .forEach {
                    val pos = parametricCurve[it]
                    val deriv = parametricCurve.deriv(it)
                    val secondDeriv = parametricCurve.secondDeriv(it)
                    out.println("$it,${pos.x},${pos.y},${deriv.x},${deriv.y},${secondDeriv.x},${secondDeriv.y}")
                }
        }
    }

    fun savePath(name: String, path: Path, resolution: Int = 1000) {
        File(CSV_DIR).mkdirs()

        File("$CSV_DIR$name.csv").printWriter().use { out ->
            out.println("t,x,y,heading,dx,dy,omega,d2x,d2y,alpha")
            val dx = path.length() / resolution
            (0..resolution)
                    .map { it * dx }
                    .forEach {
                        val pos = path[it]
                        val deriv = path.deriv(it)
                        val secondDeriv = path.secondDeriv(it)
                        out.println("$it,${pos.x},${pos.y},${pos.heading},${deriv.x},${deriv.y},${deriv.heading},${secondDeriv.x},${secondDeriv.y},${secondDeriv.heading}")
                    }
        }
    }
}