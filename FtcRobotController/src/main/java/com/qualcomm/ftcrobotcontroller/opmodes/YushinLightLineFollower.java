package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by matt on 11/7/15.
 */
public class YushinLightLineFollower extends LinearOpMode {

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private LightSensor lightSensor;
    private TouchSensor touchSensor;
    private GyroSensor sensorGyro;
    final int GyroTolerance = 10;

    // heading is never negative, but it can flip from 1 to 359, so do modulus 180
    // If this function returns negative, it means turns right to correct; positive means turn left
    private int SubtractFromCurrHeading (int x) {
        int result = 0;
        int ch = sensorGyro.getHeading();
        int diff = Math.abs(ch - x);
        if (diff >= 180) { // more than 180deg apart, so flip
            result = 360 - diff;
            if (x < 180) { result = -result; }
        } else {
            result = ch - x;
        }
        return result;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        lightSensor = hardwareMap.lightSensor.get("lightSensor");
        lightSensor.enableLed(true);
        sensorGyro = hardwareMap.gyroSensor.get("gyro_sensor");

        // Gyro stuff
        sensorGyro.calibrate();
        while (sensorGyro.isCalibrating()) {
            Thread.sleep(50);
        }

        waitForStart();
        sensorGyro.resetZAxisIntegrator();

        while (Math.abs(SubtractFromCurrHeading(0)) < 150) { // stop if we lose line and start spinning
            telemetry.addData("Light", lightSensor.getLightDetected());
            telemetry.addData("Heading", String.format("h:%03d diff:%03d", sensorGyro.getHeading(), SubtractFromCurrHeading(0)));

            if (lightSensor.getLightDetected() < 0.5) {  // light carpet, go left
                rightMotor.setPower(0.20);
                leftMotor.setPower(0);
            }

            else { // dark carpet edge, go right
                leftMotor.setPower(0.20);
                rightMotor.setPower(0);

            }
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        telemetry.addData("State", "stopped");
    }
}

