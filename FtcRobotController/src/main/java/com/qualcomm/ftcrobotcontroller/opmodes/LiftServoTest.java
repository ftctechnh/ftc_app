package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.*;


/**
 * Created by Nikhil on 11/6/2015.
 */
public class LiftServoTest extends OpMode {

    Servo rightLiftServo;
    double ServoPosition = 0;


    @Override
    public void init() {

       rightLiftServo = hardwareMap.servo.get("rightLiftServo");

        rightLiftServo.setPosition(ServoPosition);



    }

    @Override
    public void loop() {

        if (gamepad1.dpad_up) {

            ServoPosition  += 0.1;
        }

        if (gamepad1.dpad_down){

            ServoPosition -= 0.1;
        }

        ServoPosition = Range.clip(ServoPosition,0,1);

        rightLiftServo.setPosition(ServoPosition);


        telemetry.addData("rightLiftServo", "rightLiftServo:  " + String.format("%.2f", ServoPosition));

    }
}
