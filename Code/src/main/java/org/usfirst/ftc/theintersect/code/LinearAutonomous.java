package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

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
		mountainClimberRelease = hardwareMap.servo.get("mountainClimberRelease");
		bumper = hardwareMap.servo.get("bumper");
        ClassFactory.createSwerveColorSensor(this, lineColor);

        autonomousInit();

		waitForStart();

		if(opModeIsActive()) {
			//Autonomous Routine

		}
	}


    public static void directionInit() {
        rightWheel.setDirection(DcMotor.Direction.REVERSE);
        leftWheel.setDirection(DcMotor.Direction.FORWARD);
        mountainClimber.setDirection(Servo.Direction.FORWARD);
        mountainClimberRelease.setDirection(Servo.Direction.REVERSE);
        bumper.setDirection(Servo.Direction.FORWARD);
        tubeExtender.setDirection(Servo.Direction.REVERSE);
        tubeTilt.setDirection(Servo.Direction.REVERSE);
    }

	public static void autonomousInit() {
		servoInit();
        directionInit();
	}

    public static void servoInit(){
        mountainClimber.setPosition(Functions.mountainClimberInitPosition);
        mountainClimberRelease.setPosition(Functions.mountainClimberReleaseInitPosition);
        tubeExtender.setPosition(Functions.tubeExtenderInitPosition);
        tubeTilt.setPosition(Functions.tubeTiltInitPosition);
        bumper.setPosition(Functions.bumperInitPosition);
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

	public static void moveRobotBackwardRotations(double rotations, double power,
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

	public static void spinRobotLeftRotations(double degrees, double power,
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

	public static void spinRobotRightRotations(double degrees, double power,
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

	public static void turnRobotRightForwardRotations(double degrees, double
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

	public static void turnRobotLeftForwardRotations(double degrees, double
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

	public static void turnRobotRightBackwardRotations(double degrees, double
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

	public static void turnRobotLeftBackwardRotations(double degrees, double
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
