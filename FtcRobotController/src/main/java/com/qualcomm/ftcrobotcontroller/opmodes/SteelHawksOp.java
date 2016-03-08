/* Copyright (c) 2014 Qualcomm Technologies Inc
l rights reserved.
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
 * SteelHawks OpMode
 * //<p>
 * Enables control of the robot via the gamepad
 */
public class SteelHawksOp extends OpMode
{

	final static double CLIMBER_LEFT_MIN_RANGE  = 0.35;
	final static double CLIMBER_LEFT_MAX_RANGE  = 1;
    final static double CLIMBER_RIGHT_MIN_RANGE = 0;
    final static double CLIMBER_RIGHT_MAX_RANGE = .45;
	final static double WINCHHOOK_MIN_RANGE  = 0;
	final static double WINCHHOOK_MAX_RANGE  = 1;
    final static double CLIMBER_DUMP_MIN_RANGE = 0;
    final static double CLIMBER_DUMP_MAX_RANGE = 1;

	// position of the arm servo.

	DcMotor motorRight; //driving
	DcMotor motorLeft; //driving
	DcMotor motorHarvester;
	DcMotor motorShoulder;
	DcMotor motorArm;
	DcMotor motorWinch;
  
	Servo climberLeft;
    Servo climberRight;
	Servo winchHook;
    Servo climberDump;
  
	boolean isTurboOnGamePad1;
	boolean isTurboOnGamePad2;
	double climberPositionLeft;
    double climberPositionRight;
    double winchHookPosition;
    double climberDumpPosition;
	final static double servoDelta = 0.01;




	/**
	 * Constructor
	 */

	public SteelHawksOp()
	{


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

	motorRight = hardwareMap.dcMotor.get("motorRight");
	motorLeft = hardwareMap.dcMotor.get("motorLeft");
	motorHarvester = hardwareMap.dcMotor.get("motorHarvester");
	motorShoulder = hardwareMap.dcMotor.get("motorShoulder");
	motorArm = hardwareMap.dcMotor.get("motorArm");
	motorWinch = hardwareMap.dcMotor.get("motorWinch");
	
	climberLeft = hardwareMap.servo.get("climberLeft");
    climberRight = hardwareMap.servo.get("climberRight");
	winchHook = hardwareMap.servo.get("winchHook");
    climberDump = hardwareMap.servo.get("climberDump");

       climberPositionLeft = 1.0;
        climberPositionRight = 0.0;
        winchHookPosition = 1.0;
        climberDumpPosition = 0.0;


	}

    /*\\
    * This method will be called repeatedly in a loop
    *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
    */


	@Override
	public void loop() {

         /*
        GamePad1:
	    Left & Right Analog Sticks: motorLeft and motorRight Turbo
	    Left & Right Triggers: harvester
	    DPad Left, DPad Right: climber servo left
	    DPad Up, DPad Down: climber servo right
	    Left & Right Bumpers: motorWinch
	    Y Button - Turbo Mode (motorLeft + motorRight Speed)
	    A Button - Normal Mode (motorLeft + motorRight speed)


        GamePad2:
        left and Right Bumper : winchHook
        left stick : shoulder
        right stick : arm
        Y Button - Turbo Mode (motorLeft + motorRight Speed)
	    A Button - Normal Mode (motorLeft + motorRight speed)
        DPad Left, DPad right: Climber dump
        */

        //GamePad 1
        float rightMotorPower = gamepad1.left_stick_y;
        float leftMotorPower = -gamepad1.right_stick_y;

        float harvesterPower = gamepad1.right_trigger;
        float harvesterPowerReversed = gamepad1.left_trigger;

        boolean turboPower1 = gamepad1.y;
        boolean normalPower1 = gamepad1.a;
        if (turboPower1) {
            isTurboOnGamePad1 = true;
        }
        if (normalPower1) {
            isTurboOnGamePad1 = false;
        }




        // Gamepad 2
        float shoulderPower = gamepad2.left_stick_y;
        float armPower = gamepad2.right_stick_y;


        boolean turboPower2 = gamepad2.y;
        boolean normalPower2 = gamepad2.a;
        if (turboPower2) {
            isTurboOnGamePad2 = true;
        }
        if (normalPower2) {
            isTurboOnGamePad2 = false;
        }


        // Servo Positioning Increased/Decreased by constant servoDelta
        if (gamepad2.left_bumper) {
            winchHookPosition += servoDelta;
        }
        if (gamepad2.right_bumper) {
            winchHookPosition -= servoDelta;
        }

        if (gamepad1.dpad_left) {
            climberPositionLeft += servoDelta;
        }
        if (gamepad1.dpad_right) {
            climberPositionLeft -= servoDelta;
        }

        if (gamepad1.dpad_up) {
            climberPositionRight += servoDelta;
        }
        if (gamepad1.dpad_down) {
            climberPositionRight -= servoDelta;
        }


        if (gamepad2.dpad_up) {
            climberDumpPosition += servoDelta;
        }
        if (gamepad2.dpad_down) {
            climberDumpPosition -= servoDelta;
        }



            // Clip servos to within their min/max range
            winchHookPosition = Range.clip(winchHookPosition, WINCHHOOK_MIN_RANGE, WINCHHOOK_MAX_RANGE);
            climberPositionLeft = Range.clip(climberPositionLeft, CLIMBER_LEFT_MIN_RANGE, CLIMBER_LEFT_MAX_RANGE);
            climberPositionRight = Range.clip(climberPositionRight, CLIMBER_RIGHT_MIN_RANGE, CLIMBER_RIGHT_MAX_RANGE);
            climberDumpPosition = Range.clip(climberDumpPosition, CLIMBER_DUMP_MIN_RANGE, CLIMBER_DUMP_MAX_RANGE);


            // Set servo positions
            climberLeft.setPosition(climberPositionLeft);
            climberRight.setPosition(climberPositionRight);
            winchHook.setPosition(winchHookPosition);
            climberDump.setPosition(climberDumpPosition);



        // clip the right/left values so that the values never exceed +/- 1

        leftMotorPower = Range.clip(leftMotorPower, -1, 1);
        rightMotorPower = Range.clip(rightMotorPower, -1, 1);

        shoulderPower = Range.clip(shoulderPower, -1, 1);
        armPower = Range.clip(armPower, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        leftMotorPower = (float) scaleInput(leftMotorPower);
        rightMotorPower = (float) scaleInput(rightMotorPower);
        shoulderPower = (float) scaleInput(shoulderPower);
        armPower = (float) scaleInput(armPower);

        // write the values to the motors
        motorRight.setPower(isTurboOnGamePad1 ? rightMotorPower : rightMotorPower / 3);
        motorLeft.setPower(isTurboOnGamePad1 ? leftMotorPower : leftMotorPower / 3);
        motorShoulder.setPower(isTurboOnGamePad2 ? shoulderPower : shoulderPower / 3);
        motorArm.setPower(isTurboOnGamePad2 ? armPower : armPower / 3);

        if (harvesterPower > 0.2 && harvesterPower < 0.6) {
            motorHarvester.setPower(-0.5);
        }
        else if (harvesterPower > 0.6) {
            motorHarvester.setPower(-1);
        }
        else if (harvesterPowerReversed > 0.2 && harvesterPowerReversed < 0.6) {
            motorHarvester.setPower(0.5);
        }
        else if (harvesterPowerReversed > 0.6) {
            motorHarvester.setPower(1);
        }
        else {motorHarvester.setPower(0);}

        if (gamepad1.right_bumper) {
            motorWinch.setPower(0.5);
        }
        if (gamepad1.left_bumper) {
            motorWinch.setPower(-0.5);
        }
        if (!gamepad1.left_bumper && !gamepad1.right_bumper) {
            motorWinch.setPower(0);
        }

        /*
        * Send telemetry data back to driver station. Note that if we are using
        * a legacy NXT-compatible motor controller, then the getPower() method
        * will return a null value. The legacy NXT-compatible motor controllers
        * are currently write only.
         */

            //IMPORTANT: RobotController supports a maximum of 3 lines of telemetry
            telemetry.addData("dump the climber Position", "dump climber:  " + String.format("%.2f", climberDumpPosition));
            telemetry.addData("right climber Position", "climber:  " + String.format("%.2f", climberPositionRight));
            telemetry.addData("winchHook Position", "winchHook:  " + String.format("%.2f", winchHookPosition));
    } //don't delete this curly brace, otherwise the telemetry gets lonely :-(
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


	}
	else {

	dScale = scaleArray[index];

	}

	// return scaled value.


	return dScale;


	}

}
