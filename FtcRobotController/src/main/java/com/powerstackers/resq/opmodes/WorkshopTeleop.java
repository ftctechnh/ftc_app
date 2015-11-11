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

import com.powerstackers.resq.common.Robot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Teleop program made at the workshop.
 */
public class WorkshopTeleop extends OpMode{

    Robot robot;

    @Override
    public void init() {
        robot = new Robot(this);
    }

    @Override
    public void loop() {

        int light = robot.opticalSensor.getLightDetectedRaw();

        robot.toString();


        //telemetry.addData("optical", String.format("%d", light));
    }
}
