package com.qualcomm.ftcrobotcontroller.opmodes;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;

import java.sql.Time;
import java.util.concurrent.TimeUnit;


/**
 * Created by Nikhil on 11/11/2015.
 */
public class ColorSensorTest extends LinearOpMode {

    Servo rightButtonServo;
    Servo leftButtonServo;
    ColorSensor sensorRGB;

    double rightButtonServoPressed = 0.45;
    double leftButtonServoPressed = 0.55;

    public void runOpMode() throws InterruptedException {

        sensorRGB = hardwareMap.colorSensor.get("sensorRGB");
        rightButtonServo = hardwareMap.servo.get("rightButtonServo");
        leftButtonServo = hardwareMap.servo.get("leftButtonServo");

        rightButtonServo.setPosition(0.5);
        leftButtonServo.setPosition(0.5);

        waitOneFullHardwareCycle();

        waitForStart();

        while (opModeIsActive()) {
            if (sensorRGB.blue() > sensorRGB.red()) {

                rightButtonServo.setPosition(rightButtonServoPressed);
                TimeUnit.MILLISECONDS.sleep(2300);
                rightButtonServo.setPosition(0.5);
            }

            if (sensorRGB.red() >  sensorRGB.blue()) {

                leftButtonServo.setPosition(leftButtonServoPressed);
                TimeUnit.MILLISECONDS.sleep(2300);
                rightButtonServo.setPosition(0.5);
            }

            telemetry.addData("sensorRGB", "sensorRGB:  " + String.format("%d", sensorRGB.blue()));
        }
    }




}
