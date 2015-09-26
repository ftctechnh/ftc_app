/*
 * Copyright (C) 2015 Powerstackers
 *
 * Simple teleop program.
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

package com.powerstackers.resq.opmodes;

import com.powerstackers.resq.opmodes.common.Robot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Teleop program made at the workshop.
 */
public class WorkshopTeleop extends OpMode{

    Robot robot = new Robot();

    @Override
    public void init() {
        robot.motorLeft = hardwareMap.dcMotor.get("motor_1");
        robot.motorRight = hardwareMap.dcMotor.get("motor_2");

        robot.servoArm = hardwareMap.servo.get("servo_1");
        robot.servoClaw = hardwareMap.servo.get("servo_2");

        robot.touchSensor = hardwareMap.touchSensor.get("sensor_touch");
        robot.opticalSensor = hardwareMap.opticalDistanceSensor.get("sensor_optical");
    }

    @Override
    public void loop() {

    }
}
