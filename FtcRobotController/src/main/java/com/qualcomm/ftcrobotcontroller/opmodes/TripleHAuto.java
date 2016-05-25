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


// From SteelHawksAutoBlue.java

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.hardware.hitechnic.HiTechnicNxtDcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.Timer;

/**
 * TeleOp Mode
 */
public class ArtRobot extends OpMode {

	// position of the arm servo.

	DcMotor motorRight;
	DcMotor motorLeft;
  
	Servo arm;
	
	final static double servoDelta = 0.01;

	// variables for autonomous only
	double motorPowerRight, motorPowerLeft;
	boolean killTheBot;


	/**
	 * Constructor - make sure the access keyword is public and is the same name as the class!
	 */

	public ArtRobot() {

	}

	/*
    * Code to run when the op mode is first enabled goes here
	 */

	@Override
	public void init() {

	/*
    * Use the hardwareMap to get the dc motors and servos by name. Note
    * that the names of the devices must match the names used when you
    * configured your robot and created the configuration file.
  */

		motorRight = hardwareMap.dcMotor.get("right");
		motorLeft = hardwareMap.dcMotor.get("left");
		motorLeft.setDirection(DcMotor.Direction.REVERSE);

    arm = hardwareMap.servo.get("arm");

	}

    /*\\
    * This method will be called repeatedly in a loop
    *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
    */


	@Override
	public void loop() {


		// if the code has not run, killTheBot will be false (default).
		// This if statement will run the code since it was never run.

		if (!killTheBot) {
			
      /*
				Draw Triangle
			*/
			
			// 0 - 1 sec, turn to the right a bit
			if (this.time <= 1) {
				motorPowerLeft = 0.2;
				motorPowerRight = 0;
			}
			// 1 - 2 sec, place the marker down
			if (this.time > 1 && this.time <= 2) {
				motorPowerLeft = 0;
				motorPowerRight = 0;
				arm.setPosition(0);
			}
			// 2 - 4 sec, go for 2 seconds
			if (this.time > 2 && this.time <= 4) {
				motorPowerLeft = 1;
				motorPowerRight = 1;
			}
			// 4 - 5 sec, lift the marker up
			if (this.time > 4 && this.time <= 5) {
				motorPowerLeft = 0;
				motorPowerRight = 0;
				arm.setPosition(1);
			}
			// 5 - 6 sec, go back a bit and rotate to the right a bit
			if (this.time > 5 && this.time <= 5) {
				motorPowerLeft = 0.2;
				motorPowerRight = 0.4;
			}
			// 6 - 7 sec, place the marker down
			if (this.time > 6 && this.time <= 7) {
				motorPowerLeft = 0;
				motorPowerRight = 0;
				arm.setPosition(0);
			}
			// 7 - 9 sec, go for 2 seconds
			if (this.time > 7 && this.time <= 9) {
				motorPowerLeft = 1;
				motorPowerRight = 1;
			}
			// 9 - 10 sec, lift the marker up
			if (this.time > 9 && this.time <= 10) {
				motorPowerLeft = 0;
				motorPowerRight = 0;
				arm.setPosition(1);
			}
			// 10 - 11 sec, go back a bit and rotate to the right a bit
			if (this.time > 10 && this.time <= 11) {
				motorPowerLeft = 0.2;
				motorPowerRight = 0.4;
			}
			// 11 - 12 sec, place the marker down
			if (this.time > 11 && this.time <= 12) {
				motorPowerLeft = 0;
				motorPowerRight = 0;
				arm.setPosition(0);
			}
			// 12 - 14 sec, go for 2 seconds
			if (this.time > 12 && this.time <= 14) {
				motorPowerLeft = 1;
				motorPowerRight = 1;
			}

			
			/*
				Draw Circle
			*/
			
			// 14 - 15 sec
			if (this.time > 14 && this.time <= 15) {
				motorPowerLeft = 0;
				motorPowerRight = 0;
				arm.setPosition(1);
			}
			// 15 - 17 sec
			if (this.time > 15 && this.time <= 17) {
				motorPowerLeft = 1;
				motorPowerRight = 1;
			}
			// 17 - 18 sec
			if (this.time > 17 && this.time <= 18) {
				motorPowerLeft = 0;
				motorPowerRight = 0;
				arm.setPosition(0);
			}
			// 18 - 24 sec
			if (this.time > 17 && this.time <= 23) {
				motorPowerLeft = 0;
				motorPowerRight = 1;
			}
			// 24 - 25 sec
			if (this.time > 23 && this.time <= 24) {
				motorPowerLeft = 0;
				motorPowerRight = 0;
				arm.setPosition(1);
			}
			
			/*
				Draw Square
			*/
			
			// 25 - 26 sec
			if (this.time > 24 && this.time <= 25) {
				// Stuff
			}
			
			
			motorLeft.setPower(motorPowerLeft);
			motorRight.setPower(motorPowerRight);
		}
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
