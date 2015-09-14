package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by JackV on 9/12/15.
 */
public class TestAutoMode extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    Servo claw;
    Servo arm;

    @Override
    public void runOpMode() throws InterruptedException {
        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        arm = hardwareMap.servo.get("servo_1");
        claw = hardwareMap.servo.get("servo_6");

        motorRight.setPower(.5);
        motorLeft.setPower(.5);
        sleep(2000);
        motorRight.setPower(0);
        motorLeft.setPower(0);
    }
}
