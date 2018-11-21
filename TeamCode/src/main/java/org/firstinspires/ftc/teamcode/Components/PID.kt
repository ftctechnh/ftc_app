package org.firstinspires.ftc.teamcode.Components

data class PIDCoefficients(val Kp: Double, val Ki: Double, val Kd: Double)

class PID(val pidCoefficients: PIDCoefficients, val desiredVal: Double) {
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

            val P = pidCoefficients.Kp * e
            runningI += pidCoefficients.Ki * e * dt
            val D = pidCoefficients.Kd * de / dt

            return P + runningI + D
        } else {
            throw KotlinNullPointerException("PID controller not initialized!")
        }
    }
}