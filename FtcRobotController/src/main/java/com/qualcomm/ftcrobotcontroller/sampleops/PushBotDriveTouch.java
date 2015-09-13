package com.qualcomm.ftcrobotcontroller.sampleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Jordan Burklund on 7/30/2015. An example linear op mode where the pushbot
 * will run its motors unless a touch sensor is pressed.
 */
public class PushBotDriveTouch extends LinearOpMode {

	DcMotor leftMotor;
	DcMotor rightMotor;
	TouchSensor touchSensor;

	@Override
	public void runOpMode() throws InterruptedException {
		// Get references to the motors from the hardware map
		this.leftMotor = this.hardwareMap.dcMotor.get("left_drive");
		this.rightMotor = this.hardwareMap.dcMotor.get("right_drive");

		// Reverse the right motor
		this.rightMotor.setDirection(DcMotor.Direction.REVERSE);

		// Get a reference to the touch sensor
		this.touchSensor = this.hardwareMap.touchSensor.get("sensor_touch");

		// Wait for the start button to be pressed
		this.waitForStart();

		while (this.opModeIsActive()) {
			if (this.touchSensor.isPressed()) {
				// Stop the motors if the touch sensor is pressed
				this.leftMotor.setPower(0);
				this.rightMotor.setPower(0);
			} else {
				// Keep driving if the touch sensor is not pressed
				this.leftMotor.setPower(0.5);
				this.rightMotor.setPower(0.5);
			}

			this.telemetry.addData("isPressed", String.valueOf(this.touchSensor.isPressed()));

			// Wait for a hardware cycle to allow other processes to run
			this.waitOneHardwareCycle();
		}

	}
}
