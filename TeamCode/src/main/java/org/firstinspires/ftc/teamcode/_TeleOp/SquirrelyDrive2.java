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

package org.firstinspires.ftc.teamcode._TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad using Squirrely Wheels
 * Like normal tank drive (left and right sticks in y direction) +
 * squirrley traversal (left and right sticks in x direction)
 */
@TeleOp(name="SquirrelyDrive2", group="Test")  // @Autonomous(...) is the other common choice
//@Disabled
public class SquirrelyDrive2 extends OpMode {

	DcMotor motorFrontRight;
	DcMotor motorFrontLeft;
	DcMotor motorBackRight;
	DcMotor motorBackLeft;

	boolean bDebug = false;

	/**
	 * Constructor
	 */
	public SquirrelyDrive2() {

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */
	@Override
	public void init() {

		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */
		
		/*
		 * For this test, we assume the following,
		 *   There are four motors
		 *   "fl" and "bl" are front and back left wheels
		 *   "fr" and "br" are front and back right wheels
		 */
		try {
			motorFrontRight = hardwareMap.dcMotor.get("fr");
			motorFrontLeft = hardwareMap.dcMotor.get("fl");
			motorBackRight = hardwareMap.dcMotor.get("br");
			motorBackLeft = hardwareMap.dcMotor.get("bl");
			motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
			motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
		}
		catch (IllegalArgumentException iax) {
			bDebug = true;
		}
	}


	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		// tank drive
		// note that if y equal -1 then joystick is pushed all of the way forward.
		float left = -gamepad1.left_stick_y;
		float right = -gamepad1.right_stick_y;

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		left =  (float)scaleInput(left);
		right = (float)scaleInput(right);

		// squirrely drive is controlled by the x axis of both sticks
		float xLeft = gamepad1.left_stick_x;
		float xRight = gamepad1.right_stick_x;

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		xLeft =  (float)scaleInput(xLeft);
		xRight = (float)scaleInput(xRight);

		// combine turning and squirrely drive inputs and
		double fr = Range.clip(right+xRight, -1, 1);
		double br = Range.clip(right-xRight, -1, 1);
		double fl = Range.clip(left+xLeft, -1, 1);
		double bl = Range.clip(left-xLeft, -1, 1);

		// write the values to the motors
		if (!bDebug) {
			motorFrontRight.setPower(fr);
			motorBackRight.setPower(br);
			motorFrontLeft.setPower(fl);
			motorBackLeft.setPower(bl);
		}

		/*
		 * Send telemetry data back to driver station.
		 */
		telemetry.addData("Squirrely Tank Drive", "*** v2.1 ***");
		telemetry.addData("front left/right power:", "%.2f %.2f", fl, fr);
		telemetry.addData("back left/right power:", "%.2f %.2f", bl, br);
		telemetry.addData("gamepad1:", gamepad1);
		//telemetry.addData("gamepad2", gamepad2);
	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}
	
	/*
	 * This method scales the joystick input so for low joystick values, the 
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds. Raising to an odd power (3)
	 * preserves the sign of the input.
	 */
	double scaleInput(double dVal)  {
		return dVal*dVal*dVal;		// maps {-1,1} -> {-1,1}
	}

}
