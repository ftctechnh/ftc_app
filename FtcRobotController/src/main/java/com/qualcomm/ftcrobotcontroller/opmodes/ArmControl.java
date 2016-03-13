package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by arkapurkayastha on 12/03/2016.
 */
public class ArmControl extends OpMode {

    DcMotor leftArm;
    DcMotor rightArm;

    @Override
    public void init() {
        leftArm = hardwareMap.dcMotor.get("left_arm");
        rightArm = hardwareMap.dcMotor.get("right_arm");
    }

    @Override
    public void loop() {
        if (gamepad1.y) {
            leftArm.setPower(1.0);
            rightArm.setPower(1.0);
        }
        else if(gamepad1.a) {
            leftArm.setPower(-0.5);
            rightArm.setPower(-.5);
        }
        else {
            leftArm.setPower(0);
            rightArm.setPower(0);
        }
    }
}

