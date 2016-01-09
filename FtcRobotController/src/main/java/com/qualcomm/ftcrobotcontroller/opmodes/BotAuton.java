package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.Values;
import com.qualcomm.ftcrobotcontroller.hardware.Power;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class BotAuton extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;

    @Override
    public void runOpMode() throws InterruptedException {
        initMotors();
        //autonomous here
        //full speed = 2.35294 ft/sec
        motorLeft.setPower(Power.NORMAL_SPEED);
        motorRight.setPower(Power.NORMAL_SPEED);
        Thread.sleep(1700);
        //calibrated for 75% speed, 36 inches forward
        stopMotors();
        initMotors();
        motorRight.setPower(Power.NORMAL_SPEED);
        Thread.sleep(300);
        //this is a guess for 90 degree turn to the left
        stopMotors();
        initMotors();
        motorRight.setPower(Power.NORMAL_SPEED);
        Thread.sleep(1794);
        //calibrated for 75% speed, 38 inches forward
        stopMotors();
        initMotors();
        motorRight.setPower(Power.NORMAL_SPEED);
        Thread.sleep(133);
        //this is a guess for 40 degree turn to the left
        stopMotors();
        initMotors();
        motorRight.setPower(Power.NORMAL_SPEED);
        Thread.sleep(2267);
        //calibrated for 75% speed, 48 inches forward
        stopMotors();
    }

    public void initMotors() {
        motorRight = hardwareMap.dcMotor.get(Values.RIGHT_MOTOR);
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorLeft = hardwareMap.dcMotor.get(Values.LEFT_MOTOR);
    }

    public void stopMotors() {
        motorLeft.setPower(Power.FULL_STOP);
        motorRight.setPower(Power.FULL_STOP);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setDirection(DcMotor.Direction.REVERSE);
    }
}

//Then Jake show up late... Srry pal