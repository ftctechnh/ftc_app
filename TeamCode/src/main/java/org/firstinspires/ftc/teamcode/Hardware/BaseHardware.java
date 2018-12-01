package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataResponse;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Utilities.Logging.LogQueue;
import org.firstinspires.ftc.teamcode.Utilities.Startup.Alliance;

import java.io.IOException;

public class BaseHardware {

    public static final BNO055IMU.Parameters metricParameters = new BNO055IMU.Parameters();
    static {
        metricParameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        metricParameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
    }

    // Sensors
    public LynxModule leftHub;
    public BNO055IMU primaryIMU;
    public Orientation angles; // Used to read IMU
    public LynxGetBulkInputDataResponse bulkDataResponse;

    // Telemetry
    public Telemetry tel;
    public long currentTick;

    // Utility mechanisms
    public DcMotor[] motorArr;
    public LinearOpMode opMode;
    public HardwareMap hwMap;
    public Alliance color;
    ElapsedTime period = new ElapsedTime();
    LogQueue logger;


    public BaseHardware(LinearOpMode opMode) {
        this.opMode = opMode;
        tel = opMode.telemetry;
        hwMap = opMode.hardwareMap;
        currentTick = 0;
        try {
            logger = new LogQueue(this);
        } catch (IOException e) {
            tel.log().add("Recieved 'IOException'");
            tel.log().add(e.toString());
        } catch (NoSuchFieldException e) {
            tel.log().add("Recieved 'NoSuchFieldException'");
        }

        leftHub = hwMap.get(LynxModule.class, "leftHub");
        primaryIMU = hwMap.get(BNO055IMU.class, "primaryIMU");

    }

    public void calibrate() {
        // Calibration
        tel.log().add("Gyro Calibrating. Do Not Move!!");

        primaryIMU.initialize(metricParameters);
        sleep(100);
        updateReadings();
        tel.log().add("Gyro Calibration Complete.");
    }

    public void updateREVHubReadings() {
        LynxGetBulkInputDataCommand bulkDataInput = new LynxGetBulkInputDataCommand(leftHub);
        try {
            bulkDataResponse = bulkDataInput.sendReceive();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (LynxNackException e) {
            e.printStackTrace();
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
            Thread.sleep(Math.abs(milliseconds));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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

        currentTick += 1;

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
        updateReadings();
        try {
            logger.update();
        } catch (IllegalAccessException e) {
            tel.log().add("Recieved 'IllegalAccessException'");
        }
    }

    public void updateReadings() {
        angles = primaryIMU.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
    }

    /**
     * Translates inputted angle between 0 and 2 pi radians
     *
     * @param a   The angle to be translated
     * @return    The translated angle from 0 to 2 pi
     */
    public static double normAngle(double a) {
        a = a % (Math.PI * 2);

        if (a < 0) {
            a += Math.PI * 2;
        }
        return a;
    }

    /**
     *
     * @param d1  The first angle
     * @param d2  The second angle
     * @return    The difference between the angles, between 0 and 2Pi
     */



    public static double getAngleDifference(double d1, double d2) {
        double diff = Math.abs(d2 - d1);
        diff = diff % (Math.PI * 2);
        return diff > Math.PI ? (Math.PI * 2) - diff : diff;
    }

    public double getSignedAngleDifference(double d1, double d2) {
        double zeroed = normAngle(d2 - d1);

        if (zeroed >= Math.PI) { // If it's in the pI to 2PI range
            return -((Math.PI * 2) - zeroed);
        } else {
            return zeroed;
        }

    }
    /**
     * @return    The error adjusted gyro position, between 0 and 2pi radians
     */
    public double getGyroHeading() {
        return normAngle(angles.firstAngle);
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

    public void stop() {
        logger.shutdown();
    }

    public void updateLogs() {
        if (bulkDataResponse == null) {
            updateREVHubReadings();
        }
        try {
            logger.update();
        } catch (IllegalAccessException e) {
            tel.log().add("Recieved 'IllegalAccessException'");
        }
        bulkDataResponse = null; // Reset it
    }
}

