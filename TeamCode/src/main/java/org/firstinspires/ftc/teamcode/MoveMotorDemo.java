package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name="SANFORD DRIVE", group="__Sanford")
public class MoveMotorDemo extends LinearOpMode {

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;

    @Override
    public void runOpMode() {
        leftFront = hardwareMap.dcMotor.get("left_front");
        rightFront = hardwareMap.dcMotor.get("right_front");
        leftBack = hardwareMap.dcMotor.get("left_back");
        rightBack = hardwareMap.dcMotor.get("right_back");

        waitForStart();

        while (opModeIsActive()) {
            leftFront.setPower(gamepad1.left_stick_x);
            leftBack.setPower(gamepad1.left_stick_y);
            rightFront.setPower(gamepad1.right_stick_x);
            rightBack.setPower(gamepad1.right_stick_y);
        }
    }
}