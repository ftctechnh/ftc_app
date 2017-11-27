package org.chathamrobotics.common.systems;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 10/5/2017
 */

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.chathamrobotics.common.robot.Robot;
import org.chathamrobotics.common.robot.RobotFace;
import org.chathamrobotics.common.robot.RobotLogger;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * The representation of a holonomic drive system
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue"})
public class HolonomicDriver implements Driver {
    public static final double HOLONOMIC_AXIS_CORRECTION = Math.PI / 4;
    private static final double ROOT_TWO = Math.sqrt(2);

    private static final String TAG = "HolonomicDriver";

    // Hardware
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    // Robot Utils
    private RobotLogger logger;

    // Settings
    private double offsetAngle;

    /**
     * Builds a new {@link HolonomicDriver} using FrontLeft, FrontRight, BackLeft and BackRight for the motor names
     * @param robot     the robot to build the holonomic driver from
     * @return          the built {@link HolonomicDriver}
     */
    public static HolonomicDriver build(Robot robot) {
        return build(robot.getHardwareMap(), robot.log.systemLogger(TAG));
    }

    /**
     * Builds a new {@link HolonomicDriver} using FrontLeft, FrontRight, BackLeft and BackRight for the motor names
     * @param hardwareMap   the robot's hardware map
     * @return              the built {@link HolonomicDriver}
     */
    public static HolonomicDriver build(HardwareMap hardwareMap) {
        return build(hardwareMap, (RobotLogger) null);
    }

    /**
     * Builds a new {@link HolonomicDriver} using FrontLeft, FrontRight, BackLeft and BackRight for the motor names
     * @param hardwareMap   the robot's hardware map
     * @param telemetry     the opmode's telemetry
     * @return              the built {@link HolonomicDriver}
     */
    public static HolonomicDriver build(HardwareMap hardwareMap, Telemetry telemetry) {
        return build(hardwareMap, new RobotLogger(TAG, telemetry));
    }

    /**
     * Builds a new {@link HolonomicDriver} using FrontLeft, FrontRight, BackLeft and BackRight for the motor names
     * @param hardwareMap   the robot's hardware map
     * @param logger        the robot's logger
     * @return              the built {@link HolonomicDriver}
     */
    public static HolonomicDriver build(HardwareMap hardwareMap, RobotLogger logger) {
        return new HolonomicDriver(
                hardwareMap.dcMotor.get("FrontLeft"),
                hardwareMap.dcMotor.get("FrontRight"),
                hardwareMap.dcMotor.get("BackLeft"),
                hardwareMap.dcMotor.get("BackRight"),
                logger
        );
    }

    /**
     * Creates a new instance of {@link HolonomicDriver}
     * @param frontLeft     the front left motor
     * @param frontRight    the front right motor
     * @param backLeft      the back left motor
     * @param backRight     the back right motor
     */
    public HolonomicDriver(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight) {
        this(frontLeft, frontRight, backLeft, backRight, null);
    }

    /**
     * Creates a new instance of {@link HolonomicDriver}
     * @param frontLeft     the front left motor
     * @param frontRight    the front right motor
     * @param backLeft      the back left motor
     * @param backRight     the back right motor
     * @param logger        the robot's logger
     */
    public HolonomicDriver(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, RobotLogger logger) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;

        this.logger = logger;
    }

    /**
     * Set the front of the robot
     * @param face  the face to set the front of the robot to
     */
    public void setFront(RobotFace face) {
        this.offsetAngle = face.holonomicOffset;
    }

    /**
     * Sets the offset angle
     * @param offsetAngle   the offset angle (assumed to be in radians)
     */
    public void setOffsetAngle(double offsetAngle) {
        setOffsetAngle(offsetAngle, AngleUnit.RADIANS);
    }

    /**
     * Sets the offset angle
     * @param offsetAngle   the offset angle
     * @param angleUnit     the offset angle's units
     */
    public void setOffsetAngle(double offsetAngle, AngleUnit angleUnit) {
        this.offsetAngle = angleUnit.toRadians(offsetAngle);
    }

    /**
     * Gets the current offset angle
     * @return  the offset angle in radians
     */
    public double getOffsetAngle() {
        return offsetAngle;
    }

    /**
     * Stops the driver
     */
    public void stop() {
        setDrivePower(0,0,0);
    }

    /**
     * Sets the robot's drive train's power
     * @param direction     the direction to direct the power (assumed to be in radians)
     * @param magnitude     the magnitude of the power
     */
    public void setDrivePower(double direction, double magnitude) {
        setDrivePower(direction, magnitude, 0);
    }

    /**
     * Sets the robot's drive train's power
     * @param direction     the direction to direct the power (assumed to be in radians)
     * @param magnitude     the magnitude of the power
     * @param rotation      the factor by which to rotate the robot
     */
    public void setDrivePower(double direction, double magnitude, double rotation) {
        setDrivePower(direction, AngleUnit.RADIANS, magnitude, rotation);
    }

    /**
     * Sets the robot's drive train's power
     * @param direction     the direction to direct the power (assumed to be in radians)
     * @param angleUnit     the direction angle's units
     * @param magnitude     the magnitude of the power
     * @param rotation      the factor by which to rotate the robot
     */
    public void setDrivePower(double direction, AngleUnit angleUnit, double magnitude, double rotation) {
        Range.throwIfRangeIsInvalid(magnitude, 0, ROOT_TWO);
        Range.throwIfRangeIsInvalid(rotation, -1, 1);

        direction = angleUnit.toRadians(direction);

        if (logger != null) {
            logger.debug("Drive Direction (Radians)", direction);
            logger.debug("Drive Magnitude", magnitude);
            logger.debug("Drive Rotation", rotation);
        }

        // adjust direction by 45 degrees and then whatever the offset is
        direction += HOLONOMIC_AXIS_CORRECTION + offsetAngle;

        if (logger != null) {
            logger.debug("Drive Direction Corrected (Radians)", direction);
        }

        frontLeft.setPower(calcMotorValue(true, true, direction, magnitude, rotation));
        frontRight.setPower(calcMotorValue(true, false, direction, magnitude, rotation));
        backLeft.setPower(calcMotorValue(false, true, direction, magnitude, rotation));
        backRight.setPower(calcMotorValue(false, false, direction, magnitude, rotation));
    }

    /**
     * Rotates the robot
     * @param rotationPower the power to set the motors to
     */
    public void rotate(double rotationPower) {
        setDrivePower(0 , 0, rotationPower);
    }

    private double calcMotorValue(boolean isFront, boolean isLeft, double direction, double magnitude, double rotation) {
        return Range.clip(
                (isFront ? 1 : -1) * ( // reverse motors in back
                        // get value for motor
                        magnitude * (isFront == isLeft ? Math.sin(direction) : Math.cos(direction))
                        + (isFront ? -rotation : rotation) // add or subtract the rotation
                ),
                -1,
                1
        );
    }
}
