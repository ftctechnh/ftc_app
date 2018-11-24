package org.firstinspires.ftc.teamcode.Utils

import com.google.gson.JsonObject
import org.firstinspires.ftc.teamcode.Models.PIDConstants
import org.json.JSONObject


public class PIDController(val pidConstants: PIDConstants, val desiredVal: Double,val broadcast:Boolean=false,val wss:WSS? = null) {
    var prevTime: Long? = null
    var prevError: Double? = null
    var runningI: Double = 0.0
    val l:Logger = Logger("PID")


    fun initController(actualVal: Double) {
        prevTime = System.currentTimeMillis()
        prevError = desiredVal - actualVal
        clearFile("pidReadout.txt")
    }

    fun output(actualVal: Double,adjustError:(Double)-> Double = {it}): Double {
        if (prevTime != null && prevError != null) {
            val e: Double = adjustError(desiredVal - actualVal)
            val de = e - prevError!!
            val currentTime = System.currentTimeMillis()
            val dt:Double = (prevTime!! - currentTime).toDouble()/1000

            val P = pidConstants.kP * e
            l.log("${pidConstants.kP} * $e = $P")
            runningI += -pidConstants.kI * e * dt
            println(dt)
            println(runningI)
            val D = -pidConstants.kD * de / dt

//            if(e>0){
//                if(D>0){
//                    //error is positive and slope is positive, moving further away.
//                    D*=-1
//                }else{
//                    //error is pos but moving negative, good. make D positive.
//                }
//            }else{
//                if(D>0){
//                    //error is neg and slope is positive, moving closer to desired.
//                }else{
//                    //error is neg and slope is neg, moving further to desired.
//                    D*=-1
//                }
//            }

            println(D)
            val output = P + runningI + D

            l.lineBreak()
            l.logData("ts",System.currentTimeMillis())
            l.logData("err",e)
            l.logData("de",de)
            l.logData("dt",dt)
            l.logData("p",P)
            l.logData("i",runningI)
            l.logData("d",D)
            l.logData("out",output)
            l.lineBreak()

            prevError = e
            prevTime = currentTime
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
            if(broadcast && wss != null){

                wss.broadcastMessage(message=message)
            }
            writeFile("pidReadout.txt",content=message,overWrite = false)

            return output
        } else {
            throw KotlinNullPointerException("PIDController controller not initialized!")
        }
    }
}