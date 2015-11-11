package com.qualcomm.ftcrobotcontroller.opmodes;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.concurrent.TimeUnit;

public class ColorSensorServoTest extends OpMode {

    Servo rightButtonServo;
    Servo leftButtonServo
    double ServoPosition = 0;


    @Override
    public void init() {

        rightButtonServo = hardwareMap.servo.get("rightButtonServo");
        //leftButtonServo = hardwareMap.servo.get("leftButtonServo")

        rightButtonServo.setPosition(ServoPosition);
        //leftButtonServo.setPosition(ServoPosition);




    }

    @Override
    public void loop() {


        if (gamepad1.dpad_up) {

            ServoPosition  += 0.05;

        }

        if (gamepad1.dpad_down){

            ServoPosition -= 0.05;
        }


        ServoPosition = Range.clip(ServoPosition, 0, 1);

        rightButtonServo.setPosition(ServoPosition);
        //leftButtonServo.setPosition(ServoPosition);


        telemetry.addData("rightLiftServo", "rightLiftServo:  " + String.format("%.2f", ServoPosition));

        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
