package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Rohan Mathur on 9/26/18.
 */
@TeleOp(name="MineralArmTest", group="Pushbot")

public class MineralArmTest extends LinearOpMode {

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
			if(gamepad1.a){
				robot.motor0.setPower(1);
			}
			else{
				robot.motor0.setPower(0);
			}

			if(gamepad1.b){
				robot.motor1.setPower(1);
			}
			else{
				robot.motor1.setPower(0);
			}
		}

	}

}