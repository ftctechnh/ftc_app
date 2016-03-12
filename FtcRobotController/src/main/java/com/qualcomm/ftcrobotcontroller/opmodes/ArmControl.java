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
        if (gamepad2.y) {
            leftArm.setPower(.1);
            rightArm.setPower(.1);
        }
        else if(gamepad2.b) {
            leftArm.setPower(-.1);
            rightArm.setPower(-.1);
        }
        else {
            leftArm.setPower(0);
            rightArm.setPower(0);
        }
    }
}

