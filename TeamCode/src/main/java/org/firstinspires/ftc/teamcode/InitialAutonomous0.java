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

	public void upTics(double power, int tics){
		if(tics<0) power = power*-1;
		int initPos = robot.motor0.getCurrentPosition();
		int currPos = initPos;
		while(currPos != initPos){
			robot.moveUpDown(power);
			currPos = robot.motor0.getCurrentPosition();
			telemetry.addData("Pos 0", robot.motor0.getCurrentPosition());
		}
	}
	@Override
	public void runOpMode() throws InterruptedException {
		robot.init(hardwareMap);
		waitForStart();
		upTics(0.5, 50);
/*		robot.moveUpDown(0.5, -50);

		robot.moveLeftRight(0.5, 50);
		robot.moveLeftRight(0.5, -50);

		robot.rotate(0.5, 360);
		robot.rotate(0.5, -360);*/

	}
}
