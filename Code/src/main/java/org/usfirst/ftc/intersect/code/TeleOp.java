package org.usfirst.ftc.intersect.code;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import org.swerverobotics.library.SynchronousOpMode;

/**
 * Main TeleOp
 */
@org.swerverobotics.library.interfaces.TeleOp(name = "TeleOp")
public class TeleOp extends SynchronousOpMode {
	//Declare hardware
	DcMotor frontRightWheel;
	DcMotor frontLeftWheel;
	DcMotor backRightWheel;
	DcMotor backLeftWheel;

	DcMotor linearSlideR;
	DcMotor linearSlideL;

	Servo containerTilt;
	Servo tubeExtender;
	DcMotor sweeper;

	//Declare gamepad objects
	float rightWheel;
	float leftWheel;

	boolean linearSlideForward = false;
	boolean linearSlideBackward = false;

	boolean containerTiltRight = false;
	boolean containerTiltLeft = false;

	boolean sweeperForward = false;
	boolean sweeperBackward = false;

	boolean tubeExtend = false;
	boolean tubeRetract = false;
	@Override public void main() throws InterruptedException {
		//Initialize hardware
		frontRightWheel = hardwareMap.dcMotor.get("frontRightWheel");
		frontLeftWheel = hardwareMap.dcMotor.get("frontLeftWheel");
		backRightWheel = hardwareMap.dcMotor.get("backRightWheel");
		backLeftWheel = hardwareMap.dcMotor.get("backLeftWheel");

		linearSlideR = hardwareMap.dcMotor.get("linearSlideR");
		linearSlideL = hardwareMap.dcMotor.get("linearSlideL");

		//containerTilt = hardwareMap.servo.get("containerTilt");
        //tubeExtender = hardwareMap.servo.get("tubeExtender");

		sweeper = hardwareMap.dcMotor.get("sweeper");

		//Set motor channel modes and direction
		frontRightWheel.setDirection(DcMotor.Direction.REVERSE);
		frontRightWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		frontLeftWheel.setDirection(DcMotor.Direction.FORWARD);
		frontLeftWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		backRightWheel.setDirection(DcMotor.Direction.FORWARD);
		backRightWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		backLeftWheel.setDirection(DcMotor.Direction.REVERSE);
		backLeftWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

		linearSlideR.setDirection(DcMotor.Direction.REVERSE);
		linearSlideR.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		linearSlideL.setDirection(DcMotor.Direction.FORWARD);
		linearSlideL.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

		//Wait for the game to start
		waitForStart();

		//Game Loop
		while(opModeIsActive()) {
			if(updateGamepads()) {
				//Defines gamepad buttons
				rightWheel = gamepad1.right_stick_y;
				leftWheel = gamepad1.left_stick_y;

				linearSlideForward = gamepad1.right_bumper || gamepad2.right_bumper;
				linearSlideBackward = gamepad1.left_bumper || gamepad2.left_bumper;

				containerTiltRight = gamepad1.dpad_right || gamepad2.dpad_right;
				containerTiltLeft = gamepad1.dpad_left || gamepad1.dpad_left;

				tubeExtend = gamepad1.x || gamepad2.x;
				tubeRetract = gamepad1.b || gamepad2.b;

				if(gamepad1.a || gamepad2.a) {
					sweeperForward = !sweeperForward;
					sweeperBackward = false;
				} else if(gamepad1.y || gamepad2.y) {
					sweeperBackward  = !sweeperBackward;
					sweeperForward = false;
				}

				//Use gamepad values to move robot
				Functions.moveTwoMotors(backRightWheel,frontRightWheel,
						Functions.convertGamepad(rightWheel));
				Functions.moveTwoMotors( backLeftWheel,frontLeftWheel,
						Functions.convertGamepad(leftWheel));

				if(linearSlideForward) {
					Functions.moveTwoMotors(linearSlideR, linearSlideL, 0.3);
				} else if(linearSlideBackward) {
					Functions.moveTwoMotors(linearSlideR, linearSlideL, -0.3);
				} else {
					Functions.moveTwoMotors(linearSlideR, linearSlideL, 0.0);
				}

				if(tubeExtend) {
					tubeExtender.setPosition(0.75);
				} else if(tubeRetract) {
					tubeExtender.setPosition(-0.75);
				} else {
					tubeExtender.setPosition(0);
				}

				if(containerTiltRight) {
					containerTilt
							.setPosition(containerTilt.getPosition() + 0.005);
				} else if(containerTiltLeft) {
					containerTilt
							.setPosition(containerTilt.getPosition() - 0.005);
				}

				if(sweeperForward) {
					sweeper.setPower(-1.0);
				} else if(sweeperBackward){
					sweeper.setPower(1.0);
				} else {
					sweeper.setPower(0);
				}
			}

			telemetry.update();
			idle();
		}
	}
}