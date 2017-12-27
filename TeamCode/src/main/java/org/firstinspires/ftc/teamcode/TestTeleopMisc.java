/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

// Created by Roma Bhatia  on 9/21/17
//
//
// Last edit: 10/21/17 by Mrinaal Ramachandran

/**
 * This file provides basic Teleop driving for a Holonomic robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Holonomic hardware class to define the devices on the robot.
 * All device access is managed through the HolonomicHardware class.
 *
 * This particular OpMode executes a basic Holonomic Drive Teleop for a Holonomic bot with omniwheels
 * facing at 45 degree angles.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
    /*
    * Created by Roma Bhatia (@sugarcrystals01) on 12/17/17
    * Last edit: 12/17/17
    */
@TeleOp(name="Test_TeleOp", group="We Love Pi")

public class TestTeleopMisc extends OpMode {

    // DECLARE OPMODE MEMBERS
    TestHardwareMisc robot = new TestHardwareMisc(); // use the class created to define a Pushbot's hardware

    double limiter = 1;

    @Override
    public void init() {
        // INIT robot
        robot.init(hardwareMap);
        robot.dropper.setPosition(0.5);

        // TELL DRIVER STATION THAT ROBOT IS INIT
        telemetry.addData("Status", "Initialized");    //
    }

    @Override
    public void loop() {





            robot.dropper.setPosition(gamepad1.right_trigger);








    // TELEMETRY WITH INFO ABOUT POWER, AND VALUES OF (X,Y)

        telemetry.addData("test","%b",gamepad1.right_trigger);
    }
}
