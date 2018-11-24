data class PIDCoefficients(val Kp: Double, val Ki: Double, val Kd: Double)

class PID(val pidCoefficients: PIDCoefficients, val desiredVal: Double) {
    var prevTime: Long? = null
    var prevError: Double? = null
    var runningI: Double = 0.0

    fun initController(actualVal: Double) {
        prevTime = System.currentTimeMillis()
        prevError = desiredVal - actualVal
    }

    fun output(actualVal: Double, ws: WebSocketServer): Double {
        fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

        if (prevTime != null && prevError != null) {
            val e: Double = desiredVal - actualVal
            val de = e - prevError!!
            val t = System.currentTimeMillis()
            val dt = prevTime!! - t

            val p = pidCoefficients.Kp * e
            runningI += pidCoefficients.Ki * e * dt
            val d = if (dt == 0.toLong()) 0.0 else pidCoefficients.Kd * de / dt

            prevError = e

            ws.broadcastMessage("[TELEMETRY]:P=$p I=$runningI D=$d")

            return p + runningI + d
        } else {
            throw KotlinNullPointerException("PID controller not initialized!")
        }
    }
}
