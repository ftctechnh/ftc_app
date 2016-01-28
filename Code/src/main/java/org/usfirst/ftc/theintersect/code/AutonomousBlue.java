package org.usfirst.ftc.theintersect.code;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.TelemetryDashboardAndLog;

/**
 * An AutonomousBlue.
 */

@org.swerverobotics.library.interfaces.Autonomous(name = "Autonomous Blue")
public class AutonomousBlue extends SynchronousOpMode {
    //Declare hardware
    static DcMotor frontRightWheel;
    static DcMotor frontLeftWheel;
    static DcMotor backRightWheel;
    static DcMotor backLeftWheel;
    static DcMotor sweeper;

    static ColorSensor lineColor;
    String color = "b";
    static GyroSensor gyro;
    static UltrasonicSensor ultrasonic;

    static Servo mountainClimber;
    static Servo mountainClimberRelease;
    static Servo bumper;
    static Servo tubeExtender;
    static Servo containerTilt;
    @Override
    public void main() throws InterruptedException {
        //Initialize hardware
        rightWheel = hardwareMap.dcMotor.get("rightWheel");
        leftWheel = hardwareMap.dcMotor.get("leftWheel");

        sweeper = hardwareMap.dcMotor.get("sweeper");

        lineColor = hardwareMap.colorSensor.get("lineColor");
        gyro = hardwareMap.gyroSensor.get("gyro");
        ultrasonic = hardwareMap.ultrasonicSensor.get("ultrasonic");
        containerTilt = hardwareMap.servo.get("tubeTilt");
        tubeExtender = hardwareMap.servo.get("tubeExtender");
        mountainClimber = hardwareMap.servo.get("mountainClimber");
        mountainClimberRelease = hardwareMap.servo.get("mountainClimberRelease");

        bumper = hardwareMap.servo.get("bumper");

        //Set motor and servo directions
        rightWheel.setDirection(DcMotor.Direction.REVERSE);
        leftWheel.setDirection(DcMotor.Direction.FORWARD);

        mountainClimber.setDirection(Servo.Direction.FORWARD);
        mountainClimber.setPosition(0.05);
        mountainClimberRelease.setDirection(Servo.Direction.REVERSE);
        mountainClimberRelease.setPosition(0);
        bumper.setDirection(Servo.Direction.FORWARD);
        bumper.setPosition(0);
        tubeExtender.setDirection(Servo.Direction.REVERSE);
        tubeExtender.setPosition(0.5);
        containerTilt.setDirection(Servo.Direction.REVERSE);
        containerTilt.setPosition(0.5);
        telemetry.clearDashboard();
        telemetry.update();
        //Enable the LED for the line following color sensor
        lineColor.enableLed(true);

        //Gyro Calibration
        /*gyro.calibrate();
        while(gyro.isCalibrating()) {
            telemetry.addData("Gyro Calibration", "Calibrating");
            telemetry.update();
        }
        //telemetry.addData("Gyro Calibration", "Calibration Done");
        telemetry.update();*/

        //Reset position of all the servos
        mountainClimber.setPosition(0);
        mountainClimberRelease.setPosition(0);

        //AutonomousBlue Start
        waitForStart();
        if (opModeIsActive()) {
            if (color.equals("b")) {
                moveEncoderBackward(7000, 0.5, telemetry);
                Functions.waitFor(1000);
                stopAtWhite(0.25, 1000, telemetry);
                Functions.waitFor(1000);
                moveEncoderBackward(800, 0.5, telemetry);
                turnRightBackwards(1200, 0.5, telemetry);
            }
        } else if (color.equals("r")) {
            moveEncoderBackward(7000, 0.5, telemetry);
            Functions.waitFor(1000);
            stopAtWhite(0.25, 1000, telemetry);
            Functions.waitFor(1000);
            moveEncoderForward(500, 0.5, telemetry);
            turnLeftBackwards(1200, 0.5, telemetry);
        } else {
            simpleAutonomous(telemetry);
            return;
        }

            Functions.waitFor(1000);
            moveBackwardsTime(0.2, 3000);
            moveRobotPower(0, 0);
            while (ultrasonic.getUltrasonicLevel() < 15) {
                moveRobotPower(0.2, 0.2);
            }
            moveRobotPower(0, 0);
            Functions.waitFor(1000);
            dumpClimbersUltra(telemetry);
            end();

        telemetry.addData("AutonomousBlue", "Done");
        //AutonomousBlue End
    }

    //Functions

    public static void simpleAutonomous(TelemetryDashboardAndLog telemetry){
        moveEncoderBackward(7100, 0.5, telemetry);
        dumpClimbers(0.8);
        end();
    }
    public static void moveBackwardsTime(double power, int time){
        moveRobotPower(-power,-power);
        Functions.waitFor(time);
        moveRobotPower(0,0);
    }

    public static void moveForwardsTime(double power, int time){
        moveRobotPower(power,power);
        Functions.waitFor(time);
        moveRobotPower(0,0);
    }
    public static void turnRightBackwards(double rotation, double power, TelemetryDashboardAndLog telemetry){
        moveRobotEncoder(-rotation, rotation, -power, power, telemetry);
        moveRobotPower(0,0);
    }
    public static void turnLeftBackwards(double rotation, double power, TelemetryDashboardAndLog telemetry){
        moveRobotEncoder(rotation, -rotation, power, -power , telemetry);
        moveRobotPower(0,0);
    }


    public static void moveEncoderBackward(double rotation, double power, TelemetryDashboardAndLog telemetry) {
        moveRobotEncoder(-rotation, -rotation, -power, -power, telemetry);


    }

    public static void moveEncoderForward(double rotation, double power, TelemetryDashboardAndLog telemetry) {
        moveRobotEncoder(rotation, rotation, power, power, telemetry);
    }

    //Turns robot using motor encoders
   /*public static void backwardRightTurn(double rotations, double power) {
        resetEncoders();
        rightWheel.setTargetPosition((int) (rotations * Functions.neveRestPPR));
        leftWheel.setPower(0);
        leftWheel.setPower(0);
        leftWheel.setPower(power);
        rightWheel.setPower(power);
        while(rightWheel.isBusy() || leftWheel.isBusy()) {
            Functions.waitFor(100);
        }
        moveRobotPower(0);
    }*/

    //Aligns the robot so that it is perpendicular to the wal
    /*public static void alignRobot(TelemetryDashboardAndLog telemetry, long timeout, String color) {
        long endTime = System.currentTimeMillis() + timeout;
        while(System.currentTimeMillis() < endTime) {
            int counter = 0;
            int done = 10000;
            while (counter <= done) {
                counter = 0;
                while (detectWhite(telemetry)) {
                    moveRobotPower(-0.25);
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
                    rightWheel.setPower(-0.25);
                    rightWheel.setPower(-0.25);
                    leftWheel.setPower(0.25);
                    leftWheel.setPower(0.25);
                } else {
                    rightWheel.setPower(0.25);
                    leftWheel.setPower(0.25);
                    rightWheel.setPower(-0.25);
                    leftWheel.setPower(-0.25);
                }

                while (!detectWhite(telemetry)) {
                    Functions.waitFor(100);
                }
                moveRobotPower(0);
            }
        }
    }*/

    //Detects white
    public static boolean detectWhite(TelemetryDashboardAndLog telemetry) {
        int red = lineColor.red();
        int green = lineColor.green();
        int blue = lineColor.blue();
        double average = (red + green + blue)/3.0;
        //if(average > 15 /*+ Functions.colorError && red >= average-Functions.colorError && red <= average+Functions.colorError && green >= average-Functions.colorError && green <= average+Functions.colorError && blue >= average-Functions.colorError && blue <= average+Functions.colorError*/) {
        if (red >10 && blue > 10 && green > 10){
            /*telemetry.addData("Red", red);
            telemetry.addData("Blue" , blue);
            telemetry.addData("Green", green);
            telemetry.addData("Average" , average);
            telemetry.addData("Debug", "Success");
            telemetry.update();*/
            return true;
        }
        /*telemetry.addData("Red", red);
        telemetry.addData("Blue", blue);
        telemetry.addData("Green", green);
        telemetry.update();*/
        return false;
    }

    //Dumps climbers based on position from the wall
    public static void dumpClimbers(double position) {
        /*double[] positions = {100, 102.8, 105.6, 108.4, 111.2, 114, 116.8, 119.6, 121.4, 123.2, 128, 130.8, 133.6, 136.4, 139.2, 142, 141.5, 147.6, 150.4, 153.2, 156, 0, 0, 0, 0, 170};
        double ultraVal  = ultrasonic.getUltrasonicLevel();
        telemetry.update();
        if(ultraVal < 5) {
            ultraVal = 5;
        } else if (ultraVal >= 25) {
            return;
        }
        telemetry.clearDashboard();
        telemetry.addData("Ultrasonic Level", ultraVal);
        telemetry.addData("Dumper Position", positions[(int) ultraVal - 5] / 180);
        mountainClimber.setPosition(positions[(int) ultraVal - 5] / 180);
        Functions.waitFor(5000);
        mountainClimberRelease.setPosition(2.0);
        Functions.waitFor(5000);
        mountainClimberRelease.setPosition(0.0);*/
        mountainClimber.setPosition(position);
        Functions.waitFor(3000);
        mountainClimberRelease.setPosition(1.0);
        Functions.waitFor(2000);
        mountainClimberRelease.setPosition(0);
    }
    public static void dumpClimbersUltra(TelemetryDashboardAndLog telemetry) {
        double[] positions = {98, 100, 102.8, 105.6, 108.4, 111.2, 114, 116.8, 129.6, 129.4, 120.2, 125, 137.8, 133.6, 135.4, 137.2, 142, 141.5, 147.6, 150.4, 153.2, 156, 160, 165, 167   , 170};
        double ultraVal  = ultrasonic.getUltrasonicLevel();
        telemetry.addData("Ultrasonic Level", ultraVal);
        telemetry.update();
        if(ultraVal <= 5) {
            return;
        } else if (ultraVal >= 25) {
            return;
        }
        telemetry.clearDashboard();
        telemetry.addData("Ultrasonic Level", ultraVal);
        telemetry.addData("Dumper Position", positions[(int) ultraVal - 5] / 180.0);
        telemetry.update();
        mountainClimber.setPosition((positions[(int) ultraVal -5]) / 180.0);
        Functions.waitFor(3000);
        mountainClimberRelease.setPosition(2.0);
        Functions.waitFor(3000);
        mountainClimberRelease.setPosition(0.0);
        Functions.waitFor(100);
        mountainClimberRelease.setPosition(2.0);
        Functions.waitFor(100);
        mountainClimberRelease.setPosition(0.0);
    }
    //Resets the encoders and forces the motors to run to the target position
    public static void resetEncoders() {
        backRightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backRightWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        backLeftWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backLeftWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    //Moves robot at a certain power
    public static void moveRobotPower(double leftPower, double rightPower) {
        frontRightWheel.setPower(rightPower);
        frontLeftWheel.setPower(leftPower);
        backRightWheel.setPower(rightPower);
        backLeftWheel.setPower(leftPower);
    }

    //Moves the robot a certain amount of rotations
    public static void moveRobotRotations(double rightRotations, double leftRotations, double leftPower, double rightPower, TelemetryDashboardAndLog telemetry) {
        resetEncoders();
        backRightWheel.setTargetPosition((int) (rightRotations * Functions.neveRestPPR));
        backLeftWheel.setTargetPosition((int) (leftRotations * Functions.neveRestPPR));
        moveRobotPower(leftPower, rightPower);

        /*while(!(Math.abs(rightWheel.getCurrentPosition()) >= Math.abs(rightWheel.getTargetPosition())
                - Functions.encoderError && Math.abs(rightWheel.getCurrentPosition())
                <= Math.abs(rightWheel.getTargetPosition()) + Functions.encoderError)
                && !(Math.abs(leftWheel.getCurrentPosition()) >= Math.abs(leftWheel.getTargetPosition())
                - Functions.encoderError && leftWheel.getCurrentPosition()
                <= Math.abs(leftWheel.getTargetPosition()) + Functions.encoderError)) {
            telemetry.addData("Right: ", rightWheel.getCurrentPosition());
            telemetry.addData("Left: ", leftWheel.getCurrentPosition());
            telemetry.update();
        }
        moveRobotPower(0, 0);*/
        while(backRightWheel.isBusy() && backLeftWheel.isBusy()) {
            Functions.waitFor(10);
        }
        moveRobotPower(0, 0);
    }

    public static void moveRobotEncoder(double rightRotations, double leftRotations, double rightPower, double leftPower, TelemetryDashboardAndLog telemetry) {
        resetEncoders();
        backRightWheel.setTargetPosition((int) rightRotations);
        backLeftWheel.setTargetPosition((int) leftRotations);
        moveRobotPower(leftPower,rightPower);

        /*while(!(Math.abs(rightWheel.getCurrentPosition()) >= Math.abs(rightWheel.getTargetPosition())
                - Functions.encoderError && Math.abs(rightWheel.getCurrentPosition())
                <= Math.abs(rightWheel.getTargetPosition()) + Functions.encoderError)
                && !(Math.abs(leftWheel.getCurrentPosition()) >= Math.abs(leftWheel.getTargetPosition())
                - Functions.encoderError && leftWheel.getCurrentPosition()
                <= Math.abs(leftWheel.getTargetPosition()) + Functions.encoderError)) {
            telemetry.addData("Right: ", rightWheel.getCurrentPosition());
            telemetry.addData("Left: ", leftWheel.getCurrentPosition());
            telemetry.update();
        }
        moveRobotPower(0, 0);*/
        if (leftRotations == 0){
            while(backRightWheel.isBusy() /*|| leftWheel.isBusy()*/) {
                Functions.waitFor(10);
            }
        } else if(rightRotations == 0){
            while(backLeftWheel.isBusy() /*|| leftWheel.isBusy()*/) {
                Functions.waitFor(10);
            }
        } else {
            while(backRightWheel.isBusy() || backLeftWheel.isBusy()) {
                Functions.waitFor(10);
            }
        }
        disableEncoders();
        moveRobotPower(0, 0);
        telemetry.update();

        //rightWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        //leftWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        //resetEncoders();

    }

    //Moves the robot a certain amount of degrees
    /*public static void moveRobotDegrees(double degrees, double power, TelemetryDashboardAndLog telemetry) {
        resetEncoders();
        rightWheel.setTargetPosition((int) (degrees*Functions.neveRestDegreeRatio));
        leftWheel.setTargetPosition((int) (degrees*Functions.neveRestDegreeRatio));
        moveRobotPower(power);

        while (!(rightWheel.getCurrentPosition() >= rightWheel.getTargetPosition() - Functions.encoderError && rightWheel.getCurrentPosition() <= rightWheel.getTargetPosition() + Functions.encoderError) && !(leftWheel.getCurrentPosition() >= leftWheel.getTargetPosition() - Functions.encoderError && leftWheel.getCurrentPosition() <= leftWheel.getTargetPosition() + Functions.encoderError)) {
            telemetry.addData("Right: ", rightWheel.getCurrentPosition());
            telemetry.addData("Left: ", leftWheel.getCurrentPosition());
            telemetry.update();
        }
        moveRobotPower(0);
    }*/

    //Moves the robot a certain amount of inches
    //1 in ~ 88
    public static void moveRobotInches(double inches, double power, TelemetryDashboardAndLog telemetry) {
        moveRobotRotations(inches / Functions.backWheelCircumfrence, inches / Functions.backWheelCircumfrence, power, power, telemetry);
    }

    //Moves the robot for a certain amount of time
    /*public static void moveRobotSeconds(long seconds, double power) {
        long endTime = System.currentTimeMillis() + (seconds*1000);
        moveRobotPower(power);
        while(System.currentTimeMillis() < endTime) {

        }
        moveRobotPower(0);
    }*/

    //Moves the robot until the robot is above white by using the MR Color Sensor
    public static void stopAtWhite(double power, long timeout, TelemetryDashboardAndLog telemetry) {
        moveRobotPower(-power,-power);
        long endTime = System.currentTimeMillis() + (timeout*1000);
        while (System.currentTimeMillis() < endTime) {
            if(detectWhite(telemetry)) {
                break;
            }
        }
        moveRobotPower(0,0);
        Functions.waitFor(5000);
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
        else if (start < target) {
            protectedValue = 360;
        }
        telemetry.clearDashboard();
        telemetry.update();
        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("Protected Value", protectedValue);
        telemetry.addData("timeout", timeout * 1000);
        telemetry.addData("End Time", endtime);
        telemetry.update();

        Functions.waitFor(1000);
        backLeftWheel.setPower(power);
        backRightWheel.setPower(-power);
        frontRightWheel.setPower(-power);
        frontLeftWheel.setPower(power);
        heading = gyro.getHeading();;
        if (heading > protectedValue) {
            heading = heading - 360;
        }
        //while ((heading < target) && (System.currentTimeMillis() < endtime)) {
        while (heading < target) {
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
        telemetry.addData("Start", start);
        telemetry.addData("Heading", gyro.getHeading());
        telemetry.addData("Target", target);
        telemetry.addData("Protect", protectedValue);
        telemetry.addData("timeout" , timeout*1000);
        telemetry.addData("End Time" , endtime);

        telemetry.update();

        backLeftWheel.setPower(0);
        backRightWheel.setPower(0);
        frontRightWheel.setPower(0);
        frontLeftWheel.setPower(0);
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
		backRightWheel.setPower(0);
        frontRightWheel.setPower(0);
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

    public static void disableEncoders(){
        backRightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backRightWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        backLeftWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backLeftWheel.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    }

    //Sets all the motor powers to 0 and closes all the hardware devices
    public static void end() {
        moveRobotPower(0, 0);
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
