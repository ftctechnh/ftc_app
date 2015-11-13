package com.qualcomm.ftcrobotcontroller.opmodes.IntelitekSolutions;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class ExampleDriveForward extends LinearOpMode{
    DcMotor leftMotor;
    DcMotor rightMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        //setup the left and right motors from the configuration file
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        //reverse the right side motor
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the start button to be pressed
        waitForStart();

        //Set the motors to drive the robot forward
        leftMotor.setPower(0.5);
        rightMotor.setPower(0.5);

        //Wait for 2 seconds
        sleep(2000);

        //Stop the robot
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }
}

