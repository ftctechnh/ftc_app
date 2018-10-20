package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Rohan Mathur on 10/13/18.
 */

@Autonomous(name = "Autonomous Test", group = "Pushbot")

public class InitialAutonomous0 extends LinearOpMode {

	/* Declare OpMode members. */
	AvesAblazeHardwarePushbot robot   = new AvesAblazeHardwarePushbot();   // Use a Pushbot's hardware
	private ElapsedTime runtime = new ElapsedTime();

	public void upTics(double power, int tics){
		if(tics<0) power = power*-1;
		int initPos = robot.getMotorVal(0);
		int currPos = initPos;
		while(currPos != initPos){
			robot.moveUpDown(power);
			currPos = robot.getMotorVal(0);
			telemetry.addData("Pos 0", robot.getMotorVal(0));
			telemetry.update();
		}
	}
	@Override
	public void runOpMode() {
		robot.init(hardwareMap);
		telemetry.update();

		robot.resetCoordinates();
		robot.resetEncodes();

		waitForStart();

		robot.moveUpDown(1, 50);
		robot.rotate(1);
		robot.moveLeftRight(1, 50);
	}

	public void printAll(){
		telemetry.addData("motor0", robot.getMotorVal(0));
		telemetry.addData("motor1", robot.getMotorVal(1));
		telemetry.addData("motor2", robot.getMotorVal(2));
		telemetry.addData("motor3", robot.getMotorVal(3));

		telemetry.update();
	}
}
