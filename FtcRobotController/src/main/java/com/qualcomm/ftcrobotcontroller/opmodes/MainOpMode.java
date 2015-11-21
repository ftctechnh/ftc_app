package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by JackV on 9/12/15.
 */
public class MainOpMode extends OpMode{

    // MOTOR VALUES
    DcMotor motorRightBack;
    DcMotor motorLeftBack;
    DcMotor motorRightFront;
    DcMotor motorLeftFront;


    // CONSTRUCTOR
    public MainOpMode() {   }

    // Called when robot is first enabled
    @Override
    public void init() {
        motorLeftFront = hardwareMap.dcMotor.get("motor_1");
        motorLeftBack = hardwareMap.dcMotor.get("motor_3");

        motorRightFront = hardwareMap.dcMotor.get("motor_2");
        motorRightBack = hardwareMap.dcMotor.get("motor_4");
    }

    // Called repeatedly every 10 ms
    @Override
    public void loop() {
        motorLeftFront.setPower(-squareInputs(gamepad1.left_stick_y));
        motorLeftBack.setPower(-squareInputs(gamepad1.left_stick_y));

        motorRightFront.setPower(squareInputs(gamepad1.right_stick_y));
        motorRightBack.setPower(squareInputs(gamepad1.right_stick_y));
    }

    // Called when robot is disabled
    @Override
    public void stop() {
        motorLeftFront.setPower(0);
        motorLeftBack.setPower(0);

        motorRightFront.setPower(0);
        motorRightBack.setPower(0);

    }

    // ADDITIONAL METHODS

    float squareInputs(float input) {
        if (input < 0)
            return -(input * input);
        else
            return input * input;
    }
}