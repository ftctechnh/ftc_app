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







package com.qualcomm.ftcrobotcontroller.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;



/**
 * RoboWolves OpMode
 * //<p>
 * Enables control of the robot via the gamepad
 */


public class RoboWolvesOp extends OpMode {

	DcMotor motorRight; //driving
	DcMotor motorLeft; //driving
	DcMotor motorShoulder;
	DcMotor motorElbow;

	Servo servoHook;
	Servo servoPusher;

	double hookPosition;
	double pusherPosition;

	double servoDelta = .1;



	/**
	 * Constructor
	 */


	public RoboWolvesOp() {

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

		//devMode = DcMotorController.DeviceMode.WRITE_ONLY;

		motorRight = hardwareMap.dcMotor.get("motorRight");
		motorLeft = hardwareMap.dcMotor.get("motorLeft");
		motorLeft.setDirection(DcMotor.Direction.REVERSE);

		motorShoulder = hardwareMap.dcMotor.get("motorShoulder");
		motorElbow = hardwareMap.dcMotor.get("motorElbow");

		servoHook = hardwareMap.servo.get("servoHook");
		servoPusher = hardwareMap.servo.get("servoPusher");

	}


    /*\\
	 * This method will be called repeatedly in a loop
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */


	@Override


	public void loop() {
	/*
	 *
	 *
	 * Gamepad 1 controls the driving via the left and right stick of Gamepad 1
	 * Gamepad 2 controls the arm and the dumping action via the left and right stick of Gamepad 2
	 */


		float leftMotorPower = gamepad1.left_stick_y;
		float rightMotorPower = gamepad1.right_stick_y;
		float shoulderMotorPower = -gamepad2.left_stick_y;
		float elbowMotorPower = gamepad2.right_stick_y;

		// clip the right/left values so that the values never exceed +/- 1
		leftMotorPower = Range.clip(leftMotorPower, -1, 1);
		rightMotorPower = Range.clip(rightMotorPower, -1, 1);
		shoulderMotorPower = Range.clip(shoulderMotorPower, -1, 1);
		elbowMotorPower = Range.clip(elbowMotorPower, -1, 1);

		// scale the joystick value to make it easier to control

		// the robot more precisely at slower speeds.
		leftMotorPower = (float)scaleInput(leftMotorPower);
		rightMotorPower =  (float)scaleInput(rightMotorPower);
		shoulderMotorPower=  (float)scaleInput(shoulderMotorPower);
		elbowMotorPower =  (float)scaleInput(elbowMotorPower);


		/*if (gamepad2.a) {
			// if the A button is pushed on gamepad1, increment the position of
			// the arm servo.
			hookPosition += servoDelta;
		}

		if (gamepad2.y) {
			// if the Y button is pushed on gamepad1, decrease the position of
			// the arm servo.
			hookPosition -= servoDelta;
		}

		// update the position of the claw
		if (gamepad2.x) {
			pusherPosition += servoDelta;
		}

		if (gamepad2.b) {
			pusherPosition -= servoDelta;
		}*/

		// clip the position values so that they never exceed their allowed range.
		//hookPosition = Range.clip(hookPositi, ARM_MIN_RANGE, ARM_MAX_RANGE);
		//pusherPosition = Range.clip(clawPosition, CLAW_MIN_RANGE, CLAW_MAX_RANGE);

		// write position values to the wrist and claw servo
		/*servoHook.setPosition(hookPosition);
		servoPusher.setPosition(pusherPosition);
*/



		// write the values to the motors


		motorRight.setPower(rightMotorPower);
		motorLeft.setPower(leftMotorPower);
		motorElbow.setPower(elbowMotorPower);
		motorShoulder.setPower(shoulderMotorPower);




/*
	 * Send telemetry data back to driver station. Note that if we are using
	 * a legacy NXT-compatible motor controller, then the getPower() method
	 * will return a null value. The legacy NXT-compatible motor controllers
	 * are currently write only.
	 */

/*
		telemetry.addData("Text", "*** RoboWolves Supercool Robot Data***");
		telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", leftMotorPower));
		telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", rightMotorPower));
		telemetry.addData("shoulder tgt pwr", "shoulder pwr: " + String.format("%.2f", shoulderMotorPower));
		telemetry.addData("elbow tgt pwr", "elbow pwr: " + String.format("%.2f", elbowMotorPower));
		telemetry.addData("hook tgt pwr", "hook pwr: " + String.format("%.2f", hookPosition));
		telemetry.addData("pusher tgt pwr", "pusher pwr: " + String.format("%.2f", pusherPosition));*/
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


		double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,


				0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };


		// get the corresponding index for the scaleInput array.


		int index = (int) (dVal * 16.0);

		// index should be positive.


		if (index < 0) {


			index = -index;


		}
		// index cannot exceed size of array minus 1.


		if (index > 16) {


			index = 16;

		}

		// get value from the array.


		double dScale = 0.0;


		if (dVal < 0) {


			dScale = -scaleArray[index];


		} else {


			dScale = scaleArray[index];


		}
		// return scaled value.

		return dScale;

	}

}