/*
 * Copyright (C) 2015 Powerstackers
 *
 * Code to run our 2015-16 robot.
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

import org.swerverobotics.library.SynchronousOpMode;

/**
 * @author Jonathan Thomas
 */
public class AutonomousProgram extends SynchronousOpMode {

    AllianceColor allianceColor;
    Robot robot;

    public AutonomousProgram(AllianceColor allianceColor) {
        this.allianceColor = allianceColor;
        this.robot = new Robot(this);
    }

    /**
     * Run the actual program.
     */
    @Override
    protected void main() throws InterruptedException {
        // Initialize any sensors and servos
        robot.initializeRobot();
        // Wait for the start of the match
        this.waitForStart();

        // Run any actions we desire
        robot.tapBeacon(allianceColor);
    }
}
