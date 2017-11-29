package org.chathamrobotics.common.systems;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.GyroSensor;

import org.chathamrobotics.common.hardware.utils.HardwareListener;
import org.chathamrobotics.common.robot.RobotLogger;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * Created by carsonstorm on 11/28/2017.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class GyroHandler implements System {
    // CONSTANTS
    public static final double DEFAULT_TOLERANCE = 15;

    private static final HardwareListener.HardwareCondition<GyroSensor> FINISHED_CALIBRATING =
            gyro -> ! gyro.isCalibrating();

    // CLASS METHODS & FIELDS

    /**
     * Finds the angle relative to the reference angle
     * (transforming the angles so that the reference angle is at 0)
     * @param referenceAngle    the reference angle in radians
     * @param angle             the angle to transform in radians
     * @return                  the relative angle in radians
     */
    public static double relativeAngle(double referenceAngle, double angle) {
        return relativeAngle(referenceAngle, angle, AngleUnit.RADIANS);
    }

    /**
     * Finds the angle relative to the reference angle
     * (transforming the angles so that the reference angle is at 0)
     * @param referenceAngle    the reference angle
     * @param angle             the angle to transform
     * @param angleUnit         the unit of measure for angles
     * @return                  the relative angle
     */
    public static double relativeAngle(double referenceAngle, double angle, AngleUnit angleUnit) {
        return Math.abs(angle - referenceAngle + angleUnit.fromDegrees(360)) % angleUnit.fromDegrees(360);
    }

    // INSTANCE

    private final GyroSensor gyro;
    private final HardwareListener listener;
    private final RobotLogger logger;

    private double tolerance = DEFAULT_TOLERANCE;
    private int initialHeading;

    private boolean isCalibrated;
    private boolean isInitialized;

    private boolean isInitializing;

    /**
     * Creates a new instance of {@link GyroHandler}
     * @param gyro      the gyro sensor
     * @param listener  the hardware listener
     * @param logger    the robot logger
     */
    public GyroHandler(@NonNull GyroSensor gyro, @NonNull HardwareListener listener, @NonNull RobotLogger logger) {
        this.gyro = gyro;
        this.listener = listener;
        this.logger = logger;
    }

    // Getters and Setters

    /**
     * Sets the gyro handler's tolerance to differences in angles
     * @param tolerance the tolerance to set
     */
    public void setTolerance(double tolerance) {this.tolerance = tolerance;}

    /**
     * Returns the gyro handler's current tolerance
     * @return the gyro handler's current tolerance
     */
    public double getTolerance() {return  this.tolerance;}

    /**
     * Gets the gyro's heading in radians
     * @return  the gyro's heading in radians
     */
    public double getHeading() {
        return getHeading(AngleUnit.RADIANS);
    }

    /**
     * Get's the gyro's heading
     * @param angleUnit the angle unit to give the heading in
     * @return          the gyro's heading
     */
    public double getHeading(AngleUnit angleUnit) {
        return angleUnit.fromDegrees(gyro.getHeading());
    }

    /**
     * Gets the heading relative to the initial heading
     * @return          the relative heading in radians
     */
    public double getRelativeHeading() {return getRelativeHeading(initialHeading, AngleUnit.RADIANS);}

    /**
     * Gets the heading relative to the initial heading
     * @param angleUnit the unit of measure for angles
     * @return          the relative heading
     */
    public double getRelativeHeading(AngleUnit angleUnit) {return  getRelativeHeading(initialHeading, angleUnit);}

    /**
     * Gets the heading relative to the reference heading
     * @param reference the reference angle in radians
     * @return          the relative heading in radians
     */
    public double getRelativeHeading(double reference) {return getRelativeHeading(reference, AngleUnit.RADIANS);}

    /**
     * Gets the heading relative to the reference heading
     * @param reference the reference angle
     * @param angleUnit the unit of measure for angles
     * @return          the relative heading
     */
    public double getRelativeHeading(double reference, AngleUnit angleUnit) {
        return relativeAngle(reference, angleUnit.fromDegrees(gyro.getHeading()), angleUnit);
    }

    // Behavior

    /**
     * Calibrates the gyro
     */
    public void calibrate() {calibrate(null);}

    /**
     * Calibrates the gyro and calls the callback when it's finished
     * @param callback  called when the calibration is finished
     */
    public void calibrate(Runnable callback) {
        logger.debug("Starting Calibration");
        if (gyro.isCalibrating()) return;

        gyro.calibrate();

        listener.once(gyro, FINISHED_CALIBRATING, () -> {
            isCalibrated = true;
            logger.debug("Finished Calibration");

            if (callback != null) callback.run();
        });
    }

    /**
     * Calibrates the gyro synchronously (blocks the thread)
     * @throws InterruptedException thrown if the thread is interrupted while calibrating
     */
    public void calibrateSync() throws InterruptedException {
        if (gyro.isCalibrating()) {
            waitForCalibration();
            return;
        }

        logger.debug("Starting Calibration");
        gyro.calibrate();

        waitForCalibration();

        logger.debug("Finished Calibration");
    }

    /**
     * Blocks the thread until the calibration is finished
     * @throws InterruptedException thrown if the thread is interrupted while waiting
     */
    public void waitForCalibration() throws InterruptedException {
        while(! isCalibrated) Thread.sleep(10);
    }

    /**
     * Returns true if the gyro is currently calibrating
     * @return  true if the gyro is currently calibrating
     */
    public boolean isCalibrating() {return gyro.isCalibrating();}

    /**
     * Returns true if the gyro has been calibrated
     * @return  true if the gyro has been calibrated
     */
    public boolean isCalibrated() {return isCalibrated;}

    /**
     * Initializes the gyro
     */
    public void init() {init(null);}

    /**
     * Initializes the gyro and calls the callback when finished
     * @param callback  called when initialization is finished
     */
    public void init(Runnable callback) {
        if (isInitializing) return;

        isInitializing = true;
        logger.debug("Starting Initialization");

        calibrate(() -> {
            initialHeading = gyro.getHeading();
            isInitialized = true;
            isInitializing = false;
            logger.debug("Finished Initialization");

            if (callback != null) callback.run();
        });
    }

    /**
     * Initializes the gyro synchronously (blocks thread)
     * @throws InterruptedException thrown if the thread is interrupted while initializing
     */
    public void initSync() throws InterruptedException {
        if (isInitializing) {
           waitForInit();
           return;
        }

        logger.debug("Starting Initialization");
        isInitializing = true;

        calibrateSync();

        initialHeading = gyro.getHeading();
        isInitialized = true;
        isInitializing = false;
        logger.debug("Finished Initialization");
    }

    /**
     * Blocks the thread until the initialization is finished
     * @throws InterruptedException thrown if the thread is interrupted while waiting
     */
    public void waitForInit() throws InterruptedException {
        while(isInitializing) Thread.sleep(10);
    }

    /**
     * Returns true if the gyro is currently initializing
     * @return  true if the gyro is currently initializing
     */
    public boolean isInitializing() {return isInitializing;}

    /**
     * Returns true if the gyro handler has been initialized
     * @return  true if the gyro handler has been initialized
     */
    public boolean isInitialized() {return isInitialized;}

    @Override
    public void stop() {

    }
}
