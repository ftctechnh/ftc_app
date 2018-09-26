package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Rohan Mathur on 9/24/18.
 */
@TeleOp(name="ReleaseTest", group="Pushbot")

public class ReleaseTest extends LinearOpMode {

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
			telemetry.addData("Servo Position", robot.threader.getPosition());
			if (gamepad1.a) {
				robot.threader.setPosition(1);
				telemetry.addData("Servo Position", robot.threader.getPosition());
			}
			else{
				robot.threader.setPosition(0.5);
				telemetry.addData("Servo Position", robot.threader.getPosition());
			}

			if(gamepad1.b){
				robot.door.setPosition(1);
			}
		}

	}

}