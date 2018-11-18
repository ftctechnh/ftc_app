package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "RoverRuckesTeleOp")
public class RoverRuckesTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        AnimatornicsRobot robot = new AnimatornicsRobot(hardwareMap, telemetry);

        waitForStart();

        while (opModeIsActive()) {
            robot.manualDrive(this);

            telemetry.update();
        }
    }
}
