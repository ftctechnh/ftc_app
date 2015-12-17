package com.qualcomm.ftcrobotcontroller.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.*;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.util.Range;

import java.util.concurrent.TimeUnit;

/**
 * Created by Carlos on 11/23/2015.
 */
public class RedAuto extends LinearOpMode{

    Drivetrain drivetrain = new Drivetrain();
    Intake intake = new Intake();
    Autonomous autonomous = new Autonomous();


    double rightButtonServoPressed = 0.45;
    double leftButtonServoPressed = 0.57;
    Servo climberServo;

    boolean isRed = false;


    @Override
    public void runOpMode() throws InterruptedException {

        drivetrain.init(hardwareMap);
        telemetry.addData("Drivetrain Init Complete", "");
        intake.init(hardwareMap);
        telemetry.addData("Intake Init Complete", "");
        autonomous.init(hardwareMap);
        telemetry.addData("Autonomous Init Complete", "");
        autonomous.rightButtonServo.setPosition(0.5);
        telemetry.addData("Right Button Servo Init Complete", "");
        autonomous.leftButtonServo.setPosition(0.5);
        telemetry.addData("Left Button Servo Init Complete", "");
        climberServo = hardwareMap.servo.get("climberServo");
        telemetry.addData("Climber Servo Init Complete", "");
        climberServo.setPosition(1);
        telemetry.addData("Climber Servo Init Position Complete", "");


        waitForStart();

        telemetry.addData("Start Autonomous", ".");

        drivetrain.moveDistance(1200, .75);
        telemetry.addData("Step 1 Complete", ".");

        while(drivetrain.getHeading() < 45 || drivetrain.getHeading() > 60)
            drivetrain.arcadeDrive(0,-1);

        drivetrain.arcadeDrive(0, 0);
        telemetry.addData("Step 2 Complete", ".");
        sleep(500);

        intake.outward();
        drivetrain.resetEncoders();
        sleep(500);

        drivetrain.moveDistance(3800, 0.75);
        telemetry.addData("Step 3 Complete", ".");

        while(drivetrain.getHeading() < 90)
            drivetrain.arcadeDrive(0,-1);

        drivetrain.arcadeDrive(0, 0);
        telemetry.addData("Step 4 Complete", ".");

        intake.stop();
        sleep(500);

        drivetrain.moveDistance(250, 0.5);
        telemetry.addData("Step 5 Complete", ".");
        sleep(500);

        climberServo.setPosition(0);
        sleep(100);
        climberServo.setPosition(1);

        //isRed = (autonomous.sensorRGB.red() > 0.5);
        //telemetry.addData("Step 6 Complete", ".");

        drivetrain.moveDistance(500, -0.5);
        telemetry.addData("Step 7 Complete", ".");
        sleep(500);

        /*if (isRed) {
            autonomous.PressLeftButton();
        }
        else {
            autonomous.PressRightButton();
        }
        telemetry.addData("Step 8 Complete", ".");

        drivetrain.moveDistance(750, 0.5);
        telemetry.addData("Step 9 Complete", ".");
        sleep(500);*/



    }


}


