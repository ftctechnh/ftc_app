package org.usfirst.ftc.theintersect.code;

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

    boolean bumperUp = false;
    boolean bumperDown = false;
    boolean toggleChurroHooks = false;
    boolean churroHooksDown = false;
    boolean churroHooksUp = false;

    boolean barHooksDown = false;
    boolean toggleBarHooks = false;

    boolean extendLeftHangString = false;
    boolean extendRightHangString = false;
    boolean retractRightHangString = false;
    boolean retractLeftHangString = false;

	float slowDriveBack;
	float slowDriveForward;

    int extendCount = 0;
    int retractCount = 0;
    int normalCount = 0;

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
        rightWheel.setDirection(DcMotor.Direction.FORWARD);
        rightWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        leftWheel.setDirection(DcMotor.Direction.REVERSE);
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
        rightChurroHook.setDirection(Servo.Direction.REVERSE);
        rightBarHook.setDirection(Servo.Direction.REVERSE);
        bumper.setDirection(Servo.Direction.FORWARD);

        //Wait for the game to start
        teleInit();
        waitForStart();
        //long endTime = System.currentTimeMillis() + 120000;
        //Game Loop
        while (opModeIsActive() /*&& System.currentTimeMillis() < endTime*/) {
            //Controls for held buttons
            positionClimbersForward = gamepad2.dpad_up;
            positionClimbersBackward = gamepad2.dpad_down;
            releaseClimbers = gamepad2.back;
            containerTiltRight = gamepad1.dpad_left;
            containerTiltLeft = gamepad1.dpad_right;
            sweeperForward = (gamepad1.y || gamepad2.y);
            sweeperBackward = (gamepad1.a || gamepad2.a);
            linearSlideForward = gamepad2.right_bumper;
            linearSlideBackward = gamepad2.left_bumper;
            //bumperDown = (gamepad1.dpad_down || gamepad2.b);
            bumperDown = gamepad2.b;
            //bumperUp = (gamepad1.dpad_up || gamepad2.x);
            bumperUp = gamepad2.x;
            retractLeftHangString = gamepad1.left_bumper;
            retractRightHangString = gamepad1.right_bumper;
            extendLeftHangString = gamepad1.left_trigger > Functions.triggerThreshold;
            extendRightHangString = gamepad1.right_trigger > Functions.triggerThreshold;


            //Moves robot when some of the buttons are held
            if (extendRightHangString) {
                rightHangString.setPower(-Functions.hangStringPower);
            } else if (retractRightHangString) {
                rightHangString.setPower(Functions.hangStringPower);
            } else {
                rightHangString.setPower(0);
            }
            if (extendLeftHangString) {
                leftHangString.setPower(-Functions.hangStringPower);
            } else if (retractLeftHangString) {
                leftHangString.setPower(Functions.hangStringPower);
            } else {
                leftHangString.setPower(0);
            }
            if (linearSlideForward) {
                linearSlide.setPower(-Functions.linearSlidePower);
            } else if (linearSlideBackward) {
                linearSlide.setPower(Functions.linearSlidePower);
            } else {
                linearSlide.setPower(0);
            }

            if (bumperDown) {
                //bumper.setPosition(0.9);
            } else if (bumperUp) {
                //bumper.setPosition(0.1);
            } else {
                //bumper.setPosition(0.5);
            }

            if (sweeperForward) {
                sweeper.setPower(-Functions.sweeperPower);
            } else if (sweeperBackward) {
                sweeper.setPower(Functions.sweeperPower);
            } else {
                sweeper.setPower(0);
            }

            if (containerTiltRight) {
                tubeTilt.setPosition(tubeTilt.getPosition() + 0.02);
            } else if (containerTiltLeft) {
                tubeTilt.setPosition(tubeTilt.getPosition() - 0.02);
            } else {
                tubeTilt.setPosition(tubeTilt.getPosition());
            }


            //Old code (right trigger hangs, left trigger unhangs)
            /*if(gamepad1.right_trigger > Functions.triggerThreshold){
                leftHangString.setPower(gamepad1.right_trigger);
                rightHangString.setPower(gamepad1.right_trigger);
            } else if (gamepad2.right_trigger > Functions.triggerThreshold){
                leftHangString.setPower(gamepad2.right_trigger);
                rightHangString.setPower(gamepad2.right_trigger);
            } else if(gamepad1.left_trigger > Functions.triggerThreshold){
                leftHangString.setPower(-gamepad1.left_trigger);
                rightHangString.setPower(-gamepad1.left_trigger);
            } else if(gamepad2.left_trigger > Functions.triggerThreshold){
                leftHangString.setPower(-gamepad2.left_trigger);
                rightHangString.setPower(-gamepad2.left_trigger);
            } else{
                leftHangString.setPower(0);
                rightHangString.setPower(0);
            }*/

            /*if (gamepad1.right_trigger > Functions.triggerThreshold) {
                //leftHangString.setPower(gamepad1.right_trigger);
                rightHangString.setPower(gamepad1.right_trigger);
            } else if (gamepad2.right_trigger > Functions.triggerThreshold) {
                leftHangString.setPower(gamepad2.right_trigger);
                rightHangString.setPower(gamepad2.right_trigger);
            } else if (gamepad1.left_trigger > Functions.triggerThreshold) {
                leftHangString.setPower(gamepad1.left_trigger);
                //rightHangString.setPower(-gamepad1.left_trigger);
            } else if (gamepad2.left_trigger > Functions.triggerThreshold) {
                leftHangString.setPower(-gamepad2.left_trigger);
                rightHangString.setPower(-gamepad2.left_trigger);
            } else {
                leftHangString.setPower(0);
                rightHangString.setPower(0);
            }*/

            if (positionClimbersForward) {
                try {
                    mountainClimber.setPosition(
                            mountainClimber.getPosition() + 0.03);
                } catch (Exception e) {

                }
                positionClimbersBackward = false;
            } else if (positionClimbersBackward) {
                try {
                    mountainClimber.setPosition(
                            (mountainClimber.getPosition() - 0.03));
                } catch(Exception e) {

                }
                positionClimbersForward = false;
            } else {
                mountainClimber.setPosition(mountainClimber.getPosition());
            }
            if (releaseClimbers) {
                mountainClimberRelease.setPosition(Functions.mountainClimberReleaseOpen);
            } else {
                mountainClimberRelease.setPosition(Functions.mountainClimberReleaseClose);
            }

            //For toggle buttons
            if (updateGamepads()) {
                //Defines gamepad buttons for toggle buttons
                rightWheelPower = gamepad1.right_stick_y;
                leftWheelPower = gamepad1.left_stick_y;
                tubeExtend = gamepad1.x;
                tubeRetract = gamepad1.b;
                toggleChurroHooks = gamepad1.back;
                toggleBarHooks = gamepad2.start;

                /*slowDriveBack = gamepad1.left_trigger;
                slowDriveForward = gamepad1.right_trigger;!!*/

                //Use gamepad values to move robot
                if (slowDriveForward != 0 || slowDriveBack != 0) {
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
            }

            if (tubeRetract) {
                tubeExtender.setPosition(0.15);
                bumper.setPosition(0.15);
                /*try {
                    tubeExtender.setPosition(tubeExtender.getPosition() + 0.05);
                    bumper.setPosition(bumper.getPosition() + 0.05);
                } catch(Exception e) {

                }*/
                retractCount += 1;
            } else if (tubeExtend) {
                tubeExtender.setPosition(0.9);
                bumper.setPosition(0.9);
                /*try {
                    tubeExtender.setPosition(tubeExtender.getPosition() - 0.005);
                    bumper.setPosition(bumper.getPosition() - 0.005);
                } catch(Exception e) {

                }*/
                extendCount += 1;
            } else {
                tubeExtender.setPosition(0.5);
                bumper.setPosition(0.5);
                /*tubeExtender.setPosition(tubeExtender.getPosition());
                bumper.setPosition(bumper.getPosition());*/
                normalCount += 1;
            }

            if (toggleChurroHooks && churroHooksDown) {
                leftChurroHook.setPosition(Functions.churroHookUpPos);
                rightChurroHook.setPosition(Functions.churroHookUpPos);
                churroHooksDown = false;
            } else if (toggleChurroHooks && !churroHooksDown) {
                leftChurroHook.setPosition(Functions.churroHookDownPos);
                rightChurroHook.setPosition(Functions.churroHookDownPos);
                churroHooksDown = true;
            }

            if (toggleBarHooks && barHooksDown) {
                leftBarHook.setPosition(Functions.barHookUpPos);
                rightBarHook.setPosition(Functions.barHookUpPos);
                barHooksDown = false;
            } else if (toggleBarHooks && !barHooksDown) {
                leftBarHook.setPosition(Functions.barHookDownPos);
                rightBarHook.setPosition(Functions.barHookDownPos);
                barHooksDown = true;
            }

            if (isStopRequested()) {
                break;
            }

            if ((retractCount + extendCount + normalCount) >= 1) {
                telemetry.addData("Retract Count", retractCount);
                telemetry.addData("Extend Count", extendCount);
                telemetry.addData("Normal Count", normalCount);
                telemetry.addData("Bumper Position", bumper.getPosition());
                telemetry.addData("Tube Extender Position", tubeExtender.getPosition());
                telemetry.updateNow();
                retractCount = 0;
                extendCount = 0;
                normalCount = 0;
            }

            idle();
        }
		end();
	}

    public static void moveSlidesUpTime(int time, double power){
        linearSlide.setPower(-power);
        Functions.waitFor(time * 1000);
        linearSlide.setPower(0);
    }

    public static void stopRobot() {
        leftWheel.setPower(0);
        rightWheel.setPower(0);
    }

    public static void moveRobotForward(double leftPower, double rightPower) {
        leftWheel.setPower(-leftPower);
        rightWheel.setPower(-rightPower);
    }

    public static void moveRobotBackward(double leftPower, double rightPower) {
        leftWheel.setPower(leftPower);
        rightWheel.setPower(rightPower);
    }
    public static void moveRobotForwardTime(int seconds, double power) {
        moveRobotForward(power, power);
        Functions.waitFor(seconds * 1000);
        stopRobot();
    }

    public static void moveRobotBackwardTime(double seconds, double power) {
        moveRobotBackward(power, power);
        Functions.waitFor((int) seconds * 1000);
        stopRobot();
    }
    public static void autoHookChurro(int time, double speed){
        moveSlidesUpTime(2,0.5);
        moveRobotForwardTime(time, speed);
        leftChurroHook.setPosition(Functions.churroHookDownPos);
        rightChurroHook.setPosition(Functions.churroHookDownPos);
    }

    public static void autoUnhookChurro(int time, double speed){
        moveRobotForwardTime(time, speed);
        leftChurroHook.setPosition(Functions.churroHookDownPos);
        rightChurroHook.setPosition(Functions.churroHookDownPos);
    }

	public static void teleInit() {
        mountainClimber.setPosition(Functions.mountainClimberInitPosition);
        mountainClimberRelease.setPosition(Functions.mountainClimberReleaseClose);
        tubeExtender.setPosition(Functions.tubeExtenderInitPosition);
		tubeTilt.setPosition(Functions.tubeTiltInitPosition);
		bumper.setPosition(Functions.bumperInitPosition);
        rightChurroHook.setPosition(Functions.churroHookUpPos);
        leftChurroHook.setPosition(Functions.churroHookUpPos);
        rightBarHook.setPosition(Functions.barHookTeleInitPos);
        leftBarHook.setPosition(Functions.barHookTeleInitPos);
    }

	public static void end() {
        rightWheel.setPower(0);
        leftWheel.setPower(0);
        linearSlide.setPower(0);
        sweeper.setPower(0);
        leftHangString.setPower(0);
        rightHangString.setPower(0);
    }
}
