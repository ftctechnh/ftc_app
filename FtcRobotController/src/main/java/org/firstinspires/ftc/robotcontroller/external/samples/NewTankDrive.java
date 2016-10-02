

//@Author Eric Adams, Lead Programmer Team 8417, The 'Lectric Legends

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;


public class NewTankDrive extends OpMode{


	DcMotor MotorRight;
	DcMotor MotorLeft;
	DcMotor Motor1;
	DcMotor Motor2;
	DcMotor Motor3;
	DcMotor Motor4;
    TouchSensor touchSensor;


	/*
     * Code to run when w22jthe op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
	@Override
	public void init() {

		MotorLeft = hardwareMap.dcMotor.get("Motor_Left");
		MotorRight = hardwareMap.dcMotor.get("Motor_Right");
		Motor1 = hardwareMap.dcMotor.get("Motor_1");
		Motor2 = hardwareMap.dcMotor.get("Motor_2");
		Motor3 = hardwareMap.dcMotor.get("Motor_3");
		Motor4 = hardwareMap.dcMotor.get("Motor_4");
		MotorLeft.setDirection(DcMotor.Direction.REVERSE);
		MotorRight.setDirection(DcMotor.Direction.REVERSE);

		touchSensor = hardwareMap.touchSensor.get("sensor_touch");
	}


	@Override
	public void loop() {


        float right = -gamepad1.left_stick_y; //Right motor control
        float left = gamepad1.right_stick_y;  //Left motor control
        float FirstPP = -gamepad2.left_stick_y;  //First Pivot Point motor control
        float SecondPP = gamepad2.right_stick_y; //Second Pivot Point motor control
        double ThirdPP = 0;
        double WinchPwr = 0;
        float FirstPower = 0;
        float SecondPower = 0;
        float ThirdPower = 0;





        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);
        FirstPP = Range.clip(FirstPP, -1, 1);
        SecondPP = Range.clip(SecondPP, -1, 1);
        ThirdPP = Range.clip(ThirdPP, -1, 1);

		while(touchSensor.isPressed()){ //this is SUPPOSED to make it back up when the touch sensor is pressed.
			right = 84;  //fixed this to make it not do donuts :)  (hopefully)
			left = 98;  //dont forget the rotation of the motors we fixed it but it was doing donuts when pressed
		}


        right = (float) scaleInput(right);
        left = (float) scaleInput(left);
        FirstPP = (float) scaleInputArm(FirstPP);   // changed this as to change the arm speed to slower.
        SecondPP = (float) scaleInputArm(SecondPP);  // changed this as to change the arm speed to slower.
        FirstPower = FirstPP;
        SecondPower = SecondPP;//find out how to get encoder, and set the motor to stay at the position.




        MotorRight.setPower(right);
        MotorLeft.setPower(left);/*
		motor1.setPower(FirstPwr);
		motor2.setPower(SecondPwr);
		motor3.setPower(ThirdPwr);
		motor4.setPower(FourthPwr);
*/


        telemetry.addData("", "* FTC 8417 Robot Data *");
        telemetry.addData("left joy position", "left  Pwr: " + String.format("%.2f", -left));

        telemetry.addData("right joy position", "right Pwr: " + String.format("%.2f", -right));

    }




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




