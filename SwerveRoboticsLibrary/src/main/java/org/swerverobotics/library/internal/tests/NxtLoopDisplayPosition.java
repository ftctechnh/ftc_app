/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.swerverobotics.library.internal.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * NXTLoopEncoderPosition
 * Demonstrates how to use a DC motor controller to display encoder values. This works
 * both with legacy and modern motor controllers.
 */
@TeleOp(name="Display Position (Loop)", group="Swerve Tests")
@Disabled
public class NxtLoopDisplayPosition extends OpMode {

	//----------------------------------------------------------------------------------------------
	// State
	//----------------------------------------------------------------------------------------------

	DcMotor motorRight;
	DcMotor motorLeft;

	int leftPos;
	int rightPos;

	//----------------------------------------------------------------------------------------------
	// Init
	//----------------------------------------------------------------------------------------------

	@Override
	public void init() {
		// initialize variables.
		leftPos = 0;
		rightPos = 0;

		// get references to the hardware.
		motorRight = hardwareMap.dcMotor.get("motorRight");
		motorLeft  = hardwareMap.dcMotor.get("motorLeft");

		// don't forget to reverse motorLeft
		motorLeft.setDirection(DcMotor.Direction.REVERSE);

		// reset the encoders.
		motorLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		motorRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

		// take it out of reset encoders so motors can drive
		motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		// set the motors to float so user can turn the motor shafts more easily.
		motorLeft.setPowerFloat();
		motorRight.setPowerFloat();
	}

	//----------------------------------------------------------------------------------------------
	// Loop
	//----------------------------------------------------------------------------------------------

	@Override
	public void loop() {

		leftPos = motorLeft.getCurrentPosition();
		rightPos = motorRight.getCurrentPosition();

		// send telemetry info.
		telemetry.addData("0. leftPos", String.format("%06d", leftPos));
		telemetry.addData("1. rightPos", String.format("%06d", rightPos));
	}

}
