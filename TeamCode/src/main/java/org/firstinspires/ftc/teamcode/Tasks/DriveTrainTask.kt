package org.firstinspires.ftc.teamcode.Tasks

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Components.DriveTrain
import org.firstinspires.ftc.teamcode.Models.Task

public class DriveTrainTask(opMode:LinearOpMode, taskName:String): Task(opMode,taskName){
    var dt = DriveTrain(opMode)

    override fun run(){
        runWhileActive(this::main)
    }

    fun main(){
        dt.setPowers(
                opMode.gamepad1.left_stick_y.toDouble(),
                opMode.gamepad1.right_stick_y.toDouble()
        );    }

}