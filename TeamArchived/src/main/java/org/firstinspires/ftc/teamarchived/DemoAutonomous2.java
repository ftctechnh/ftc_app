package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;


/**
 * Created by Ian Ramsey on 5/21/16
 */
public class DemoAutonomous2 extends OpMode {
    DcMotorController wheelControllerLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;
    DcMotorController wheelControllerRight;
    DcMotor motorBackRight;
    DcMotor motorFrontRight;
    DcMotor arm;
    DcMotorController armController;
    ServoController hand;
    Servo leftFinger;
    Servo rightFinger;
    public void init(){
        motorBackRight = hardwareMap.dcMotor.get("RightBack");
        motorFrontRight = hardwareMap.dcMotor.get("RightFront");
        motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
        motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
        arm = hardwareMap.dcMotor.get("Arm1");
        wheelControllerRight = hardwareMap.dcMotorController.get("Right");
        wheelControllerLeft = hardwareMap.dcMotorController.get("Left");
        armController = hardwareMap.dcMotorController.get("Arm");
        hand = hardwareMap.servoController.get("Hand");
        rightFinger = hardwareMap.servo.get("RightFinger");
        leftFinger = hardwareMap.servo.get("LeftFinger");

    }

    public void loop() {
        if (getRuntime() < 3) {
            motorBackLeft.setPower(1);
            motorFrontLeft.setPower(1);
            motorBackRight.setPower(-1);
            motorFrontRight.setPower(-1);
        } else if (getRuntime() < 3.9) {
            motorBackLeft.setPower(0);
            motorFrontLeft.setPower(0);
            motorBackRight.setPower(0);
            motorFrontRight.setPower(0);
        }
        if (getRuntime() > 4) {
            if (getRuntime() < 5.5) {
                motorBackLeft.setPower(-1);
                motorFrontLeft.setPower(-1);
                motorBackRight.setPower(-1);
                motorFrontRight.setPower(-1);
            } else if (getRuntime() < 6) {
                motorBackLeft.setPower(0);
                motorFrontLeft.setPower(0);
                motorBackRight.setPower(0);
                motorFrontRight.setPower(0);
            }
        }
        if (getRuntime() > 7) {
            if (getRuntime() < 10) {
                motorBackLeft.setPower(1);
                motorFrontLeft.setPower(1);
                motorBackRight.setPower(-1);
                motorFrontRight.setPower(-1);
            } else {
                motorBackLeft.setPower(0);
                motorFrontLeft.setPower(0);
                motorBackRight.setPower(0);
                motorFrontRight.setPower(0);
            }
        }
    }
}
