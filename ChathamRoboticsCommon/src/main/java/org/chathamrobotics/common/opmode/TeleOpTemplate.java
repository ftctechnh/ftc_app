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

import org.chathamrobotics.common.Controller;
import org.chathamrobotics.common.robot.Robot;

/**
 * A template for a teleop opmode
 * @param <R>   the robot class
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class TeleOpTemplate<R extends Robot> extends OpMode {
    /**
     * The robot object
     */
    public R robot;

    protected Controller controller1;
    protected Controller controller2;

    @Override
    public void init() {
        controller1 = new Controller(gamepad1);
        controller2 = new Controller(gamepad2);
    }

    @Override
    public void internalPostLoop() {
        controller1.update();
        controller2.update();
        if (robot != null) this.robot.debugHardware();

        super.internalPostLoop();
    }

    @Override
    public void start() {
        super.start();
        if (robot != null) robot.start();
    }

    @Override
    public void stop() {
        super.stop();
        if (robot != null) robot.stop();
    }
}
