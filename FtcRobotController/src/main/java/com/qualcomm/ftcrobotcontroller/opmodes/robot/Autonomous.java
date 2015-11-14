package com.qualcomm.ftcrobotcontroller.opmodes.robot;

import com.qualcomm.robotcore.hardware.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by Nikhil on 11/13/2015.
 */
public class Autonomous {


    public Servo rightButtonServo;
    public Servo leftButtonServo;
    public ColorSensor sensorRGB;

    double rightButtonServoPressed = 0.45;
    double leftButtonServoPressed = 0.57;
    double leftButtonServoReset = 0.43;
    double rightButtonServoRest = 0.55;

    public Autonomous(){
    }

    public void init(HardwareMap hardwareMap){

        sensorRGB = hardwareMap.colorSensor.get("sensorRGB");
        rightButtonServo = hardwareMap.servo.get("rightButtonServo");
        leftButtonServo = hardwareMap.servo.get("leftButtonServo");

        rightButtonServo.setPosition(0.5);
        leftButtonServo.setPosition(0.5);

    }

    public void PressBlueButton() throws InterruptedException {

            rightButtonServo.setPosition(rightButtonServoPressed);
            TimeUnit.MILLISECONDS.sleep(2100);
            rightButtonServo.setPosition(0.5);


    }

    public void PressRedButton() throws InterruptedException {

            leftButtonServo.setPosition(leftButtonServoPressed);
            TimeUnit.MILLISECONDS.sleep(2100);
            leftButtonServo.setPosition(0.5);

    }

    public void ResetButtonPressers() throws InterruptedException {

        leftButtonServo.setPosition(leftButtonServoReset);
        rightButtonServo.setPosition(rightButtonServoPressed);
        TimeUnit.MILLISECONDS.sleep(2100);
        leftButtonServo.setPosition(0.5);
        rightButtonServo.setPosition(0.5);
    }
}




