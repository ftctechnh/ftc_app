package com.qualcomm.ftcrobotcontroller.opmodes;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;


/**
 * Created by Nikhil on 11/11/2015.
 */
public class ColorSensorTest extends LinearOpMode {

    Servo rightButtonServo;
    Servo leftButtonServo;
    ColorSensor sensorRGB;

    double rightButtonServoPressed = 0;
    double leftButtonServoPressed = 0;

    public void runOpMode() throws InterruptedException {

        sensorRGB = hardwareMap.colorSensor.get("sensorRGB");

        waitOneFullHardwareCycle();

        waitForStart();

        if (sensorRGB.blue() > sensorRGB.red()) {

            rightButtonServo.setPosition(rightButtonServoPressed);
        }

        if (sensorRGB.blue() < sensorRGB.red()){

            leftButtonServo.setPosition(leftButtonServoPressed);
        }
    }


}
