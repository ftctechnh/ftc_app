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

import org.firstinspires.ftc.teamcode._Libs.AutoLib;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad using Squirrely Wheels
 */
@TeleOp(name="SquirrelyDrive1", group="Test")  // @Autonomous(...) is the other common choice
//@Disabled
public class SquirrelyDrive1 extends OpMode {

	DcMotor motorFrontRight;
	DcMotor motorFrontLeft;
	DcMotor motorBackRight;
	DcMotor motorBackLeft;

	boolean bDebug = false;

	/**
	 * Constructor
	 */
	public SquirrelyDrive1() {

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

		// turning drive is on the left stick
		float tx = gamepad1.left_stick_x;
		float ty = -gamepad1.left_stick_y;	// y is reversed :(
		float left = (ty + tx/2);
		float right = (ty - tx/2);

		// clip the right/left values so that the values never exceed +/- 1
		left = Range.clip(left, -1, 1);
		right = Range.clip(right, -1, 1);

		// scale the joystick values to make it easier to control
		// the robot more precisely at slower speeds.
		//left =  (float)scaleInput(left);
		//right = (float)scaleInput(right);

		// squirrely drive is on the right stick
		float x = gamepad1.right_stick_x;
		float y = -gamepad1.right_stick_y;	// y is reversed :(

		// clip the values so that the values never exceed +/- 1
		x = Range.clip(x, -1, 1);
		y = Range.clip(y, -1, 1);

		// direction angle of stick >> the relative direction we want to move
		double theta = Math.atan2(-x, y);	// stick angle: zero = +y, positive CCW, range +-pi
		double heading = theta * 180.0/Math.PI;		// radians to degrees

		// compute front and back wheel relative speeds needed to go in desired direction
		AutoLib.MotorPowers mp = AutoLib.GetSquirrelyWheelMotorPowers(heading);
		double front = mp.Front();
		double back = mp.Back();

		// power is the magnitude of the stick vector
		double power = Math.sqrt(x*x + y*y);

		// scale the values by the desired power
		front *= power;
		back *= power;

		// combine turning and squirrely drive inputs and
		double fr = Range.clip(front+right, -1, 1);
		double br = Range.clip(back+right, -1, 1);
		double fl = Range.clip(front+left, -1, 1);
		double bl = Range.clip(back+left, -1, 1);

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
		telemetry.addData("Text", "*** v1.1 ***");
		telemetry.addData("front left/right power:", "%.2f %.2f", fl, fr);
		telemetry.addData("back left/right power:", "%.2f %.2f", bl, br);
		telemetry.addData("heading:", "%.2f", heading);
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
	 * the robot more precisely at slower speeds.
	 */
	double scaleInput(double dVal)  {
		return dVal*dVal*dVal;		// maps {-1,1} -> {-1,1}
	}

}
