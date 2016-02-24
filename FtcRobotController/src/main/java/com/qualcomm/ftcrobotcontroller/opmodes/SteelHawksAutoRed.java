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

import com.qualcomm.hardware.hitechnic.HiTechnicNxtDcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.TouchSensor;


import java.util.Timer;

/**
 * TeleOp Mode
 * //<p>
 * Enables control of the robot via the gamepad
 */
public class SteelHawksAutoRed extends OpMode {

	DcMotor motorRight; //driving


	DcMotor motorLeft; //driving


	DcMotor harvester;


	DcMotor motorArmUp;


	DcMotor motorArmDump;


	DcMotorController.DeviceMode devMode;


	DcMotorController legacyController;


	TouchSensor autoTouch, armTouch;




	float leftMotorPower, rightMotorPower;

	boolean isMoving = false;
	double movingArmUpstart = 0;
	double  movingArmUpend =0;
	double movingArmDownstart = 0;
	double  movingArmDownend =0;
	double dumpArmStart = 0;
	double dumpArmEnd = 0;
	double closingArmStart = 0;
	double closingArmEnd = 0;

	double start = 0, end = 0;
	double motorPowerLeft, motorPowerRight;
	boolean killTheBot = false;

	//Servo claw;
	//Servo arm;

	/**
	 * Constructor
	 */
	public SteelHawksAutoRed() {

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


		devMode = DcMotorController.DeviceMode.WRITE_ONLY;

		motorRight = hardwareMap.dcMotor.get("motor_2");


		motorLeft = hardwareMap.dcMotor.get("motor_1");


		motorLeft.setDirection(DcMotor.Direction.REVERSE);

		motorArmUp = hardwareMap.dcMotor.get("motor_3");


		motorArmDump = hardwareMap.dcMotor.get("motor_4");


		legacyController = hardwareMap.dcMotorController.get("legacyController");


		harvester = hardwareMap.dcMotor.get("harvester");

		armTouch = hardwareMap.touchSensor.get("armTouch");
		autoTouch = hardwareMap.touchSensor.get("autoTouch");
	}

	/*\\
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

		/*
		 * Gamepad 1
		 * 
		 * Gamepad 1 controls the motors via the left stick, and it controls the
		 * wrist/claw via the a,b, x, y buttons
		 */

		// throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
		// 1 is full down
		// direction: left_stick_x ranges from -1 to 1, where -1 is full left
		// and 1 is full right


		// clip the right/left values so that the values never exceed +/- 1


		// write here the values for the arm motor, remember that you only need throttle and to change for gamepad2 (and left stick)


		// write here the values for the dump motor, remember you only need the throttle and to change for gamepad2 (and right stick)

		// write the values to the motors

		//motorArm.setPower(movongArmUp);
		//motorArm2.setPower(dumpingArm);

    
    
    // if the code has not run, killTheBot will be false (default).
    // This if statement will run the code since it was never run.
    
	if (!killTheBot) {
    
    // Time gone by: 0 - 1 sec
		// Turn left for .5 sec
		if (this.time <= 1) {
			motorPowerRight = (0.5);
			motorPowerLeft = (0);


		}
    
    // Time gone by: 0.6 - 2.5  sec
		// Move forward for 2 sec
		if (this.time > .5 && this.time <= 2.5) {
			motorPowerRight = (1);
			motorPowerLeft = (1);


		}
    
    // Time gone by: 2.6 - 3 sec
		// Turn left for .5 sec
		if (this.time > 2.5 && this.time <= 3.0) {
			motorPowerRight = (0.5);
			motorPowerLeft = (0);

		}
    
    // Time gone by: 3 to *whatever* sec
		// Move forward until touch sensor is pressed
		if (this.time > 3.0) {
      // Go forward, slower than before
			motorPowerLeft = (.75);
			motorPowerRight = .75;
			
      // If the touch sensor is pressed
			if (autoTouch.isPressed()) {
        // Stop moving
				motorPowerLeft = 0;
				motorPowerRight = 0;
        // This will stop the code from running again
				killTheBot = true;

			}

		}

		motorLeft.setPower(motorPowerLeft);
		motorRight.setPower(motorPowerRight);
	}
/*
		//SAVE THIS CODE FOR LATER IF NEEDED
		// update the position of the arm.
		if (gamepad1.y && isMoving == false) {
			// if the Y button is pushed on gamepad1, increment the position of
			// the arm servo.
			motorArm.setPower(0.2);
			isMoving = true;
			movingArmUpstart = System.currentTimeMillis();
			movingArmUpend = movingArmUpstart  + 0.5*1000;
		}
		if(movingArmUpend<System.currentTimeMillis())
		{
			motorArm.setPower(0);
			isMoving = false;
		}
		if (gamepad1.a && isMoving ==false) {
			// if the A button is pushed on gamepad1, decrease the position of
			// the arm servo.
			motorArm.setPower(-0.2);
			isMoving = true;
			movingArmDownstart = System.currentTimeMillis();
			movingArmDownend = movingArmDownstart + 0.5*1000;
		}
		if(movingArmDownend<System.currentTimeMillis())
		{
			motorArm.setPower(-0.2);
			isMoving = true;
		}
		// update the position of the claw
		if (gamepad1.x && isMoving==false) {
			// if the A button is pushed on gamepad1, decrease the position of
			// the arm servo.
			motorArmDump.setPower(0.2);
			isMoving = true;
			dumpArmStart = System.currentTimeMillis();
			dumpArmEnd = dumpArmStart + 0.5*1000;
		}
		if(dumpArmEnd<System.currentTimeMillis())
		{
			motorArm.setPower(-0.2);
			isMoving = true;
		}
		if (gamepad1.b && isMoving==false) {
			// if the A button is pushed on gamepad1, decrease the position of
			// the arm servo.
			motorArmDump.setPower(-0.2);
			isMoving = true;
			closingArmStart = System.currentTimeMillis();
			closingArmEnd = closingArmStart + 0.5*1000;
		}
		if(closingArmEnd<System.currentTimeMillis())
		{
			motorArmDump.setPower(-0.2);
			isMoving = true;
		}
		// clip the position values so that they never exceed their allowed range.
		// write position values to the wrist and claw servo
		
*/

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
		///telemetry.addData("Text", "*** Robot Data***");
		//telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", leftMotorPower));
		//telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", rightMotorPower));



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
