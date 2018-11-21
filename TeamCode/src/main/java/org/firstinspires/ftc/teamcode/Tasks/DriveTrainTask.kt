package org.firstinspires.ftc.teamcode.Tasks

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Components.DriveTrain
import org.firstinspires.ftc.teamcode.Models.Task

public class DriveTrainTask(opMode:LinearOpMode, taskName:String): Task(opMode,taskName){
    var dt = DriveTrain(opMode)

    override fun run(){
        runWhileActive(this::main)
    }

    fun joyToPower(joy:Double):Double{
        return(Math.pow(joy,3.0))
    }
    fun main(){
        dt.setPowers(
                joyToPower(opMode.gamepad1.left_stick_y.toDouble()),
                joyToPower(opMode.gamepad1.left_stick_y.toDouble())
        );
    }

}