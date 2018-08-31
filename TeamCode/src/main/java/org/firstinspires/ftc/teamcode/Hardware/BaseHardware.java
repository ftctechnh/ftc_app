package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Utilities.Startup.Alliance;


// This is NOT an opmode

public class BaseHardware {

    // Sensors
    public BNO055IMU imu;
    public Orientation angles; // Used to read IMU

    // Telemetry
    public Telemetry tel;
    public long currentTick;

    // Utility mechanisms
    public DcMotor[] motorArr;
    public LinearOpMode opMode;
    public HardwareMap hwMap;
    public Alliance color;
    ElapsedTime period = new ElapsedTime();
    boolean initialized = false;

    /*File file; // Our logfile
    FileOutputStream fOut;
    OutputStreamWriter oW;
    LinearOpMode opMode;
    ScheduledExecutorService loggerThread;*/


    public BaseHardware(LinearOpMode opMode) {
        this.opMode = opMode;
        tel = opMode.telemetry;
        hwMap = opMode.hardwareMap;
        currentTick = 0;
    }

    public void init() {
        imu = opMode.hardwareMap.get(BNO055IMU.class, "primaryIMU");
        calibrateGyro();
        initialized = true;
    }

    private void calibrateGyro() {
        // Calibration
        tel.log().add("Gyro Calibrating. Do Not Move!!");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.RADIANS;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu.initialize(parameters);
        sleep(100);
        updateReadings();
        tel.log().add("Gyro Calibration Complete.");

    }

    void resetMotorEncoders() {
        for (DcMotor motor : motorArr) {
            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        sleep(100);
        for (DcMotor motor : motorArr) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
    }

    public void updateReadings() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
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
}

