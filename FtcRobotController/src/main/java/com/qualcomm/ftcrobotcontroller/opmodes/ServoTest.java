package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;

/**
 * Created by Nikhil on 9/25/2015.
 */
public class ServoTest extends OpMode {
    Servo servo1;

    @Override
    public void init() {
        servo1 = hardwareMap.servo.get("servo1");
        servo1.setPosition(0);

    }

    @Override
    public void loop() {

       if(gamepad1.a){

           servo1.setPosition(1);

       }

        if(gamepad1.b){

            servo1.setPosition(0);
        }

    }
}

