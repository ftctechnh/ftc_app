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


public class AutoStraightDelayed extends OpMode {

	final static double MOTOR_POWER = 0.15;


	DcMotor motor2PP;
	DcMotor motorRight;
	DcMotor motorLeft;


	/**
	 * Constructor
	 */
	public AutoStraightDelayed() {

	}

	@Override
	public void init() {


		motorRight = hardwareMap.dcMotor.get("motor_2");
		motorLeft = hardwareMap.dcMotor.get("motor_1");
		motor2PP = hardwareMap.dcMotor.get("motor_2P");
		motorLeft.setDirection(DcMotor.Direction.REVERSE);



	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {
		double SecondPower = 0;
		double reflection = 0.0;
		double left = 0, right = 0;


		if (this.time > 1 && this.time <= 4) {
			// from 1 to 5 seconds, run the motors for five seconds.
			left = 0.75;
			right = 0.75;
		} else if (this.time > 4.3 && this.time <= 6) {
			// bet   ween 5.5 and 7 seconds, idle.
			left = 0.5;
			right = 0.5;
		} else if (this.time > 6.d && this.time <= 7.25d) {
			// between 7 and 8.25 seconds, point turn left.
			left = 0.25;
			right = 0.25;
		} else if (this.time > 7.25d && this.time <= 15.5d){
			// between 8.25 seconds and 11.5 seconds, run the drive motors so the robot goes up the mountain
			left = .15;
			right = .15;
		} else if (this.time > 15.5 && this.time <= 16.25){
			SecondPower = .75;
		}

		/*
		 * set the motor power
		 */
		motorRight.setPower(-left);
		motorLeft.setPower(-right);
		motor2PP.setPower(SecondPower);

		telemetry.addData("Text", "*** Robot Data***");
		telemetry.addData("time", "elapsed time: " + Double.toString(this.time));
		telemetry.addData("left tgt pwr",  "left  pwr: " + Double.toString(left));
		telemetry.addData("right tgt pwr", "right pwr: " + Double.toString(right));
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