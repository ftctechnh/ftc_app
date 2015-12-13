package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.*;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Nikhil on 12/12/2015.
 */
public class TurnTest extends LinearOpMode {

    Drivetrain drivetrain = new Drivetrain();
    GyroSensor gyro;
    int currentHeading;
    int goalHeading;
    int targetAngle = 45;
    double speed = -0.75;

    int xVal, yVal, zVal = 0;
    int heading = 0;


    @Override
    public void runOpMode() throws InterruptedException {
        drivetrain.init(hardwareMap);
        gyro = hardwareMap.gyroSensor.get("gyro");
        telemetry.addData("Drivetrain Init Complete", "");

        waitForStart();
        gyro.calibrate();

        while(opModeIsActive()) {
            while( Math.abs(gyro.getHeading()-90) > 3){
                drivetrain.arcadeDrive(0, speed);
                telemetry.addData("4. h", String.format("%03d", heading));
            }
            drivetrain.arcadeDrive(0, 0);
            sleep(5000);
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
        }
        /*

        */
    }
}
