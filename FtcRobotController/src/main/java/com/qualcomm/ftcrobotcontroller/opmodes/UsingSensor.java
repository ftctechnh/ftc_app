package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by freemmar001 on 10/14/2015.
 */


/**
 * Created by freemmar001 on 10/11/2015.
 */
public class UsingSensor extends OpMode {
    DcMotor left;
    DcMotor right;

    public void init() {
        left = hardwareMap.dcMotor.get("Motor-left");
        right = hardwareMap.dcMotor.get("Motor-right");


        //one = hardwareMap.servo.get("servo-8-2");
    }

    @Override
    public void loop() {
        float leftDC = gamepad1.left_stick_y;
        float rightDC = gamepad1.right_stick_y;

        right.setPower(rightDC);
        left.setPower(1);
        //direction = Range.clip(direction, 0, 1);
        //one.setPosition(direction);


    }

}