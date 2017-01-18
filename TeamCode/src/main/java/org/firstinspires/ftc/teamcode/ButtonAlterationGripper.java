package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.external.samples.*;

/**
 * Created by robotics on 12/23/2016.
 */


public class ButtonAlterationGripper extends OpMode {

    //Press X to Open Gripper
    //Press A to Close Gripper
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
        //This code will open and close the gripper with two buttons
        //using 1 button to open and another to close the gripper
        if(gamepad2.x) {
            leftGripper.setPosition(LEFT_OPEN_POSITION);
            rightGripper.setPosition(RIGHT_OPEN_POSITION);
        }
        if(gamepad2.a) {
            leftGripper.setPosition(LEFT_CLOSED_POSITION);
            rightGripper.setPosition(RIGHT_CLOSED_POSITION);

        }
    }
}
