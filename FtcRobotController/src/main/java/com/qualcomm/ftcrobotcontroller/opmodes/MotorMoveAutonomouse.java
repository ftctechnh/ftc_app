package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by BooAsh99 on 10/12/2015.
 */
public class MotorMoveAutonomouse extends LinearOpMode {
    final static double MOTOR_POWER = 0.15;
    private double firstGyroReading;
    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor winch;
    GyroSensor gyro;
    ColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        motorRight = hardwareMap.dcMotor.get("right");
        motorLeft = hardwareMap.dcMotor.get("left");
        winch = hardwareMap.dcMotor.get("winch");
        gyro = hardwareMap.gyroSensor.get("gyro");
        firstGyroReading = gyro.getRotation();
        colorSensor = hardwareMap.colorSensor.get("nxt");
        float hsvValues[] = {0,0,0};

        double turn = 90;
        double degreesToTurn = turn;
        double degreesSoFar = 0;
        double left = 0.0;
        double right = 0.0;
        double multiplicationValue = 0.15;

        if (degreesSoFar < degreesToTurn) {
            double currentGyroReading = gyro.getRotation() - firstGyroReading;
            degreesSoFar = degreesSoFar + currentGyroReading * multiplicationValue;
            motorLeft.setPower(-MOTOR_POWER);
            motorRight.setPower(MOTOR_POWER);
        } else {
            left = 0.0;
            right = 0.0;
        }

        Color.RGBToHSV(colorSensor.red(), colorSensor.green(), colorSensor.blue(), hsvValues);

        telemetry.addData("Clear", colorSensor.alpha());
        telemetry.addData("Red  ", colorSensor.red());
        telemetry.addData("Green", colorSensor.green());
        telemetry.addData("Blue ", colorSensor.blue());
        telemetry.addData("Hue", hsvValues[0]);

        while(true) {
            telemetry.addData("right", gamepad1.right_stick_y);
            telemetry.addData("left", gamepad1.left_stick_y);

            motorRight.setPower(gamepad1.right_stick_y);
            motorLeft.setPower(-gamepad1.left_stick_y);
            if (gamepad1.a) {
                winch.setPower(.9);
            } else {
                winch.setPower(0);
            }
            if (gamepad1.b) {
                winch.setPower(-.9);
            } else {
                winch.setPower(0);
            }
        }
    }
}

