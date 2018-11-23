package org.firstinspires.ftc.teamcode.Utils

import com.google.gson.JsonObject
import org.firstinspires.ftc.teamcode.Models.PIDConstants
import org.json.JSONObject


class PIDController(val pidConstants: PIDConstants, val desiredVal: Double,val broadcast:Boolean=false,val port:Int=8080,val path:String="pid") {
    var prevTime: Long? = null
    var prevError: Double? = null
    var runningI: Double = 0.0
    var wss:WSS? = null
    init{
        if(broadcast){
            wss = WSS(port=port,path=path)

        }
    }

    fun initController(actualVal: Double) {
        prevTime = System.currentTimeMillis()
        prevError = desiredVal - actualVal
        if(broadcast && wss != null){
            wss!!.start()
        }
    }

    fun output(actualVal: Double, correctError: (Double) -> Double = { a -> a }): Double {
        if (prevTime != null && prevError != null) {
            val e: Double = desiredVal - actualVal
            val de = e - prevError!!
            val dt = prevTime!! - System.currentTimeMillis()

            val P = pidConstants.kP * e
            runningI += pidConstants.kI * e * dt
            val D = pidConstants.kD * de / dt
            val output = P + runningI + D

            if(broadcast && wss != null){
                val message =  JSONObject()
                        .put("ts",System.currentTimeMillis())
                        .put("error",e)
                        .put("de",de)
                        .put("dt",dt)
                        .put("p",P)
                        .put("i",runningI)
                        .put("d",D)
                        .put("output",output)
                        .toString()
                wss!!.broadcastMessage(message=message)
            }
            return output
        } else {
            throw KotlinNullPointerException("PIDController controller not initialized!")
        }
    }
}