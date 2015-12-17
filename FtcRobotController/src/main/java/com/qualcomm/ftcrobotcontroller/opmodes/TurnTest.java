package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.*;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Nikhil on 12/12/2015.
 */
public class TurnTest extends LinearOpMode {

    Drivetrain drivetrain = new Drivetrain();
    int currentHeading;
    int goalHeading;
    int targetAngle = 45;
    double speed = -0.75;
    int rotationalVelocity;
    long dt = 50;

    int xVal, yVal, zVal = 0;
    int heading = 0;
    int error = 0;


    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain.init(hardwareMap);
        telemetry.addData("Drivetrain Init Complete", "");

        waitForStart();



        while (opModeIsActive()) {
            while(drivetrain.getHeading() < 45 || drivetrain.getHeading() > 60) {
                drivetrain.arcadeDrive(0, -1);
                telemetry.addData("Heading ", String.format("%03d", drivetrain.getHeading()));
            }
            telemetry.addData("Heading ", String.format("%03d", drivetrain.getHeading()));
            drivetrain.arcadeDrive(0, 0);
            telemetry.addData("Complete: ", "");
            sleep(5000);
            /*
            rotationalVelocity = gyro.rawZ();
            if(Math.abs(rotationalVelocity) > 5)
                currentHeading += gyro.rawZ()*(dt/1000.0);

            telemetry.addData("Heading", currentHeading);
            telemetry.addData("Absolute Heading ", String.format("%03d", gyro.getHeading()));
            sleep(dt);
            */

            /*
            while (gyro.getHeading() < 90) {
                telemetry.addData("Heading ", String.format("%03d", gyro.getHeading()));
                error = Math.abs(90 - gyro.getHeading());
                telemetry.addData("Error ", error);
            }
            telemetry.addData("Turning Complete", "");
            sleep(5000);
            */

        }
        /*
        currentHeading = 0;
        goalHeading = (currentHeading + targetAngle)%360;
        speed = Math.abs(speed) * targetAngle/Math.abs(targetAngle);

        while( Math.abs(goalHeading - gyro.getHeading()) > drivetrain.headingTolerance){
            drivetrain.arcadeDrive(0, speed);
        }
        drivetrain.arcadeDrive(0, 0);
        sleep(5000);
        */

        /*

        */
    }
}
