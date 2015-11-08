package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by matt on 11/7/15.
 */
public class ColorLineFollower extends LinearOpMode {

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private ColorSensor colorSensor;
    private TouchSensor touchSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        colorSensor = hardwareMap.colorSensor.get("colorSensor");
        touchSensor = hardwareMap.touchSensor.get("touchSensor");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        while (!touchSensor.isPressed()) {
            telemetry.addData("Touch", touchSensor.getValue());
            telemetry.addData("Total", colorSensor.alpha() + colorSensor.red() + colorSensor.green() + colorSensor.blue());

            if (colorSensor.alpha() + colorSensor.red() + colorSensor.green() + colorSensor.blue() < 20) {
                rightMotor.setPower(0.20);
                leftMotor.setPower(0);
            }

            else {
                leftMotor.setPower(0.20);
                rightMotor.setPower(0);

            }
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }
}

