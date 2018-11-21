package org.firstinspires.ftc.teamcode.Components

import org.firstinspires.ftc.teamcode.Models.PIDConstants


class PID(val pidConstants: PIDConstants, val desiredVal: Double) {
    var prevTime: Long? = null
    var prevError: Double? = null
    var runningI: Double = 0.0

    fun initController(actualVal: Double) {
        prevTime = System.currentTimeMillis()
        prevError = desiredVal - actualVal
    }

    fun output(actualVal: Double, correctError: (Double) -> Double = { a -> a }): Double {
        if (prevTime != null && prevError != null) {
            val e: Double = desiredVal - actualVal
            val de = e - prevError!!
            val dt = prevTime!! - System.currentTimeMillis()

            val P = pidConstants.Kp * e
            runningI += pidConstants.Ki * e * dt
            val D = pidConstants.Kd * de / dt

            return P + runningI + D
        } else {
            throw KotlinNullPointerException("PID controller not initialized!")
        }
    }
}