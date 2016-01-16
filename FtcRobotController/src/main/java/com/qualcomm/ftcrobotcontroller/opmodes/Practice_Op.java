package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by MRHFTC on 1/16/2016.
 */
public class Practice_Op extends OpMode {

    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;

    public Practice_Op() {

    }


    @Override
    public void init() {
        motor1 = hardwareMap.dcMotor.get("Motor 1");
        motor2 = hardwareMap.dcMotor.get("Motor 2");
        motor3 = hardwareMap.dcMotor.get("Motor 3");
    }

    @Override
    public void loop() {
        double throttleRight = gamepad1.right_stick_y;
        double throttleLeft = gamepad1.left_stick_y;

        motor1.setPower(throttleRight);
        motor2.setPower(throttleLeft);

    }
}
