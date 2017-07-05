package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cCompassSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDeviceInterfaceModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.Date;

import static com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor.Command;

// This is NOT an opmode

public class NullbotHardware {
    // Motors
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    // Sensors
    public ModernRoboticsI2cGyro gyro;
    public ModernRoboticsI2cCompassSensor compass;
    public ModernRoboticsI2cColorSensor leftLineColor;
    public ModernRoboticsI2cColorSensor rightLineColor;
    public ModernRoboticsUsbDeviceInterfaceModule deviceInterface;

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
        // Motor initialization
        frontLeft = hwMap.dcMotor.get("frontLeft");
        frontRight = hwMap.dcMotor.get("frontRight");
        backLeft = hwMap.dcMotor.get("backLeft");
        backRight = hwMap.dcMotor.get("backRight");
        gyro = hwMap.get(ModernRoboticsI2cGyro.class, "gyro");
        compass = hwMap.get(ModernRoboticsI2cCompassSensor.class, "acc");
        leftLineColor = hwMap.get(ModernRoboticsI2cColorSensor.class, "leftLineColor");
        rightLineColor = hwMap.get(ModernRoboticsI2cColorSensor.class, "rightLineColor");
        deviceInterface = hwMap.get(ModernRoboticsUsbDeviceInterfaceModule.class, "deviceInterface");

        leftLineColor.writeCommand(Command.ACTIVE_LED);
        rightLineColor.writeCommand(Command.ACTIVE_LED);

        compass.setMode(CompassSensor.CompassMode.MEASUREMENT_MODE);


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

    public void init(HardwareMap ahwMap, LinearOpMode oM, boolean teleoperated) {
        hwMap = ahwMap;
        tel = oM.telemetry;
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
    }

    public void enableMotorEncoders() {
        for (DcMotor m : motorArr) {
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
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

    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

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
    public void writeLogFile() {
        tel.log().add("Gyro calibration complete");
        tel.update();
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
        {
        }

    }
    public double normAngle(double a) {
        // Takes in an angle in radians, outputs it as an angle between 0 and tau
        a = a % (Math.PI * 2);

        if (a < 0) {
            a += Math.PI * 2;
        }
        return a;
    }
    public double getGyroHeadingRaw() {
        return Math.toRadians(gyro.getHeading());
    }
    public double getCompassHeading() {
        return normAngle(Math.toRadians(compass.getDirection()) - initialCompassHeading);
    }
    public double getGyroHeading() {
        return getGyroHeadingRaw() - gyroError;
    }
}

