package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Rohan Mathur on 9/26/18.
 */
@TeleOp(name="MechanumTest", group="Pushbot")

public class MechanumTest extends LinearOpMode {

	/* Declare OpMode members. */
	HardwarePushbotTest robot   = new HardwarePushbotTest();   // Use a Pushbot's hardware
	private ElapsedTime runtime = new ElapsedTime();

	@Override
	public void runOpMode() {

		/*
		 * Initialize the drive system variables.
		 * The init() method of the hardware class does all the work here
		 */
		robot.init(hardwareMap);

		waitForStart();

		while(opModeIsActive()) {
			if(gamepad1.left_stick_y != 0){

			}
		}

	}

	public void runMotor(int motorNum, double speed){
		switch (motorNum){
			case 0: robot.motor0.setPower(speed);
			case 1: robot.motor1.setPower(speed);
			case 2: robot.motor2.setPower(speed);
			case 3: robot.motor3.setPower(speed);
		}
	}
}