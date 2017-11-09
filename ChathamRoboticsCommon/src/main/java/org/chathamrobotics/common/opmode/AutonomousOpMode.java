package org.chathamrobotics.common.opmode;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 9/17/2017
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.chathamrobotics.common.opmode.exceptions.StoppedException;
import org.chathamrobotics.common.robot.Robot;
import org.chathamrobotics.common.robot.RobotErrors;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.lang.reflect.ParameterizedType;

/**
 * A template for a autonomous opmode
 * @param <R>   the robot class
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class AutonomousOpMode<R extends Robot> extends LinearOpMode {
    /**
     * The robot object
     */
    protected R robot;

    /**
     * Creates a new instance of AutonomousOpMode
     */
    public AutonomousOpMode() {

    }

    @Override
    public void runOpMode() throws InterruptedException {
        try {
            robot.init();
            run();
        } catch (StoppedException | InterruptedException err) {
            // Do nothing
        } catch (Exception err) {
            RobotErrors.reportGlobalError(this.getClass().getSimpleName(), err, "Encountered error while running opmode");
        } finally {
            robot.stop();
            stop();
        }
    }

    /**
     * Runs the robot
     */
    public abstract void run() throws InterruptedException, StoppedException;

    /**
     * Check if opmode is active and throws a stopped exception if it is not
     * @throws StoppedException throw if opmode is inactive
     */
    public void checkActivity() throws StoppedException {
        if (! opModeIsActive()) throw new StoppedException();
    }
}
