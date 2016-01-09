package org.usfirst.ftc.intersect.code;

import org.swerverobotics.library.SynchronousOpMode;

/**
 * A skeletal do-nothing OpMode. It is used as
 */

public class SkeletalOpMode extends SynchronousOpMode {
	//Declare hardware

	//Declare gamepad objects

	@Override public void main() throws InterruptedException {
		//Initialize haedware

		//Wait for the game to start
		waitForStart();

		//Game Loop
		while(opModeIsActive()) {
			if(updateGamepads()) {

			}

			telemetry.update();
			idle();
		}
	}
}
