package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.robot.Drivetrain;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Carlos on 11/30/2015.
 */
public class GyroTest  extends LinearOpMode {

    Drivetrain drivetrain = new Drivetrain();
    GyroSensor gyro;
    int xVal, yVal, zVal = 0;
    int heading = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        drivetrain.init(hardwareMap);
        gyro = hardwareMap.gyroSensor.get("gyro");

        waitForStart();

        telemetry.addData("Calibration:", "Starting");

        gyro.calibrate();

        while (gyro.isCalibrating())  {
            Thread.sleep(50);
        }

        telemetry.addData("Calibration:", "Complete");

        while (opModeIsActive())  {

            drivetrain.tankDrive(gamepad1.left_stick_y, gamepad1.right_stick_y);


            if(gamepad1.a && gamepad1.b)  {
                gyro.resetZAxisIntegrator();
            }

            xVal = gyro.rawX();
            yVal = gyro.rawY();
            zVal = gyro.rawZ();

            heading = gyro.getHeading();

            telemetry.addData("1. x", String.format("%03d", xVal));
            telemetry.addData("2. y", String.format("%03d", yVal));
            telemetry.addData("3. z", String.format("%03d", zVal));
            telemetry.addData("4. h", String.format("%03d", heading));

        }


    }
}