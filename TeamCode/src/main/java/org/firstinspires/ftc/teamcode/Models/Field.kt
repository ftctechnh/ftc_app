package org.firstinspires.ftc.teamcode.Models

import org.firstinspires.ftc.teamcode.Components.DriveTrain
import org.firstinspires.ftc.teamcode.Utils.Logger
import org.firstinspires.ftc.teamcode.Utils.wait
import org.json.JSONObject
import java.lang.RuntimeException


class Field(val dt:DriveTrain){
    val l = Logger("FIELD")
    val fieldX:Double = 288.0
    val fieldY:Double = 288.0
    var currentAngle = 0.0
    var currentX = 0.0
    var currentY = 0.0

    fun isOutOfBounds(x:Double,y:Double) = (x > fieldX || x < 0 || y > fieldY || y < 0)

    fun registerPosition(x:Double,y:Double,angle:Double){
        if(isOutOfBounds(x,y)){
            l.log("($currentX,$currentY) is out of bounds!")
            throw RuntimeException("($currentX,$currentY) is out of bounds")
        }
        this.currentX = Math.max(Math.min(x,fieldX),0.0)
        this.currentY = Math.max(Math.min(y,fieldY),0.0)
        this.currentAngle = angle
        if(this.currentX != x) l.log("Wrapped X value from $x => $currentX")
        if(this.currentY != y) l.log("Wrapped Y value from $y => $currentY")
        l.log("Registered position: ($currentX,$currentY)")
    }

    fun moveTo(x:Double,y:Double){
        l.log("moveTo($x,$y) called")
        val angle = Math.toDegrees(Math.atan((y-currentY)/(x-currentX)))
        val dist = Math.sqrt((x-currentX))*((x-currentX) + (y-currentY))*((y-currentY))
        l.logData("Calculated angle",angle)
        l.logData("Calculated distance",dist)
        dt.rotateTo(angle)
        wait(500)
        l.logData("Actual angle",dt.imu.angle)
        dt.drive(Direction.FORWARD,dist,timeout=10)
        wait(500)
        l.logData("Final angle",dt.imu.angle)
        registerPosition(x,y,dt.imu.angle)
    }

    fun getPositionJson() = JSONObject().put("x",currentX).put("y",currentY).put("angle",currentAngle)
    fun getPositionString() = getPositionJson().toString()















}