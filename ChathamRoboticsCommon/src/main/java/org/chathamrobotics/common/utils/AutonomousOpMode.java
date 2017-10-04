package org.chathamrobotics.common.utils;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.chathamrobotics.common.Robot;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.lang.reflect.ParameterizedType;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 9/17/2017
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class AutonomousOpMode<R extends Robot> extends LinearOpMode {
    protected R robot;

    /**
     * Creates a new instance of AutonomousOpMode
     */
    public AutonomousOpMode() {
        try {
            //noinspection unchecked
            robot = (R)(
                    // Get generic type class
                    ((Class)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0])
                    // Get robot class
                    .getConstructor(HardwareMap.class, Telemetry.class)
                    // Instantiate robot class
                    .newInstance(hardwareMap, telemetry)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate the robot class", e);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        try {
            robot.init();
            run();
        } catch (StoppedException | InterruptedException err) {
            // Do nothing
        } catch (Exception err) {
            RobotErrors.reportError(this.getClass().getSimpleName(), err, "Encountered error while running opmode");
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
