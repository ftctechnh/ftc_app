package com.qualcomm.ftcrobotcontroller.sampleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Jordan Burklund on 7/30/2015. An example linear op mode where the pushbot
 * will track an IR beacon.
 */
public class PushBotIrSeek extends LinearOpMode {

	final static double kBaseSpeed = 0.15; // Higher values will cause the robot to move
											// faster

	final static double kMinimumStrength = 0.08; // Higher values will cause the robot to
													// follow closer
	final static double kMaximumStrength = 0.60; // Lower values will cause the robot to
													// stop sooner

	IrSeekerSensor irSeeker;
	DcMotor leftMotor;
	DcMotor rightMotor;

	@Override
	public void runOpMode() throws InterruptedException {
		this.irSeeker = this.hardwareMap.irSeekerSensor.get("sensor_ir");
		this.leftMotor = this.hardwareMap.dcMotor.get("left_drive");
		this.rightMotor = this.hardwareMap.dcMotor.get("right_drive");
		this.rightMotor.setDirection(DcMotor.Direction.REVERSE);

		// Wait for the start button to be pressed
		this.waitForStart();

		// Continuously track the IR beacon
		while (this.opModeIsActive()) {
			double angle = this.irSeeker.getAngle() / 30; // value between -4...4
			double strength = this.irSeeker.getStrength();
			if (strength > PushBotIrSeek.kMinimumStrength && strength < PushBotIrSeek.kMaximumStrength) {
				double leftSpeed = Range.clip(PushBotIrSeek.kBaseSpeed + (angle / 8), -1, 1);
				double rightSpeed = Range.clip(PushBotIrSeek.kBaseSpeed - (angle / 8), -1, 1);
				this.leftMotor.setPower(leftSpeed);
				this.rightMotor.setPower(rightSpeed);
			} else {
				this.leftMotor.setPower(0);
				this.rightMotor.setPower(0);
			}
			this.telemetry.addData("Seeker", this.irSeeker.toString());
			this.telemetry.addData("Speed",
					" Left=" + this.leftMotor.getPower() + " Right=" + this.rightMotor.getPower());
					
			// Wait one hardware cycle to avoid taxing the processor
			this.waitOneHardwareCycle();
		}

	}
}
