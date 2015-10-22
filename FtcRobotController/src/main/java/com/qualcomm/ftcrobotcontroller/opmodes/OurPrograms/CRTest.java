package com.qualcomm.ftcrobotcontroller.opmodes.OurPrograms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class CRTest extends OpMode {

    //final double LEFT_OPEN_POSITION = 0.0;
    //final double LEFT_CLOSED_POSITION = 0.5;
    final double RIGHT_OPEN_POSITION = 1.0;
    final double RIGHT_CLOSED_POSITION = 0;

    //double leftposition;
    double rightposition;

    //Servo leftComb;
    Servo rightComb;

    @Override
    public void init() {
        //leftComb = hardwareMap.servo.get("servo_2");
        rightComb = hardwareMap.servo.get("servo_1");
    }

    @Override
    public void loop() {
        // This code will keep the gripper open as long as the button is
        //pressed. When the button is released, the gripper is closed
        /*
        if(gamepad1.x) {
            leftComb.setPosition(LEFT_OPEN_POSITION);
            leftposition = 0.0;
        }
        else {
            //leftComb.setPosition(LEFT_CLOSED_POSITION);
            rightComb.setPosition(RIGHT_CLOSED_POSITION);
            //leftposition = 0.5;
            rightposition = 0.5;
        }
        */
        if(gamepad1.b){
            rightComb.setPosition(RIGHT_OPEN_POSITION);
            rightposition = -1;
        }
        else{
            //leftComb.setPosition(LEFT_CLOSED_POSITION);
            rightComb.setPosition(RIGHT_CLOSED_POSITION);
            //leftposition = 0.5;
            rightposition = 1;
        }

        telemetry.addData("right", "right:  " + String.format("%.2f", rightposition));
        //telemetry.addData("left", "left:  " + String.format("%.2f", rightposition));
    }
    @Override
    public void stop() {

    }

}

