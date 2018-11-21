package org.firstinspires.ftc.teamcode.Components

import org.firstinspires.ftc.teamcode.Components.Motor

public class MotorGroup(){
    var motors:ArrayList<Motor> = arrayListOf()


    fun addMotor(motor: Motor){
        motors.add(motor)
    }

    fun removeMotor(motor: Motor){
        motors.remove(motor)
    }

    fun useEncoders(){
        motors.forEach { useEncoders() }
    }

    fun setPower(power:Double){
        motors.forEach { setPower(power) }
    }

    fun logInfo(){
        motors.forEach { logInfo() }
    }

    fun prepareEncoderDrive(){
        motors.forEach { prepareEncoderDrive() }
    }

    fun stop(coast:Boolean=false){
        motors.forEach { stop(coast=coast) }
    }
}