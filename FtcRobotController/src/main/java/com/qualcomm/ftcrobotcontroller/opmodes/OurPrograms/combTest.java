package com.qualcomm.ftcrobotcontroller.opmodes.OurPrograms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class combTest extends OpMode {

    //double leftposition;
    double rightposition;

    final double RIGHT_OPEN_POSITION = 0;
    final double RIGHT_CLOSED_POSITION = 1;

    Servo rightComb;

    @Override
    public void init() {
        rightComb = hardwareMap.servo.get("servo_1");
    }

    @Override
    public void loop() {

        if(gamepad1.b){
            rightComb.setPosition(RIGHT_OPEN_POSITION);
            rightposition = -1;
        }
        else{
            rightComb.setPosition(RIGHT_CLOSED_POSITION);
            rightposition = 1;
        }

        telemetry.addData("Right Comb", ":  " + String.format("%.2f", rightposition));
    }
    @Override
    public void stop() {

    }

}

