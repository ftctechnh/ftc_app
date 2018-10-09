package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by Rohan Mathur on 9/26/18.
 */
@TeleOp(name="MechanumTest", group="Pushbot")
@Disabled
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
			telemetry.update();

			double xValL = gamepad1.left_stick_x;
			double yValL = gamepad1.left_stick_y;

			double xValR = gamepad1.right_stick_x;


			//Move forward or backwards, set all motors to positive (forwards) or negative (backwards)
			if(yValL != 0){
				//Set all motors to forwards
				for(int i = 0; i<4; i++){
					runMotor(i,yValL);
				}
			}
			else{

			}

			//Strafing, wheels move inwards on the side of movement, and outwards on the opposite side
			/*
			If left_stick moved to the right, value is positive:
				motor0 and motor3 move forwards
				motor1 and motor2 move backwards
			And visa versa
			 */
			if(xValL != 0){
				runMotor(0,xValL);
				runMotor(3,xValL);
				runMotor(1,xValL*-1);
				runMotor(2,xValL*-1);
			}
			else{

			}

			//Turning, wheels on direction of turn move backwards, and opposite sides move forwards
			if(xValR != 0){
				runMotor(0,xValR);
				runMotor(2,xValR);
				runMotor(1,xValR*-1);
				runMotor(3,xValR*-1);
			}
			else{

			}

			telemetry.addData("motor0",robot.motor0.getCurrentPosition());
			telemetry.addData("motor1",robot.motor1.getCurrentPosition());
			telemetry.addData("motor2",robot.motor2.getCurrentPosition());
			telemetry.addData("motor3",robot.motor3.getCurrentPosition());

			telemetry.addData("xValL", xValL);
			telemetry.addData("yValL", yValL);
			telemetry.addData("xValR", xValR);

			telemetry.update();
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