package org.firstinspires.ftc.teamcode.OpModes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Components.DriveTrain
import org.firstinspires.ftc.teamcode.Components.PIDController
import org.firstinspires.ftc.teamcode.Models.PIDConstants
import org.firstinspires.ftc.teamcode.Utils.*
import java.util.*

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp 2 Wheel")

class  Tele2Wheel: LinearOpMode(){
    val l:Logger = Logger("TELE2WHEEL")
    init{
        l.log("Initialized")
//        val pid:PIDConstants = getPIDConstantsFromFile("pid_rotation.json")
//        telemetry.addData("PID",pid.toString())
//        l.log(pid.toString())
    }
    override fun runOpMode() {
        val dt = DriveTrain(this)
        l.log("waiting for start")
        telemetry.update()
//        clearFile("pidReadout.txt")
        waitForStart()
        l.log("Opmode Started")
//        val r = Random()
        while(opModeIsActive() && !isStopRequested){
//            writeFile("pidReadout.txt","${r.nextDouble()*100}||${System.currentTimeMillis()}\n")
//            wait(500)
            dt.setPowers(
                    gamepad1.left_stick_y.toDouble(),
                    gamepad1.right_stick_y.toDouble()
            );
        }

    }

}
