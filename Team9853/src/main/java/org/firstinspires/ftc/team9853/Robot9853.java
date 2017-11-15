package org.firstinspires.ftc.team9853;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.chathamrobotics.common.robot.Robot;
import org.chathamrobotics.common.systems.HolonomicDriver;
import org.chathamrobotics.common.robot.RobotFace;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.team9853.systems.GlyphGripper;

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

    public HolonomicDriver driver;
    public GlyphGripper glyphGripper;
    public DcMotor lift;

    public static Robot9853 build(OpMode opMode) {
        return new Robot9853(opMode.hardwareMap, opMode.telemetry);
    }

    public Robot9853(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap, telemetry);
    }

    @Override
    public void init() {
        driver = HolonomicDriver.build(this);
        glyphGripper = GlyphGripper.build(this);
        lift = getHardwareMap().dcMotor.get("Lift");
        lift.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void start() {
        glyphGripper.close();
    }

    public void driveWithControls(Gamepad gp) {
        double magnitude = Math.hypot(gp.left_stick_x, -gp.left_stick_y);
        double direction = Math.atan2(-gp.left_stick_y, gp.left_stick_x);

        if (gp.dpad_up) driver.setFront(RobotFace.FRONT);
        if (gp.dpad_down) driver.setFront(RobotFace.BACK);
        if (gp.dpad_left) driver.setFront(RobotFace.LEFT);

        driver.setDrivePower(direction, magnitude, gp.right_stick_x);
    }
}
