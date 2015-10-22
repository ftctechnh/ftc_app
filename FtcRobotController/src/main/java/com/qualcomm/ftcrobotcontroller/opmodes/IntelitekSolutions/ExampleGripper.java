package com.qualcomm.ftcrobotcontroller.opmodes.IntelitekSolutions;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class ExampleGripper extends OpMode {

    final double LEFT_OPEN_POSITION = 0.0;
    final double LEFT_CLOSED_POSITION = 0.5;
    final double RIGHT_OPEN_POSITION = 1.0;
    final double RIGHT_CLOSED_POSITION = 0.5;

    Servo leftGripper;
    Servo rightGripper;

    @Override
    public void init() {
        leftGripper = hardwareMap.servo.get("left_hand");
        rightGripper = hardwareMap.servo.get("right_hand");
    }

    @Override
    public void loop() {
        // This code will open and close the gripper with two buttons
        // using 1 button to open and another to close the gripper
        if(gamepad1.x) {
            leftGripper.setPosition(LEFT_OPEN_POSITION);
            rightGripper.setPosition(RIGHT_OPEN_POSITION);
        }
        if(gamepad1.a) {
            leftGripper.setPosition(LEFT_CLOSED_POSITION);
            rightGripper.setPosition(RIGHT_CLOSED_POSITION);
        }
    }
}

