package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by ftc6347 on 10/16/16.
 */
public abstract class LinearOpModeBase extends LinearOpMode {

    private static final int WHEEL_DIAMETER_INCHES = 4;

    private static final int COUNTS_PER_MOTOR_REV = 1120;

    private static final double GYRO_ERROR_THRESHOLD = 1.0;

    private static final double P_GYRO_TURN_COEFF = 0.01;
    private static final double P_GYRO_DRIVE_COEFF = 0.008;

    protected static final int COUNTS_PER_INCH = (int)(COUNTS_PER_MOTOR_REV /
            (WHEEL_DIAMETER_INCHES * Math.PI));

    private static final double LIGHT_SENSOR_PERFECT_VALUE = 2.5;

    private static final double LIGHT_THRESHOLD = 1.0;

    private static final double P_RANGE_DRIVE_COEFF = 0.04;

    protected static final double LAUNCHER_CHAMBER_COLOR_SENSOR_THRESHOLD = 12;

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
    private Servo touchSensorServo;

    private ModernRoboticsI2cColorSensor colorSensor1;
    private ModernRoboticsI2cColorSensor colorSensor2;

    private ModernRoboticsI2cRangeSensor frontRange;
    private ModernRoboticsI2cRangeSensor leftRange;

    private ColorSensor launcherChamberColorSensor;
    private OpticalDistanceSensor diskOds;
    private OpticalDistanceSensor leftOds;
    private OpticalDistanceSensor rightOds;

    private ModernRoboticsI2cGyro gyroSensor;

    private TouchSensor touchSensor;

    private ElapsedTime robotRuntime;

    private int numAutoParticlesToLaunch = 2;

    /**
     * An enumeration type that represents the directions the robot is able to drive in.
     *
     * @see #encoderDrive(double, double, RobotDirection)
     * @see #rangeGyroStrafe(double, double, int, RobotDirection)
     */
    public enum RobotDirection {
        FORWARD, BACKWARD, LEFT, RIGHT, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST
    }

    /**
     * Initialize robot hardware. In addition, this method is responsible for the following:
     *
     * <ul>
     * <li>Sets the I2c addresses of two color sensors</li>
     * <li>Resets the Z axis of the gyro sensor</li>
     * <li>Sets the correct directions for the spool motors</li>
     * <li>Stops all motors</li>
     * </ul>
     */
    protected void initializeHardware() {
        // initialize robotRuntime instance variable
        robotRuntime = new ElapsedTime();

        frontLeftDrive = hardwareMap.dcMotor.get("fl");
        frontRightDrive = hardwareMap.dcMotor.get("fr");
        backLeftDrive = hardwareMap.dcMotor.get("bl");
        backRightDrive = hardwareMap.dcMotor.get("br");

        launcherMotor = hardwareMap.dcMotor.get("launcher");
        // keep launcher motor speed consistent
        launcherMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        intakeMotor = hardwareMap.dcMotor.get("intake");

        spoolMotor1 = hardwareMap.dcMotor.get("s1");
        spoolMotor2 = hardwareMap.dcMotor.get("s2");

        beaconsServo1 = hardwareMap.servo.get("b1");  // Up = 0.3, Down = 1.0
        beaconsServo2 = hardwareMap.servo.get("r2");   // Up = 0.7, Down = 0.0
        door3 = hardwareMap.servo.get("d3");  // Closed = 0.55, Open = 0.25
        latch4 = hardwareMap.servo.get("l4"); // Up = 0.5
        pusher5 = hardwareMap.servo.get("p5"); // Up = 0.0, Down = 0.7
        touchSensorServo = hardwareMap.servo.get("l6"); // Up = 0.7, Down = 0.14

        colorSensor1 = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "clr");
        colorSensor1.setI2cAddress(I2cAddr.create8bit(0x3C));

        colorSensor2 = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "clr2");
        colorSensor2.setI2cAddress(I2cAddr.create8bit(0x3E));

        // disable LEDs for both color sensors
        colorSensor1.enableLed(false);
        colorSensor2.enableLed(false);

        frontRange = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "frs");
//        leftRange = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "lrs");

        diskOds = hardwareMap.opticalDistanceSensor.get("diskOds");
        leftOds = hardwareMap.opticalDistanceSensor.get("lOds");
        rightOds = hardwareMap.opticalDistanceSensor.get("rOds");

        launcherChamberColorSensor = hardwareMap.colorSensor.get("lcCs");
        launcherChamberColorSensor.setI2cAddress(I2cAddr.create8bit(0x40));

        // disable LED first due to bug
        launcherChamberColorSensor.enableLed(false);
        launcherChamberColorSensor.enableLed(true);

        gyroSensor = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gy");

        touchSensor = hardwareMap.touchSensor.get("ts");

        // reverse only one spool motor
        spoolMotor1.setDirection(DcMotor.Direction.REVERSE);
        spoolMotor2.setDirection(DcMotor.Direction.FORWARD);

        robotRuntime.reset();

        // put the button pusher servos down
        beaconsServo1.setPosition(0);
        beaconsServo2.setPosition(1);

        door3.setPosition(0.53);
        latch4.setPosition(0.5);
        pusher5.setPosition(0);
        touchSensorServo.setPosition(0.7);

        // stop all motors
        launcherMotor.setPower(0);
        intakeMotor.setPower(0);
        stopRobot();

        // reset gyro heading
        gyroSensor.resetZAxisIntegrator();

        telemetry.addData("status","end of init");
        telemetry.update();
    }

    /**
     * This method uses the ODS (optical distance sensor) on the bottom of the robot
     * to drive to the white line leading to one of the beacons.
     *
     * @param speed the speed at which the robot will drive
     * @param direction the direction the robot drives in;
     *                  only LEFT or RIGHT are acceptable arguments
     */
    protected void stopOnLine(double speed, RobotDirection direction) {
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        OpticalDistanceSensor ods = (direction == RobotDirection.LEFT ? leftOds : rightOds);

        double error = LIGHT_SENSOR_PERFECT_VALUE - ods.getRawLightDetected();

        while(opModeIsActive() && Math.abs(error) > LIGHT_THRESHOLD) {

            error = LIGHT_SENSOR_PERFECT_VALUE - ods.getRawLightDetected();

            // strafe
            if(direction == RobotDirection.RIGHT) {
                driveRight(speed);
            } else {
                driveLeft(speed);
            }

            telemetry.addData("ods error", error);
            telemetry.update();
        }
        stopRobot();
    }

    /**
     * The autonomous initialization loop. This method provides the following functionality:
     *
     * <ul>
     * <li>Set a predefined delay using the dpad on the first gamepad</li>
     * <li>Increment/decrement the delay using the left and right triggers on the first gamepad</li>
     * <li>Automatically reset the gyro integrated Z axis if it changes every 250 milliseconds</li>
     * <li>Reset the gyro integrated Z axis manually using the A button on the first gamepad</li>
     * </ul>
     */
    protected void autonomousInitLoop() {
        int delay = 0;

        while(!opModeIsActive() && !isStopRequested()) {
            // dpad control to set the delay
            if(gamepad1.dpad_up) {
                delay = 1;
            } else if(gamepad1.dpad_down) {
                delay = 2;
            } else if(gamepad1.dpad_left) {
                delay = 5;
            } else if(gamepad1.dpad_right) {
                delay = 10;
            }

            if(robotRuntime.milliseconds() > 250) {
                // set the delay using the left and right triggers
                if (gamepad1.right_trigger > 0) {
                    delay++;
                } else if (gamepad1.left_trigger > 0) {
                    if(delay > 0) {
                        delay--;
                    }
                }

                // allow the gyro to be reset in initialization
                if(gamepad1.a) {
                    gyroSensor.resetZAxisIntegrator();
                }

                // automatically reset gyro Z axis
                if(Math.abs(gyroSensor.getIntegratedZValue()) > 0) {
                    gyroSensor.resetZAxisIntegrator();
                }

                // toggle between particles to launch
                if(gamepad1.x) {
                    if (numAutoParticlesToLaunch == 2) {
                        numAutoParticlesToLaunch = 1;
                    } else {
                        numAutoParticlesToLaunch = 2;
                    }
                }

                robotRuntime.reset();
            }

            // print sensor values
            telemetry.addData("gyro Z axis", gyroSensor.getIntegratedZValue());

            telemetry.addData("color sensor 1", "red: %d, blue: %d",
                    getColorSensor1().red(), getColorSensor1().blue());
            telemetry.addData("color sensor 2", "red: %d, blue: %d",
                    getColorSensor2().red(), getColorSensor2().blue());
            telemetry.addData("number of particles to launch",
                    numAutoParticlesToLaunch);

            // print the delay
            telemetry.addData("delay", "%ds", delay);

            telemetry.update();
        }

        // actual delay loop
        while(opModeIsActive() && robotRuntime.seconds() < delay) {
            telemetry.addData("starting in", "%.2fs", delay - robotRuntime.seconds());
            telemetry.update();
        }
    }

    protected void touchSensorDrive() {
        // lower the touch sensor servo
        touchSensorServo.setPosition(0.14);

        // drive forward while touch sensor is not pressed
        while(opModeIsActive() && !getTouchSensor().isPressed()) {
            driveForward(0.1);
        }
        stopRobot();

        // raise the servo back up
        touchSensorServo.setPosition(0.7);

        // drive back from beacon
        encoderDrive(0.2, 1, RobotDirection.BACKWARD);

        // be square with wall
        gyroPivot(0.8, 0, true);
    }

    /**
     * Claim a beacon to be red. This algorithm uses the following procedure to claim a beacon:
     *
     * <ul>
     * <li>Drive to 10cm from the wall in front of the beacon</li>
     * <li>Return if the beacon is already claimed for our alliance</li>
     * <li>Raise the appropriate button pusher servo motor using the color sensors to detect
     * the configuration of the beacon</li>
     * <li>Drive to 5cm from the wall to push the beacon button or timeout at 600ms</li>
     * <li>Check if the beacon has been claimed appropriately and re-push as necessary</li>
     * </ul>
     */
    protected void claimBeaconRed() {
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // reset again before pressing beacon
        gyroPivot(0.8, 0, true);

        // when the beacon is already claimed, move on
        if(colorSensor1.red() > 0 && colorSensor2.red() > 0) {
            return;
        }

        telemetry.addData("color sensor 1", "red: %d, blue: %d", colorSensor1.red(), colorSensor1.blue());
        telemetry.addData("color sensor 2", "red: %d, blue: %d", colorSensor2.red(), colorSensor2.blue());
        telemetry.update();

        // move the button pusher servos
        if(colorSensor1.red() > 0 || colorSensor2.blue() > 0) {
            beaconsServo1.setPosition(0.6);
        } else if(colorSensor1.blue() > 0 || colorSensor2.red() > 0) {
            beaconsServo2.setPosition(0.4);
        } else {
            telemetry.addData(">", "beacon not detected!");
            telemetry.update();
        }

        // wait for button pusher servo
        robotRuntime.reset();
        while(opModeIsActive() && robotRuntime.milliseconds() < 600) {
            idle();
        }

        // put the button pusher servos down
        beaconsServo1.setPosition(0);
        beaconsServo2.setPosition(1);

        // check if both color sensors do not detect red
        if(colorSensor1.blue() > 0 || colorSensor2.blue() > 0) {
            repositionBeacons();

            // when the beacon is already claimed, move on
            if(colorSensor1.red() > 0 && colorSensor2.red() > 0) {
                return;
            }

            // move the button pusher servos
            if(colorSensor1.red() > 0 || colorSensor2.blue() > 0) {
                beaconsServo1.setPosition(0.6);
            } else if(colorSensor1.blue() > 0 || colorSensor2.red() > 0) {
                beaconsServo2.setPosition(0.4);
            } else {
                telemetry.addData(">", "beacon not detected!");
                telemetry.update();
            }

            // wait for button pusher servo
            robotRuntime.reset();
            while(opModeIsActive() && robotRuntime.milliseconds() < 600) {
                idle();
            }
        }

        // put the button pusher servos back
        beaconsServo1.setPosition(0);
        beaconsServo2.setPosition(1);
    }

    private void repositionBeacons() {
        rangeSensorDrive(10, 0.2);

        encoderDrive(0.1, 4, RobotDirection.RIGHT);

        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        touchSensorDrive();

        // drive left to white line
        stopOnLine(0.1, RobotDirection.LEFT);

        // reset again before pressing beacon
        gyroPivot(0.8, 0, true);
    }

    /**
     * Claim a beacon to be blue. This algorithm uses the following procedure to claim a beacon:
     *
     * <ul>
     * <li>Drive to 10cm from the wall in front of the beacon</li>
     * <li>Return if the beacon is already claimed for our alliance</li>
     * <li>Raise the appropriate button pusher servo motor using the color sensors to detect
     * the configuration of the beacon</li>
     * <li>Drive to 5cm from the wall to push the beacon button or timeout at 600ms</li>
     * <li>Check if the beacon has been claimed appropriately and re-push as necessary</li>
     * </ul>
     */
    protected void claimBeaconBlue() {
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // reset again before pressing beacon
        gyroPivot(0.8, 0, true);

        // when the beacon is already claimed, move on
        if(colorSensor1.blue() > 0 && colorSensor2.blue() > 0) {
            return;
        }

        telemetry.addData("color sensor 1", "red: %d, blue: %d", colorSensor1.red(), colorSensor1.blue());
        telemetry.addData("color sensor 2", "red: %d, blue: %d", colorSensor2.red(), colorSensor2.blue());
        telemetry.update();

        // move the button pusher servos
        if(colorSensor1.blue() > 0 || colorSensor2.red() > 0) {
            beaconsServo1.setPosition(0.6);
        } else if(colorSensor1.red() > 0 || colorSensor2.blue() > 0) {
            beaconsServo2.setPosition(0.4);
        } else {
            telemetry.addData(">", "beacon not detected!");
            telemetry.update();
        }

        // wait for button pusher servo
        robotRuntime.reset();
        while(opModeIsActive() && robotRuntime.milliseconds() < 600) {
            idle();
        }

        // put the button pusher servos down
        beaconsServo1.setPosition(0);
        beaconsServo2.setPosition(1);

        // check if both color sensors do not detect blue
        if(colorSensor1.red() > 0 || colorSensor2.red() > 0) {
            repositionBeacons();

            // when the beacon is already claimed, move on
            if(colorSensor1.blue() > 0 && colorSensor2.blue() > 0) {
                return;
            }

            // move the button pusher servos
            if(colorSensor1.blue() > 0 || colorSensor2.red() > 0) {
                beaconsServo1.setPosition(0.6);
            } else if(colorSensor1.red() > 0 || colorSensor2.blue() > 0) {
                beaconsServo2.setPosition(0.4);
            } else {
                telemetry.addData(">", "beacon not detected!");
                telemetry.update();
            }

            // wait for button pusher servo
            robotRuntime.reset();
            while(opModeIsActive() && robotRuntime.milliseconds() < 600) {
                idle();
            }
        }

        // put the button pusher servos back
        beaconsServo1.setPosition(0);
        beaconsServo2.setPosition(1);
    }

    /**
     * Launch a particle loaded in the launcher chamber using
     * the launcher ODS (optical distance sensor).
     */
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

    protected void autoLaunchParticle() {

        int particlesLaunched = 0;

        while(opModeIsActive() && particlesLaunched < numAutoParticlesToLaunch) {
            if (getLauncherChamberColorSensor().alpha()
                    > LAUNCHER_CHAMBER_COLOR_SENSOR_THRESHOLD) {
                getIntakeMotor().setPower(0);
                launchParticle();

                particlesLaunched++;
            } else {
                // otherwise, run the intake
                getIntakeMotor().setPower(-1.0);
            }
        }
        getIntakeMotor().setPower(0);
    }

    /**
     * Drive the robot using encoders a specific distance in inches.
     *
     * @param speed the speed the robot  to drive at, within a range of -1 to 1
     * @param distance the distance to drive in inches
     * @param direction the direction to drive
     * @see RobotDirection for the available directions
     */
    protected void encoderDrive(double speed, double distance, RobotDirection direction) {
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int target = (int)(distance * COUNTS_PER_INCH);

        // set the target position for each motor
        switch(direction) {
            case FORWARD:
                getFrontLeftDrive().setTargetPosition(target);
                getFrontRightDrive().setTargetPosition(-target);
                getBackRightDrive().setTargetPosition(-target);
                getBackLeftDrive().setTargetPosition(target);
                break;
            case BACKWARD:
                getFrontLeftDrive().setTargetPosition(-target);
                getFrontRightDrive().setTargetPosition(target);
                getBackRightDrive().setTargetPosition(target);
                getBackLeftDrive().setTargetPosition(-target);
                break;
            case LEFT:
                getFrontLeftDrive().setTargetPosition(-target);
                getFrontRightDrive().setTargetPosition(-target);
                getBackRightDrive().setTargetPosition(target);
                getBackLeftDrive().setTargetPosition(target);
                break;
            case RIGHT:
                getFrontLeftDrive().setTargetPosition(target);
                getFrontRightDrive().setTargetPosition(target);
                getBackRightDrive().setTargetPosition(-target);
                getBackLeftDrive().setTargetPosition(-target);
                break;
            default:
                throw new IllegalArgumentException(
                        "Only forward, backward, left, or right are valid directions");
        }

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

    protected void encoderDriveDiagonal(double speed, int distance, RobotDirection direction) {
        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMotorsMode(DcMotor.RunMode.RUN_TO_POSITION);

        int target = distance * COUNTS_PER_INCH;

        DcMotor driveMotor1;
        DcMotor driveMotor2;

        switch(direction) {
            case NORTH_EAST:
                driveMotor1 = getFrontLeftDrive();
                driveMotor2 = getBackRightDrive();
                driveMotor1.setTargetPosition(target);
                driveMotor2.setTargetPosition(-target);
                break;
            case NORTH_WEST:
                driveMotor1 = getFrontRightDrive();
                driveMotor2 = getBackLeftDrive();
                driveMotor1.setTargetPosition(-target);
                driveMotor2.setTargetPosition(target);
                break;
            case SOUTH_EAST:
                driveMotor1 = getFrontRightDrive();
                driveMotor2 = getBackLeftDrive();
                driveMotor1.setTargetPosition(target);
                driveMotor2.setTargetPosition(-target);
                break;
            case SOUTH_WEST:
                driveMotor1 = getFrontLeftDrive();
                driveMotor2 = getBackRightDrive();
                driveMotor1.setTargetPosition(-target);
                driveMotor2.setTargetPosition(target);
                break;
            default:
                throw new IllegalArgumentException(
                "Only north east, north west, south east, or south west are valid directions");
        }

        // set motor powers
        driveMotor1.setPower(speed);
        driveMotor2.setPower(speed);

        while(opModeIsActive() &&
                (driveMotor1.isBusy() && driveMotor2.isBusy())) {
            telemetry.addData("Path",  "Running at %d :%d",
                    driveMotor1.getCurrentPosition(),
                    driveMotor2.getCurrentPosition());
        }
        stopRobot();

        // set RUN_WITHOUT_ENCODER for each motor
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Drives along a wall at a set distance with proportional correction
     * using encoders the range sensor, and the gyro sensor.
     *
     * @throws IllegalArgumentException if forward or backward directions are used
     *
     * @param angle the angle that the gyro sensor should maintain while driving
     * @param rangeDistance the distance the robot should stay at using the range sensor
     * @param distance the distance to drive using the encoders
     * @param direction the direction to drive (only left or right are valid options)
     */
    protected void rangeGyroStrafe(double angle, double rangeDistance,
                                   int distance, RobotDirection direction) {
        double gyroSteer;
        double rangeSteer;

        double gyroDiffFromTarget;
        double rangeDiffFromTarget;

        double frontRightPower;
        double frontLeftPower;
        double backRightPower;
        double backLeftPower;

        double currRangeValue;

        double lastRangeValue = rangeDistance;

        // speed is constant for now
        final double SPEED = 0.8;

        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMotorsMode(DcMotor.RunMode.RUN_TO_POSITION);

        int target = distance * COUNTS_PER_INCH;

        // set the target position for each motor
        switch(direction) {
            case LEFT:
                getFrontLeftDrive().setTargetPosition(-target);
                getFrontRightDrive().setTargetPosition(-target);
                getBackRightDrive().setTargetPosition(target);
                getBackLeftDrive().setTargetPosition(target);
                break;
            case RIGHT:
                getFrontLeftDrive().setTargetPosition(target);
                getFrontRightDrive().setTargetPosition(target);
                getBackRightDrive().setTargetPosition(-target);
                getBackLeftDrive().setTargetPosition(-target);
                break;
            default:
                throw new IllegalArgumentException(
                        "Only LEFT or RIGHT are valid directions for rangeGyroStrafe()");
        }

        // set the power initially for the left drive motors
        getFrontLeftDrive().setPower(SPEED);
        getBackLeftDrive().setPower(SPEED);

        // set the power initially for the right drive motors
        getFrontRightDrive().setPower(SPEED);
        getBackRightDrive().setPower(SPEED);

        while(opModeIsActive() && areDriveMotorsBusy()) {
            gyroDiffFromTarget = getGyroError(angle);

            currRangeValue = frontRange.cmUltrasonic();

            // use last read value that is not 255
            if(currRangeValue != 255) {
                lastRangeValue = currRangeValue;
            }

            rangeDiffFromTarget = rangeDistance - lastRangeValue;

            gyroSteer = gyroDiffFromTarget * P_GYRO_DRIVE_COEFF;
            rangeSteer = Math.abs(rangeDiffFromTarget * P_RANGE_DRIVE_COEFF);

            // if driving right
            if(direction == RobotDirection.RIGHT) {
                gyroSteer *= -1;
            }

            // adjust powers for front motors
            frontLeftPower = SPEED - gyroSteer;
            frontRightPower = SPEED - gyroSteer;

            // adjust powers for back motors
            backLeftPower = SPEED + gyroSteer;
            backRightPower = SPEED + gyroSteer;

            // too far from wall
            if (rangeDiffFromTarget < 0) {
                // if driving right
                if (direction == RobotDirection.RIGHT) {
                    frontRightPower -= rangeSteer;
                    backLeftPower -= rangeSteer;
                } else {
                    // if driving left
                    frontLeftPower -= rangeSteer;
                    backRightPower -= rangeSteer;
                }
            // too close to wall
            } else {
                // if driving right
                if (direction == RobotDirection.RIGHT) {
                    frontLeftPower -= rangeSteer;
                    backRightPower -= rangeSteer;
                } else {
                    // if driving left
                    frontRightPower -= rangeSteer;
                    backLeftPower -= rangeSteer;
                }
            }

            // set the motor powers and clip them on a range of 0 to speed
            getFrontLeftDrive().setPower(Range.clip(frontLeftPower, 0, SPEED));
            getFrontRightDrive().setPower(Range.clip(frontRightPower, 0, SPEED));
            getBackLeftDrive().setPower(Range.clip(backLeftPower, 0, SPEED));
            getBackRightDrive().setPower(Range.clip(backRightPower, 0, SPEED));

            telemetry.addData("gyro steer", gyroSteer);
            telemetry.addData("range steer", rangeSteer);
            telemetry.addData("range sensor value", frontRange.cmUltrasonic());

            telemetry.addData("front right", getFrontRightDrive().getPower());
            telemetry.addData("front left", getFrontLeftDrive().getPower());
            telemetry.addData("back right", getBackRightDrive().getPower());
            telemetry.addData("back left", getBackLeftDrive().getPower());

            telemetry.update();
        }

        stopRobot();

        // set RUN_USING_ENCODER for each motor
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private double getGyroError(double targetAngle) {
         // adding since getIntegratedZValue() returns a negative number
         return targetAngle + gyroSensor.getIntegratedZValue();
    }

    /**
     * Drive a to a specific distance from the wall.
     * The robot will drive backward or forward accordingly.
     *
     * @param distanceCm the distance the robot should drive to
     * @param speed the drive speed
     */
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

    /**
     * Set the power of drive motors to drive right
     *
     * @param power the speed to drive at
     */
    protected void driveRight(double power) {
        frontLeftDrive.setPower(power);
        backLeftDrive.setPower(-power);
        frontRightDrive.setPower(power);
        backRightDrive.setPower(-power);
    }

    /**
     * Set the power of drive motors to drive left
     *
     * @param power the speed to drive at
     */
    protected void driveLeft(double power) {
        frontLeftDrive.setPower(-power);
        backLeftDrive.setPower(power);
        frontRightDrive.setPower(-power);
        backRightDrive.setPower(power);
    }

    /**
     * Set the power of drive motors to drive forward
     *
     * @param power the speed to drive at
     */
    protected void driveForward(double power) {
        frontLeftDrive.setPower(power);
        backLeftDrive.setPower(power);
        frontRightDrive.setPower(-power);
        backRightDrive.setPower(-power);
    }

    /**
     * Set the power of drive motors to drive backward
     *
     * @param power the speed to drive at
     */
    protected void driveBackward(double power) {
        frontLeftDrive.setPower(-power);
        backLeftDrive.setPower(-power);
        frontRightDrive.setPower(power);
        backRightDrive.setPower(power);
    }

    /**
     * Stop all drive motors; that is, set the power of each drive motor to zero.
     */
    protected void stopRobot() {
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
    }

    /**
     * Pivot the robot to the specified degree angle using the gyro sensor.
     *
     * @param speed the speed of the drive motors while pivoting
     * @param angle the degree angle to pivot to; a negative value is
     *              counter clockwise and a positive value is clockwise
     * @param absolute whether or not the angle should be relative to where the
     *                 gyro sensor last calibrated or the robot's current rotational position
     */
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

            getFrontLeftDrive().setPower(proportionalSpeed);
            getFrontRightDrive().setPower(proportionalSpeed);

            getBackLeftDrive().setPower(proportionalSpeed);
            getBackRightDrive().setPower(proportionalSpeed);
        }

        telemetry.addData(">", "stop");
        telemetry.update();

        // when we're on target, stop the robot
        stopRobot();

        // reset to run without encoders
        setDriveMotorsMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /**
     * Set the <code>RunMode</code> of the drive motors.
     *
     * @param runMode the run mode that the drive motors will be set to
     * @see DcMotor.RunMode
     */
    protected void setDriveMotorsMode(DcMotor.RunMode runMode) {
        backLeftDrive.setMode(runMode);
        backRightDrive.setMode(runMode);
        frontLeftDrive.setMode(runMode);
        frontRightDrive.setMode(runMode);
    }

    /**
     * Return whether all drive motors are busy.
     * <p>
     * This will not return <code>true</code> if <em>any</em> of the drive motors are busy.
     *
     * @return a boolean corresponding to whether all drive motors are busy
     * @see DcMotor#isBusy()
     */
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

    protected Servo getTouchSensorServo() {
        return touchSensorServo;
    }

    protected Servo getDoor3() { return door3; }

    protected Servo getLatch4() {
        return latch4;
    }

    protected Servo getPusher5() {
        return pusher5;
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

    protected OpticalDistanceSensor getDiskOds() { return diskOds; }

    protected OpticalDistanceSensor getLeftOds() {
        return leftOds;
    }

    protected OpticalDistanceSensor getRightOds() {
        return rightOds;
    }

    protected ColorSensor getLauncherChamberColorSensor() {
        return launcherChamberColorSensor;
    }

    protected ModernRoboticsI2cGyro getGyroSensor() {
        return gyroSensor;
    }

    protected TouchSensor getTouchSensor() {
        return touchSensor;
    }

    protected ElapsedTime getRobotRuntime() {
        return robotRuntime;
    }

}
