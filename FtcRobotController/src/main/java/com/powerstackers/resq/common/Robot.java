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

        servoTapeMeasure = mode.hardwareMap.servo.get("servo_2");
        servoBeacon      = mode.hardwareMap.servo.get("servoBeacon");
        servoRight       = mode.hardwareMap.servo.get("servoRight");
        servoLeft        = mode.hardwareMap.servo.get("servoLeft");

        dim = mode.hardwareMap.deviceInterfaceModule.get("dim");
        sensorColor = ClassFactory.createSwerveColorSensor(mode,
                mode.hardwareMap.colorSensor.get("sensorColor"));
        sensorColor.enableLed(true);

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
     * @param setting ServoSetting enum value; FORWARD, STOP, or REVERSE.
     */
    public void setTapeMeasure(ServoSetting setting) {
        switch (setting) {
            case REVERSE:
                servoTapeMeasure.setPosition(CRS_REVERSE);
                break;
            case STOP:
                servoTapeMeasure.setPosition(CRS_STOP);
                break;
            case FORWARD:
                servoTapeMeasure.setPosition(CRS_FORWARD);
                break;
            default:
                servoTapeMeasure.setPosition(CRS_STOP);
        }
    }

    /**
     * Set the movement of the brush motor.
     * @param setting ServoSetting indicating the direction.
     */
    public void setBrush(ServoSetting setting) {
        switch (setting) {
            case REVERSE:
                motorBrush.setPower(-BRUSH_SPEED);
                break;
            case STOP:
                motorBrush.setPower(0);
                break;
            case FORWARD:
                motorBrush.setPower(BRUSH_SPEED);
                break;
            default:
                motorBrush.setPower(0);
                break;
        }
    }

    /**
     * Set the direction of the lift: REVERSE, STOP, or FORWARD.
     * @param setting ServoSetting enum indicating the direction.
     */
    public void setLift(ServoSetting setting) {
        switch (setting) {
            case REVERSE:
                motorLift.setPower(-LIFT_SPEED);
                break;
            case STOP:
                motorLift.setPower(0);
                break;
            case FORWARD:
                motorLift.setPower(LIFT_SPEED);
                break;
            default:
                motorLift.setPower(0);
                break;
        }
    }
}
