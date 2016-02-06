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

package com.powerstackers.resq.opmodes.autonomous;

import com.powerstackers.resq.common.AutonomousProgram;
import com.powerstackers.resq.common.enums.PublicEnums.AllianceColor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.swerverobotics.library.interfaces.Autonomous;

/**
 * @author Jonathan Thomas
 */
@Autonomous(name = "Blue autonomous", group = "Powerstackers")
public class BlueAutonomous extends LinearOpMode {
    DcMotor motor;
    public BlueAutonomous() {

        motor = hardwareMap.dcMotor.get("motorFLeft");
    }

    @Override
    public void runOpMode() throws InterruptedException {

    }

}
