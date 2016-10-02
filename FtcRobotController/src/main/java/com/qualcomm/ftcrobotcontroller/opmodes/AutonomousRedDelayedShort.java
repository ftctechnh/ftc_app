
package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Example autonomous program.
 * <p>
 * This example program uses elapsed time to determine how to move the robot.
 * The OpMode.java class has some class members that provide time information
 * for the current op mode.
 * The public member variable 'time' is updated before each call to the run() event.
 * The method getRunTime() returns the time that has elapsed since the op mode
 * starting running to when the method was called.
 */
public class AutonomousRedDelayedShort extends OpMode {

	final static double MOTOR_POWER = 0.15;

	double armPosition;
	double clawPosition;

	DcMotor motorRight;
	DcMotor motorLeft;


	/**
	 * Constructor
	 */
	public AutonomousRedDelayedShort() {

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */
	@Override
	public void init() {


		motorRight = hardwareMap.dcMotor.get("motor_2");
		motorLeft = hardwareMap.dcMotor.get("motor_1");
		motorLeft.setDirection(DcMotor.Direction.REVERSE);



	}

	/*
	 * This method will be called repeatedly in a loop
	 *
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {
		double reflection = 0.0;
		double left = 0, right = 0;


		if (this.time > 10 && this.time <= 12.65) {
			// from 1 to 5 seconds, run the motors for five seconds.
			left = 0.75;
			right = 0.75;
		} else if (this.time > 14.3 && this.time <= 16) {
			// between 5.5 and 7 seconds, idle.
			left = 0.0;
			right = 0.0;
		} else if (this.time > 16.d && this.time <= 17.25d) {
			// between 7 and 8.25 seconds, point turn left.
			left = -0.35;
			right = 0.35;
		} else if (this.time > 17.25d && this.time <= 25.d){
			// between 8.25 seconds and 11.5 seconds, run the drive motors so the robot goes up the mountain
			left = -.25;
			right = -.25;
		}

		/*
		 * set the motor power
		 */
		motorRight.setPower(-left);
		motorLeft.setPower(-right);

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
