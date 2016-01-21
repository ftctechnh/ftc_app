package org.usfirst.ftc.theintersect.code;

import org.swerverobotics.library.SynchronousOpMode;

/**
 * A skeletal do-nothing OpMode.
 */

public class SkeletalOpMode extends SynchronousOpMode {
	//Declare hardware

	//Declare gamepad objects

	@Override public void main() throws InterruptedException {
		//Initialize hardware

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
