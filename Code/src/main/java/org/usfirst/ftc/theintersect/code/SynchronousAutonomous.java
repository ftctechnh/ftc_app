package org.usfirst.ftc.theintersect.code;

import android.provider.Settings;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.robocol.Telemetry;
import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.TelemetryDashboardAndLog;

/**
 * An Autonomous for both teams using the LinearOpMode
 */

@org.swerverobotics.library.interfaces.Autonomous(name = "SynchronousAutonomous")
public class SynchronousAutonomous extends SynchronousOpMode {
    static String team = "8865";
    static int delay = 0;
    static long endTime;

    static DcMotor rightWheel;
    static DcMotor leftWheel;

    static DcMotor sweeper;

    static Servo tubeTilt;
    static Servo tubeExtender;

    static Servo mountainClimber;
    static Servo mountainClimberRelease;

    static Servo bumper;

    static ColorSensor lineColor;
    static ModernRoboticsI2cGyro gyro;
    static UltrasonicSensor ultrasonicLeft;
    static UltrasonicSensor ultrasonicRight;

    Thread mountainClimberMove;

    @Override
    public void main() throws InterruptedException {
        rightWheel = hardwareMap.dcMotor.get("rightWheel");
        leftWheel  = hardwareMap.dcMotor.get("leftWheel");

        sweeper = hardwareMap.dcMotor.get("sweeper");
        lineColor = hardwareMap.colorSensor.get("lineColor");
        gyro = (ModernRoboticsI2cGyro) unthunkedHardwareMap.gyroSensor.get("gyro");

        ultrasonicLeft =  hardwareMap.ultrasonicSensor.get("ultrasonicLeft");
        ultrasonicRight = hardwareMap.ultrasonicSensor.get("ultrasonicRight");

        tubeTilt = hardwareMap.servo.get("tubeTilt");
        tubeExtender = hardwareMap.servo.get("tubeExtender");
        mountainClimber = hardwareMap.servo.get("mountainClimber");
        mountainClimberRelease = hardwareMap.servo.get("mountainClimberRelease");
        bumper = hardwareMap.servo.get("bumper");

        autonomousInit(telemetry);
//        lineColor.enableLed(true); Done in autonomousInit already

        //Delay And Team Selection
/*        while (true) {
            if (updateGamepads()) {
                if (gamepad1.x) {
                    team = "Blue";
                    //dim.setLED(0, true);
                } else if (gamepad1.b) {
                    team = "Red";
                    //dim.setLED(1, true);
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
                    telemetry.updateNow();
                    break;
                }
                telemetry.addData("Team", team);
                telemetry.addData("Delay", delay);
                telemetry.updateNow();
            }
        }

*/
/*
        mountainClimberMove = new Thread() {
            public void run() {
                while (endTime > System.currentTimeMillis()) {
                    if ((endTime - 2000) < System.currentTimeMillis()) {
                        mountainClimber.setPosition(0.5);
                        break;
                    }
                }
            }
        };

*/

        waitForStart();
//        mountainClimberMove.start();
//        Functions.waitFor(delay * 1000);
        endTime = System.currentTimeMillis() + 30000;
        telemetry.clearDashboard();
        telemetry.addData("gyroInit: ", endTime);
        telemetry.updateNow();
        while (opModeIsActive() && !isStopRequested() && endTime > System.currentTimeMillis()) {
            //Starting based off of the delay
            //sleep(delay * 1000);
            //Autonomous Routine
            telemetry.addData("Status", "Working...");
            double left;
            double right;
            left = ultrasonicLeft.getUltrasonicLevel();
            right= ultrasonicRight.getUltrasonicLevel();
           // telemetry.addData("Right",right);
            //telemetry.addData("Left ",left);
            //telemetry.updateNow();
            //telemetry.updateNow();
            ultrasonicSpin(0.1, telemetry);
            Functions.waitFor(10000);
            /*moveRobotBackwardTime(1, 0.2);
            Functions.waitFor(3000);
            stopAtWhite(0.2, 10000000000L, telemetry);
            Functions.waitFor(3000);*/
            //turnRobotLeftBackwardDegrees(270, 0.5, 10000000000L);
//            moveRobotBackwardTime(2.4, 0.3);
//            Functions.waitFor(2000);
//            spinClockwiseGyroCorrection(90, 0.5, 5000);
//            Functions.waitFor(3000);
//            spinCounterClockwiseGyroCorrection(270, 0.5, 5000);
           // telemetry.addData("Status", "Done");
            //telemetry.updateNow();
            //spinRobotLeftDegrees(90,0.3,60000,telemetry);
            //spinRobotLeftDegrees(90, 0.3, 60000, telemetry);
            //spinRobotLeftDegrees(90, 0.3, 60000, telemetry);
            end();
        }
        end();
    }


    public static void directionInit() {
        rightWheel.setDirection(DcMotor.Direction.FORWARD);
        leftWheel.setDirection(DcMotor.Direction.REVERSE);
        mountainClimber.setDirection(Servo.Direction.FORWARD);
        mountainClimberRelease.setDirection(Servo.Direction.REVERSE);
        bumper.setDirection(Servo.Direction.FORWARD);
        tubeExtender.setDirection(Servo.Direction.REVERSE);
        tubeTilt.setDirection(Servo.Direction.REVERSE);

        rightWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

    }


    public static void gyroInit(TelemetryDashboardAndLog telemetry) {

        gyro.calibrate();
        gyro.setHeadingMode(
                ModernRoboticsI2cGyro.HeadingMode.HEADING_CARTESIAN);
        while (gyro.isCalibrating()) {
            telemetry.addData("gyroInit: ", "Calibrating Gyro");
            telemetry.updateNow();
        }
        telemetry.addData("gyroInit: ", "Calibration Complete");
        telemetry.updateNow();
        initGyroHeading(telemetry);
    }

    // setup gyro so it will have stable reading of heading
    public static void initGyroHeading(TelemetryDashboardAndLog telemetry) {
        int repeatCount = 0;
        int currentPosition;
        int previousPosition;
        int totalCount = 0;

        previousPosition = gyro.getIntegratedZValue();
        Functions.waitFor(500);
        while (repeatCount < 5) {
            currentPosition = gyro.getIntegratedZValue();
            if (currentPosition == previousPosition) {
                repeatCount++;
                telemetry.addData("initGyroHeading CurrentHeading", currentPosition);
                telemetry.updateNow();
            } else {
                previousPosition = currentPosition;
                repeatCount = 0;
            }
            Functions.waitFor(500);
            totalCount++;
        }
        telemetry.addData("initGyroHeading: Done ", totalCount);
        telemetry.updateNow();
    }

    public static void autonomousInit(TelemetryDashboardAndLog telemetry) {
        lineColor.enableLed(true);
//        servoInit();
        directionInit();
        resetEncoders();
        rightWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        gyroInit(telemetry);
    }

    public static void servoInit() {
        mountainClimber.setPosition(Functions.mountainClimberInitPosition);
        mountainClimberRelease.setPosition(
                Functions.mountainClimberReleaseClose);
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

    public static void moveRobotBackwardTime(double seconds, double power) {
        moveRobotBackward(power, power);
        Functions.waitFor((int) seconds * 1000);
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

    // Move Robot backwoard based on encoder setting
    // Use run to position mode
    public static void moveRobotBackwardRotations(double rotations, double power,
                                                  long timeoutMill) {
        resetEncoders(); // reset encoder and turn on run_to_position mode
        long endTime = System.currentTimeMillis() + timeoutMill;
        double encoderVal = rotations * Functions.neveRestDegreeRatio;

        // setup run to position mode
        rightWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        leftWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        // set target position
        rightWheel.setTargetPosition(-(int) encoderVal);
        leftWheel.setTargetPosition(-(int) encoderVal);
        // set target speed
        rightWheel.setPower(power);
        leftWheel.setPower(power);

        while ((System.currentTimeMillis() < endTime) &&
                (rightWheel.getTargetPosition() > encoderVal) &&
                (leftWheel.getTargetPosition() > encoderVal)) {
            Functions.waitFor(50);
        }
        stopRobot();
    }

    public static void moveRobotForwardRotations(double rotations, double power,
                                                 long timeoutMill) {

        resetEncoders(); // reset encoder and turn on run_to_position mode
        long endTime = System.currentTimeMillis() + timeoutMill;
        double encoderVal = rotations * Functions.neveRestDegreeRatio;

        // setup run to position mode
        rightWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        leftWheel.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        // set target position
        rightWheel.setTargetPosition((int) encoderVal);
        leftWheel.setTargetPosition((int) encoderVal);
        // set target speed
        rightWheel.setPower(power);
        leftWheel.setPower(power);

        while ((System.currentTimeMillis() < endTime) &&
                (rightWheel.getTargetPosition() < encoderVal) &&
                (leftWheel.getTargetPosition() < encoderVal)) {
            Functions.waitFor(50);
        }
        stopRobot();
    }

    public static void moveRobotBackwardRotationsGyro(double rotations, double power, long timeoutMill, TelemetryDashboardAndLog telemetry) {
        long endTime;
        double adjustedPower;
        int targetHeading = gyro.getIntegratedZValue();
        double leftpower = power;
        double rightpower = power;

        telemetry.addData("moveRobotBackwardRotationsGyro", targetHeading);
        telemetry.updateNow();

        // set target encoder value to both wheels
        double encoderVal = rotations * Functions.neveRestDegreeRatio;
        resetEncoders(); // reset Encoder to 0
        rightWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        endTime = System.currentTimeMillis() + timeoutMill;

        while ((rightWheel.getCurrentPosition() > encoderVal) ||
                (leftWheel.getCurrentPosition() > encoderVal)) {

            if (System.currentTimeMillis() > endTime) { // check timeout
                telemetry.addData("Timeout", System.currentTimeMillis());
                telemetry.updateNow();
                break;
            }

            int currentHeading = gyro.getIntegratedZValue(); // current heading
            int delta = currentHeading - targetHeading; // heading drift

            adjustedPower = power + (Math.abs(delta) / Functions.straightGyroCorrectionFactor);
            if (adjustedPower > Functions.adjustedPowerMax) {
                adjustedPower = Functions.adjustedPowerMax;
            } else if (adjustedPower < Functions.adjustedPowerMin) {
                adjustedPower = Functions.adjustedPowerMin;
            }
            telemetry.addData("currentHeading", currentHeading);
            telemetry.updateNow();

            if (delta < 0) { //drifting right
                leftpower = adjustedPower;
                rightpower = power;

            } else if (delta > 0) { //drifting left
                leftpower = power;
                rightpower = adjustedPower;
            }
            moveRobotBackward(leftpower, rightpower); // set motor power
            telemetry.addData("Current position L:", leftWheel.getCurrentPosition());
            telemetry.addData("Current position R:", rightWheel.getCurrentPosition());
            telemetry.updateNow();
        }
        stopRobot();
        telemetry.addData("Complete", targetHeading);
        telemetry.updateNow();
    }


    public static void moveRobotForwardRotationsGyro(double rotations, double power,
                                                     long timeoutMill) {
        long endTime = System.currentTimeMillis() + timeoutMill;
        resetEncoders();
        double encoderVal = rotations * Functions.neveRestDegreeRatio;
        rightWheel.setTargetPosition((int) -encoderVal);
        leftWheel.setTargetPosition((int) -encoderVal);
        double adjustedPower = power + (gyro.getIntegratedZValue() / Functions.straightGyroCorrectionFactor);
        if (adjustedPower > 1.0) {
            adjustedPower = 1.0;
        } else if (adjustedPower < 0) {
            adjustedPower = 0.0;
        }
        while (endTime > System.currentTimeMillis()) {
            if (gyro.getIntegratedZValue() < 0) { //drifting right
                moveRobotForward(adjustedPower, power);
            } else if (gyro.getIntegratedZValue() > 0) { //drifting left
                moveRobotForward(power, adjustedPower);
            } else {
                moveRobotForward(power, power);
            }
            if (!rightWheel.isBusy() && !leftWheel.isBusy()) {
                stopRobot();
            }
        }
    }

    public static void spinRobotLeftRotations(double degrees, double power, long timeoutMill) {
        long endTime = System.currentTimeMillis() + timeoutMill;
        resetEncoders();
        double encoderVal = degrees * Functions.neveRestDegreeRatio;
        rightWheel.setTargetPosition((int) encoderVal);
        leftWheel.setTargetPosition(-(int) encoderVal);
        while (endTime > System.currentTimeMillis()) {
            spinRobotLeft(power);
            if (!rightWheel.isBusy() && !leftWheel.isBusy()) {
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
        while (endTime > System.currentTimeMillis()) {
            spinRobotRight(power);
            if (!rightWheel.isBusy() && !leftWheel.isBusy()) {
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
        while (endTime > System.currentTimeMillis()) {
            turnRobotRightForward(power);
            if (!rightWheel.isBusy() && !leftWheel.isBusy()) {
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
        while (endTime > System.currentTimeMillis()) {
            turnRobotLeftForward(power);
            if (!rightWheel.isBusy() && !leftWheel.isBusy()) {
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
        while (endTime > System.currentTimeMillis()) {
            turnRobotRightBackward(power);
            if (!rightWheel.isBusy() && !leftWheel.isBusy()) {
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
        while (endTime > System.currentTimeMillis()) {
            turnRobotRightBackward(power);
            if (!rightWheel.isBusy() && !leftWheel.isBusy()) {
                stopRobot();
                break;
            }
        }
    }

    public static void spinRobotLeftDegrees(int degrees, double power, long timeoutMill, TelemetryDashboardAndLog telemetry) {
        long endTime;
        int startPosition;
        int currentPosition;
        int endPosition;

        initGyroHeading(telemetry);
        startPosition = gyro.getIntegratedZValue();
        endPosition = startPosition + degrees;
        endTime = System.currentTimeMillis() + timeoutMill;
        telemetry.addData("startPosition ", startPosition);
        telemetry.updateNow();

        spinRobotLeft(power);
        while (System.currentTimeMillis() < endTime && (currentPosition = gyro.getIntegratedZValue()) < endPosition) {
            Functions.waitFor(1);
            telemetry.addData("CurrentHeading", currentPosition);
            telemetry.addData("Endposition ", endPosition);
            telemetry.updateNow();
        }
        spinRobotLeft(0);
        telemetry.addData("Endposition ", endPosition);
        telemetry.addData("Done?", "Yes");
        telemetry.updateNow();
        stopRobot();
    }


    public static void spinRobotRightDegrees(int degrees, double power, long timeoutMill) {
        long endTime = System.currentTimeMillis() + timeoutMill;
        int endPosition = gyro.getIntegratedZValue() + degrees;
        spinRobotRight(power);
        while (endTime > System.currentTimeMillis()) {
            if (endPosition < gyro.getIntegratedZValue()) {
                spinRobotLeft(0.1);
            }
        }
        stopRobot();
    }

    public static void turnRobotRightForwardDegrees(int degrees, double
            power, long timeoutMill) {
        long endTime = System.currentTimeMillis() + timeoutMill;
        int endPosition = gyro.getIntegratedZValue() - degrees;
        boolean control = false;
        turnRobotLeftBackward(power);
        while (endTime > System.currentTimeMillis()) {
            if (endPosition > gyro.getIntegratedZValue()) {
                turnRobotRightBackward(0.1);
                control = true;
            } else if (control && endPosition < gyro.getIntegratedZValue()) {
                turnRobotLeftBackward(0.1);
            } else if (endPosition == gyro.getIntegratedZValue()) {
                break;
            }
        }
        stopRobot();
    }

    public static void turnRobotLeftForwardDegrees(int degrees, double
            power, long timeoutMill) {
        long endTime = System.currentTimeMillis() + timeoutMill;
        int endPosition = gyro.getIntegratedZValue() - degrees;
        boolean control = false;
        resetEncoders(); // reset encoder and turn on run_to_position mode
        leftWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        turnRobotLeftForward(power);
        while (endTime > System.currentTimeMillis()) {
            if (endPosition < gyro.getIntegratedZValue()) {
                turnRobotRightForward(0.1);
                control = true;
            } else if (control && endPosition > gyro.getIntegratedZValue()) {
                turnRobotLeftForward(0.1);
            } else if (endPosition == gyro.getIntegratedZValue()) {
                break;
            }
        }
        stopRobot();
    }

    public static void turnRobotRightBackwardDegrees(int degrees, double
            power, long timeoutMill) {
        long endTime = System.currentTimeMillis() + timeoutMill;
        int endPosition = gyro.getIntegratedZValue() + degrees;
        boolean control = false;
        turnRobotRightBackward(power);
        while (endTime > System.currentTimeMillis()) {
            if (endPosition > gyro.getIntegratedZValue()) {
                turnRobotLeftBackward(0.1);
                control = true;
            } else if (control && endPosition < gyro.getIntegratedZValue()) {
                turnRobotRightBackward(0.1);
            } else if (endPosition == gyro.getIntegratedZValue()) {
                break;
            }
        }
        stopRobot();
    }

    public static void turnRobotLeftBackwardDegrees(int degrees, double
            power, long timeoutMill) {
        long endTime = System.currentTimeMillis() + timeoutMill;
        int endPosition = gyro.getIntegratedZValue() - degrees;
        boolean control = false;
        resetEncoders(); // reset encoder and turn on run_to_position mode
        leftWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        turnRobotLeftBackward(power);
        while (endTime > System.currentTimeMillis()) {
            if (endPosition > gyro.getIntegratedZValue()) {
                turnRobotRightBackward(0.1);
                control = true;
            } else if (control && endPosition < gyro.getIntegratedZValue()) {
                turnRobotLeftBackward(0.1);
            } else if (endPosition == gyro.getIntegratedZValue()) {
                break;
            }
        }
        stopRobot();
    }

    public static void spinClockwiseGyroCorrection(int degrees, double power, long
            timeoutMill) {
        long endTime = System.currentTimeMillis() + timeoutMill;
        int endPosition = gyro.getIntegratedZValue() - degrees;
        boolean control = false;
        resetEncoders(); // reset encoder and turn on run_to_position mode
        leftWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        spinRobotRight(power);
        while (endTime > System.currentTimeMillis()) {
            if (endPosition > gyro.getIntegratedZValue()) {
                spinRobotLeft(0.1);
                control = true;
            } else if (control && endPosition < gyro.getIntegratedZValue()) {
                spinRobotRight(0.1);
            } else if (endPosition == gyro.getIntegratedZValue()) {
                break;
            }
        }
        stopRobot();
    }

    public static void spinCounterClockwiseGyroCorrection(int degrees, double power, long
            timeoutMill) {
        long endTime = System.currentTimeMillis() + timeoutMill;
        int endPosition = gyro.getIntegratedZValue() + degrees;
        boolean control = false;
        resetEncoders(); // reset encoder and turn on run_to_position mode
        leftWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        spinRobotLeft(power);
        while (endTime > System.currentTimeMillis()) {
            if (endPosition < gyro.getIntegratedZValue()) {
                spinRobotRight(0.1);
                control = true;
            } else if (control && endPosition > gyro.getIntegratedZValue()) {
                spinRobotLeft(0.1);
            } else if (endPosition == gyro.getIntegratedZValue()) {
                break;
            }
        }
        stopRobot();
    }

    public static void spinClockwiseGyroNoCorrection(int degrees, double power, long
            timeoutMill) {
        long endTime = System.currentTimeMillis() + timeoutMill;
        int endPosition = gyro.getIntegratedZValue() - degrees;
        boolean control = false;
        resetEncoders(); // reset encoder and turn on run_to_position mode
        leftWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        spinRobotRight(power);
        while (endTime > System.currentTimeMillis()) {
            if (endPosition <= gyro.getIntegratedZValue()) {
                break;
            }
        }
        stopRobot();
    }

    public static void spinCounterClockwiseGyroNoCorrection(int degrees, double power, long
            timeoutMill) {
        long endTime = System.currentTimeMillis() + timeoutMill;
        int endPosition = gyro.getIntegratedZValue() + degrees;
        boolean control = false;
        resetEncoders(); // reset encoder and turn on run_to_position mode
        leftWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightWheel.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        spinRobotLeft(power);
        while (endTime > System.currentTimeMillis()) {
            if (endPosition >= gyro.getIntegratedZValue()) {
                break;
            }
        }
        stopRobot();
    }

    public static void spinClockwiseTime(double seconds, double power) {
        spinRobotRight(power);
        Functions.waitFor((int) (seconds * 1000));
        stopRobot();
    }

    public static void spinCounterClockwiseTime(double seconds, double power) {
        spinRobotLeft(power);
        Functions.waitFor((int) (seconds * 1000));
        stopRobot();
    }

    public static void spinClockwiseRotations(double degrees, double power, long
            timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        int endPos = (int) (degrees * Functions.neveRestDegreeRatio);
        rightWheel.setTargetPosition(-endPos);
        leftWheel.setTargetPosition(endPos);
        moveRobotForward(power, power);
        while (endTime > System.currentTimeMillis()) {
            if (!rightWheel.isBusy() && !leftWheel.isBusy()) {
                break;
            }
        }
    }

    public static void spinCounterClockwiseRotations(double degrees, double power, long
            timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        int endPos = (int) (degrees * Functions.neveRestDegreeRatio);
        rightWheel.setTargetPosition(endPos);
        leftWheel.setTargetPosition(-endPos);
        moveRobotForward(power, power);
        while (endTime > System.currentTimeMillis()) {
            if (!rightWheel.isBusy() && !leftWheel.isBusy()) {
                break;
            }
        }
    }

    //Moves the robot until the robot is above white by using the MR Color Sensor
    public static void stopAtWhite(double power, long timeout, TelemetryDashboardAndLog telemetry) {
        moveRobotBackward(power, power);
        long endTime = System.currentTimeMillis() + (timeout * 1000);
        while (System.currentTimeMillis() < endTime) {
            if (detectWhite(telemetry)) {
                break;
            }
        }
        stopRobot();
    }

    //Returns whether the robot is over white
    public static boolean detectWhite(TelemetryDashboardAndLog telemetry) {
        int red = lineColor.red();
        int green = lineColor.green();
        int blue = lineColor.blue();
        //double average = (red + green + blue)/3.0;
        //if(average > 15 /*+ Functions.colorError && red >= average-Functions.colorError && red <= average+Functions.colorError && green >= average-Functions.colorError && green <= average+Functions.colorError && blue >= average-Functions.colorError && blue <= average+Functions.colorError*/) {
        if (red > 10 && blue > 10 && green > 10) {
            telemetry.addData("Red", red);
            telemetry.addData("Blue", blue);
            telemetry.addData("Green", green);
            //telemetry.addData("Average" , average);
            telemetry.addData("Debug", "Success");
            telemetry.updateNow();
            return true;
        }
        telemetry.addData("Red", red);
        telemetry.addData("Blue", blue);
        telemetry.addData("Green", green);
        telemetry.updateNow();
        return false;
    }

    public enum startingPosition {
        LEFT,
        RIGHT
    }


    /*public static void followLine(Position previousPosition, boolean right ,TelemetryDashboardAndLog telemetry) {

        boolean white;
        Position currentPosition = Position.CENTER;

        while (ultrasonicLeft.getUltrasonicLevel() > 5) {
            white = detectWhite(telemetry);
            switch (currentPosition) {
                case LEFT: //on the  left
                    turnRobotRightForward(0.1);
                    if (white) {
                        previousPosition = Position.LEFT;
                        currentPosition = Position.CENTER; // move to center
                    }
                    break;

                case CENTER:
                    if (!white) {
                        if (previousPosition == Position.LEFT) {
                            turnRobotRightForward(0.1);
                            currentPosition = Position.RIGHT;
                        } else {
                            turnRobotLeftForward(0.1);
                            currentPosition = Position.LEFT;
                        }
                        break;
                    }

                case RIGHT:
                    turnRobotLeftForward(0.1);
                    if (white) {
                        previousPosition = Position.RIGHT;
                        currentPosition = Position.CENTER; // move to center
                    }
                    break;
            }
        }
    }*/

    public static void followLine(double power , startingPosition startingposition, TelemetryDashboardAndLog telemetry) {
        while (ultrasonicLeft.getUltrasonicLevel() > 5) {
            switch (startingposition){
                case RIGHT:
                    if (detectWhite(telemetry)) {
                        turnRobotRightForward(power);
                    } else {
                        turnRobotLeftForward(power);
                    }
                case LEFT :
                    if (detectWhite(telemetry)) {
                        turnRobotLeftForward(power);
                    } else {
                        turnRobotRightForward(power);
                    }
                }
            }
        stopRobot();
        }

    public static void ultrasonicSpin(double power , TelemetryDashboardAndLog telemetry){
        double right;
        double left;
        do {
            left = ultrasonicLeft.getUltrasonicLevel();
            right= ultrasonicRight.getUltrasonicLevel();
            telemetry.addData("Right",right);
            telemetry.addData("Left ",left);
            telemetry.updateNow();

            if( left > right) {
                //spinRobotRight(power);
            }else if ( left < right ){
                //spinRobotLeft(power);
            }
        } while ( Math.abs(left-right) > 1 );
        stopRobot();
        telemetry.addData("ultrasonicSpin", "done");
        telemetry.addData("Right",right);
        telemetry.addData("Left ",left);
        telemetry.updateNow();
    }

    // Reset Encoder of both motors
    //
    public static void resetEncoders() {
        rightWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        leftWheel.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        while(rightWheel.getTargetPosition() != 0 ) {
            Functions.waitFor(50);
        }

        while(leftWheel.getTargetPosition() != 0 ) {
            Functions.waitFor(50);
        }
    }

	public static void end() {
		rightWheel.setPowerFloat();
		leftWheel.setPowerFloat();
		sweeper.setPowerFloat();
		lineColor.enableLed(false);
		lineColor.close();
	}
}
