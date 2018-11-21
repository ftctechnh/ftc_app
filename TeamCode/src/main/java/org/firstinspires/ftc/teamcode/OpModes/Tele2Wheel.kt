package org.firstinspires.ftc.teamcode.OpModes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Utils.Logger
import org.firstinspires.ftc.teamcode.Components.DriveTrain2

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp 2 Wheel")

class  Tele2Wheel: LinearOpMode(){
    val l:Logger = Logger("TELE2WHEEL")
    init{
        l.log("Initialized")
    }
    override fun runOpMode() {
        val dt = DriveTrain2(this)
        l.log("waiting for start")

        waitForStart()
        l.log("Opmode Started")
        while(opModeIsActive() && !isStopRequested){
            dt.setPowers(
                    gamepad1.left_stick_y.toDouble(),
                    gamepad1.right_stick_y.toDouble()
            );
        }

    }

}