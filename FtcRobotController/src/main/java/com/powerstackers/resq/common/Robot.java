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
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * A general representation of a robot, with simple interaction methods.
 * @author Jonathan Thomas
 */
public class Robot {
    private DcMotor motorLeftA;
    private DcMotor motorLeftB;
    private DcMotor motorRightA;
    private DcMotor motorRightB;

    private Servo servoArm;
    private Servo servoClaw;

    private TouchSensor touchSensor;
    public OpticalDistanceSensor opticalSensor;

    /**
     * Construct a Robot object.
     * @param mode The OpMode in which the robot is being used.
     */
    public Robot(OpMode mode) {
        motorLeftA = mode.hardwareMap.dcMotor.get("motor_1");
        motorLeftB = mode.hardwareMap.dcMotor.get("motor_3");
        motorRightA = mode.hardwareMap.dcMotor.get("motor_2");
        motorRightB = mode.hardwareMap.dcMotor.get("motor_4");

        servoArm = mode.hardwareMap.servo.get("servo_1");
        servoClaw = mode.hardwareMap.servo.get("servo_6");

        touchSensor = mode.hardwareMap.touchSensor.get("touch_sensor");
        opticalSensor = mode.hardwareMap.opticalDistanceSensor.get("optical_distance_sensor");
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


}
