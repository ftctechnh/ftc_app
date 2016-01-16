package org.usfirst.ftc.intersect.code;

import org.swerverobotics.library.SynchronousOpMode;

/**
 * A skeletal do-nothing OpMode. It is used as
 */

public class CleanWheels extends SynchronousOpMode {
	//Declare hardware

	//Declare gamepad objects
	boolean cleanFrontLeft = false;
	boolean cleanFrontRight = false;
	boolean cleanBackLeft = false;
	boolean cleanBackRight = false;

	@Override public void main() throws InterruptedException {
		//Initialize hardware

		//Wait for the game to start
		waitForStart();

		//Game Loop
		while(opModeIsActive()) {
			if(updateGamepads()) {
				cleanFrontLeft = gamepad1.y || gamepad2.y;
				cleanFrontRight = gamepad1.b || gamepad2.b;
				cleanBackLeft = gamepad1.x || gamepad2.x;
				cleanBackRight = gamepad1.a || gamepad2.a;

				if(cleanBackRight){
					Autonomous.backRightWheel.setPower(0.1);
				} else if (cleanBackLeft){
					Autonomous.backLeftWheel.setPower(0.1);
				}else if (cleanFrontRight) {
					Autonomous.frontRightWheel.setPower(0.1);
				}else if (cleanFrontLeft) {
					Autonomous.frontLeftWheel.setPower(0.1);
				}else{
					Autonomous.frontLeftWheel.setPower(0);
					Autonomous.frontRightWheel.setPower(0);
					Autonomous.backRightWheel.setPower(0);
					Autonomous.backLeftWheel.setPower(0);
				}

			}

			telemetry.update();
			idle();
		}
	}
}
