package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class FreeDriveMode extends LinearOpMode {
    private DcMotor left;
    private DcMotor right;

    @Override
    public void runOpMode(){

        left = hardwareMap.get(DcMotor.class, "leftMotor");
        right = hardwareMap.get(DcMotor.class, "rightMotor");

        telemetry.addData("Status", "Meep! Initialized!");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()){
            float leftPower = gamepad1.left_stick_y;
            float rightPower = gamepad1.right_stick_y;
            left.setPower(leftPower);
            right.setPower(rightPower);
            telemetry.addData("Status", "Left: "+leftPower+", Right: "+rightPower);
            telemetry.update();
        }
    }
}
