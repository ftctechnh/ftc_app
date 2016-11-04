package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;



/**
 * Created by robotics on 9/26/2015.
 */
public class Debug extends OpMode {
    DcMotorController wheelControllerLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;
    DcMotorController wheelControllerRight;
    DcMotor motorBackRight;
    DcMotor motorFrontRight;

        public void init() {
            motorBackRight = hardwareMap.dcMotor.get("RightBack");
            motorFrontRight = hardwareMap.dcMotor.get("RightFront");
            motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
            motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
            wheelControllerRight = hardwareMap.dcMotorController.get("Right");
            wheelControllerLeft = hardwareMap.dcMotorController.get("Left");
        }
        public void loop() {
            motorBackLeft.setPower(1);
            motorFrontLeft.setPower(1);
            motorBackRight.setPower(1);
            motorFrontRight.setPower(1);
        }
    }