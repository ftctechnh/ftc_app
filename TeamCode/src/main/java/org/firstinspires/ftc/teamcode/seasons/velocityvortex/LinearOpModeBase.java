package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceImpl;
import com.qualcomm.robotcore.hardware.I2cDeviceReader;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by ftc6347 on 10/16/16.
 */
public abstract class LinearOpModeBase extends LinearOpMode {

    private static final int WHEEL_DIAMETER_INCHES = 4;

    private static final int COUNTS_PER_MOTOR_REV = 1120;

    private static final double GYRO_ERROR_THRESHOLD = 1.0;

    private static final double P_GYRO_TURN_COEFF = 0.01;
    private static final double P_GYRO_DRIVE_COEFF = 0.05;

    protected static final int COUNTS_PER_INCH = (int)(COUNTS_PER_MOTOR_REV /
            (WHEEL_DIAMETER_INCHES * Math.PI));

    private static final double LIGHT_SENSOR_PERFECT_VALUE = 2.5;

    private static final double LIGHT_THRESHOLD = 1.0;

    private static final int RANGE_SENSOR_THRESHOLD = 1; // 1cm threshold

    private static final int RANGE_SLOW_DOWN_DISTANCE = 5;

    private static final double P_RANGE_DRIVE_COEFF = 1.0 / RANGE_SLOW_DOWN_DISTANCE;

    private DcMotor frontLeftDrive;
    private DcMotor frontRightDrive;
    private DcMotor backLeftDrive;
    private DcMotor backRightDrive;

    private DcMotor launcherMotor;
    private DcMotor intakeMotor;

    private DcMotor spoolMotor1;
    private DcMotor spoolMotor2;

    private Servo beaconsServo1;
    private Servo beaconsServo2;
    private Servo door3;
    private Servo latch4;
    private Servo pusher5;

    private LightSensor frontLightSensor;
    private LightSensor backLightSensor;

    private ModernRoboticsI2cColorSensor colorSensor1;
    private ModernRoboticsI2cColorSensor colorSensor2;

    private ModernRoboticsI2cRangeSensor frontRange;
    private ModernRoboticsI2cRangeSensor leftRange;

    private OpticalDistanceSensor launcherOds;
    private OpticalDistanceSensor diskOds;
    private OpticalDistanceSensor ods3;

    private ModernRoboticsI2cGyro gyroSensor;

    private ElapsedTime robotRuntime;

    protected void initializeHardware() {
        // initialize robotRuntime instance variable
        robotRuntime = new ElapsedTime();

        frontLeftDrive = hardwareMap.dcMotor.get("fl");
        frontRightDrive = hardwareMap.dcMotor.get("fr");
        backLeftDrive = hardwareMap.dcMotor.get("bl");
        backRightDrive = hardwareMap.dcMotor.get("br");

        launcherMotor = hardwareMap.dcMotor.get("launcher");
        intakeMotor = hardwareMap.dcMotor.get("intake");

        spoolMotor1 = hardwareMap.dcMotor.get("s1");
        spoolMotor2 = hardwareMap.dcMotor.get("s2");

        beaconsServo1 = hardwareMap.servo.get("b1");  // Up = 0.3, Down = 1.0
        beaconsServo2 = hardwareMap.servo.get("r2");   // Up = 0.7, Down = 0.0
        door3 = hardwareMap.servo.get("d3");  // Closed = 0.55, Open = 0.25
        latch4 = hardwareMap.servo.get("l4"); // Up = 0.5
        pusher5 = hardwareMap.servo.get("p5"); // Up = 0.0, Down = 0.7

        colorSensor1 = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "clr");
        colorSensor1.setI2cAddress(I2cAddr.create8bit(0x3C));

        colorSensor2 = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "clr2");
        colorSensor2.setI2cAddress(I2cAddr.create8bit(0x3E));

        // disable LEDs for both color sensors
        colorSensor1.enableLed(false);
        colorSensor2.enableLed(false);

        frontRange = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "frs");
        //leftRange = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "lrs");

        launcherOds = hardwareMap.opticalDistanceSensor.get("launcherOds");
        diskOds = hardwareMap.opticalDistanceSensor.get("diskOds");
        ods3 = hardwareMap.opticalDistanceSensor.get("ods3");

        gyroSensor = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gy");

        // reverse only one spool motor
        spoolMotor1.setDirection(DcMotor.Direction.REVERSE);
        spoolMotor2.setDirection(DcMotor.Direction.FORWARD);

        robotRuntime.reset();

        // put the button pusher servos down
        beaconsServo1.setPosition(1);
        beaconsServo2.setPosition(0);

        door3.setPosition(0.53);
        latch4.setPosition(0.5);
        pusher5.setPosition(0);

        // stop all motors
        launcherMotor.setPower(0);
        intakeMotor.setPower(0);
        stopRobot();

//        telemetry.addData(">", "Calibrating Gyro");
//        telemetry.update();
//
//        gyroSensor.calibrate();
//
//        // make sure the gyro is calibrated before continuing
//        while (!isStopRequested() && gyroSensor.isCalibrating()) {
//            idle();
//        }
//
//        telemetry.addData(">", "Gyro calibrated");
//        telemetry.update();

        // reset gyro heading
        gyroSensor.resetZAxisIntegrator();

        telemetry.addData("status","end of init");
        telemetry.update();
    }

    protected void stopOnLine(double speed, boolean driveRight) {
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double error = LIGHT_SENSOR_PERFECT_VALUE - getOds3().getRawLightDetected();

        while(opModeIsActive() && Math.abs(error) > LIGHT_THRESHOLD) {

            error = LIGHT_SENSOR_PERFECT_VALUE - getOds3().getRawLightDetected();

            // strafe
            if(driveRight) {
                driveRight(speed);
            } else {
                driveLeft(speed);
            }

            telemetry.addData("ods error", error);
            telemetry.update();
        }
        stopRobot();
    }

    protected void autonomousInitLoop() {
        int autonomousDelay = 0;

        while(!opModeIsActive() && !isStopRequested()) {
            if (gamepad1.a) {
                telemetry.addData(">", "Calibrating Gyro");
                telemetry.update();

                gyroSensor.calibrate();
            } else if (gamepad1.b) {
                telemetry.addData(">", "Performing black-level calibration... ");
                telemetry.update();

                // command register = 0x03
                // 0x43 = command for black-level calibration
//                colorSensor1Sync.write8(0x03, 0x42);
//                colorSensor2Sync.write8(0x03, 0x42);
            }

            // dpad control to set the delay
            if(gamepad1.dpad_up) {
                autonomousDelay = 1;
            } else if(gamepad1.dpad_down) {
                autonomousDelay = 2;
            } else if(gamepad1.dpad_left) {
                autonomousDelay = 5;
            } else if(gamepad1.dpad_right) {
                autonomousDelay = 10;
            }

            if(gamepad1.left_trigger > 0) {
                robotRuntime.reset();
                while(opModeIsActive() && robotRuntime.milliseconds() < 500) {
                    idle();
                }
                autonomousDelay++;

            } else if(gamepad1.right_trigger > 0) {
                robotRuntime.reset();
                while(opModeIsActive() && robotRuntime.milliseconds() < 500) {
                    idle();
                }
                autonomousDelay--;
            }

            // print the delay
            telemetry.addData("Autonomous delay", autonomousDelay);

            telemetry.addData("Gyro Z axis", gyroSensor.getIntegratedZValue());

            // print color sensor values
            telemetry.addData("Left color sensor", "red: %d, blue: %d",
                    getColorSensor1().red(), getColorSensor1().blue());
            telemetry.addData("Right color sensor", "red: %d, blue: %d",
                    getColorSensor2().red(), getColorSensor2().blue());
            telemetry.update();
        }

        // actual delay
        while(opModeIsActive() && robotRuntime.seconds() < autonomousDelay) {
            telemetry.addData(">", "Autonomous delay in progress... ");
            telemetry.update();
            idle();
        }
    }

    protected void claimBeaconRed() {
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // drive to 10cm from the wall
        rangeSensorDrive(10, 0.1);

        // when the beacon is already claimed, moved on
        if(colorSensor1.red() > 0 && colorSensor2.red() > 0) {
            return;
        }

        telemetry.addData("color sensor 1", "red: %d, blue: %d", colorSensor1.red(), colorSensor1.blue());
        telemetry.addData("color sensor 2", "red: %d, blue: %d", colorSensor2.red(), colorSensor2.blue());
        telemetry.update();

        if(colorSensor1.red() > 0 || colorSensor2.blue() > 0) {
            beaconsServo1.setPosition(0.3);
        } else if(colorSensor1.blue() > 0 || colorSensor2.red() > 0) {
            beaconsServo2.setPosition(0.75);
        } else {
            telemetry.addData(">", "beacon not detected!");
            telemetry.update();
        }

        stopRobot();

        // wait for the servo to raise
        robotRuntime.reset();
        while(robotRuntime.milliseconds() < 400) {
            idle();
        }

        robotRuntime.reset();

        // first push
        while(opModeIsActive() &&
                (getFrontRange().cmUltrasonic() >= 6 && robotRuntime.milliseconds() < 600)) {
            // run without encoders again
            driveForward(0.2);
        }
        //stopRobot();

        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("color sensor 1", "red: %d, blue: %d", colorSensor1.red(), colorSensor1.blue());
        telemetry.addData("color sensor 2", "red: %d, blue: %d", colorSensor2.red(), colorSensor2.blue());
        telemetry.update();

        // check if both color sensors do not detect red
        if(colorSensor1.blue() > 0 || colorSensor2.blue() > 0) {
            repositionBeacons();

            // second push
            while(opModeIsActive() &&
                    (getFrontRange().cmUltrasonic() >= 6 && robotRuntime.milliseconds() < 600)) {
                // run without encoders again
                driveForward(0.2);
            }
            stopRobot();
        }

        // drive backward to 15 cm
        rangeSensorDrive(15, 0.1);

        // lower button pushers
        beaconsServo1.setPosition(1);
        beaconsServo2.setPosition(0);
    }

    private void repositionBeacons() {
        gyroPivot(0.8, 0, false);

        rangeSensorDrive(12, 0.1);

        // drive right past line
        encoderStrafe(0.1, 4, 4);

        // look for the white line leading to the second beacon
        while(opModeIsActive() && getOds3().getRawLightDetected() < 1.5) {
            driveLeft(0.1);
            telemetry.addData("ods3", getOds3().getRawLightDetected());
            telemetry.update();
        }
        stopRobot();

        // drive to 10cm from the wall
        rangeSensorDrive(10, 0.1);
    }

    protected void claimBeaconBlue() {
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // drive to 10cm from the wall
        rangeSensorDrive(10, 0.1);

        // when the beacon is already claimed, moved on
        if(colorSensor1.blue() > 0 && colorSensor2.blue() > 0) {
            return;
        }

        telemetry.addData("color sensor 1", "red: %d, blue: %d", colorSensor1.red(), colorSensor1.blue());
        telemetry.addData("color sensor 2", "red: %d, blue: %d", colorSensor2.red(), colorSensor2.blue());
        telemetry.update();

        if(colorSensor1.blue() > 0 || colorSensor2.red() > 0) {
            beaconsServo1.setPosition(0.3);
        } else if(colorSensor1.red() > 0 || colorSensor2.blue() > 0) {
            beaconsServo2.setPosition(0.75);
        } else {
            telemetry.addData(">", "beacon not detected");
            telemetry.update();
        }

        stopRobot();

        // wait for the servo to raise
        robotRuntime.reset();
        while(robotRuntime.milliseconds() < 400) {
            idle();
        }

        // first push
        while(opModeIsActive() &&
                (getFrontRange().cmUltrasonic() >= 6 && robotRuntime.milliseconds() < 1500)) {
            // run without encoders again
            driveForward(0.2);
        }
        //stopRobot();

        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("color sensor 1", "red: %d, blue: %d", colorSensor1.red(), colorSensor1.blue());
        telemetry.addData("color sensor 2", "red: %d, blue: %d", colorSensor2.red(), colorSensor2.blue());
        telemetry.update();

        // check if both color sensors do not detect blue
        if(colorSensor1.red() > 0 || colorSensor2.red() > 0) {
            repositionBeacons();

            // second push
            while(opModeIsActive() &&
                    (getFrontRange().cmUltrasonic() >= 6 && robotRuntime.milliseconds() < 1500)) {
                // run without encoders again
                driveForward(0.2);
            }
            stopRobot();
        }

        // drive backward to 15 cm
        rangeSensorDrive(15, 0.1);

        // lower button pushers
        beaconsServo1.setPosition(1);
        beaconsServo2.setPosition(0);
    }

    protected synchronized void launchParticle() {
        robotRuntime.reset();

        // run launcher motor for an entire rotation
        while(opModeIsActive() && robotRuntime.milliseconds() < 900) {
            launcherMotor.setPower(1.0);
        }

        // stop the launcher motor on the black line
        while(opModeIsActive() && diskOds.getRawLightDetected() > 1) {
            launcherMotor.setPower(0.3);
        }

        launcherMotor.setPower(0);
    }

    protected void encoderDrive(double speed, double leftInches, double rightInches) {
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int leftTarget = (int)(leftInches * COUNTS_PER_INCH);
        int rightTarget = (int)(rightInches * COUNTS_PER_INCH);

        // set the target position for each motor
        getFrontLeftDrive().setTargetPosition(leftTarget);
        getFrontRightDrive().setTargetPosition(-rightTarget);
        getBackRightDrive().setTargetPosition(-rightTarget);
        getBackLeftDrive().setTargetPosition(leftTarget);

        // set RUN_TO_POSITION for each motor
        setDriveMotorsMode(DcMotor.RunMode.RUN_TO_POSITION);

        // set the power for the left drive motors
        getFrontLeftDrive().setPower(speed);
        getBackLeftDrive().setPower(speed);

        // set the power for the right drive motors
        getFrontRightDrive().setPower(speed);
        getBackRightDrive().setPower(speed);

        while(opModeIsActive() && areDriveMotorsBusy()) {
            idle();
        }

        stopRobot();

        // set RUN_WITHOUT_ENCODER for each motor
        setDriveMotorsMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    protected void rangeGyroStrafe(double speed, double angle, double rangeDistance,
                                   double frontInches, double backInches, boolean enableGyro) {
        double error;
        double diffFromTarget;
        double gyroSteer;
        double rangeSteer;

        double proportionalSpeed;

        // initialize motor powers to the initial speed
        double frontRightPower;
        double frontLeftPower;
        double backRightPower;
        double backLeftPower;

        String state = "none";

        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMotorsMode(DcMotor.RunMode.RUN_TO_POSITION);

        int frontTarget = (int)(frontInches * COUNTS_PER_INCH);
        int backTarget = (int)(backInches * COUNTS_PER_INCH);

        // set the target position for each motor
        getFrontLeftDrive().setTargetPosition(frontTarget);
        getFrontRightDrive().setTargetPosition(frontTarget);

        getBackRightDrive().setTargetPosition(-backTarget);
        getBackLeftDrive().setTargetPosition(-backTarget);

        // set the power for the left drive motors
        getFrontLeftDrive().setPower(speed);
        getBackLeftDrive().setPower(speed);

        // set the power for the right drive motors
        getFrontRightDrive().setPower(speed);
        getBackRightDrive().setPower(speed);

        while(opModeIsActive() && areDriveMotorsBusy()) {
            error = getGyroError(angle);
            diffFromTarget = rangeDistance - frontRange.cmUltrasonic();

            gyroSteer = error * P_GYRO_DRIVE_COEFF;
//            rangeSteer = Math.abs(diffFromTarget * P_RANGE_DRIVE_COEFF);
            rangeSteer = speed * 0.5;

            // reset power variables every iteration of loop
            frontRightPower = speed;
            frontLeftPower = speed;
            backRightPower = speed;
            backLeftPower = speed;

            if(enableGyro && Math.abs(error) > GYRO_ERROR_THRESHOLD) {
                // if robot is rotated counterclockwise from 0
                if (error < 0) {
                    proportionalSpeed = -gyroSteer;

                    // if driving right
                    if(frontInches > 0 && backInches > 0) {
                        // decrease power of back motors
                        frontRightPower += proportionalSpeed;
                        frontLeftPower += proportionalSpeed;
                    } else {
                        backRightPower += proportionalSpeed;
                        backLeftPower += proportionalSpeed;
                    }

                // else the robot is rotated clockwise from 0
                } else {
                    proportionalSpeed = gyroSteer;

                    // if driving right
                    if(frontInches > 0 && backInches > 0) {
                        // decrease power of front motors
                        frontLeftPower += proportionalSpeed;
                        backRightPower += proportionalSpeed;
                    } else {
                        backRightPower += proportionalSpeed;
                        frontLeftPower += proportionalSpeed;
                    }
                }
            }

            if(Math.abs(diffFromTarget) > RANGE_SENSOR_THRESHOLD) {
                // too far from wall
                if (diffFromTarget < 0) {
                    // if driving right
                    if (frontInches > 0 && backInches > 0) {
//                       frontLeftPower -= proportionalSpeed;
//                       backRightPower -= proportionalSpeed;

                        frontRightPower *= rangeSteer;
                        backLeftPower *= rangeSteer;

                        state = "driving right, too far";
                    } else {
                        // if driving left
//                  frontRightPower -= proportionalSpeed;
//                  backLeftPower -= proportionalSpeed;

                        frontLeftPower *= rangeSteer;
                        backRightPower *= rangeSteer;

//                  frontLeftPower = 0;
//                  backRightPower = 0;

                        state = "driving left, too far: " + rangeSteer;
                    }
                    // too close to wall
                } else {
                    // if driving right
                    if (frontInches > 0 && backInches > 0) {
//                        frontRightPower += proportionalSpeed;
//                        backLeftPower += proportionalSpeed;

                        frontLeftPower *= rangeSteer;
                        backRightPower *= rangeSteer;

                        state = "driving right, too close";
                    } else {
                        // if driving left
//                        frontLeftPower += proportionalSpeed;
//                        backRightPower += proportionalSpeed;

                        frontRightPower *= rangeSteer;
                        backLeftPower *= rangeSteer;

//                        frontRightPower = 0;
//                        backLeftPower = 0;

                        state = "driving left, too close: " + rangeSteer;
                    }
                }
            }

            // set the motor powers
            getFrontLeftDrive().setPower(Range.clip(frontLeftPower, -speed, speed));
            getFrontRightDrive().setPower(Range.clip(frontRightPower, -speed, speed));
            getBackLeftDrive().setPower(Range.clip(backLeftPower, -speed, speed));
            getBackRightDrive().setPower(Range.clip(backRightPower, -speed, speed));

            telemetry.addData("gyro steer", gyroSteer);
            telemetry.addData("range steer", rangeSteer);
            telemetry.addData("range sensor value", frontRange.cmUltrasonic());
            telemetry.addData("State", state);

            telemetry.addData("front right", getFrontRightDrive().getPower());
            telemetry.addData("front left", getFrontLeftDrive().getPower());
            telemetry.addData("back right", getBackRightDrive().getPower());
            telemetry.addData("back left", getBackLeftDrive().getPower());
            telemetry.update();

            idle();
        }

        stopRobot();

        // set RUN_USING_ENCODER for each motor
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    protected void encoderStrafe(double speed, double frontInches, double backInches) {
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMotorsMode(DcMotor.RunMode.RUN_TO_POSITION);

        int frontTarget = (int)(frontInches * COUNTS_PER_INCH);
        int backTarget = (int)(backInches * COUNTS_PER_INCH);

        // set the target position for each motor
        getFrontLeftDrive().setTargetPosition(frontTarget);
        getFrontRightDrive().setTargetPosition(frontTarget);

        getBackRightDrive().setTargetPosition(-backTarget);
        getBackLeftDrive().setTargetPosition(-backTarget);

        // set RUN_TO_POSITION for each motor
        // setDriveMotorsMode(DcMotor.RunMode.RUN_TO_POSITION);

        // set the power for the left drive motors
        getFrontLeftDrive().setPower(speed);
        getBackLeftDrive().setPower(speed);

        // set the power for the right drive motors
        getFrontRightDrive().setPower(speed);
        getBackRightDrive().setPower(speed);

        while(opModeIsActive() && areDriveMotorsBusy()) {
            idle();
        }

        stopRobot();

        // set RUN_USING_ENCODER for each motor
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private double getGyroError(double targetAngle) {
         return targetAngle - gyroSensor.getIntegratedZValue();
    }

    protected void rangeSensorDrive(int distanceCm, double speed) {
        if(getFrontRange().cmUltrasonic() > distanceCm) {
            while (opModeIsActive() && getFrontRange().cmUltrasonic() > distanceCm) {
                driveForward(speed);
            }
        } else {
            while (opModeIsActive() && getFrontRange().cmUltrasonic() < distanceCm) {
                driveBackward(speed);
            }
        }
        stopRobot();
    }

    protected void driveRight(double power) {
        frontLeftDrive.setPower(power);
        backLeftDrive.setPower(-power);
        frontRightDrive.setPower(power);
        backRightDrive.setPower(-power);
    }

    protected void driveLeft(double power) {
        frontLeftDrive.setPower(-power);
        backLeftDrive.setPower(power);
        frontRightDrive.setPower(-power);
        backRightDrive.setPower(power);
    }

    protected void driveForward(double power) {
        frontLeftDrive.setPower(power);
        backLeftDrive.setPower(power);
        frontRightDrive.setPower(-power);
        backRightDrive.setPower(-power);
    }

    protected void driveBackward(double power) {
        frontLeftDrive.setPower(-power);
        backLeftDrive.setPower(-power);
        frontRightDrive.setPower(power);
        backRightDrive.setPower(power);
    }

    protected void stopRobot() {
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
    }

    protected void gyroPivot(double speed, double angle, boolean absolute) {
        double steer;
        double proportionalSpeed;
        double error = getGyroError(angle);

        if(!absolute) {
            error += getGyroError(0);
        }

        // just return if the error is too low
        if(Math.abs(error) <= 2) {
            return;
        }

        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while(opModeIsActive() && Math.abs(error) > GYRO_ERROR_THRESHOLD) {

            telemetry.addData("error", error);
            telemetry.addData("integrated Z axis", gyroSensor.getIntegratedZValue());
            telemetry.update();

            error = getGyroError(angle);

            if(!absolute) {
                error += getGyroError(0);
            }

            steer = Range.clip(error * P_GYRO_TURN_COEFF , -1, 1);

            proportionalSpeed = speed * steer;

            getFrontLeftDrive().setPower(-proportionalSpeed);
            getFrontRightDrive().setPower(-proportionalSpeed);

            getBackLeftDrive().setPower(-proportionalSpeed);
            getBackRightDrive().setPower(-proportionalSpeed);
        }

        telemetry.addData(">", "stop");
        telemetry.update();

        // when we're on target, stop the robot
        stopRobot();

        // reset to run without encoders
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    protected void setDriveMotorsMode(DcMotor.RunMode runMode) {
        backLeftDrive.setMode(runMode);
        backRightDrive.setMode(runMode);
        frontLeftDrive.setMode(runMode);
        frontRightDrive.setMode(runMode);
    }

    protected boolean areDriveMotorsBusy() {
        return frontLeftDrive.isBusy() && frontRightDrive.isBusy()
                && backLeftDrive.isBusy() && backRightDrive.isBusy();
    }

    protected DcMotor getIntakeMotor() {
        return intakeMotor;
    }

    protected DcMotor getFrontLeftDrive() {
        return frontLeftDrive;
    }

    protected DcMotor getFrontRightDrive() {
        return frontRightDrive;
    }

    protected DcMotor getBackLeftDrive() {
        return backLeftDrive;
    }

    protected DcMotor getBackRightDrive() {
        return backRightDrive;
    }

    protected DcMotor getLauncherMotor() {
        return launcherMotor;
    }

    protected DcMotor getSpoolMotor1() {
        return spoolMotor1;
    }

    protected DcMotor getSpoolMotor2() {
        return spoolMotor2;
    }

    protected Servo getBeaconsServo1() { return beaconsServo1; }

    protected Servo getBeaconsServo2() { return beaconsServo2; }

    protected Servo getDoor3() { return door3; }

    protected Servo getLatch4() {
        return latch4;
    }

    protected Servo getPusher5() {
        return pusher5;
    }

    protected LightSensor getFrontLightSensor() {
        return frontLightSensor;
    }

    protected LightSensor getBackLightSensor() {
        return backLightSensor;
    }

    protected ColorSensor getColorSensor1() {
        return colorSensor1;
    }

    protected ColorSensor getColorSensor2() {
        return colorSensor2;
    }

    protected ModernRoboticsI2cRangeSensor getFrontRange() {
        return frontRange;
    }

    protected ModernRoboticsI2cRangeSensor getLeftRange() {
        return leftRange;
    }

    protected OpticalDistanceSensor getLauncherOds() { return launcherOds; }

    protected OpticalDistanceSensor getDiskOds() { return diskOds; }

    /**
    ODS on the bottom
     */
    protected OpticalDistanceSensor getOds3() {
        return ods3;
    }

    protected ModernRoboticsI2cGyro getGyroSensor() {
        return gyroSensor;
    }

    protected ElapsedTime getRobotRuntime() {
        return robotRuntime;
    }

}
