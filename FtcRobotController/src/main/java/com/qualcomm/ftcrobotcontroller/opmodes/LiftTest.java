package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Carlos on 11/11/2015.
 */
public class LiftTest extends OpMode {

    public DcMotor rightMotor;
    public DcMotor leftMotor;

    int targetPosition = 0;
    int currentPosition = 0;
    double KP = 0.0005;
    int error;
    double output;

    @Override
    public void init() {
        leftMotor = hardwareMap.dcMotor.get("leftLiftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightLiftMotor");

        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    @Override
    public void init_loop() {
        super.init_loop();

        targetPosition = 0;
        currentPosition = 0;
        error = 0;
        output = 0;

        leftMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        leftMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    @Override
    public void loop() {

        /*
        if(Math.abs(gamepad1.left_stick_y) > 0.2)
            targetPosition += gamepad1.left_stick_y*10;

        currentPosition = (rightMotor.getCurrentPosition()+leftMotor.getCurrentPosition())/2;

        error = targetPosition - currentPosition;

        output = error * KP;

        */

        if(Math.abs(gamepad1.left_stick_y) > 0.2)
            output = gamepad1.left_stick_y;
        else
            output = 0;

        output = Range.clip(output, -1, 1);

        leftMotor.setPower(output);
        rightMotor.setPower(output);


        telemetry.addData("Text", "Lift Test");
        telemetry.addData(" Target Position", targetPosition);
        telemetry.addData("Current Position", currentPosition);
        telemetry.addData("          Output", output);
        telemetry.addData("   Left Position", leftMotor.getCurrentPosition());
        telemetry.addData("  Right Position", rightMotor.getCurrentPosition());
        telemetry.addData("      Left Motor",leftMotor.getPower());
        telemetry.addData("     Right Motor", rightMotor.getPower());
    }
}

