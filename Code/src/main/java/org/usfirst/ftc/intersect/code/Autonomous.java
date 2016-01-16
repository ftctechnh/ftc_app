package org.usfirst.ftc.intersect.code;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;
import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.TelemetryDashboardAndLog;

/**
 * An Autonomous.
 */

@org.swerverobotics.library.interfaces.Autonomous(name = "Autonomous")
public class Autonomous extends SynchronousOpMode {
	//Declare hardware
	static DcMotor frontRightWheel;
	static DcMotor frontLeftWheel;
	static DcMotor backRightWheel;
	static DcMotor backLeftWheel;

	static DcMotor sweeper;

	@Override public void main() throws InterruptedException {
		//Initialize hardware
		frontRightWheel = hardwareMap.dcMotor.get("frontRightWheel");
		frontLeftWheel = hardwareMap.dcMotor.get("frontLeftWheel");
		backRightWheel = hardwareMap.dcMotor.get("backRightWheel");
		backLeftWheel = hardwareMap.dcMotor.get("backLeftWheel");

		sweeper = hardwareMap.dcMotor.get("sweeper");

		//Set motor directions
		frontRightWheel.setDirection(DcMotor.Direction.REVERSE);
		frontLeftWheel.setDirection(DcMotor.Direction.FORWARD);
		backRightWheel.setDirection(DcMotor.Direction.FORWARD);
		backLeftWheel.setDirection(DcMotor.Direction.REVERSE);

		//Wait for the game to start
		waitForStart();

		//Autonomous Start
		while(opModeIsActive()) {

		}
		//Autonomous End
	}

	//Functions
	public static void prepareMotors() {
		//Resets the encoders and forces the motors to run to the target position
		frontRightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		frontRightWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		frontLeftWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		frontLeftWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		backRightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		backRightWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		backLeftWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		backLeftWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
	}

	public static void moveRobotPower(double power) {
		frontRightWheel.setPower(power);
		frontLeftWheel.setPower(power);
		backRightWheel.setPower(power);
		backLeftWheel.setPower(power);

	}

	public static void moveRobotRotations(double rotations, double power, TelemetryDashboardAndLog telemetry) {
		prepareMotors();
		backRightWheel.setTargetPosition((int)(rotations));
		backLeftWheel.setTargetPosition((int)(rotations));
		backRightWheel.setPower(power);
		backLeftWheel.setPower(power);
		frontLeftWheel.setPower(power);
		frontRightWheel.setPower(power);

		while(!(backRightWheel.getCurrentPosition() >= backRightWheel.getTargetPosition()-Functions.encoderError && backRightWheel.getCurrentPosition() <= backRightWheel.getTargetPosition()+Functions.encoderError) && !(backLeftWheel.getCurrentPosition() >= backLeftWheel.getTargetPosition()-Functions.encoderError && backLeftWheel.getCurrentPosition() <= backLeftWheel.getTargetPosition()+Functions.encoderError)) {
			telemetry.addData("Right: ", backRightWheel.getCurrentPosition());
			telemetry.addData("Left: ", backLeftWheel.getCurrentPosition());
			telemetry.update();
		}
		moveRobotPower(0);
	}

	public static void moveRobotInches(double inches, double power, TelemetryDashboardAndLog telemetry) {
		moveRobotRotations(inches/Functions.backWheelCircumfrence, power, telemetry);
	}
}
