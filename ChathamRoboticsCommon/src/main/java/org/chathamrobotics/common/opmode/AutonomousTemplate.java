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

import org.chathamrobotics.common.opmode.exceptions.StoppedException;
import org.chathamrobotics.common.robot.Robot;
import org.chathamrobotics.common.robot.RobotErrors;

/**
 * A template for a autonomous opmode
 * @param <R>   the robot class
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class AutonomousTemplate<R extends Robot> extends LinearOpMode {
    /**
     * The robot object
     */
    protected R robot;

    private final boolean isRedTeam;

    /**
     * Creates a new instance of AutonomousTemplate
     */
    public AutonomousTemplate(boolean isRedTeam) {
        this.isRedTeam = isRedTeam;
    }

    /**
     * Runs the robot
     */
    public abstract void run() throws InterruptedException, StoppedException;

    /**
     * Sets up the robot
     */
    public abstract void setup();

    @Override
    public void runOpMode() throws InterruptedException {
        try {
            setup();
            telemetry.update();
            waitForStart();
            run();
            debug();
        } catch (StoppedException | InterruptedException err) {
            // Do nothing
        } catch (Exception err) {
            RobotErrors.reportGlobalError(this.getClass().getSimpleName(),"Encountered error while running opmode:" + err.getMessage());
        } finally {
            if (robot != null) robot.stop();
            stop();
        }
    }

    /**
     * Check if opmode is active and throws a stopped exception if it is not
     * @throws StoppedException throw if opmode is inactive
     */
    public void checkActivity() throws StoppedException {
        if (! opModeIsActive()) throw new StoppedException();
    }

    /**
     * Debugs the robot and checks the opmode activity
     * @throws StoppedException throw if the opmode is no longer active
     */
    public void debug() throws StoppedException {
        if (robot != null) this.robot.debugHardware();
        this.checkActivity();
    }

    /**
     * Checks whether the opmode is being run on the red team
     * @return  whether the opmode is being run on the red team
     */
    public boolean isRedTeam() {
        return this.isRedTeam;
    }
}
