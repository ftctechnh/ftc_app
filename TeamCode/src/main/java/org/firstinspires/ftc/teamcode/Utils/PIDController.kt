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

    fun output(actualVal: Double): Double {
        if (prevTime != null && prevError != null) {
            val e: Double = desiredVal - actualVal
            val de = e - prevError!!
            val dt:Double = (prevTime!! - System.currentTimeMillis()).toDouble()/1000

            val P = pidConstants.kP * e
            runningI += -pidConstants.kI * e * dt
            println(dt)
            println(runningI)
            var D = pidConstants.kD * de / dt

            if(e>0){
                if(D>0){
                    //error is positive and slope is positive, moving further away.
                    D*=-1
                }else{
                    //error is pos but moving negative, good. make D positive.
                }
            }else{
                if(D>0){
                    //error is neg and slope is positive, moving closer to desired.
                }else{
                    //error is neg and slope is neg, moving further to desired.
                    D*=-1
                }
            }

            println(D)
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