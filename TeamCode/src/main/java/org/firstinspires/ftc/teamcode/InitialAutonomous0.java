package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Rohan Mathur on 10/13/18.
 */

@Autonomous(name = "Something Intelligent: Autonomous", group = "Pushbot")

public class InitialAutonomous0 extends LinearOpMode {

	/* Declare OpMode members. */
	AvesAblazeHardwarePushbot robot   = new AvesAblazeHardwarePushbot();   // Use a Pushbot's hardware
	private ElapsedTime runtime = new ElapsedTime();

	@Override
	public void runOpMode() {
		robot.init(hardwareMap);
		waitForStart();
		while(opModeIsActive()){

		}
	}
}
