package com.qualcomm.ftcrobotcontroller.sampleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/*
 * An example linear op mode where the pushbot
 * will drive in a square pattern using sleep()
 * and a for loop.
 */
public class PushBotSquare extends LinearOpMode {

	DcMotor leftMotor;
	DcMotor rightMotor;

	@Override
	public void runOpMode() throws InterruptedException {
		this.leftMotor = this.hardwareMap.dcMotor.get("left_drive");
		this.rightMotor = this.hardwareMap.dcMotor.get("right_drive");
		this.rightMotor.setDirection(DcMotor.Direction.REVERSE);
		this.leftMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
		this.rightMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

		this.waitForStart();

		for (int i = 0; i < 4; i++) {
			this.leftMotor.setPower(1.0);
			this.rightMotor.setPower(1.0);

			this.sleep(1000);

			this.leftMotor.setPower(0.5);
			this.rightMotor.setPower( -0.5);

			this.sleep(500);
		}

		this.leftMotor.setPowerFloat();
		this.rightMotor.setPowerFloat();

	}
}
