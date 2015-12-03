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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;



/**
 * TeleOpTank Mode
 * <p>
 * Enables control of the robot via the gamepad
 */

@TeleOp(name="TeleOpMecanum")
public class  TeleOpMecanum extends OpMode {
	DcMotor motorFrontRight;
	DcMotor motorBackRight;
	DcMotor motorFrontLeft;
	DcMotor motorBackLeft;
	DcMotor churroMotor;
	DcMotor armMotor;

	Servo servoRightWing;
	Servo servoLeftWing;
	Servo servoClimberRelease;

	boolean wasRightBumper = false;
	boolean wasLeftBumper = false;
	boolean wasB = false;

	boolean rightWingDown = false;
	boolean leftWingDown = false;
	boolean climberRelease = false;

	public TeleOpMecanum() {

	}

	/*
	 * Code to run when the op mode is initialized goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
	 */
	@Override
	public void init() {
		motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
		motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
		motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
		motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
		churroMotor = hardwareMap.dcMotor.get(("churroMotor"));
		armMotor = hardwareMap.dcMotor.get(("armMotor"));

		servoLeftWing = hardwareMap.servo.get("servoLeftWing");
		servoRightWing = hardwareMap.servo.get("servoRightWing");
		servoClimberRelease = hardwareMap.servo.get("servoClimberRelease");

		motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
		motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop()
	{
		float rightX = Range.clip(gamepad1.right_stick_x, -1, 1);
		float rightY = Range.clip(gamepad1.right_stick_y, -1, 1);
		float leftX = Range.clip(gamepad1.left_stick_x, -1, 1);
		float leftY = Range.clip(gamepad1.left_stick_y, -1, 1);

		boolean rightBumper = gamepad2.right_bumper;
		boolean leftBumper = gamepad2.left_bumper;
		boolean b = gamepad2.b;

		boolean upDpad = gamepad1.dpad_up;
		boolean downDpad = gamepad1.dpad_down;

		boolean upDpad2 = gamepad2.dpad_up;
		boolean downDpad2 = gamepad2.dpad_down;

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		rightX = scaleJoystickValue(rightX);
		rightY = scaleJoystickValue(rightY);
		leftX = scaleJoystickValue(leftX);
		leftY =  scaleJoystickValue(leftY);
		
		// write the values to the motors
		motorFrontRight.setPower(Range.clip(leftY + rightX + leftX, -1, 1));
		motorBackRight.setPower(Range.clip(leftY + rightX - leftX, -1, 1));
		motorFrontLeft.setPower(Range.clip(leftY - rightX - leftX, -1, 1));
		motorBackLeft.setPower(Range.clip(leftY - rightX + leftX, -1, 1));

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */

		if (b && !wasB)
		{
			climberRelease = !climberRelease;
		}

		if (rightBumper && !wasRightBumper)
		{
			rightWingDown = !rightWingDown;
		}

		if (leftBumper && !wasLeftBumper)
		{
			leftWingDown = !leftWingDown;
		}

		if (climberRelease)
		{
			servoClimberRelease.setPosition(0.90);
		}
		else
		{
			 servoClimberRelease.setPosition(0);
		}

		if (rightWingDown)
		{
			servoRightWing.setPosition(0.85);
		}
		else
		{
			servoRightWing.setPosition(0.10);
		}

		if (leftWingDown)
		{
			servoLeftWing.setPosition(0.10);
		}
		else
		{
			servoLeftWing.setPosition(0.83);
		}

		if (upDpad)
		{
			churroMotor.setPower(0.95);
		}
		else if (downDpad)
		{
			churroMotor.setPower(-0.95);
		}
		else
		{
			churroMotor.setPower(0);
		}

		if (upDpad2)
		{
			armMotor.setPower(0.6);
		}
		else if (downDpad2)
		{
			armMotor.setPower(-0.6);
		}
		else
		{
			armMotor.setPower(0);
		}


		wasB = b;
		wasLeftBumper = leftBumper;
		wasRightBumper = rightBumper;

        telemetry.addData("Text", rightX + ", " + rightY + ", " + leftX + ", " + leftY);
		telemetry.addData("rightWingPos",servoRightWing.getPosition());
		telemetry.addData("leftWingPos",servoLeftWing.getPosition());
	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}

	float scaleJoystickValue(float joystickValue)
	{
		if(joystickValue > 0)
		{
			return (float)((joystickValue*joystickValue)*.62);
		}
		else
		{
			return (float)(-(joystickValue*joystickValue)*.62);
		}
	}

}
