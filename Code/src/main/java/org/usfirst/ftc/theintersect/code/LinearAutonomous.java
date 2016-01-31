package org.usfirst.ftc.theintersect.code;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.robocol.Telemetry;

import org.swerverobotics.library.ClassFactory;

import java.util.Arrays;

/**
 * An Autonomous for both teams using the LinearOpMode
 */

@org.swerverobotics.library.interfaces.Autonomous(name = "Autonomous")
public class LinearAutonomous extends LinearOpMode {
	String team = "";
	int delay = 0;

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
	static ModernRoboticsI2cGyro gyro;
	static UltrasonicSensor ultrasonic;

	@Override
	public void runOpMode() throws InterruptedException {
		rightWheel = hardwareMap.dcMotor.get("rightWheel");
		leftWheel = hardwareMap.dcMotor.get("leftWheel");
		sweeper = hardwareMap.dcMotor.get("sweeper");
		lineColor = hardwareMap.colorSensor.get("lineColor");
		gyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");
		ultrasonic = hardwareMap.ultrasonicSensor.get("ultrasonic");
		tubeTilt = hardwareMap.servo.get("tubeTilt");
		tubeExtender = hardwareMap.servo.get("tubeExtender");
		mountainClimber = hardwareMap.servo.get("mountainClimber");
		mountainClimberRelease = hardwareMap.servo.get("mountainClimberRelease");
		bumper = hardwareMap.servo.get("bumper");


		autonomousInit(telemetry);
		/*ClassFactory.createEasyMotorController(this, linearSlideL,
				linearSlideR);
		ClassFactory.createEasyMotorController(this, rightWheel, leftWheel);
		ClassFactory.createEasyMotorController(this, sweeper, null);*/
		ClassFactory.createEasyServoController(this, Arrays.asList(mountainClimberRelease, mountainClimber, tubeExtender, tubeTilt, bumper));
		ClassFactory.createSwerveColorSensor(this, lineColor);


		//Delay And Team Selection

		/*while(true) {
            if (gamepad1.x) {
                team = "Blue";
            } else if (gamepad1.b) {
                team = "Red";
            } else if (gamepad1.dpad_up) {
                delay += 1;
            } else if (gamepad1.dpad_down) {
                delay -= 1;
                if (delay < 0) {
                    delay = 0;
                }
            } else if (gamepad1.a) {
                telemetry.addData("Team: ", team + " Confirmed!");
                telemetry.addData("Delay: ", delay + " Confirmed!");
                break;
            }
            telemetry.addData("Team", team);
            telemetry.addData("Delay", delay);
            sleep(300);
		}*/
//
		waitForStart();

		if(opModeIsActive()) {
			telemetry.clearData();
			//Starting based off of the delay
			//sleep(delay * 1000);
			//Autonomous Routine
            moveRobotBackwardRotationsGyro(3000, 0.4, 10000);
			//spinRobotLeftDegrees(90,0.3,60000,telemetry);
            //spinRobotLeftDegrees(90, 0.3, 60000, telemetry);
            //spinRobotLeftDegrees(90, 0.3, 60000, telemetry);


            end();
		}
	}


    public static void directionInit() {
        rightWheel.setDirection(DcMotor.Direction.FORWARD);
        leftWheel.setDirection(DcMotor.Direction.REVERSE);
        mountainClimber.setDirection(Servo.Direction.FORWARD);
        mountainClimberRelease.setDirection(Servo.Direction.REVERSE);
        bumper.setDirection(Servo.Direction.FORWARD);
        tubeExtender.setDirection(Servo.Direction.REVERSE);
        tubeTilt.setDirection(Servo.Direction.REVERSE);
    }


    public static void gyroInit(Telemetry telemetry) {
        int previousPosition;
        int repeatCount = 0;
        int currentPosition;

        gyro.calibrate();
        gyro.setHeadingMode(
                ModernRoboticsI2cGyro.HeadingMode.HEADING_CARTESIAN);
        while (gyro.isCalibrating()) {
            telemetry.addData("gyroInit: ", "Calibrating Gyro");
        }
        telemetry.addData("gyroInit: ", "Calibration Complete");
        previousPosition = gyro.getIntegratedZValue();
        initGyroHeading(telemetry);
    }

    // setup gyro so it will have stable reading of heading
    public static void initGyroHeading(Telemetry telemetry ) {
        int repeatCount = 0;
        int currentPosition;
        int previousPosition;
        int totalCount = 0;

        previousPosition = gyro.getIntegratedZValue();
        Functions.waitFor(500);
        while (repeatCount < 5 ){
            currentPosition = gyro.getIntegratedZValue();
            if ( currentPosition == previousPosition) {
                repeatCount++;
                telemetry.addData("initGyroHeading CurrentHeading", currentPosition);
            } else {
                previousPosition = currentPosition;
                repeatCount = 0;
            }
            Functions.waitFor(500);
            totalCount++;
        }
        telemetry.addData("initGyroHeading: Done ", totalCount);

    }

	public static void autonomousInit(Telemetry telemetry) {
        lineColor.enableLed(true);
        servoInit();
        directionInit();
        gyroInit(telemetry);
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

	public static void moveRobotForwardTime(int seconds, double power) {
		moveRobotForward(power, power);
		Functions.waitFor(seconds * 1000);
		stopRobot();
	}

	public static void moveRobotBackwardTime(int seconds, double power) {
		moveRobotBackward(power, power);
		Functions.waitFor(seconds * 1000);
		stopRobot();
	}

    public static void moveRobotForwardTimeGyro(int seconds, double power) {
        moveRobotForward(power, power);
        Functions.waitFor(seconds * 1000);
        stopRobot();
    }

    public static void moveRobotBackwardTimeGyro(int seconds, double power) {
        moveRobotBackward(power, power);
        Functions.waitFor(seconds * 1000);
        stopRobot();
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
		rightWheel.setPower(-power);
		leftWheel.setPower(power);
	}
    public static void moveRobotBackwardRotations(double rotations, double power,
                                                  long timeoutMill) {
        long endTime = System.currentTimeMillis() + timeoutMill;
        resetEncoders();
        double encoderVal = rotations * Functions.neveRestDegreeRatio;
        rightWheel.setTargetPosition(-(int) encoderVal);
        leftWheel.setTargetPosition(-(int) encoderVal);
        while(endTime > System.currentTimeMillis()) {
            moveRobotBackward(power, power);
            if(!rightWheel.isBusy() && !leftWheel.isBusy()) {
                stopRobot();
                break;
            }
        }
    }

    public static void moveRobotForwardRotations(double rotations, double power,
                                                 long timeoutMill) {
        long endTime = System.currentTimeMillis() + timeoutMill;
        resetEncoders();
        double encoderVal = rotations * Functions.neveRestDegreeRatio;
        rightWheel.setTargetPosition((int) -encoderVal);
        leftWheel.setTargetPosition((int) -encoderVal);
        while(endTime > System.currentTimeMillis()) {
            moveRobotForward(power, power);
            if(!rightWheel.isBusy() && !leftWheel.isBusy()) {
                stopRobot();
            }
        }
    }
	public static void moveRobotBackwardRotationsGyro(double rotations, double power, long timeoutMill) {
		long endTime = System.currentTimeMillis() + timeoutMill;
		resetEncoders();
		double encoderVal = rotations * Functions.neveRestDegreeRatio;
		rightWheel.setTargetPosition(-(int) encoderVal);
		leftWheel.setTargetPosition(-(int) encoderVal);
        double adjustedPower;
        int targetHeading = gyro.getIntegratedZValue();

        // start robot
        moveRobotBackward(power, power);


		while(endTime > System.currentTimeMillis()) { // check timeout
            int currentHeading = gyro.getIntegratedZValue();
            int delta = currentHeading - targetHeading;
            int x = delta;

            if ( x < 0) x = -x;
            adjustedPower = power + (x/Functions.straightGyroCorrectionFactor);
            if(adjustedPower > Functions.adjustedPowerMax){
                adjustedPower = Functions.adjustedPowerMax;
            }else if (adjustedPower < Functions.adjustedPowerMin){
                adjustedPower = Functions.adjustedPowerMin;
            }

            if( delta < 0 ){ //drifting right
                moveRobotBackward(adjustedPower, power);
            } else if (delta > 0){ //drifting left
                moveRobotBackward(power, adjustedPower);
            }

			if(!rightWheel.isBusy() && !leftWheel.isBusy()) {
				stopRobot();
				break;
			}
		}
	}

	public static void moveRobotForwardRotationsGyro(double rotations, double power,
			long timeoutMill) {
		long endTime = System.currentTimeMillis() + timeoutMill;
		resetEncoders();
		double encoderVal = rotations * Functions.neveRestDegreeRatio;
		rightWheel.setTargetPosition((int) -encoderVal);
		leftWheel.setTargetPosition((int) -encoderVal);
        double adjustedPower = power + (gyro.getIntegratedZValue()/Functions.straightGyroCorrectionFactor);
        if(adjustedPower > 1.0){
            adjustedPower = 1.0;
        }else if (adjustedPower < 0){
            adjustedPower = 0.0;
        }
		while(endTime > System.currentTimeMillis()) {
            if(gyro.getIntegratedZValue() < 0){ //drifting right
                moveRobotForward( adjustedPower, power);
            } else if (gyro.getIntegratedZValue() > 0){ //drifting left
                moveRobotForward(power, adjustedPower);
            }else {
                moveRobotForward(power , power);
            }			if(!rightWheel.isBusy() && !leftWheel.isBusy()) {
				stopRobot();
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
				stopRobot();
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
				stopRobot();
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
				stopRobot();
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
				stopRobot();
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
				stopRobot();
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
				stopRobot();
				break;
			}
		}
	}

	public static void spinRobotLeftDegrees(int degrees, double power, long timeoutMill, Telemetry telemetry) {
		long endTime;
		int startPosition;
		int currentPosition;
        int endPosition;

        initGyroHeading(telemetry);
        startPosition = gyro.getIntegratedZValue();
        endPosition = startPosition + degrees;
        endTime = System.currentTimeMillis() + timeoutMill;
        telemetry.addData("startPosition ", startPosition);

        spinRobotLeft(power);
		while(System.currentTimeMillis() < endTime && (currentPosition = gyro.getIntegratedZValue()) < endPosition)
		{
            Functions.waitFor(1);
			telemetry.addData("CurrentHeading", currentPosition);
            telemetry.addData("Endposition ", endPosition);
        }
        spinRobotLeft(0);
        telemetry.addData("Endposition ", endPosition);
        telemetry.addData("Done?" , "Yes");
        stopRobot();
	}


	public static void spinRobotRightDegrees(int degrees, double power, long timeoutMill) {
		long endTime = System.currentTimeMillis() + timeoutMill;
		int endPosition = gyro.getIntegratedZValue() + degrees;
		spinRobotRight(power);
		while(endTime > System.currentTimeMillis()) {
			if(endPosition < gyro.getIntegratedZValue()) {
				spinRobotLeft(0.1);
			}
		}
		stopRobot();
	}

	public static void turnRobotRightForwardDegrees(int degrees, double
			power, long timeoutMill) {
		long endTime = -System.currentTimeMillis() - timeoutMill;
		int endPosition = gyro.getIntegratedZValue() + degrees;
		turnRobotRightForward(power);
		while(endTime > System.currentTimeMillis()) {
			if(endPosition < gyro.getIntegratedZValue()) {
				turnRobotLeftForward(0.1);
			}
		}
		stopRobot();
	}

	public static void turnRobotLeftForwardDegrees(int degrees, double
			power, long timeoutMill) {
		long endTime = System.currentTimeMillis() + timeoutMill;
		int endPosition = gyro.getIntegratedZValue() + degrees;
		turnRobotLeftForward(power);
		while(endTime > System.currentTimeMillis()) {
			if(endPosition > gyro.getIntegratedZValue()) {
				turnRobotRightForward(0.1);
			}
		}
		stopRobot();
	}

	public static void turnRobotRightBackwardDegrees(int degrees, double
			power, long timeoutMill) {
		long endTime = -System.currentTimeMillis() - timeoutMill;
		int endPosition = gyro.getIntegratedZValue() + degrees;
		turnRobotRightBackward(power);
		while(endTime > System.currentTimeMillis()) {
			if(endPosition < gyro.getIntegratedZValue()) {
				turnRobotLeftBackward(0.1);
			}
		}
		stopRobot();
	}

	public static void turnRobotLeftBackwardDegrees(int degrees, double
			power, long timeoutMill) {
		long endTime = System.currentTimeMillis() + timeoutMill;
		int endPosition = gyro.getIntegratedZValue() + degrees;
		turnRobotLeftBackward(power);
		while(endTime > System.currentTimeMillis()) {
			if(endPosition > gyro.getIntegratedZValue()) {
				turnRobotRightBackward(0.1);
			}
		}
		stopRobot();
	}


	public static void resetEncoders() {
		rightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		rightWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
		leftWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
		leftWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
	}

	public static void end() {
		stopRobot();
		rightWheel.close();
		leftWheel.close();
		sweeper.setPower(0);
		sweeper.close();
		lineColor.enableLed(false);
		lineColor.close();
	}
}
