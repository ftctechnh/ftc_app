package org.chathamrobotics.common.utils;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 9/17/2017
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class AutonomousOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        try {
            run();
        } catch (StoppedException | InterruptedException err) {
            stop();
        } catch (Exception err) {
            Log.wtf(this.getClass().getSimpleName(), err);
            RobotLog.setGlobalErrorMsg(err.getMessage());
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
