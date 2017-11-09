package org.chathamrobotics.common.opmode;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 9/23/2017
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.chathamrobotics.common.robot.Robot;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.lang.reflect.ParameterizedType;

/**
 * A template for a teleop opmode
 * @param <R>   the robot class
 */
@SuppressWarnings("unused")
public abstract class TeleOpMode<R extends Robot> extends OpMode {
    /**
     * The robot object
     */
    public R robot;

    /**
     * Creates an instance of TeleOpMode
     */
    public TeleOpMode() {

    }

    @Override
    public void start() {
        super.start();
        robot.start();
    }

    @Override
    public void stop() {
        super.stop();
        robot.stop();
    }
}
