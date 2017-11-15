package org.chathamrobotics.common.systems;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 10/29/2017
 */

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.chathamrobotics.common.robot.Robot;
import org.chathamrobotics.common.hardware.LimitSwitch;
import org.chathamrobotics.common.hardware.modernrobotics.ModernRoboticsLimitSwitch;
import org.chathamrobotics.common.hardware.utils.HardwareListener;
import org.chathamrobotics.common.IsBusyException;
import org.chathamrobotics.common.robot.RobotLogger;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Timer;

/**
 * The representation of a rack and pinion with limit switches
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class RackAndPinion {
    private static final Timer TIMER = new Timer();
    private static final String TAG = "RackAndPinion";

    private final CRServo crServo;
    private final LimitSwitch upperLimit;
    private final LimitSwitch lowerLimit;
    private final HardwareListener hardwareListener;
    private final RobotLogger logger;

    private boolean isBusy = false;

    /**
     * Builds a new rack and pinion system using CRServo as the name for the continuous servo
     * @param robot     the robot to get the hardware and logger from
     * @return          the built rack and pinion system
     */
    public static RackAndPinion build(Robot robot) {
        return build(robot.getHardwareMap(), robot, robot.log);
    }

    /**
     * Builds a new rack and pinion system using CRServo as the name for the continuous servo
     * @param hardwareMap   the robot's hardware mape
     * @param telemetry     the opmode's telemetry
     * @return              the built rack and pinion system
     */
    public static RackAndPinion build(HardwareMap hardwareMap, HardwareListener hardwareListener, Telemetry telemetry) {
        return build(hardwareMap, hardwareListener, new RobotLogger("RackAndPinion", telemetry));
    }

    /**
     * Builds a new rack and pinion system using CRServo as the name for the continuous servo
     * @param hardwareMap   the robot's hardware mape
     * @param logger        the robot's logger
     * @return              the built rack and pinion system
     */
    public static RackAndPinion build(HardwareMap hardwareMap, HardwareListener hardwareListener, RobotLogger logger) {
        return new RackAndPinion(
                hardwareMap.crservo.get("CRServo"),
                new ModernRoboticsLimitSwitch(hardwareMap.digitalChannel.get("UpperLimit")),
                new ModernRoboticsLimitSwitch(hardwareMap.digitalChannel.get("LowerLimit")),
                hardwareListener,
                logger
        );
    }

    /**
     * Creates a new instance of RackAndPinion
     * @param crServo   the continuous servo
     * @param logger    the logger for debugging
     */
    public RackAndPinion(
            CRServo crServo,
            LimitSwitch upperLimit,
            LimitSwitch lowerLimit,
            HardwareListener hardwareListener,
            RobotLogger logger
    ) {
        this.crServo = crServo;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.hardwareListener = hardwareListener;
        this.logger = logger;
    }

    /**
     * Check whether or not the servo is currently moving
     * @return  whether or not the servo is currently moving
     */
    public boolean isBusy() {
        return this.isBusy;
    }

    /**
     * Moves the rack and pinion system to its upper limit synchronously.
     * (moves in the positive direction until the upper limit switch is hit)
     * This method will not exit until the lower limit is reached.
     * @throws IsBusyException      thrown if the system is busy when called
     * @throws InterruptedException thrown if the thread is interrupted while waiting
     */
    public void moveToUpperSync() throws IsBusyException, InterruptedException {
        debug("Moving to upper limit");
        start(1);

        while (! upperLimit.isPressed()) Thread.sleep(10);

        stop();
        debug("Reached upper limit");
    }

    /**
     * Moves the rack and pinion system to its upper limit.
     * (moves in the positive direction until the upper limit switch is hit)
     * @throws IsBusyException  thrown if the system is busy when called
     */
    public void moveToUpper() throws IsBusyException {
        debug("Moving to upper limit");
        start(1);

        hardwareListener.on(upperLimit, LimitSwitch::isPressed, () -> {
            stop();

            debug("Reached upper limit");
        });
    }

    /**
     * Moves the rack and pinion system to its lower limit synchronously.
     * (moves in the negative direction until the lower limit switch is hit)
     * This method will not exit until the lower limit is reached
     * @throws IsBusyException      thrown if the system is busy when called
     * @throws InterruptedException thrown if the thread is interrupted while waiting
     */
    public void moveToLowerSync() throws IsBusyException, InterruptedException {
        debug("Moving to lower limit");
        start(-1);

        while (! lowerLimit.isPressed()) Thread.sleep(10);

        stop();
        debug("Reached lower limit");
    }

    /**
     * Moves the rack and pinion system to its lower limit.
     * (moves in the negative direction until the lower limit switch is hit)
     * @throws IsBusyException  thrown if the system is busy when called
     */
    public void moveToLower() throws IsBusyException {
        debug("Moving to lower limit");

        start(-1);

        hardwareListener.on(lowerLimit, LimitSwitch::isPressed, () -> {
            stop();

            debug("Reached lower limit");
        });
    }

    private void debug(String line) {
        synchronized (logger) {
            logger.debug(line);
        }
    }

    private void start(double power) throws IsBusyException {
        if (isBusy) throw new IsBusyException("RackAndPinion is busy");

        synchronized (crServo) {
            crServo.setPower(power);
        }
        isBusy = true;
    }

    private void stop() {
        synchronized (crServo) {
            crServo.setPower(0);
        }

        isBusy = false;
    }
}
