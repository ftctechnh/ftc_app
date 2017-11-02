package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cCompassSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDcMotorController;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbLegacyModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.DifferentialControlLoopCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.PIDTesting.PIDTestInterface;
import org.firstinspires.ftc.teamcode.PIDTesting.PIDTestMashupPID;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.Date;

// This is NOT an opmode

public class NullbotHardware {
    // Alliance
    Alliance color;
    boolean isTestChassis;

    // Motors
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    public PIDTestInterface[] driveInterface;

    public DcMotor lift;
    public DcMotor zType;

    // Servos
    public Servo leftWhipSnake;
    public Servo rightWhipSnake;
    public Servo leftBlockClaw;
    public Servo rightBlockClaw;
    public Servo relicClaw;
    public Servo relicClawFlipper;

    // Sensors
    public ModernRoboticsI2cGyro gyro;
    public ModernRoboticsI2cCompassSensor compass;
    public ManualHeadingAdjustmentController headingAdjuster;

    // Adjustment gamepad
    private Gamepad gp2;

    // Telemetry
    public Telemetry tel;

    // Utility mechanisms
    public DcMotor[] motorArr;
    public LogTick[] log;
    public final int hz = 100;
    public final int secondsToTrack = 60;
    public int logPosition;
    double initialCompassHeading;
    double gyroError;
    int msMagFieldKill = 3000; // Robot must stay still for three seconds before it is assumed the
                              // motors are not generating any magnetic field

    double absurdValueFiltering = (Math.PI * 2) / 180; // Any attempt to alter the gyroscope's error
                            // by more than this threshold will be denied

    int msSinceMoved;

    // Local vars
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    public NullbotHardware(){}
    private void init() {

        try {
            hwMap.get(ModernRoboticsUsbLegacyModule.class, "legacyModule");
            isTestChassis = true;
        } catch (IllegalArgumentException e) {
            isTestChassis = false;
        }

        // Motor initialization
        frontLeft = hwMap.dcMotor.get("frontLeft");
        frontRight = hwMap.dcMotor.get("frontRight");
        backLeft = hwMap.dcMotor.get("backLeft");
        backRight = hwMap.dcMotor.get("backRight");
        logPosition = 0;

        if (!isTestChassis) {
            lift = hwMap.dcMotor.get("lift");
            zType = hwMap.dcMotor.get("zType");

            leftWhipSnake = hwMap.servo.get("leftGemHitter");
            rightWhipSnake = hwMap.servo.get("rightGemHitter");

            leftBlockClaw = hwMap.servo.get("leftClaw");
            rightBlockClaw = hwMap.servo.get("rightClaw");

            relicClawFlipper = hwMap.servo.get("relicClawFlipper");
            retractFlipper();
            /*ServoControllerEx flipperCtrl = (ServoControllerEx) relicClawFlipper.getController();
            int flipperPort = relicClawFlipper.getPortNumber();
            PwmControl.PwmRange flipperRange = new PwmControl.PwmRange(553, 2425);
            flipperCtrl.setServoPwmRange(flipperPort, flipperRange);*/

            relicClaw = hwMap.servo.get("relicClaw");

            raiseWhipSnake();
            openBlockClaw();

            ModernRoboticsUsbDcMotorController leftWheels =
                    hwMap.get(ModernRoboticsUsbDcMotorController.class, "leftWheels");

            ModernRoboticsUsbDcMotorController rightWheels =
                    hwMap.get(ModernRoboticsUsbDcMotorController.class, "rightWheels");
            DifferentialControlLoopCoefficients d = new DifferentialControlLoopCoefficients(160, 32, 112);

            for (int i = 1; i <= 2; i++) { // Runs for i = 1 and i = 2
                /*leftWheels.setMotorType(i, MotorConfigurationType.getMotorType(NeveRest40Gearmotor.class));
                rightWheels.setMotorType(i, MotorConfigurationType.getMotorType(NeveRest40Gearmotor.class));*/
                leftWheels.setDifferentialControlLoopCoefficients(i, d);
                rightWheels.setDifferentialControlLoopCoefficients(i, d);
            }
        }

        gyro = hwMap.get(ModernRoboticsI2cGyro.class, "gyro");
        compass = hwMap.get(ModernRoboticsI2cCompassSensor.class, "acc");

        // Set color

        color = Alliance.BLUE;


        compass.setMode(CompassSensor.CompassMode.MEASUREMENT_MODE);

        headingAdjuster = new ManualHeadingAdjustmentController(gp2);

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        // MotorArr utility setup
        motorArr = new DcMotor[4];

        motorArr[0] = frontLeft;
        motorArr[1] = frontRight;
        motorArr[2] = backLeft;
        motorArr[3] = backRight;

        driveInterface = new PIDTestInterface[4];

        for (int i = 0; i < motorArr.length; i++) {
            driveInterface[i] = new PIDTestMashupPID(motorArr[i], tel);
        }

    }

    public void init(HardwareMap ahwMap, LinearOpMode oM, boolean teleoperated, Gamepad g) {
        hwMap = ahwMap;
        tel = oM.telemetry;
        gp2 = g;
        init();
        calibrate(oM);
        if (teleoperated) {
            log = new LogTick[hz*secondsToTrack];
        }
    }

    private void calibrate(LinearOpMode m) {
        // Calibration
        ElapsedTime calibrationTimer = new ElapsedTime();

        tel.log().add("Gyro Calibrating. Do Not Move!");
        gyro.calibrate();
        for (DcMotor motor : motorArr) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        if (!isTestChassis) {
            setLiftMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            zType.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        calibrationTimer.reset();
        while (!m.isStopRequested() && gyro.isCalibrating()) {
            tel.addData("Calibrating", "%s", Math.round(calibrationTimer.seconds()) % 2 == 0 ? "|.." : "..|");
            tel.update();
            sleep(50);
        }
        tel.log().add("Gyro calibration complete");
        initialCompassHeading = Math.toRadians(compass.getDirection());
        gyroError = 0;
        msSinceMoved = 0;
        tel.log().add("Compass heading locked");
        tel.update();
        for (DcMotor motor : motorArr) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        if (!isTestChassis) {
            setLiftMode(DcMotor.RunMode.RUN_TO_POSITION);
            zType.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    /**
     * Sets runMode to RUN_USING_ENCODER for all drivetrain motors
     *
     * @see DcMotor.RunMode
     */
    public void adjustMotorEncoders(DcMotor.RunMode r) {
        if (frontRight.getMode() == r) {
            return; // It's already in a position we like
        }
        for (DcMotor m : motorArr) {
            m.setMode(r);
        }
    }

    /**
     * Takes an array of motor speeds, and then uses
     * built-in PID control to run motors at those speeds
     *
     * @param speeds   The speeds for the motors to run at, between -1.0 and 1.0.
     *                 Motor order is front left, front right, back left, back right.
     */
    public void setMotorSpeeds(double[] speeds) {
        for (int i = 0; i < motorArr.length; i++) {
            motorArr[i].setPower(clamp(speeds[i]));
        }
    }

    /**
     * Clamps inputted value between -1.0 and 1.0, for use with DcMotor.setPower
     *
     * @param val   The value to be clamped
     * @return      The clamped value as a double between -1.0 and 1.0
     * @see          DcMotor
     */
    public static double clamp(double val) {
        return Math.max(-1, Math.min(1, val));
    }

    /**
     * Intelligently waits a given number of milliseconds. If program is terminated, sleeping
     * will end immediately.
     *
     * @param milliseconds   The amount of time to wait
     */
    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Intelligently waits a given number of milliseconds. If program is terminated, sleeping
     * will end immediately.
     *
     * @param g   The amount of time to wait
     */
    public void writeLogTick(Gamepad g) {
        // Logging
        if (logPosition < hz * secondsToTrack) {
            log[logPosition] =
                    new LogTick(g, this, motorArr);
            logPosition += 1;

            if (logPosition == hz * secondsToTrack) {
                writeLogFile();
            }
        }
    }

    /**
     * Most op modes are set to run a certain number of times per second. waitForTick() is
     * called at the end of an op mode, in order to perform clean-up steps. It sets msSinceLastMoved,
     * updates the gyroscope error with respect to the compass, and then sleeps for the requested
     * amount of time.
     *
     * @param periodMs   The amount of time to wait
     */
    public void waitForTick(long periodMs) {
        boolean moving = false;
        for (DcMotor m : motorArr) {
            if (m.getPower() != 0) {
                moving = true;
                break;
            }
        }
        if (moving) {
            msSinceMoved = 0;
        } else {
            msSinceMoved += (1000 / hz);
        }

        double newGyroError = getGyroHeading() - getCompassHeading();
        if (msSinceMoved > msMagFieldKill) {
            if (Math.abs(gyroError - newGyroError) < absurdValueFiltering) {
                gyroError = newGyroError;
            } // Otherwise, value is considered absurd and is thrown out
        }
        gyroError += headingAdjuster.getHeadingAdjustments();

        long remaining = periodMs - (long) period.milliseconds();

        // Sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }

    /**
     * Writes the current logs out to a file
     *
     */
    public void writeLogFile() {
        final File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                Environment.DIRECTORY_DOCUMENTS + "/NullbotLogs/"
                        );
        if (!path.exists()) {path.mkdirs();}

        String DTS = DateFormat.getDateTimeInstance().format(new Date());
        final File file = new File(path, "Nullbot-log-" + DTS + ".txt");
        try
        {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter oW = new OutputStreamWriter(fOut);

            oW.append("[");
            for (int i = 0; i < log.length; i++) {
                oW.append(log[i].toString());
                if (i+1 < log.length) {oW.append(",");}
            }
            oW.append("]");

            oW.close();
            fOut.flush();
            fOut.close();
        }
        catch (IOException e)
        {}

    }

    /**
     * Translates inputted angle between 0 and 2 pi radians
     *
     * @param a   The angle to be translated
     * @return    The translated angle
     */
    public double normAngle(double a) {
        a = a % (Math.PI * 2);

        if (a < 0) {
            a += Math.PI * 2;
        }
        return a;
    }

    /**
     * @return    The current raw gyroscope heading, in radians between -pi and pi
     */
    public double getGyroHeadingRaw() {
        return Math.toRadians(gyro.getHeading());
    }

    /**
     * @return    The current compass heading, in radians (-pi to pi) relative to starting position
     */
    public double getCompassHeading() {
        return normAngle(Math.toRadians(compass.getDirection()) - initialCompassHeading);
    }

    /**
     *
     * @param d1  The first angle
     * @param d2  The second angle
     * @return    The difference between the angles, between 0 and 2Pi
     */
    public static double getAngleDifference(double d1, double d2) {
        double diff = d2 - d1;
        if (d1 > Math.PI) {d1 -= 2 * Math.PI;}
        if (d2 > Math.PI) {d2 -= 2 * Math.PI;}

        double diff2 = d2 - d1;

        if (Math.abs(diff) < Math.abs(diff2)) {
            return diff;
        } else {
            return diff2;
        }
    }
    /**
     * @return    The error adjusted gyro position, between 0 and 2pi radians
     */
    public double getGyroHeading() {
        return getGyroHeadingRaw() - gyroError;
    }

    public void raiseWhipSnake() {
        raiseLeftWhipSnake();
        raiseRightWhipSnake();
    }
    public void lowerWhipSnake() {
        lowerLeftWhipSnake();
        lowerRightWhipSnake();
    }
    public void lowerLeftWhipSnake() {leftWhipSnake.setPosition(214.0/255.0);}
    public void raiseLeftWhipSnake() {leftWhipSnake.setPosition(36.0/255.0);}
    public void lowerRightWhipSnake() {rightWhipSnake.setPosition(15.0/255.0);}
    public void raiseRightWhipSnake() {rightWhipSnake.setPosition(185.0/255.0);}


    public void openBlockClaw() {
        leftBlockClaw.setPosition(55.0/255.0);
        rightBlockClaw.setPosition(210.0/255.0);
    }

    public void closeBlockClaw() {
        leftBlockClaw.setPosition(105.0/255.0);
        rightBlockClaw.setPosition(160.0/255.0);
    }

    public final double RELIC_CLAW_OPEN_POSITION = 100.0/255.0;
    public final double RELIC_CLAW_CLOSED_POSITION = 0.0/255.0;
    public boolean RELIC_CLAW_IS_OPEN = false;

    public void openRelicClaw() {relicClaw.setPosition(RELIC_CLAW_OPEN_POSITION); RELIC_CLAW_IS_OPEN = true;}
    public void closeRelicClaw() {relicClaw.setPosition(RELIC_CLAW_CLOSED_POSITION); RELIC_CLAW_IS_OPEN = false;}

    public void toggleRelicClaw() {
        if (RELIC_CLAW_IS_OPEN) {
            closeRelicClaw();
        } else {
            openRelicClaw();
        }
    }

    public double relicFipperPosition = 80;
    public final double RELIC_CLAW_FLIPPER_EXTENDED_POSITION = 74.0/255.0;
    public final double RELIC_CLAW_FLIPPER_RETRACTED_POSITION = 63.0/255.0;

    public void extendFlipper() {
        relicClawFlipper.setPosition(RELIC_CLAW_FLIPPER_EXTENDED_POSITION);
        relicFipperPosition = RELIC_CLAW_FLIPPER_EXTENDED_POSITION;
    }

    public void retractFlipper() {
        relicClawFlipper.setPosition(RELIC_CLAW_FLIPPER_RETRACTED_POSITION);
        relicFipperPosition = RELIC_CLAW_FLIPPER_RETRACTED_POSITION;
    }

    public void toggleRelicClawFlipper() {
        if (relicFipperPosition == RELIC_CLAW_FLIPPER_EXTENDED_POSITION) {
            retractFlipper();
        } else {
            extendFlipper();
        }
    }

    public void updateFlipperPos() {
        relicClawFlipper.setPosition(relicFipperPosition);
    }

    public void setLiftMode(DcMotor.RunMode m) {
        lift.setMode(m);
    }

    public void setDriveMode(DcMotor.RunMode mode) {
        for (DcMotor motor : motorArr) {
            if (motor.getMode() != mode) {
                motor.setMode(mode);
            }
        }
    }

    public void setMotorMode(DcMotor m, DcMotor.RunMode mode) {
        if (m.getMode() != mode) {
            m.setMode(mode);
        }
    }

}

