package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by BooAsh99 on 10/12/2015.
 */
public class MotorMove extends LinearOpMode {
    final static double MOTOR_POWER = 0.15;
    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor winch;
    DcMotor winchpivot;

    @Override
    public void runOpMode() throws InterruptedException {
        motorRight = hardwareMap.dcMotor.get("right");
        motorLeft = hardwareMap.dcMotor.get("left");
        winch = hardwareMap.dcMotor.get("winch");
        winchpivot = hardwareMap.dcMotor.get("winchpivot");

        while(true) {
            telemetry.addData("right", gamepad1.right_stick_y);
            telemetry.addData("left", gamepad1.left_stick_y);

            motorRight.setPower(-gamepad1.right_stick_y);
            motorLeft.setPower(gamepad1.left_stick_y);

            if (gamepad1.dpad_down) {
                winchpivot.setPower(.9);
            } else if (gamepad1.dpad_up) {
                winchpivot.setPower(-.9);
            } else {
                winchpivot.setPower(0);
            }

            if (gamepad1.a) {
                winch.setPower(.9);
            } else if (gamepad1.y) {
                winch.setPower(-.9);
            } else {
                winch.setPower(0);
            }
        }
    }
}

