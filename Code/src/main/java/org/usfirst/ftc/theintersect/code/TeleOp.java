package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.SynchronousOpMode;

import java.util.Arrays;

/**
 * Main TeleOp
 */
@org.swerverobotics.library.interfaces.TeleOp(name = "TeleOp")
public class TeleOp extends SynchronousOpMode {
	//Declare hardware
	static DcMotor rightWheel;
	static DcMotor leftWheel;

	static DcMotor linearSlideR;
	static DcMotor linearSlideL;

	static DcMotor sweeper;

	static Servo tubeTilt;
	static Servo tubeExtender;

	static Servo mountainClimber;
	static Servo mountainClimberRelease;

	static Servo bumper;

	static Servo churroHookLeft;
	static Servo churroHookRight;

	//Declare gamepad objects
	float rightWheelPower;
	float leftWheelPower;

	boolean linearSlideForward = false;
	boolean linearSlideBackward = false;

	boolean containerTiltRight = false;
	boolean containerTiltLeft = false;

	boolean sweeperForward = false;
	boolean sweeperBackward = false;

	boolean tubeExtend = false;
	boolean tubeRetract = false;

	boolean positionClimbersForward = false;
	boolean positionClimbersBackward = false;
	boolean releaseClimbers = false;

	boolean bumperDown = true;
	boolean bumperUp = false;

	boolean churroHooksDown = false;
	boolean toggleChurroHooks = false;

	float slowDriveBack;
	float slowDriveForward;

	@Override
	public void main() throws InterruptedException {
		//Initialize hardware
		rightWheel = hardwareMap.dcMotor.get("rightWheel");
		leftWheel = hardwareMap.dcMotor.get("leftWheel");

		linearSlideR = hardwareMap.dcMotor.get("linearSlideR");
		linearSlideL = hardwareMap.dcMotor.get("linearSlideL");

		tubeTilt = hardwareMap.servo.get("tubeTilt");
		tubeExtender = hardwareMap.servo.get("tubeExtender");

		mountainClimber = hardwareMap.servo.get("mountainClimber");
		mountainClimberRelease = hardwareMap.servo.get("mountainClimberRelease");

		sweeper = hardwareMap.dcMotor.get("sweeper");

		bumper = hardwareMap.servo.get("bumper");
		//churroHookLeft = hardwareMap.servo.get("churroHookLeft");
		//churroHookRight = hardwareMap.servo.get("churroHookRight");

		//Set motor channel modes and direction
		rightWheel.setDirection(DcMotor.Direction.REVERSE);
		rightWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		leftWheel.setDirection(DcMotor.Direction.FORWARD);
		leftWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

		linearSlideR.setDirection(DcMotor.Direction.REVERSE);
		linearSlideR.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
		linearSlideL.setDirection(DcMotor.Direction.FORWARD);
		linearSlideL.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

		tubeTilt.setDirection(Servo.Direction.REVERSE);
		tubeExtender.setDirection(Servo.Direction.REVERSE);
		mountainClimber.setDirection(Servo.Direction.FORWARD);
		mountainClimberRelease.setDirection(Servo.Direction.REVERSE);
		bumper.setDirection(Servo.Direction.FORWARD);
		//Wait for the game to start
		teleopInit();
		/*ClassFactory.createEasyMotorController(this, linearSlideL,
				linearSlideR);
		ClassFactory.createEasyMotorController(this, rightWheel, leftWheel);
		ClassFactory.createEasyMotorController(this, sweeper, null);*/
		ClassFactory.createEasyServoController(this, Arrays.asList
				(mountainClimberRelease, mountainClimber, tubeExtender,
						tubeTilt, bumper));

		waitForStart();
		long endTime = System.currentTimeMillis() + 120000;
		//Game Loop
		while(opModeIsActive() && System.currentTimeMillis() < endTime) {
			//Defines gamepad buttons for buttons that are held
			containerTiltRight = gamepad1.dpad_left || gamepad2.dpad_left;
			containerTiltLeft = gamepad1.dpad_right || gamepad2.dpad_right;

			tubeExtend = gamepad1.x || gamepad2.x;
			tubeRetract = gamepad1.b || gamepad2.b;

			positionClimbersForward = gamepad1.dpad_up || gamepad2.dpad_up;
			positionClimbersBackward = gamepad1.dpad_down || gamepad2.dpad_down;
			releaseClimbers = gamepad1.start || gamepad2.start;

			toggleChurroHooks = gamepad1.left_stick_button;

			//Moves robot when some of the buttons are held
			if(tubeExtend) {
				tubeExtender.setPosition(0.75);
			} else if(tubeRetract) {
				tubeExtender.setPosition(-0.9);
			} else {
				tubeExtender.setPosition(0.5);
			}
            if (toggleChurroHooks && churroHooksDown) {
                churroHookLeft.setPosition(Functions.churroHookUpPos);
                churroHookRight.setPosition(Functions.churroHookUpPos);
                churroHooksDown = false;
            } else if (toggleChurroHooks && !churroHooksDown) {
                churroHookLeft.setPosition(Functions.churroHookDownPos);
                churroHookRight.setPosition(Functions.churroHookDownPos);
                churroHooksDown = false;
            }else {
                return;
            }


			if(containerTiltRight) {
				tubeTilt.setPosition(tubeTilt.getPosition() + 0.01);
			} else if(containerTiltLeft) {
				tubeTilt.setPosition(tubeTilt.getPosition() - 0.01);
			} else {
				tubeTilt.setPosition(tubeTilt.getPosition());
			}

			if(positionClimbersForward) {
				try {
					mountainClimber.setPosition(
							mountainClimber.getPosition() + 0.01);
				} catch(Exception e) {

				}
				positionClimbersBackward = false;
			} else if(positionClimbersBackward) {
				try {
					mountainClimber.setPosition(
							(mountainClimber.getPosition() - 0.01));
				} catch(Exception e) {

				}
				positionClimbersForward = false;
			} else {
				mountainClimber.setPosition(mountainClimber.getPosition());
			}
			if(releaseClimbers) {
				mountainClimberRelease.setPosition(1.0);
			} else {
				mountainClimberRelease.setPosition(0.0);
			}

			//For buttons that are not held
			if(updateGamepads()) {
				//Defines gamepad buttons
				rightWheelPower = gamepad1.right_stick_y;
				leftWheelPower = gamepad1.left_stick_y;

				linearSlideForward =
						gamepad1.right_bumper || gamepad2.right_bumper;
				linearSlideBackward =
						gamepad1.left_bumper || gamepad2.left_bumper;

				/*slowDriveBack = gamepad1.left_trigger;
				slowDriveForward = gamepad1.right_trigger;!!*/

				if(gamepad1.back || gamepad2.back) {
					bumperUp = !bumperUp;
					bumperDown = !bumperDown;
				}

				if(gamepad1.a || gamepad2.a) {
					sweeperForward = !sweeperForward;
					sweeperBackward = false;
				} else if(gamepad1.y || gamepad2.y) {
					sweeperBackward = !sweeperBackward;
					sweeperForward = false;
				}



				sweeperForward = gamepad1.a || gamepad2.a;
				sweeperBackward = gamepad1.y || gamepad2.y;

				//Use gamepad values to move robot
				/*if(slowDriveForward != 0 || slowDriveBack != 0) {
					Functions.moveTwoMotors(rightWheel, rightWheel,
							(slowDriveForward - slowDriveBack) * 0.25);
					Functions.moveTwoMotors(leftWheel, leftWheel,
							(slowDriveForward - slowDriveBack) * 0.25);*/
				} else if(rightWheelPower != 0 || leftWheelPower != 0) {
					Functions.moveTwoMotors(rightWheel, rightWheel,
							Functions.convertGamepad(rightWheelPower));
					Functions.moveTwoMotors(leftWheel, leftWheel,
							Functions.convertGamepad(leftWheelPower));
				} else {
					Functions.moveTwoMotors(rightWheel, rightWheel, 0);
					Functions.moveTwoMotors(leftWheel, leftWheel, 0);
				}

				if(linearSlideForward) {
					Functions.moveTwoMotors(linearSlideR, linearSlideL, 0.3);
				} else if(linearSlideBackward) {
					Functions.moveTwoMotors(linearSlideR, linearSlideL, -0.3);
				} else {
					Functions.moveTwoMotors(linearSlideR, linearSlideL, 0.0);
				}

				if(sweeperForward) {
					sweeper.setPower(-1.0);
				} else if(sweeperBackward) {
					sweeper.setPower(1.0);
				} else {
					sweeper.setPower(0);
				}

				if(bumperDown) {
					bumper.setPosition(0);
				} else {
					bumper.setPosition(1);
				}
			}

			idle();
		}

	}

	public static void teleopInit() {
		mountainClimber.setPosition(Functions.mountainClimberInitPosition);
		mountainClimberRelease.setPosition(
				Functions.mountainClimberReleaseInitPosition);
		tubeExtender.setPosition(Functions.tubeExtenderInitPosition);
		tubeTilt.setPosition(Functions.tubeTiltInitPosition);
		bumper.setPosition(Functions.bumperInitPosition);
	}
}