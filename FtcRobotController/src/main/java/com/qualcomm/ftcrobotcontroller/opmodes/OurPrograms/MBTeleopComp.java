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

package com.qualcomm.ftcrobotcontroller.opmodes.OurPrograms;
//
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p>
 * Enables control of the robot via the gamepad
 */

public class MBTeleopComp extends OpMode {

	DcMotor rwa; // P0 port 1
	DcMotor rwb; // P0 port 2
	DcMotor liftL; // P1 port 1
	DcMotor liftR; // P1 port 2
	Servo leftComb; // P2 channel 1
	Servo rightComb; // P2 channel 2
	Servo trigL; // P2 channel 3
	Servo trigR; // P2 channel 4
	DcMotor scoopArm; // P3 port 1
	//DcMotor winch; - Winch not currently on robot will be //P3 port 2
	//Servo leftCR; // Not on robot- P4 channel 1
	//Servo rightCR; //Not on robot- P4 channel 2
	Servo wrist;
	Servo ddspivot; // P4 channel 3
	Servo ddsclaw; // P4 channel 4
	DcMotor lwa; // P5 port 1
	DcMotor lwb; // P5 port 2



	@Override
	public void init() {

		lwa = hardwareMap.dcMotor.get("leftwheelA");
		lwb = hardwareMap.dcMotor.get("leftwheelB");
		rwa = hardwareMap.dcMotor.get("rightwheelA");
        rwb = hardwareMap.dcMotor.get("rightwheelB");
		rwa.setDirection(DcMotor.Direction.REVERSE);
		rwb.setDirection(DcMotor.Direction.REVERSE);
		liftL = hardwareMap.dcMotor.get("liftL");
		liftR = hardwareMap.dcMotor.get("liftR");
		liftR.setDirection(DcMotor.Direction.REVERSE);
		scoopArm = hardwareMap.dcMotor.get("scoopArm");
		scoopArm.setDirection(DcMotor.Direction.REVERSE);
		wrist = hardwareMap.servo.get("wrist");
		//rightCR = hardwareMap.servo.get("rightCR");
		leftComb = hardwareMap.servo.get("leftComb");
		rightComb = hardwareMap.servo.get("rightComb");
		trigL = hardwareMap.servo.get("trigL");
		trigR = hardwareMap.servo.get("trigR");
		ddspivot = hardwareMap.servo.get("ddspivot");
		ddsclaw = hardwareMap.servo.get("ddsclaw");

	}

	@Override
	public void loop() {

// wheel control
		float left = -gamepad1.left_stick_y;
		float right = -gamepad1.right_stick_y;

		// clip the right/left values so that the values never exceed +/- 1
		right = Range.clip(right, -1, 1);
		left = Range.clip(left, -1, 1);

		rwa.setPower(right);
		rwb.setPower(right);
        lwa.setPower(left);
        lwb.setPower(left);

// lift control
		float lift = gamepad2.left_stick_y;

		// clip the right/left values so that the values never exceed +/- 1
		lift = Range.clip(lift, -1, 1);

		liftL.setPower(lift);
		liftR.setPower(lift);


		// Scoop Arm control
		float sAC = gamepad2.right_stick_y;

		scoopArm.setPower(sAC);





//Scooper control

	if(gamepad2.a)
	{
		wrist.setPosition(0);
	}

		if(gamepad2.b)
		{
			wrist.setPosition(0.8);
		}

		if(gamepad2.y)
		{
			wrist.setPosition(1);
		}





// collector spinning

//		if (gamepad2.b){
//			collector.setPower(1);
//		}
//		else{
//			collector.setPower(0);
//		}

// collector vertical control

/*
		if(gamepad1.a){
			leftCR.setPosition(1.0);
			rightCR.setPosition(0.0);
		}
		else if(gamepad1.b){
			leftCR.setPosition(0.0);
			rightCR.setPosition(1.0);
		}
		else{
			leftCR.setPosition(0.5);
			rightCR.setPosition(0.5);
		}
		*/
// Zipline release control
		if(gamepad2.right_bumper) {
		trigL.setPosition(0.25);
		}
		else{
			trigL.setPosition(1.0);
		}


		if(gamepad2.left_bumper) {
			trigR.setPosition(1.0);
		}
		else{
			trigR.setPosition(0.15);
		}



// Comb control

		if(gamepad2.left_trigger>0.5)
		{
			rightComb.setPosition(1);
		}
		else
		{
			rightComb.setPosition(0);
		}



		if(gamepad2.right_trigger<0.5)
		{
			leftComb.setPosition(1);
		}
		else
		{
			leftComb.setPosition(0);
		}





		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));


	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	/*
	@Override
	public void stop() {

	}
	

	 * This method scales the joystick input so for low joystick values, the 
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.

	double scaleInput(double dVal)  {
		double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
				0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };
		
		// get the corresponding index for the scaleInput array.
		int index = (int) (dVal * 16.0);
		if (index < 0) {
			index = -index;
		} else if (index > 16) {
			index = 16;
		}
		
		double dScale = 0.0;
		if (dVal < 0) {
			dScale = -scaleArray[index];
		} else {
			dScale = scaleArray[index];
		}
		
		return dScale;
	}
*/
}
