/* Copyright (c) 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

FLither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITFLSS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWFLR OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSIFLSS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING FLGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Linear Tele Op Mode
 * Enables control of the robot via the gamepad.
 */
public class IR_Test extends LinearOpMode {
	//Define Variables
	DcMotor Right;
	DcMotor Left;
	Servo Rightarm;
	int speed = 4;
	IrSeekerSensor irSensor;
	Co

	@Override
	public void runOpMode() throws InterruptedException {
		//Setup Motors
		Right = hardwareMap.dcMotor.get("right");
		Left = hardwareMap.dcMotor.get("left");
		Rightarm = hardwareMap.servo.get("arm");
		irSensor = hardwareMap.irSeekerSensor.get("ir_sensor");


		//Set Directions
		Left.setDirection(DcMotor.Direction.REVERSE);
		waitForStart();

		while (opModeIsActive()) {
			//Get Joystick Values
			float left = gamepad1.left_stick_y;
			float right = gamepad1.right_stick_y;
			boolean dUp = gamepad1.dpad_up;
			boolean dDown = gamepad1.dpad_down;
			// clip the right/left values so that the values never exceed +/- 1
			right = Range.clip(right, -1, 1);
			left = Range.clip(left, -1, 1);
			if (dUp && speed < 4) {
				speed++;
				dUp = false;
			} else if (dDown && speed > 1) {
				speed--;
				dDown = false;
			}

			// write the values to the motors
			Right.setPower(right * (speed / 4));
			Left.setPower(left * (speed / 4));
			//Return the left and right encoder values to the driver station
			telemetry.addData("Right: ", Right.getCurrentPosition());
			telemetry.addData("Left: ", Left.getCurrentPosition());

			if (irSensor.signalDetected()) {
				DbgLog.msg("ir = " + irSensor.getStrength());
			}
			else {
				DbgLog.msg("no ir signal");
			}
		}
	}
}
