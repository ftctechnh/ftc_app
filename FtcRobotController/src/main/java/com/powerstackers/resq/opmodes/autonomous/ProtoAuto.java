package com.powerstackers.resq.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Derek on 1/14/2016.
 */
public class ProtoAuto extends LinearOpMode { //no SYNCHRONUS

    double enRightPosition = 0.0;
    double enLeftPosition = 0.0;

    DcMotor motorBrush;
    DcMotor motorLift;
    DcMotor motorFRight;
    DcMotor motorFLeft;
    DcMotor motorBRight;
    DcMotor motorBLeft;

//    /*Color Values
//     *
//     */
//    float hsvValues[] = {0, 0, 0};
//    final float values[] = hsvValues;

    @Override
    public void runOpMode() throws InterruptedException {

        motorBrush = hardwareMap.dcMotor.get("motorBrush");
        motorLift = hardwareMap.dcMotor.get("motorLift");
        motorLift.setDirection(DcMotor.Direction.REVERSE);
        motorFRight = hardwareMap.dcMotor.get("motorFRight");
        motorFLeft = hardwareMap.dcMotor.get("motorFLeft");
        motorFRight.setDirection(DcMotor.Direction.REVERSE);
        motorBRight = hardwareMap.dcMotor.get("motorBRight");
        motorBLeft = hardwareMap.dcMotor.get("motorBLeft");
        motorBRight.setDirection(DcMotor.Direction.REVERSE);


        /*
         * Motors
         */
        motorBRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        motorFRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        waitForStart();

        while (enRightPosition > -4820) {
            enRightPosition = motorFRight.getCurrentPosition();
            motorBrush.setPower(1.0);
            motorFRight.setPower(0.15);
            motorBRight.setPower(0.15);
            motorFLeft.setPower(0.10);
            motorBLeft.setPower(0.10);
            telemetry.addData("EncoderBL", "Value: " + String.valueOf(motorBLeft.getCurrentPosition()));
            telemetry.addData("EncoderBR", "Value: " + String.valueOf(motorBRight.getCurrentPosition()));
            telemetry.addData("EncoderFR", "Value: " + String.valueOf(motorFRight.getCurrentPosition()));
            telemetry.addData("EncoderFL", "Value: " + String.valueOf(motorFLeft.getCurrentPosition()));
            telemetry.addData("motorFRight", "Power: " + String.valueOf(motorFRight.getPower()));

        }

        motorBrush.setPower(0);
        motorBLeft.setPower(0);
        motorBRight.setPower(0);
        motorFLeft.setPower(0);
        motorFRight.setPower(0);

        motorBLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        waitOneFullHardwareCycle();

        while (motorFLeft.getPower() < 1) {
            telemetry.addData("stop", "power: " + String.valueOf(motorBLeft.getPower()));
        }

        stop();

        motorBrush.setPower(0);
        motorBLeft.setPower(0);
        motorBRight.setPower(0);
        motorFLeft.setPower(0);
        motorFRight.setPower(0);
    }
}
