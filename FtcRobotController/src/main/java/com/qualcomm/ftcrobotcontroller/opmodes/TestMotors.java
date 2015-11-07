package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by sam on 19-Oct-15.
 */
public class TestMotors extends OpMode {
    DcMotor dc1;
    DcMotor dc2;
    DcMotor dc3;
    DcMotor dc4;
    DcMotor dc5;
    Servo s1;
    Servo s2;

    public void init() {
        dc1 = hardwareMap.dcMotor.get("m1");
        dc2 = hardwareMap.dcMotor.get("m2");
        dc3 = hardwareMap.dcMotor.get("m3");
        dc4 = hardwareMap.dcMotor.get("m4");
        dc5 = hardwareMap.dcMotor.get("m5");
        s1 = hardwareMap.servo.get("s1");
        s2 = hardwareMap.servo.get("s2");
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            dc1.setPower(1);
        }
        if (gamepad1.b) {
            dc2.setPower(1);
        }
        if (gamepad1.x) {
            dc3.setPower(1);
        }
        if (gamepad1.y) {
            dc4.setPower(1);
        }
        if (gamepad1.left_bumper) {
            dc5.setPower(1);
        }
        if (gamepad1.right_bumper) {
            s1.setPosition(0.5);
            s2.setPosition(0.5);
        }
        if (gamepad1.back){
            dc1.setPower(0);
            dc2.setPower(0);
            dc3.setPower(0);
            dc4.setPower(0);
            dc5.setPower(0);
        } else {
            s1.setPosition(0);
            s2.setPosition(0);
        }
    }
}