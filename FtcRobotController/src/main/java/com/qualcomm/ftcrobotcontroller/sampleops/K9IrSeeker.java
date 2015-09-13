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

package com.qualcomm.ftcrobotcontroller.sampleops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */
public class K9IrSeeker extends OpMode {

	final static double MOTOR_POWER = 0.15; // Higher values will cause the robot to move
											// faster
	final static double HOLD_IR_SIGNAL_STRENGTH = 0.50; // Higher values will cause the
														// robot to follow closer

	double armPosition;
	double clawPosition;

	DcMotor motorRight;
	DcMotor motorLeft;
	Servo claw;
	Servo arm;
	IrSeekerSensor irSeeker;

	/**
	 * Constructor
	 */
	public K9IrSeeker() {

	}

	/*
	 * Code to run when the op mode is initialized goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */
	@Override
	public void init() {

		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note that the names
		 * of the devices must match the names used when you configured your robot and
		 * created the configuration file.
		 */

		/*
		 * For the demo Tetrix K9 bot we assume the following, There are two motors
		 * "motor_1" and "motor_2" "motor_1" is on the right side of the bot. "motor_2" is
		 * on the left side of the bot.
		 *
		 * We also assume that there are two servos "servo_1" and "servo_6" "servo_1"
		 * controls the arm joint of the manipulator. "servo_6" controls the claw joint of
		 * the manipulator.
		 */
		this.motorRight = this.hardwareMap.dcMotor.get("motor_2");
		this.motorLeft = this.hardwareMap.dcMotor.get("motor_1");
		this.motorLeft.setDirection(DcMotor.Direction.REVERSE);

		this.arm = this.hardwareMap.servo.get("servo_1");
		this.claw = this.hardwareMap.servo.get("servo_6");

		// set the starting position of the wrist and claw
		this.armPosition = 0.1;
		this.clawPosition = 0.25;

		/*
		 * We also assume that we have a Hitechnic IR Seeker v2 sensor with a name of
		 * "ir_seeker" configured for our robot.
		 */
		this.irSeeker = this.hardwareMap.irSeekerSensor.get("ir_seeker");
	}

	/*
	 * This method will be called repeatedly in a loop
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {
		double angle = 0.0;
		double strength = 0.0;
		double left, right = 0.0;

		// keep manipulator out of the way.
		this.arm.setPosition(this.armPosition);
		this.claw.setPosition(this.clawPosition);

		/*
		 * Do we detect an IR signal?
		 */
		if (this.irSeeker.signalDetected()) {
			/*
			 * Signal was detected. Follow it.
			 */

			/*
			 * Get angle and strength of the signal. Note an angle of zero implies straight
			 * ahead. A negative angle implies that the source is to the left. A positive
			 * angle implies that the source is to the right.
			 */
			angle = this.irSeeker.getAngle();
			strength = this.irSeeker.getStrength();

			if (angle < -60) {
				/*
				 * IR source is to the way left. Point turn to the left.
				 */
				left = -K9IrSeeker.MOTOR_POWER;
				right = K9IrSeeker.MOTOR_POWER;

			} else if (angle < -5) {
				// turn to the left and move forward.
				left = K9IrSeeker.MOTOR_POWER - 0.05;
				right = K9IrSeeker.MOTOR_POWER;
			} else if (angle > 5 && angle < 60) {
				// turn to the right and move forward.
				left = K9IrSeeker.MOTOR_POWER;
				right = K9IrSeeker.MOTOR_POWER - 0.05;
			} else if (angle > 60) {
				// point turn to right.
				left = K9IrSeeker.MOTOR_POWER;
				right = -K9IrSeeker.MOTOR_POWER;
			} else if (strength < K9IrSeeker.HOLD_IR_SIGNAL_STRENGTH) {
				/*
				 * Signal is dead ahead but weak. Move forward towards signal
				 */
				left = K9IrSeeker.MOTOR_POWER;
				right = K9IrSeeker.MOTOR_POWER;
			} else {
				/*
				 * Signal is dead ahead and strong. Stop motors.
				 */
				left = 0.0;
				right = 0.0;
			}
		} else {
			/*
			 * Signal was not detected. Shut off motors
			 */
			left = 0.0;
			right = 0.0;
		}

		/*
		 * set the motor power
		 */
		this.motorRight.setPower(right);
		this.motorLeft.setPower(left);

		/*
		 * Send telemetry data back to driver station. Note that if we are using a legacy
		 * NXT-compatible motor controller, then the getPower() method will return a null
		 * value. The legacy NXT-compatible motor controllers are currently write only.
		 */

		this.telemetry.addData("Text", "*** Robot Data***");
		this.telemetry.addData("angle", "angle:  " + Double.toString(angle));
		this.telemetry.addData("strength", "sig strength: " + Double.toString(strength));
		this.telemetry.addData("left tgt pwr", "left  pwr: " + Double.toString(left));
		this.telemetry.addData("right tgt pwr", "right pwr: " + Double.toString(right));
	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}

}
