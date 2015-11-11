/*
 * Copyright (C) 2015 Powerstackers
 *
 * Basic configurations and capabilities of our
 *
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
 * Created by Jonathan on 9/26/2015.
 */
public class Robot {
    private DcMotor motorLeft;
    private DcMotor motorRight;

    private Servo servoArm;
    private Servo servoClaw;

    private TouchSensor touchSensor;
    private OpticalDistanceSensor opticalSensor;

    public Robot(OpMode mode) {
        motorLeft = mode.hardwareMap.dcMotor.get("motor_1");
        motorRight = mode.hardwareMap.dcMotor.get("motor_2");

        servoArm = mode.hardwareMap.servo.get("servo_1");
        servoClaw = mode.hardwareMap.servo.get("servo_6");

        touchSensor = mode.hardwareMap.touchSensor.get("touch_sensor");
        opticalSensor = mode.hardwareMap.opticalDistanceSensor.get("optical_distance_sensor");
    }

    public void setPower(double power) {
        motorLeft.setPower(power);
        motorRight.setPower(power);
    }


}
