package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by matt on 11/7/15.
 */
public class MattGyroAutonomous extends LinearOpMode {

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private GyroSensor sensorGyro;
    int heading = 0;
    final int GyroTolerance = 10;
    final int TurnDegrees = 70;

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
        sensorGyro = hardwareMap.gyroSensor.get("gyro_sensor");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        // calibrate the gyro.
        sensorGyro.calibrate();

        // wait for the start button to be pressed.
        waitForStart();

        // make sure the gyro is calibrated.
        while (sensorGyro.isCalibrating())  {
            Thread.sleep(50);
        }

        while (opModeIsActive())  {
            // if the A and B buttons are pressed, reset Z heading.
            if(gamepad1.a && gamepad1.b)  {
                // reset heading.
                sensorGyro.resetZAxisIntegrator();
            }

            // 1 rotate right 90 degrees
            while (SubtractFromCurrHeading(TurnDegrees) < GyroTolerance) {
                rightMotor.setPower(-0.40);
                leftMotor.setPower(0.40);
                telemetry.addData("count", "turn 1");
                telemetry.addData("heading", String.format("h:%03d diff:%03d", sensorGyro.getHeading(), SubtractFromCurrHeading(TurnDegrees)));
                Thread.sleep(100);
            }
            // stop
            leftMotor.setPower(0);
            rightMotor.setPower(0);
            Thread.sleep(2000);
            // set gryo back to 0
            while (sensorGyro.getHeading() > GyroTolerance)  {
                sensorGyro.resetZAxisIntegrator();
                telemetry.addData("status", String.format("waiting for reset, h:%03d", sensorGyro.getHeading()));
                Thread.sleep(50);
            }
            telemetry.addData("status", "go");

            // 2 rotate right 90 degrees
            while (SubtractFromCurrHeading(TurnDegrees) < GyroTolerance) {
                rightMotor.setPower(-0.40);
                leftMotor.setPower(0.40);
                telemetry.addData("count", "turn 2");
                telemetry.addData("heading", String.format("h:%03d diff:%03d", sensorGyro.getHeading(), SubtractFromCurrHeading(TurnDegrees)));
                Thread.sleep(100);
            }
            // stop
            leftMotor.setPower(0);
            rightMotor.setPower(0);
            // wait 2 seconds
            Thread.sleep(2000);
            // set gryo back to 0
            while (sensorGyro.getHeading() > GyroTolerance)  {
                sensorGyro.resetZAxisIntegrator();
                telemetry.addData("status", String.format("waiting for reset, h:%03d", sensorGyro.getHeading()));
                Thread.sleep(50);
            }
            telemetry.addData("status", "go");

            // 3 rotate right 90 degrees
            while (SubtractFromCurrHeading(TurnDegrees) < GyroTolerance) {
                rightMotor.setPower(-0.40);
                leftMotor.setPower(0.40);
                telemetry.addData("count", "turn 3");
                telemetry.addData("heading", String.format("h:%03d diff:%03d", sensorGyro.getHeading(), SubtractFromCurrHeading(TurnDegrees)));
                Thread.sleep(100);
            }
            // stop
            leftMotor.setPower(0);
            rightMotor.setPower(0);
            // wait 2 seconds
            Thread.sleep(2000);
            // set gryo back to 0
            while (sensorGyro.getHeading() > GyroTolerance)  {
                sensorGyro.resetZAxisIntegrator();
                telemetry.addData("status", String.format("waiting for reset, h:%03d", sensorGyro.getHeading()));
                Thread.sleep(50);
            }
            telemetry.addData("status", "go");

            // 4 rotate right 90 degrees
            while (SubtractFromCurrHeading(TurnDegrees) < GyroTolerance) {
                rightMotor.setPower(-0.40);
                leftMotor.setPower(0.40);
                telemetry.addData("count", "turn 4");
                telemetry.addData("heading", String.format("h:%03d diff:%03d", sensorGyro.getHeading(), SubtractFromCurrHeading(TurnDegrees)));
                Thread.sleep(100);
            }
            // stop
            leftMotor.setPower(0);
            rightMotor.setPower(0);
            // wait 2 seconds

            stop();

        } // while opmode active

        leftMotor.setPower(0);
        rightMotor.setPower(0);

    } // runOpMode

} // class

