package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class BotAuton extends LinearOpMode {

    final static double NORMAL_SPEED = 0.75;
    final static double FULL_SPEED = 1.0;
    final static double SLOW_TURN = 0.15;
    final static double FULL_STOP = 0.0;

    DcMotor motorRight;
    DcMotor motorLeft;
    Servo vertical;
    Servo claw;

    public void stopMotors() {
        motorLeft.setPower(FULL_STOP);
        motorRight.setPower(FULL_STOP);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initMotors();
        //autonomous here
        //full speed = 2.35294 ft/sec
        motorLeft.setPower(NORMAL_SPEED);
        motorRight.setPower(NORMAL_SPEED);
        Thread.sleep(1700);
        //calibrated for 75% speed, 36 inches forward
        stopMotors();
        initMotors();
        motorRight.setPower(NORMAL_SPEED);
        Thread.sleep(300);
        //this is a guess for 90 degree turn to the left
        stopMotors();
        initMotors();
        motorRight.setPower(NORMAL_SPEED);
        Thread.sleep(1794);
        //calibrated for 75% speed, 38 inches forward
        stopMotors();
        initMotors();
        motorRight.setPower(NORMAL_SPEED);
        Thread.sleep(133);
        //this is a guess for 40 degree turn to the left
        stopMotors();
        initMotors();
        motorRight.setPower(NORMAL_SPEED);
        Thread.sleep(2267);
        //calibrated for 75% speed, 48 inches forward
        stopMotors();
    }

    public void initMotors() {
        motorRight = hardwareMap.dcMotor.get("right_motor");
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorLeft = hardwareMap.dcMotor.get("left_motor");
        vertical = hardwareMap.servo.get("vertical");
        claw = hardwareMap.servo.get("claw");
    }
}

//Then Jake show up late... Srry pal