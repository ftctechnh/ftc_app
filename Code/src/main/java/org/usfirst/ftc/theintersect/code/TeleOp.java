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

	static DcMotor linearSlide;

	static DcMotor sweeper;

    static DcMotor leftHangString;
    static DcMotor rightHangString;

    static Servo tubeTilt;
	static Servo tubeExtender;

	static Servo mountainClimber;
	static Servo mountainClimberRelease;

	static Servo bumper;

    static Servo leftChurroHook;
    static Servo rightChurroHook;

    static Servo leftBarHook;
    static Servo rightBarHook;

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

    boolean barHooksDown = false;
    boolean toggleBarHooks = false;

	float slowDriveBack;
	float slowDriveForward;

	@Override
	public void main() throws InterruptedException {
        //Initialize hardware
        rightWheel = hardwareMap.dcMotor.get("rightWheel");
        leftWheel = hardwareMap.dcMotor.get("leftWheel");

        linearSlide = hardwareMap.dcMotor.get("linearSlide");

        leftHangString = hardwareMap.dcMotor.get("leftHangString");
        rightHangString = hardwareMap.dcMotor.get("rightHangString");


        tubeTilt = hardwareMap.servo.get("tubeTilt");
        tubeExtender = hardwareMap.servo.get("tubeExtender");

        mountainClimber = hardwareMap.servo.get("mountainClimber");
        mountainClimberRelease = hardwareMap.servo.get("mountainClimberRelease");

        sweeper = hardwareMap.dcMotor.get("sweeper");

        bumper = hardwareMap.servo.get("bumper");

        leftChurroHook = hardwareMap.servo.get("leftChurroHook");
        rightChurroHook = hardwareMap.servo.get("rightChurroHook");

        leftBarHook = hardwareMap.servo.get("leftBarHook");
        rightBarHook = hardwareMap.servo.get("rightBarHook");


        //Set motor channel modes and direction
        rightWheel.setDirection(DcMotor.Direction.REVERSE);
        rightWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        leftWheel.setDirection(DcMotor.Direction.FORWARD);
        leftWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        linearSlide.setDirection(DcMotor.Direction.REVERSE);
        linearSlide.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        leftHangString.setDirection(DcMotor.Direction.FORWARD);
        leftHangString.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        rightHangString.setDirection(DcMotor.Direction.FORWARD);
        rightHangString.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        tubeTilt.setDirection(Servo.Direction.REVERSE);
        tubeExtender.setDirection(Servo.Direction.REVERSE);
        mountainClimber.setDirection(Servo.Direction.FORWARD);
        mountainClimberRelease.setDirection(Servo.Direction.REVERSE);
        bumper.setDirection(Servo.Direction.FORWARD);
        //Wait for the game to start
		/*ClassFactory.createEasyMotorController(this, linearSlide,
				linearSlide);
		ClassFactory.createEasyMotorController(this, rightWheel, leftWheel);
		ClassFactory.createEasyMotorController(this, sweeper, null);*/
        ClassFactory.createEasyServoController(this, Arrays.asList
                (mountainClimberRelease, mountainClimber, tubeExtender,
                        tubeTilt, bumper));
        teleInit();
        waitForStart();
        long endTime = System.currentTimeMillis() + 120000;
        //Game Loop
        while (opModeIsActive() && System.currentTimeMillis() < endTime) {
            //Defines gamepad buttons for buttons that are held
            containerTiltRight = gamepad1.dpad_left || gamepad2.dpad_left;
            containerTiltLeft = gamepad1.dpad_right || gamepad2.dpad_right;

            tubeExtend = gamepad1.x || gamepad2.x;
            tubeRetract = gamepad1.b || gamepad2.b;

            positionClimbersForward = gamepad1.dpad_up || gamepad2.dpad_up;
            positionClimbersBackward = gamepad1.dpad_down || gamepad2.dpad_down;
            releaseClimbers = gamepad1.start || gamepad2.start;
            toggleChurroHooks = gamepad1.right_stick_button || gamepad2.right_stick_button;
            toggleBarHooks = gamepad1.left_stick_button || gamepad2.left_stick_button;

            //Moves robot when some of the buttons are held
            if (tubeExtend) {
                tubeExtender.setPosition(0.75);
            } else if (tubeRetract) {
                tubeExtender.setPosition(-0.9);
            } else {
                tubeExtender.setPosition(0.5);
            }
            if (toggleChurroHooks && churroHooksDown) {
                leftChurroHook.setPosition(Functions.churroHookUpPos);
                rightChurroHook.setPosition(Functions.churroHookUpPos);
                Functions.waitFor(500);
                churroHooksDown = false;
            } else if (toggleChurroHooks && !churroHooksDown) {
                leftChurroHook.setPosition(Functions.churroHookDownPos);
                rightChurroHook.setPosition(Functions.churroHookDownPos);
                Functions.waitFor(500);
                churroHooksDown = true;
            }


            if (containerTiltRight) {
                tubeTilt.setPosition(tubeTilt.getPosition() + 0.01);
            } else if (containerTiltLeft) {
                tubeTilt.setPosition(tubeTilt.getPosition() - 0.01);
            } else {
                tubeTilt.setPosition(tubeTilt.getPosition());
            }

            if (toggleBarHooks && barHooksDown) {
                leftBarHook.setPosition(Functions.barHookUpPos);
                rightBarHook.setPosition(Functions.barHookUpPos);
                Functions.waitFor(500);
                barHooksDown = false;
            } else if (toggleChurroHooks && !barHooksDown) {
                leftBarHook.setPosition(Functions.barHookDownPos);
                rightBarHook.setPosition(Functions.barHookDownPos);
                Functions.waitFor(500);
                barHooksDown = true;
            }

            if(gamepad1.right_trigger > Functions.triggerThreshold){
                leftHangString.setPower(gamepad1.right_trigger);
                rightHangString.setPower(gamepad1.right_trigger);
            } else if (gamepad2.right_trigger > Functions.triggerThreshold){
                leftHangString.setPower(gamepad2.right_trigger);
                rightHangString.setPower(gamepad2.right_trigger);
            } else if(gamepad1.left_trigger > Functions.triggerThreshold){
                leftHangString.setPower(gamepad1.left_trigger);
                rightHangString.setPower(gamepad1.left_trigger);
            } else if(gamepad2.left_trigger > Functions.triggerThreshold){
                leftHangString.setPower(gamepad2.left_trigger);
                rightHangString.setPower(gamepad2.left_trigger);
            } else{
                leftHangString.setPower(0);
                rightHangString.setPower(0);
            }


                if (positionClimbersForward) {
                try {
                    mountainClimber.setPosition(
                            mountainClimber.getPosition() + 0.05);
                } catch (Exception e) {

                }
                positionClimbersBackward = false;
            } else if (positionClimbersBackward) {
                try {
                    mountainClimber.setPosition(
                            (mountainClimber.getPosition() - 0.05));
                } catch (Exception e) {

                }
                positionClimbersForward = false;
            } else {
                mountainClimber.setPosition(mountainClimber.getPosition());
            }
            if (releaseClimbers) {
                mountainClimberRelease.setPosition(1.0);
            } else {
                mountainClimberRelease.setPosition(0.0);
            }

            //For buttons that are not held
            if (updateGamepads()) {
                //Defines gamepad buttons
                rightWheelPower = gamepad1.right_stick_y;
                leftWheelPower = gamepad1.left_stick_y;

                linearSlideForward =
                        gamepad1.right_bumper || gamepad2.right_bumper;
                linearSlideBackward =
                        gamepad1.left_bumper || gamepad2.left_bumper;

				/*slowDriveBack = gamepad1.left_trigger;
				slowDriveForward = gamepad1.right_trigger;!!*/

                if (gamepad1.back || gamepad2.back) {
                    bumperUp = !bumperUp;
                    bumperDown = !bumperDown;
                }

                if (gamepad1.a || gamepad2.a) {
                    sweeperForward = !sweeperForward;
                    sweeperBackward = false;
                } else if (gamepad1.y || gamepad2.y) {
                    sweeperBackward = !sweeperBackward;
                    sweeperForward = false;
                }


                sweeperForward = gamepad1.a || gamepad2.a;
                sweeperBackward = gamepad1.y || gamepad2.y;

                //Use gamepad values to move robot
				if(slowDriveForward != 0 || slowDriveBack != 0) {
					Functions.moveTwoMotors(rightWheel, rightWheel,
							(slowDriveForward - slowDriveBack) * 0.25);
					Functions.moveTwoMotors(leftWheel, leftWheel,
							(slowDriveForward - slowDriveBack) * 0.25);
			} else if (rightWheelPower != 0 || leftWheelPower != 0) {
                    rightWheel.setPower(rightWheelPower);
                    leftWheel.setPower(leftWheelPower);
                } else {
                    leftWheel.setPower(0);
                    rightWheel.setPower(0);
                }

                if (linearSlideForward) {
                    linearSlide.setPower(Functions.linearSlidePower);
                } else if (linearSlideBackward) {
                    linearSlide.setPower(Functions.linearSlidePower);
                } else {
                    linearSlide.setPower(0);
                }

                if (sweeperForward) {
                    sweeper.setPower(-1.0);
                } else if (sweeperBackward) {
                    sweeper.setPower(1.0);
                } else {
                    sweeper.setPower(0);
                }

                if (bumperDown) {
                    bumper.setPosition(0);
                } else {
                    bumper.setPosition(1);
                }
            }

            idle();
        }
		end();
	}



	public static void teleInit() {
		mountainClimber.setPosition(Functions.mountainClimberTelePosition);
		mountainClimberRelease
				.setPosition(Functions.mountainClimberReleaseClose);
		tubeExtender.setPosition(Functions.tubeExtenderInitPosition);
		tubeTilt.setPosition(Functions.tubeTiltInitPosition);
		bumper.setPosition(Functions.bumperInitPosition);
        rightChurroHook.setPosition(Functions.churroHookUpPos);
        leftChurroHook.setPosition(Functions.churroHookUpPos);
        rightBarHook.setPosition(Functions.barHookUpPos);
        leftBarHook.setPosition(Functions.barHookUpPos);

	}

	public static void end() {
		rightWheel.setPowerFloat();
		leftWheel.setPowerFloat();
		sweeper.setPowerFloat();
	}
}