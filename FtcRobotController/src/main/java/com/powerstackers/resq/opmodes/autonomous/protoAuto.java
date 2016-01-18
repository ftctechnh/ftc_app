package com.powerstackers.resq.opmodes.autonomous;

import com.powerstackers.resq.common.RobotAuto;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Derek on 1/14/2016.
 */
public class protoAuto extends LinearOpMode {


    DcMotor motorBrush;
    DcMotor motorLift;
    DcMotor motorFRight;
    DcMotor motorFLeft;
    DcMotor motorBRight;
    DcMotor motorBLeft;

    /*Color Values
     *
     */
    float hsvValues[] = {0, 0, 0};
    final float values[] = hsvValues;

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
        motorBLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        waitForStart();

        while (enRightPosition > -4500) {
            RobotAuto.enRightPosition = motorFRight.getCurrentPosition();
            motorBrush.setPower(1);
            motorFRight.setPower(1);
            motorBRight.setPower(1);
            motorFLeft.setPower(1);
            motorBLeft.setPower(1);
            telemetry.addData("EncoderBL", "Value: " + String.valueOf(motorBLeft.getCurrentPosition()));
            telemetry.addData("EncoderBR", "Value: " + String.valueOf(motorBRight.getCurrentPosition()));
            telemetry.addData("EncoderFR", "Value: " + String.valueOf(motorFRight.getCurrentPosition()));
            telemetry.addData("EncoderFL", "Value: " + String.valueOf(motorFLeft.getCurrentPosition()));
            telemetry.addData("motorFRight", "Power: " + String.valueOf(motorFRight.getPower()));

//            if (RobotAuto.enLeftPosition > RobotAuto.EnLeftS1 && RobotAuto.enRightPosition > RobotAuto.EnRightS1) {
//
//                RobotAuto.motorBLeft.setPower(0);
//                RobotAuto.motorBRight.setPower(0);
//
//            }

        }

        motorBrush.setPower(0);
        motorBLeft.setPower(0);
        motorBRight.setPower(0);
        motorBLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

    }
}
