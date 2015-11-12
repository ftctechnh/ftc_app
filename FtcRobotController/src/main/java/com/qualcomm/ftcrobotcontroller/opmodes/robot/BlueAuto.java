package com.qualcomm.ftcrobotcontroller.opmodes.robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by Nikhil on 11/12/2015.
 */
public class BlueAuto extends LinearOpMode {

        //Lift lift = new Lift();
        Drivetrain drivetrain = new Drivetrain();
        //Arm arm = new Arm();
        //Intake intake = new Intake();

        Servo rightButtonServo;
        Servo leftButtonServo;
        ColorSensor sensorRGB;

        double rightButtonServoPressed = 0.45;
        double leftButtonServoPressed = 0.57;

    public void runOpMode() throws InterruptedException {

        //lift.init(hardwareMap);
        drivetrain.init(hardwareMap);
        //arm.init(hardwareMap);
        //intake.init(hardwareMap);
        sensorRGB = hardwareMap.colorSensor.get("sensorRGB");
        rightButtonServo = hardwareMap.servo.get("rightButtonServo");
        leftButtonServo = hardwareMap.servo.get("leftButtonServo");

        rightButtonServo.setPosition(0.5);
        leftButtonServo.setPosition(0.5);

        waitOneFullHardwareCycle();

        waitForStart();

        drivetrain.drive(12,0.5);


    }
}
