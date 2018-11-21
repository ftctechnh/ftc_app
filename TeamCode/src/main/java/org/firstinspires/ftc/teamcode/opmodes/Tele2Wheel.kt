package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Utils.Logger
import org.firstinspires.ftc.teamcode.Utils.wait
import org.firstinspires.ftc.teamcode.robotutil.DriveTrain2
import org.firstinspires.ftc.teamcode.robotutil.DriveTrainNew

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "teleop 2 wheel")

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