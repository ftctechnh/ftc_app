package com.qualcomm.ftcrobotcontroller.opmodes.IntelitekSolutions;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class ExampleArmControl extends OpMode {

    DcMotor leftArm;

    @Override
    public void init() {
        //get references to the arm motor from the hardware map
        leftArm = hardwareMap.dcMotor.get("left_arm");
    }

    @Override
    public void loop() {
        // This code will control the up and down movement of
        // the arm using the y and b gamepad buttons.
        if(gamepad1.y) {
            leftArm.setPower(0.1);
        }
        else if(gamepad1.b) {
            leftArm.setPower(-0.1);
        }
        else {
            leftArm.setPower(0);
        }
    }

}

