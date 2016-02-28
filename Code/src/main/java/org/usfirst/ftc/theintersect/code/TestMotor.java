package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.Disabled;

/**
 * Testing OpMode, tests if motors work.
 */

@org.swerverobotics.library.interfaces.TeleOp(name = "Test Motor")
@Disabled
public class TestMotor extends SynchronousOpMode {
	//Declare hardware
	static DcMotor testMotor;

	//Declare gamepad objects
	boolean moveMotor = false;

	@Override
	public void main() throws InterruptedException {
		//Initialize hardware
		testMotor = hardwareMap.dcMotor.get("testWheel");

		//Wait for the game to start
		waitForStart();
		//Game Loop
		while (opModeIsActive()) {
			if (updateGamepads()) {
				moveMotor = gamepad1.y || gamepad2.y;

				if (moveMotor) {
					testMotor.setPower(1.0);
                    telemetry.addData("Motor" , "Moving...");

				} else {
                    testMotor.setPower(0);
                    telemetry.addData("Motor" , "Moving...");
                }
				telemetry.updateNow();
				idle();
			}
		}
	}
}
