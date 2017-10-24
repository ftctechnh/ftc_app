package org.firstinspires.ftc.team9853;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.chathamrobotics.common.Robot;
import org.chathamrobotics.common.systems.HolonomicDriver;
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

    private HolonomicDriver driver;

    public static Robot9853 build(OpMode opMode) {
        return new Robot9853(opMode.hardwareMap, opMode.telemetry);
    }

    public Robot9853(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap, telemetry);
    }

    @Override
    public void init() {
        driver = HolonomicDriver.build(this);
    }

    @Override
    public void start() {

    }

    public void driveWithControls(Gamepad gp) {
        double magnitude = Math.hypot(gp.left_stick_x, gp.left_stick_y);
        double direction = Math.atan2(gp.left_stick_y, gp.left_stick_x);

        driver.setDrivePower(direction, magnitude, gp.right_stick_x);
    }
}
