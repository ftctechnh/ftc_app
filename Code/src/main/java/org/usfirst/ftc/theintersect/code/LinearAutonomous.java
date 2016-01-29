package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.ClassFactory;

@org.swerverobotics.library.interfaces.Autonomous(name = "LinearAutonomous")
public class LinearAutonomous extends LinearOpMode {
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

	static ColorSensor lineColor;
	static GyroSensor gyro;
	static UltrasonicSensor ultrasonic;

	@Override
	public void runOpMode() throws InterruptedException {
		rightWheel = hardwareMap.dcMotor.get("rightWheel");
		leftWheel = hardwareMap.dcMotor.get("leftWheel");

		sweeper = hardwareMap.dcMotor.get("sweeper");

		lineColor = hardwareMap.colorSensor.get("lineColor");
		gyro = hardwareMap.gyroSensor.get("gyro");
		ultrasonic = hardwareMap.ultrasonicSensor.get("ultrasonic");
		tubeTilt = hardwareMap.servo.get("tubeTilt");
		tubeExtender = hardwareMap.servo.get("tubeExtender");
		mountainClimber = hardwareMap.servo.get("mountainClimber");
		mountainClimberRelease = hardwareMap.servo
				.get("mountainClimberRelease");

		bumper = hardwareMap.servo.get("bumper");

		rightWheel.setDirection(DcMotor.Direction.REVERSE);
		leftWheel.setDirection(DcMotor.Direction.FORWARD);
		mountainClimber.setDirection(Servo.Direction.FORWARD);
		mountainClimberRelease.setDirection(Servo.Direction.REVERSE);
		bumper.setDirection(Servo.Direction.FORWARD);
		tubeExtender.setDirection(Servo.Direction.REVERSE);
		tubeTilt.setDirection(Servo.Direction.REVERSE);
		autonomousInit();
		ClassFactory.createSwerveColorSensor(this, lineColor);

		waitForStart();

		if(opModeIsActive()) {
			//Autonomous Routine

		}
	}

	public static void autonomousInit() {
		mountainClimber.setPosition(0.1);
		mountainClimberRelease.setPosition(0.0);
		tubeExtender.setPosition(0.55);
		tubeTilt.setPosition(0.5);
		bumper.setPosition(0);
	}

	public static void moveRobotForward(double leftPower, double rightPower) {
		leftWheel.setPower(leftPower);
		rightWheel.setPower(rightPower);
	}

	public static void moveRobotBackward(double leftPower, double rightPower) {
		leftWheel.setPower(-leftPower);
		rightWheel.setPower(-rightPower);
	}

	public static void stopRobot() {
		leftWheel.setPower(0);
		rightWheel.setPower(0);
	}

	public static void turnRobotLeftForward(double power) {
		rightWheel.setPower(power);
		leftWheel.setPower(0);
	}

	public static void turnRobotRightForward(double power) {
		rightWheel.setPower(0);
		leftWheel.setPower(power);
	}

	public static void turnRobotLeftBackward(double power) {
		rightWheel.setPower(0);
		leftWheel.setPower(-power);
	}

	public static void turnRobotRightBackward(double power) {
		rightWheel.setPower(-power);
		leftWheel.setPower(0);
	}

	public static void spinRobotLeft(double power) {
		rightWheel.setPower(power);
		leftWheel.setPower(-power);
	}

	public static void spinRobotRight(double power) {
		rightWheel.setPower(power);
		leftWheel.setPower(-power);
	}

	public static void moveRobotBackRotations(double rotations, double power,
			long timeoutMill) {
		long endTime = System.currentTimeMillis() + timeoutMill;
		resetEncoders();
		double encoderVal = rotations * Functions.neveRestPPR;
		rightWheel.setTargetPosition(-(int) encoderVal);
		leftWheel.setTargetPosition(-(int) encoderVal);
		while(endTime > System.currentTimeMillis()) {
			moveRobotBackward(power, power);
			if(!rightWheel.isBusy() && !leftWheel.isBusy()) {
				moveRobotBackward(0, 0);
				break;
			}
		}
	}

	public static void moveRobotForwardRotations(double rotations, double power,
			long timeoutMill) {
		long endTime = System.currentTimeMillis() + timeoutMill;
		resetEncoders();
		double encoderVal = rotations * Functions.neveRestPPR;
		rightWheel.setTargetPosition((int) encoderVal);
		leftWheel.setTargetPosition((int) encoderVal);
		while(endTime > System.currentTimeMillis()) {
			moveRobotForward(power, power);
			if(!rightWheel.isBusy() && !leftWheel.isBusy()) {
				moveRobotForward(0, 0);
				break;
			}
		}
	}

	public static void spinRobotLeftDegrees(double degrees, double power,
			long timeoutMill) {
		long endTime = System.currentTimeMillis() + timeoutMill;
		resetEncoders();
		double encoderVal = degrees * Functions.neveRestDegreeRatio;
		rightWheel.setTargetPosition((int) encoderVal);
		leftWheel.setTargetPosition(-(int) encoderVal);
		while(endTime > System.currentTimeMillis()) {
			spinRobotLeft(power);
			if(!rightWheel.isBusy() && !leftWheel.isBusy()) {
				spinRobotLeft(0);
				break;
			}
		}
	}

	public static void spinRobotRightDegrees(double degrees, double power,
			long timeoutMill) {
		long endTime = System.currentTimeMillis() + timeoutMill;
		resetEncoders();
		double encoderVal = degrees * Functions.neveRestDegreeRatio;
		rightWheel.setTargetPosition(-(int) encoderVal);
		leftWheel.setTargetPosition((int) encoderVal);
		while(endTime > System.currentTimeMillis()) {
			spinRobotRight(power);
			if(!rightWheel.isBusy() && !leftWheel.isBusy()) {
				spinRobotRight(0);
				break;
			}
		}
	}

	public static void turnRobotRightForwardDegrees(double degrees, double
			power, long timeoutMill) {
		long endTime = System.currentTimeMillis() + timeoutMill;
		resetEncoders();
		double encoderVal = degrees * Functions.neveRestDegreeRatio;
		rightWheel.setTargetPosition(0);
		leftWheel.setTargetPosition((int) encoderVal);
		while(endTime > System.currentTimeMillis()) {
			turnRobotRightForward(power);
			if(!rightWheel.isBusy() && !leftWheel.isBusy()) {
				turnRobotRightForward(0);
				break;
			}
		}
	}

	public static void turnRobotLeftForwardDegrees(double degrees, double
			power, long timeoutMill) {
		long endTime = System.currentTimeMillis() + timeoutMill;
		resetEncoders();
		double encoderVal = degrees * Functions.neveRestDegreeRatio;
		rightWheel.setTargetPosition((int) encoderVal);
		leftWheel.setTargetPosition(0);
		while(endTime > System.currentTimeMillis()) {
			turnRobotLeftForward(power);
			if(!rightWheel.isBusy() && !leftWheel.isBusy()) {
				turnRobotLeftForward(0);
				break;
			}
		}
	}

	public static void turnRobotRightBackwardDegrees(double degrees, double
			power, long timeoutMill) {
		long endTime = System.currentTimeMillis() + timeoutMill;
		resetEncoders();
		double encoderVal = degrees * Functions.neveRestDegreeRatio;
		rightWheel.setTargetPosition(-(int) encoderVal);
		leftWheel.setTargetPosition(0);
		while(endTime > System.currentTimeMillis()) {
			turnRobotRightBackward(power);
			if(!rightWheel.isBusy() && !leftWheel.isBusy()) {
				turnRobotRightBackward(0);
				break;
			}
		}
	}

	public static void turnRobotLeftBackwardDegrees(double degrees, double
			power, long timeoutMill) {
		long endTime = System.currentTimeMillis() + timeoutMill;
		resetEncoders();
		double encoderVal = degrees * Functions.neveRestDegreeRatio;
		rightWheel.setTargetPosition(0);
		leftWheel.setTargetPosition(-(int) encoderVal);
		while(endTime > System.currentTimeMillis()) {
			turnRobotRightBackward(power);
			if(!rightWheel.isBusy() && !leftWheel.isBusy()) {
				turnRobotLeftBackward(0);
				break;
			}
		}
	}

	public static void resetEncoders() {
		rightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		rightWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		leftWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		leftWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
	}
}
