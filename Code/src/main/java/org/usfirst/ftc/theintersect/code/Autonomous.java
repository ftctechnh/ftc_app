package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

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

    static ColorSensor lineColor;
    static LightSensor light;
    static GyroSensor gyro;
    static UltrasonicSensor ultrasonic;

    static Servo mountainClimber;
    static Servo mountainClimberRelease;
    static Servo containerTilt;
    @Override
    public void main() throws InterruptedException {
        //Initialize hardware
        frontRightWheel = hardwareMap.dcMotor.get("frontRightWheel");
        frontLeftWheel = hardwareMap.dcMotor.get("frontLeftWheel");
        backRightWheel = hardwareMap.dcMotor.get("backRightWheel");
        backLeftWheel = hardwareMap.dcMotor.get("backLeftWheel");

        sweeper = hardwareMap.dcMotor.get("sweeper");

        lineColor = hardwareMap.colorSensor.get("lineColor");
        light = hardwareMap.lightSensor.get("light");
        gyro = hardwareMap.gyroSensor.get("gyro");
        ultrasonic = hardwareMap.ultrasonicSensor.get("ultrasonic");

        mountainClimber = hardwareMap.servo.get("mountainClimber");
        mountainClimberRelease = hardwareMap.servo.get("mountainClimberRelease");
        containerTilt = hardwareMap.servo.get("containerTilt");


        //Set motor and servo directions
        frontRightWheel.setDirection(DcMotor.Direction.REVERSE);
        frontLeftWheel.setDirection(DcMotor.Direction.FORWARD);
        backRightWheel.setDirection(DcMotor.Direction.FORWARD);
        backLeftWheel.setDirection(DcMotor.Direction.REVERSE);

        mountainClimber.setDirection(Servo.Direction.FORWARD);
        //mountainClimber.setPosition(0.25);
        mountainClimberRelease.setDirection(Servo.Direction.REVERSE);
        mountainClimberRelease.setPosition(0);
        containerTilt.setDirection(Servo.Direction.REVERSE);
        containerTilt.setPosition(0.5);


        telemetry.clearDashboard();
        telemetry.update();
        //Enable the LED for the line following color sensor
        lineColor.enableLed(true);

        //Gyro Calibration
        gyro.calibrate();
        while(gyro.isCalibrating()) {
            telemetry.addData("Gyro Calibration", "Calibrating");
            telemetry.update();
        }
        telemetry.addData("Gyro Calibration", "Calibration Done");
        telemetry.update();

        //Reset position of all the servos
        mountainClimber.setPosition(0);
        mountainClimberRelease.setPosition(0);

        //Autonomous Start
        waitForStart();
        if (opModeIsActive()) {
            telemetry.clearDashboard();
            telemetry.addData("Autonomous", "Running");
            telemetry.update();
            //moveRobotRotations(-5, 0.5, telemetry);
            //Functions.waitFor(1000);
            debugColor(telemetry);
            //stopAtWhite(-0.5, 10000, telemetry);
            /*Functions.waitFor(5000);
            turnRobotLeftDegrees(45, 0.4, 5000, telemetry);
            Functions.waitFor(5000);
            moveRobotSeconds(3, 0.3);
            Functions.waitFor(5000);
            while (ultraVal < 15) {
                moveRobotPower(-0.3);
            }
            dumpClimbers(telemetry);
            Functions.waitFor(5000);
            while (ultraVal < 30) {


                moveRobotPower(-0.3);
            }
            turnRobotRightDegrees(180, 0.5, 10000, telemetry);
            end();
*/
        }
        telemetry.addData("Autonomous", "Done");
        //Autonomous End
    }

    //Functions
    //Aligns the robot so that it is perpendicular to the wal
    public static void alignRobot(TelemetryDashboardAndLog telemetry, long timeout, String color) {
        long endTime = System.currentTimeMillis() + timeout;
        while(System.currentTimeMillis() < endTime) {
            int counter = 0;
            int done = 10000;
            while (counter <= done) {
                counter = 0;
                while (detectWhite(telemetry)) {
                    moveRobotPower(0.25);
                    counter += 1;
                    if(counter <= done) {
                        break;
                    }
                }
                moveRobotPower(0);
                if(counter <= done) {
                    break;
                }
                if (color.equals("B")) {
                    frontRightWheel.setPower(-0.25);
                    frontRightWheel.setPower(-0.25);
                    frontLeftWheel.setPower(0.25);
                    backLeftWheel.setPower(0.25);
                } else {
                    frontRightWheel.setPower(0.25);
                    frontLeftWheel.setPower(0.25);
                    backRightWheel.setPower(-0.25);
                    backLeftWheel.setPower(-0.250);
                }

                while (!detectWhite(telemetry)) {
                    Functions.waitFor(100);
                }
                moveRobotPower(0);
            }
        }
    }

    //Detects white
    public static boolean detectWhite(TelemetryDashboardAndLog telemetry) {
		int red = lineColor.red();
		int green = lineColor.green();
		int blue = lineColor.blue();
        double average = (red + lineColor.green() + lineColor.blue())/3;
        if(average > 0 && lineColor.red() >= average-Functions.colorError && lineColor.red() <= average+Functions.colorError && lineColor.green() >= average-Functions.colorError && lineColor.green() <= average+Functions.colorError && lineColor.blue() >= average-Functions.colorError && lineColor.blue() <= average+Functions.colorError) {
            telemetry.addData("Red", lineColor.red());
            telemetry.addData("Blue" , lineColor.blue());
            telemetry.addData("Green", lineColor.green());
            telemetry.update();
            return true;
        }
        telemetry.addData("Red", lineColor.red());
        telemetry.addData("Blue", lineColor.blue());
        telemetry.addData("Green", lineColor.green());
        telemetry.update();
        return false;
    }

    //Dumps climbers based on position from the wall
    public static void dumpClimbers(TelemetryDashboardAndLog telemetry) {
        double[] positions = {100, 102.8, 105.6, 108.4, 111.2, 114, 116.8, 119.6, 121.4, 123.2, 128, 130.8, 133.6, 136.4, 139.2, 142, 141.5, 147.6, 150.4, 153.2, 156, 0, 0, 0, 0, 170};
        double ultraVal  = ultrasonic.getUltrasonicLevel();
        if(ultraVal < 5) {
            ultraVal = 5;
        } else if (ultraVal >= 30) {
            ultraVal = 30;
        }
        telemetry.clearDashboard();
        telemetry.addData("Ultrasonic Level", ultraVal);
        telemetry.addData("Dumper Position", positions[(int) ultraVal - 5] / 180);
        telemetry.update();

        mountainClimber.setPosition(positions[(int) ultraVal - 5] / 180);
        Functions.waitFor(5000);
        mountainClimberRelease.setPosition(1.0);
        Functions.waitFor(5000);
        mountainClimberRelease.setPosition(0.0);

    }

    //Resets the encoders and forces the motors to run to the target position
    public static void prepareMotors() {
        frontRightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontRightWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        frontLeftWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        frontLeftWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        backRightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backRightWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        backLeftWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backLeftWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    //Moves robot at a certain power
    public static void moveRobotPower(double power) {
        //Moves the robot at a certain power
        frontRightWheel.setPower(power);
        frontLeftWheel.setPower(power);
        backRightWheel.setPower(power);
        backLeftWheel.setPower(power);
    }

    //Moves the robot a certain amount of rotations
    public static void moveRobotRotations(double rotations, double power, TelemetryDashboardAndLog telemetry) {
        prepareMotors();
        backRightWheel.setTargetPosition((int) (rotations * Functions.neveRestPPR));
        backLeftWheel.setTargetPosition((int) (rotations * Functions.neveRestPPR));
        moveRobotPower(power);

        while (!(backRightWheel.getCurrentPosition() >= backRightWheel.getTargetPosition() - Functions.encoderError && backRightWheel.getCurrentPosition() <= backRightWheel.getTargetPosition() + Functions.encoderError) && !(backLeftWheel.getCurrentPosition() >= backLeftWheel.getTargetPosition() - Functions.encoderError && backLeftWheel.getCurrentPosition() <= backLeftWheel.getTargetPosition() + Functions.encoderError)) {
            telemetry.addData("Right: ", backRightWheel.getCurrentPosition()/Functions.neveRestPPR);
            telemetry.addData("Left: ", backLeftWheel.getCurrentPosition()/Functions.neveRestPPR);
            telemetry.update();
        }
        moveRobotPower(0);
    }

    //Moves the robot a certain amount of degrees
    public static void moveRobotDegrees(double degrees, double power, TelemetryDashboardAndLog telemetry) {
        prepareMotors();
        backRightWheel.setTargetPosition((int) (degrees*Functions.neveRestDegreeRatio));
        backLeftWheel.setTargetPosition((int) (degrees*Functions.neveRestDegreeRatio));
        moveRobotPower(power);
        while (!(backRightWheel.getCurrentPosition() >= backRightWheel.getTargetPosition() - Functions.encoderError && backRightWheel.getCurrentPosition() <= backRightWheel.getTargetPosition() + Functions.encoderError) && !(backLeftWheel.getCurrentPosition() >= backLeftWheel.getTargetPosition() - Functions.encoderError && backLeftWheel.getCurrentPosition() <= backLeftWheel.getTargetPosition() + Functions.encoderError)) {
            telemetry.addData("Right: ", backRightWheel.getCurrentPosition()/Functions.neveRestDegreeRatio);
            telemetry.addData("Left: ", backLeftWheel.getCurrentPosition()/Functions.neveRestDegreeRatio);
            telemetry.update();
        }
        moveRobotPower(0);
    }

    //Moves the robot a certain amount of inches
    public static void moveRobotInches(double inches, double power, TelemetryDashboardAndLog telemetry) {
        moveRobotRotations(inches / Functions.backWheelCircumfrence, power, telemetry);
    }

    //Moves the robot for a certain amount of time
    public static void moveRobotSeconds(long seconds, double power) {
        long endTime = System.currentTimeMillis() + (seconds*1000);
        moveRobotPower(power);
        while(System.currentTimeMillis() < endTime) {

        }
        moveRobotPower(0);
    }

    //Moves the robot until the robot is above white by using the MR Color Sensor
    public static void stopAtWhite(double power, long timeout, TelemetryDashboardAndLog telemetry) {
        moveRobotPower(power);
        telemetry.addData("Color Sensing" , "Sensing");
        telemetry.update();
        long endTime = System.currentTimeMillis() + (timeout*1000);
        while (System.currentTimeMillis() < endTime) {
            if(detectWhite(telemetry)) {
                break;
            }
        }
        moveRobotPower(0);
    }

    //Turns the robot right a certain amount of degrees using the MR Gyro Sensor
    public static void turnRobotRightDegrees(int degrees, double power, int timeout, TelemetryDashboardAndLog telemetry) {
        int start = gyro.getHeading();
        int heading = start;
        long endtime = System.currentTimeMillis() + (timeout * 1000);
        int protectedValue = start - 20;
        int target = start + degrees;
        if (target >= 360) {
            target = target - 360;
        }
        if (protectedValue < 0) {
            protectedValue = protectedValue + 360;
        }
        else if (target > start) {
            protectedValue = 360;
        }
        backLeftWheel.setPower(power);
        backRightWheel.setPower(-power);
        frontRightWheel.setPower(-power);
        frontLeftWheel.setPower(power);
        while ((heading < target) && (System.currentTimeMillis() < endtime)) {
            heading = gyro.getHeading();
            if (heading > protectedValue) {
                heading = heading - 360;
            }

            telemetry.addData("Start", start);
            telemetry.addData("Heading", gyro.getHeading());
            telemetry.addData("Target", target);
            telemetry.addData("Protected Value", protectedValue);
            telemetry.update();
        }
        backLeftWheel.setPower(0);
        backRightWheel.setPower(0);
        frontRightWheel.setPower(0);
        frontLeftWheel.setPower(0);

        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("Protect", protectedValue);
        telemetry.update();

    }

    //Turns the robot left a certain amount of degrees by using the MR Gyro Sensor
	public static void turnRobotLeftDegrees(int degrees, double power, int timeout, TelemetryDashboardAndLog telemetry) {
		int start = gyro.getHeading();
		int heading = start;
		long endtime = System.currentTimeMillis() + (timeout * 1000);
		int protectedValue = start + 20;
		int target = start + degrees;
		if (target >= 360) {
			target = target - 360;
		}
		if (protectedValue >= 360) {
			protectedValue = protectedValue - 360;
		}
		else if (target < start) {
			protectedValue = -1;
		}
		backLeftWheel.setPower(-power);
		backRightWheel.setPower(power);
        frontRightWheel.setPower(power);
        frontLeftWheel.setPower(-power);
		while ((heading > target) && (System.currentTimeMillis() < endtime)) {
			heading = gyro.getHeading();
			if (heading < protectedValue) {
				heading = heading + 360;
			}

			telemetry.addData("Start", start);
			telemetry.addData("Heading", gyro.getHeading());
			telemetry.addData("Target", target);
			telemetry.addData("Protect", protectedValue);
			telemetry.update();
		}
		backLeftWheel.setPower(0);
		backRightWheel.setPower(0);
        frontRightWheel.setPower(0);
        frontLeftWheel.setPower(0);
		telemetry.addData("Start", start);
		telemetry.addData("Heading", gyro.getHeading());
		telemetry.addData("Target", target);
		telemetry.addData("Protect", protectedValue);
		telemetry.update();

	}

    //Outputs all the values for the MR Color Sensor while we debug
    public static void debugColor(TelemetryDashboardAndLog telemetry) {
        while(true) {
            telemetry.addData("Alpha", lineColor.alpha());
            telemetry.addData("ARGB", lineColor.argb());
            telemetry.addData("Red", lineColor.red());
            telemetry.addData("Green", lineColor.green());
            telemetry.addData("Blue", lineColor.blue());
            telemetry.update();
            Functions.waitFor(500);
        }
    }

    //Sets all the motor powers to 0 and closes all the hardware devices
    public static void end() {
        moveRobotPower(0);
        frontLeftWheel.close();
        frontRightWheel.close();
        backRightWheel.close();
        backLeftWheel.close();
        sweeper.setPower(0);
        sweeper.close();
        lineColor.enableLed(false);
        lineColor.close();
    }
}
