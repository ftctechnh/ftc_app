package org.firstinspires.ftc.team9853;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.chathamrobotics.common.Robot;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 9/17/2017
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Robot9853 extends Robot {

    private DcMotor frontRightMotor;
    private DcMotor frontLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor backLeftMotor;

    public static Robot9853 build(OpMode opMode) {
        return new Robot9853(opMode.hardwareMap, opMode.telemetry);
    }

    public Robot9853(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap, telemetry);
    }

    @Override
    public void init() {
        frontRightMotor = hardwareMap.dcMotor.get("FrontRight");
        frontLeftMotor = hardwareMap.dcMotor.get("FrontLeft");
        backRightMotor = hardwareMap.dcMotor.get("BackRight");
        backLeftMotor = hardwareMap.dcMotor.get("BackLeft");

        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void start() {

    }

    public void driveWithControls(Gamepad gp) {
        double rightMotorPower = Range.clip(-gp.left_stick_y, -1, 1);
        double leftMotorPower = Range.clip(-gp.left_stick_y, -1, 1);

        if (gp.right_stick_x != 0) {
            rightMotorPower = rightMotorPower == 0 ? 1 : rightMotorPower;
            leftMotorPower = leftMotorPower == 0 ? 1 : leftMotorPower;

            rightMotorPower *= -gp.right_stick_x;
            leftMotorPower *= gp.right_stick_x;
        }

        log.debug("Right Stick X", gp.right_stick_x);
        log.debug("Left Stick Y", -gp.right_stick_y);

        frontRightMotor.setPower(rightMotorPower);
        backRightMotor.setPower(rightMotorPower);

        frontLeftMotor.setPower(leftMotorPower);
        backLeftMotor.setPower(leftMotorPower);
    }
}
