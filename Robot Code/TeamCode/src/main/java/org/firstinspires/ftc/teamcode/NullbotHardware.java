package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cCompassSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDeviceInterfaceModule;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbLegacyModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

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

    public DcMotor liftLeft;
    public DcMotor liftRight;
    public DcMotor zType;

    // Servos
    public Servo leftWhipSnake;
    public Servo rightWhipSnake;
    public Servo leftBlockClaw;
    public Servo rightBlockClaw;

    // Sensors
    public ModernRoboticsI2cGyro gyro;
    public ModernRoboticsI2cCompassSensor compass;
    public ManualHeadingAdjustmentController headingAdjuster;
    public ModernRoboticsUsbDeviceInterfaceModule deviceInterface;

    // Adjustment gamepad
    private Gamepad gp2;

    // Telemetry
    public Telemetry tel;

    // Utility mechanisms
    public DcMotor[] motorArr;
    public LogTick[] log;
    public int hz = 25;
    public int secondsToTrack = 60;
    public int logPosition = 0;
    double initialCompassHeading;
    double gyroError;
    int msMagFieldKill = 3000; // Robot must stay still for half a second before it is assumed the
                              // motors are not generating any magnetic field

    double absurdValueFiltering = (Math.PI * 2) / 180; // Any attempt to alter the gyroscope's error
                            // by more than this threshold will be denied

    int msSinceMoved;
    int inducedGyroError;

    // Local vars
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    public NullbotHardware(){}
    private void init() {
        deviceInterface = hwMap.get(ModernRoboticsUsbDeviceInterfaceModule.class, "deviceInterface");

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

        if (!isTestChassis) {
            liftLeft = hwMap.dcMotor.get("liftLeft");
            liftRight = hwMap.dcMotor.get("liftRight");

            zType = hwMap.dcMotor.get("zType");

            leftWhipSnake = hwMap.servo.get("leftGemHitter");
            rightWhipSnake = hwMap.servo.get("rightGemHitter");

            leftBlockClaw = hwMap.servo.get("leftClaw");
            rightBlockClaw = hwMap.servo.get("rightClaw");

            raiseWhipSnake();
            openBlockClaw();
        }

        gyro = hwMap.get(ModernRoboticsI2cGyro.class, "gyro");
        compass = hwMap.get(ModernRoboticsI2cCompassSensor.class, "acc");

        // Set color

        deviceInterface.setDigitalChannelMode(0, DigitalChannel.Mode.INPUT);
        boolean zeropin = deviceInterface.getDigitalChannelState(0);
        if (zeropin) {
            color = Alliance.BLUE;
        } else {
            color = Alliance.RED;
        }

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
        inducedGyroError = 0;
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
    public void enableMotorEncoders() {
        for (DcMotor m : motorArr) {
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
        boolean[] resetMotors = new boolean[motorArr.length];

        for (int i = 0; i < motorArr.length; i++) {
            double finalPower = clamp(speeds[i]);

            if (Math.abs(finalPower) + 0.1 < Math.abs(motorArr[i].getPower())) {
                resetMotors[i] = true;
            } else {
                resetMotors[i] = false;
            }
            motorArr[i].setPower(finalPower);
        }

        for (int i = 0; i < motorArr.length; i++) {
            if (resetMotors[i]) {
                motorArr[i].setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
        }
        sleep(1);
        for (int i = 0; i < motorArr.length; i++) {
            if (resetMotors[i]) {
                motorArr[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
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
     * @return    The error adjusted gyro position, between 0 and 2pi radians
     */
    public double getGyroHeading() {
        return getGyroHeadingRaw() - gyroError;
    }

    public void raiseWhipSnake() {

        if (color == Alliance.BLUE) {
            rightWhipSnake.setPosition(180.0/255.0);
        } else {
            leftWhipSnake.setPosition(75.0/255.0); // This position is incorrect
        }
    }
    public void lowerWhipSnake() {
        if (color == Alliance.BLUE) {
            rightWhipSnake.setPosition(30.0/255.0);
        } else {
            leftWhipSnake.setPosition(225.0/255.0); // This position is incorrect
        }
    }

    public void openBlockClaw() {
        leftBlockClaw.setPosition(55.0/255.0);
        rightBlockClaw.setPosition(210.0/255.0);
    }

    public void closeBlockClaw() {
        leftBlockClaw.setPosition(105.0/255.0);
        rightBlockClaw.setPosition(160.0/255.0);
    }

    public void setLiftMode(DcMotor.RunMode m) {
        liftLeft.setMode(m);
        liftRight.setMode(m);
    }
}

