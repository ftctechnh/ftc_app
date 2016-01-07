/*
 * Copyright (C) 2015 Powerstackers
 *
 * Basic configurations and capabilities of our robot.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.powerstackers.resq.common;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.swerverobotics.library.ClassFactory;

/**
 * A general representation of a robot, with simple interaction methods.
 * @author Jonathan Thomas
 */
public class Robot {

    // Final constants
    private static final double CRS_REVERSE = 0.0;
    private static final double CRS_STOP    = 0.5;
    private static final double CRS_FORWARD = 1.0;
    private static final double LIFT_SPEED  = 1.0;
    private static final double BRUSH_SPEED = 1.0;

    private DcMotor motorLeftA;
    private DcMotor motorLeftB;
    private DcMotor motorRightA;
    private DcMotor motorRightB;
    private DcMotor motorBrush;
    private DcMotor motorLift;

    private Servo servoTapeMeasure;
    private Servo servoBeacon;
    private Servo servoRight;
    private Servo servoLeft;

    private DeviceInterfaceModule dim;
    private ColorSensor sensorColor;
    private TouchSensor sensorTouch;
    public OpticalDistanceSensor opticalSensor;

    /**
     * Construct a Robot object.
     * @param mode The OpMode in which the robot is being used.
     */
    public Robot(OpMode mode) {
        motorLeftA  = mode.hardwareMap.dcMotor.get("motorFLeft");
        motorLeftB  = mode.hardwareMap.dcMotor.get("motorBLeft");
        motorRightA = mode.hardwareMap.dcMotor.get("motorFRight");
        motorRightB = mode.hardwareMap.dcMotor.get("motorBRight");
        motorBrush  = mode.hardwareMap.dcMotor.get("motorBrush");
        motorLift   = mode.hardwareMap.dcMotor.get("motorLift");

        motorLift.setDirection(DcMotor.Direction.REVERSE);
        motorRightA.setDirection(DcMotor.Direction.REVERSE);
        motorRightB.setDirection(DcMotor.Direction.REVERSE);

        servoTapeMeasure = mode.hardwareMap.servo.get("servoTapeMeasure");
        servoBeacon      = mode.hardwareMap.servo.get("servoBeacon");
        servoRight       = mode.hardwareMap.servo.get("servoRight");
        servoLeft        = mode.hardwareMap.servo.get("servoLeft");

        dim = mode.hardwareMap.deviceInterfaceModule.get("dim");
        sensorColor = ClassFactory.createSwerveColorSensor(mode,
                mode.hardwareMap.colorSensor.get("sensorColor"));
        sensorColor.enableLed(true);
        opticalSensor = mode.hardwareMap.opticalDistanceSensor.get("opticalDistance");

    }

    /**
     * Set the power for the right side drive motors.
     * @param power Double from 0 to 1.
     */
    public void setPowerRight(double power) {
        motorRightA.setPower(power);
        motorRightB.setPower(power);
    }

    /**
     * Set the power for the left side drive motors.
     * @param power Double from 0 to 1.
     */
    public void setPowerLeft(double power) {
        motorLeftA.setPower(power);
        motorLeftB.setPower(power);
    }

    /**
     * Set the movement of the tape measure motor.
     * @param setting MotorSetting enum value; FORWARD, STOP, or REVERSE.
     */
    public void setTapeMeasure(MotorSetting setting) {
        toggleCRServo(servoTapeMeasure, setting);
    }

    /**
     * Set the movement of the brush motor.
     * @param setting MotorSetting indicating the direction.
     */
    public void setBrush(MotorSetting setting) {
        toggleMotor(motorBrush, setting, BRUSH_SPEED);
    }

    /**
     * Set the direction of the lift: REVERSE, STOP, or FORWARD.
     * @param setting MotorSetting enum indicating the direction.
     */
    public void setLift(MotorSetting setting) {
        toggleMotor(motorLift, setting, LIFT_SPEED);
    }

    /**
     * Toggles a motor between three settings: FORWARD, STOP, and REVERSE.
     * @param toToggle Motor to change.
     * @param setting MotorSetting indicating the direction.
     * @param power Power value to use.
     */
    private void toggleMotor(DcMotor toToggle, MotorSetting setting, double power) {
        switch (setting) {
            case REVERSE:
                toToggle.setPower(-power);
                break;
            case STOP:
                toToggle.setPower(0);
                break;
            case FORWARD:
                toToggle.setPower(power);
                break;
            default:
                toToggle.setPower(0);
                break;
        }
    }

    /**
     * Toggle a continuous rotation servo in one of three directions: FORWARD, STOP, and REVERSE.
     * @param toToggle Servo to toggle.
     * @param setting MotorSetting indicating the direction.
     */
    private void toggleCRServo(Servo toToggle, MotorSetting setting) {
        switch (setting) {
            case REVERSE:
                toToggle.setPosition(CRS_REVERSE);
                break;
            case STOP:
                toToggle.setPosition(CRS_STOP);
                break;
            case FORWARD:
                toToggle.setPosition(CRS_FORWARD);
                break;
            default:
                toToggle.setPosition(CRS_STOP);
        }
    }

    /**
     *
     * @param allianceColor
     */
    public void pressBeacon(AllianceColor allianceColor) {

    }
}
