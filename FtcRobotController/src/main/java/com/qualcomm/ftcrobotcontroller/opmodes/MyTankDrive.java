

//@Author Andrew Gowan Awesome dude and other stuff team 8417

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;


public class MyTankDrive extends OpMode{


	DcMotor motorRight;
	DcMotor motorLeft;
	//DcMotor motor1PP;
//	DcMotor motor2PP;
//	DcMotor motor3PP;
	//DcMotor motorWinch;




	/*
	 * Code to run when w22jthe op mode is first enabled goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */
	@Override
	public void init() {

		motorLeft = hardwareMap.dcMotor.get("motor_1");
		motorRight = hardwareMap.dcMotor.get("motor_2");
		/*motor1PP = hardwareMap.dcMotor.get("motor_1P");
		motor2PP = hardwareMap.dcMotor.get("motor_2P");
		motor3PP = hardwareMap.dcMotor.get("motor_3P");
		motorWinch = hardwareMap.dcMotor.get("motor_W"); //W stands for Winch, which is what this motor controls
	*/	motorLeft.setDirection(DcMotor.Direction.REVERSE);
		motorRight.setDirection(DcMotor.Direction.REVERSE);



	}


	@Override
	public void loop() {


		float right = -gamepad1.left_stick_y; //righ motor control
		float left = gamepad1.right_stick_y;  //Left motor control
	//	float FirstPP = gamepad2.left_stick_y;  //First Pivot Point motor control
	//	float SecondPP = -gamepad2.right_stick_y; //Second Pivot Point motor control
	//	double ThirdPP = 0;
	//	double WinchPwr = 0;
	//	float FirstPower = 0;
	//	float SecondPower = 0;
	//	float ThirdPower = 0;


		right = Range.clip(right, -1, 1);
		left = Range.clip(left, -1, 1);
	//	FirstPP = Range.clip(FirstPP, -1, 1);
	//	SecondPP = Range.clip(SecondPP, -1, 1);
	//	ThirdPP = Range.clip(ThirdPP, -1, 1);



		right = (float)scaleInput(right);
		left =  (float)scaleInput(left);
	//	FirstPP = (float)scaleInputArm(FirstPP);   // changed this as to change the arm speed to slower.
	//	SecondPP = (float)scaleInputArm(SecondPP);  // changed this as to change the arm speed to slower.
	//	FirstPower = FirstPP;
	//	SecondPower = SecondPP;//find out how to get encoder, and set the motor to stay at the position.

		//Start arm control function

/*		if((gamepad2.left_stick_y > -.05 && gamepad2.left_stick_y < .05) ){
			FirstPower = (float) .06;
		}
		if((gamepad2.right_stick_y > -.05 && gamepad2.left_stick_y < .05) ){
			SecondPower = (float) .05;
		}


		if(gamepad2.dpad_up){
			ThirdPP = 1;
		}
		if(gamepad2.dpad_down){
 			ThirdPP = -1;
		}
		if(gamepad2.dpad_right){
			ThirdPP = .25;
		}
		if(gamepad2.dpad_left){
			ThirdPP = -.25;
		}

		if(gamepad2.right_trigger >= .1){
			FirstPower = FirstPower/2;
			SecondPower = SecondPower/2;
			ThirdPower = ThirdPower/2;
			WinchPwr = WinchPwr/2;
		}

		//End arm control function

		//Start Winch control
		if(gamepad1.y || gamepad2.right_bumper){
			WinchPwr = 1;
		}
		if(gamepad1.a || gamepad2.left_bumper){
			WinchPwr = -1;
		}
		if(gamepad1.b){
			WinchPwr = .25;
		}
		if(gamepad1.x){
			WinchPwr = -.25;
		}
		//End reverse function

*/
		motorRight.setPower(right);
		motorLeft.setPower(left);
//		motor1PP.setPower(FirstPP);
//		motor2PP.setPower(SecondPP);
//		motor3PP.setPower(ThirdPP);
//		motorWinch.setPower(WinchPwr);



		telemetry.addData("", "* FTC 8417 Robot Data *");
		telemetry.addData("left joy position", "left  Pwr: " + String.format("%.2f", -left));
		telemetry.addData("right joy position", "right Pwr: " + String.format("%.2f", -right));
//		telemetry.addData("FirstPP",  "Pwr: " + String.format("%.2f", -FirstPP));
//		telemetry.addData("SecondPP", "Pwr: " + String.format("%.2f", -SecondPP));
//		telemetry.addData("ThirdPP", "Pwr" + String.format("%.2f", ThirdPP));
//		telemetry.addData("WinchPwr", "Pwr" + String.format("%.2f", WinchPwr));
	}


	@Override
	public void stop() {

	}

	double scaleInput(double dVal)  {
		double[] scaleArray = { 0.0, 0.1, 0.13, 0.15, 0.18, 0.2, 0.22, 0.24,
				0.30, 0.36, 0.40, 0.45, 0.50, 0.72, 0.85, 1.00, 1.00 };

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

	double scaleInputArm(double ValArm)  { // changed this as to change the arm speed to slower.
		double[] scaleArrayArm = { 0.03, 0.04, 0.07, 0.13, 0.15, 0.19, 0.25, 0.29,
				0.34, 0.4, 0.47, 0.55, 0.64, 0.73, 0.82, .91, 1 };

		// get the corresponding index for the scaleInput array.
		int indexArm = (int) (ValArm * 16.0);

		// index should be positive.
		if (indexArm < 0) {
			indexArm = -indexArm;
		}

		// index cannot exceed size of array minus 1.
		if (indexArm > 16) {
			indexArm = 16;
		}

		// get value from the array.
		double ArmScale = 0.0;
		if (ValArm < 0) {
			ArmScale = -scaleArrayArm[indexArm];
		} else {
			ArmScale = scaleArrayArm[indexArm];
		}

		// return scaled value.
		return ArmScale;
	}// changed this as to change the arm speed to slower.

}




