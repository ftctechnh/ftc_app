package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TempRedAutonomous extends LinearOpMode {

    //Array for sensor values
    List<String> debugValues = new ArrayList<String>();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss:SSS ");

    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor frontRightWheel;
        DcMotor frontLeftWheel;
        DcMotor backRightWheel;
        DcMotor backLeftWheel;
        DcMotor sweeper;

        Servo buttonServo;
        Servo button2Servo;
        Servo climberservo;
        Servo twistServo;
        Servo releaseServo;
        Servo hookServo;
        Servo zipLineServo;

        OpticalDistanceSensor opticalDistanceSensor;
        UltrasonicSensor ultrasonicSensor;
        ColorSensor colorsensor;

        //No need to tune these unless calibration is broken.
        double reflectance = 0;
        double BLACKVALUE = 0.01;
        double WHITEVALUE = 0.4;
        double REDVALUE = 0.3;
        double BLUEVALUE = 0.05;
        // double MARGIN = 0.03;atter.format(new Date()) + " Blue ");

        //map the code to hardware map
        double value;
        frontRightWheel = hardwareMap.dcMotor.get("frontR");
        frontLeftWheel = hardwareMap.dcMotor.get("frontL");
        backRightWheel = hardwareMap.dcMotor.get("backR");
        backLeftWheel = hardwareMap.dcMotor.get("backL");
        frontLeftWheel.setDirection(DcMotor.Direction.FORWARD);
        backLeftWheel.setDirection(DcMotor.Direction.REVERSE);
        frontRightWheel.setDirection(DcMotor.Direction.REVERSE);
        backRightWheel.setDirection(DcMotor.Direction.FORWARD);
        sweeper = hardwareMap.dcMotor.get("sweeper");

        buttonServo = hardwareMap.servo.get("leftbutton");
        buttonServo.setPosition(0.9);
        button2Servo = hardwareMap.servo.get("rightbutton");
        button2Servo.setPosition(0.5);
        //climberservo = hardwareMap.servo;.get("climber");
        //climberservo.setPosition(0.0);
        twistServo = hardwareMap.servo.get("twist");
        twistServo.setPosition(1);
        //releaseServo = hardwareMap.servo.get("release");
        //releaseServo.setPosition(0);
        //hookServo = hardwareMap.servo.get("signal");
        //hookServo.setPosition(1);
        zipLineServo = hardwareMap.servo.get("zipline");
        zipLineServo.setPosition(1);

        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("ultrasonic");
        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("light");
        colorsensor = hardwareMap.colorSensor.get("color");
        colorsensor.enableLed(false);

        waitForStart();
        telemetry.addData("Running Temp", "and");

        frontLeftWheel.setPower(0.3);
        backLeftWheel.setPower(0.3);
        frontRightWheel.setPower(0.3);
        backRightWheel.setPower(0.3);
        sleep(4000);
        telemetry.addData("Running for 10 secs", "and");

        frontLeftWheel.setPower(0);
        backLeftWheel.setPower(0);
        frontRightWheel.setPower(0);
        backRightWheel.setPower(0);
    }
}