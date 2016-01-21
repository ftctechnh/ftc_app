package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.swerverobotics.library.SynchronousOpMode;

/**
 * A Maintenance OpMode. It is used to clean the
 */

@org.swerverobotics.library.interfaces.TeleOp(name = "Clean Wheels")
public class CleanWheels extends SynchronousOpMode {
	//Declare hardware
	static DcMotor frontRightWheel;
	static DcMotor frontLeftWheel;
	static DcMotor backRightWheel;
	static DcMotor backLeftWheel;

	//Declare gamepad objects
	boolean cleanFrontLeft = false;
	boolean cleanFrontRight = false;
	boolean cleanBackLeft = false;
	boolean cleanBackRight = false;

	@Override public void main() throws InterruptedException {
		//Initialize hardware
		frontRightWheel = hardwareMap.dcMotor.get("frontRightWheel");
		frontLeftWheel = hardwareMap.dcMotor.get("frontLeftWheel");
		backRightWheel = hardwareMap.dcMotor.get("backRightWheel");
		backLeftWheel = hardwareMap.dcMotor.get("backLeftWheel");

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
					backRightWheel.setPower(0.1);
				} else if (cleanBackLeft){
					backLeftWheel.setPower(0.1);
				}else if (cleanFrontRight) {
					frontRightWheel.setPower(0.1);
				}else if (cleanFrontLeft) {
					frontLeftWheel.setPower(0.1);
				}else{
					frontLeftWheel.setPower(0);
					frontRightWheel.setPower(0);
					backRightWheel.setPower(0);
					backLeftWheel.setPower(0);
				}

			}

			telemetry.update();
			idle();
		}
	}
}
