package org.chathamrobotics.common.systems;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.chathamrobotics.common.hardware.utils.HardwareListener;
import org.chathamrobotics.common.robot.Robot;
import org.chathamrobotics.common.robot.RobotLogger;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue"})
public class GyroHandler implements System {
    // CONSTANTS
    public static final double DEFAULT_TOLERANCE = 15;

    private static final HardwareListener.HardwareCondition<GyroSensor> FINISHED_CALIBRATING =
            gyro -> ! gyro.isCalibrating();
    private static final String TAG = GyroHandler.class.getSimpleName();
    private static final double TWO_PIE = 2 * Math.PI;

    // CLASS METHODS & FIELDS

    public enum GyroOrientation {
        UPSIDE_UP,
        UPSIDE_DOWN
    }

    public interface Update<T> {
        void update(T value);
    }

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
        double fullCircle = angleUnit == AngleUnit.DEGREES ? 360 : TWO_PIE;
        return Math.abs(angle - referenceAngle + fullCircle) % fullCircle;
    }

    /**
     * Finds the angular displacement (the signed shortest distance between two angles. negative for clockwise)
     * @param initialAngle  the initial angle in radians
     * @param finalAngle    the final angle in radians
     * @return              the angular displacement in radians
     */
    public static double angularDisplacement(double initialAngle, double finalAngle) {
        return  angularDisplacement(initialAngle, finalAngle, AngleUnit.RADIANS);
    }

    /**
     * Finds the angular displacement (the signed shortest distance between two angles. negative for clockwise)
     * @param initialAngle  the initial angle
     * @param finalAngle    the final angle
     * @param angleUnit     the unit of measure for the angles
     * @return              the angular displacement
     */
    public static double angularDisplacement(double initialAngle, double finalAngle, @NonNull AngleUnit angleUnit) {
        double fullCircle = angleUnit == AngleUnit.DEGREES ? 360 : TWO_PIE;
        double displacement = Math.abs(finalAngle - initialAngle + fullCircle / 2) % fullCircle - fullCircle / 2;
        return displacement == -fullCircle/2 ? fullCircle / 2 : displacement;
    }

    /**
     * Builds a new  instance of {@link GyroHandler}
     * @param robot the robot to build the {@link GyroHandler} from
     * @return      a built {@link GyroHandler}
     */
    public static GyroHandler build(Robot robot) {
        return build(robot.getHardwareMap(), robot, robot.log.systemLogger(TAG));
    }

    /**
     * Builds a new  instance of {@link GyroHandler}
     * @param hardwareMap   the hardware map for the robot
     * @param listener      the hardware listener
     * @param logger        the robot logger
     * @return              a built {@link GyroHandler}
     */
    public static GyroHandler build(HardwareMap hardwareMap, HardwareListener listener, RobotLogger logger) {
        return new GyroHandler(
                hardwareMap.gyroSensor.get("Gyro"),
                listener,
                logger
        );
    }

    // INSTANCE

    private final GyroSensor gyro;
    private final HardwareListener listener;
    private final RobotLogger logger;

    // state
    private GyroOrientation orientation = GyroOrientation.UPSIDE_UP;
    private double tolerance = DEFAULT_TOLERANCE;
    private double referenceHeading;

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
     * Returns the gyro used by the gyro handler
     * @return the gyro used by the gyro handler
     */
    public GyroSensor getGyro() {return gyro;}

    /**
     * Sets the gyro's orientation
     * @param orientation   the orientation to be set
     */
    public void setOrientation(@NonNull GyroOrientation orientation) {this.orientation = orientation;}

    /**
     * Returns the gyro's orientation
     * @return  the gyro's orientation
     */
    public GyroOrientation getOrientation() {return this.orientation;}

    /**
     * Sets the gyro handler's tolerance to differences in angles
     * @param tolerance the tolerance to be set in radians
     */
    public void setTolerance(double tolerance) {setTolerance(tolerance, AngleUnit.DEGREES);}

    /**
     * Sets the gyro handler's tolerance to differences in angles
     * @param tolerance the tolerance to be set
     * @param angleUnit the unit of measure for the angles
     */
    public void setTolerance(double tolerance, @NonNull AngleUnit angleUnit) {
        this.tolerance = angleUnit.toDegrees(tolerance);
    }

    /**
     * Returns the gyro handler's current tolerance in radians
     * @return the gyro handler's current tolerance in radians
     */
    public double getTolerance() {return getTolerance(AngleUnit.RADIANS);}

    /**
     * Returns the gyro handler's current tolerance
     * @return the gyro handler's current tolerance
     */
    public double getTolerance(AngleUnit angleUnit) {
        return angleUnit.fromDegrees(tolerance);
    }

    /**
     * Sets the reference heading
     * @param referenceHeading  the reference heading to set in radians
     */
    public void setReferenceHeading(double referenceHeading) {setReferenceHeading(referenceHeading, AngleUnit.RADIANS);}

    /**
     * Sets the reference heading
     * @param referenceHeading  the reference heading to set
     * @param angleUnit         the unit of measure to use for the angle
     */
    public void setReferenceHeading(double referenceHeading, AngleUnit angleUnit) {
        this.referenceHeading = angleUnit.toDegrees(referenceHeading);
    }

    /**
     * Returns the reference heading in radians
     * @return  the reference heading in radians
     */
    public double getReferenceHeading() {return getReferenceHeading(AngleUnit.RADIANS);}

    /**
     * Returns the reference heading
     * @return  the reference heading
     */
    public double getReferenceHeading(AngleUnit angleUnit) {
        return angleUnit.fromDegrees(referenceHeading);
    }

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
    public double getHeading(@NonNull AngleUnit angleUnit) {
        if (orientation == GyroOrientation.UPSIDE_UP)
            return angleUnit.fromDegrees(gyro.getHeading());

        return angleUnit.fromDegrees(Math.abs(gyro.getHeading() - 360));
    }

    /**
     * Gets the heading relative to the reference heading
     * @return          the relative heading in radians
     */
    public double getRelativeHeading() {return getRelativeHeading(referenceHeading, AngleUnit.RADIANS);}

    /**
     * Gets the heading relative to the reference heading
     * @param angleUnit the unit of measure for angles
     * @return          the relative heading
     */
    public double getRelativeHeading(@NonNull AngleUnit angleUnit) {return  getRelativeHeading(referenceHeading, angleUnit);}

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
    public double getRelativeHeading(double reference, @NonNull AngleUnit angleUnit) {
        return relativeAngle(reference, getHeading(angleUnit), angleUnit);
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
            referenceHeading = getHeading(AngleUnit.DEGREES);
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

        referenceHeading = getHeading(AngleUnit.DEGREES);
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

    /**
     * Returns true if the difference between the target heading and current heading is within the tolerance
     * @param targetHeading the desired heading in radians
     * @return              whether or not the current heading is approximately equal to the target heading
     */
    public boolean isAtHeading(double targetHeading) {
        return isAtHeading(targetHeading, Math.toRadians(tolerance), AngleUnit.RADIANS);
    }

    /**
     * Returns true if the difference between the target heading and current heading is within the tolerance
     * @param targetHeading the desired heading in radians
     * @param tolerance     the tolerance to allow for in differences between angles in radians
     * @return              whether or not the current heading is approximately equal to the target heading
     */
    public boolean isAtHeading(double targetHeading, double tolerance) {
        return isAtHeading(targetHeading, tolerance, AngleUnit.RADIANS);
    }

    /**
     * Returns true if the difference between the target heading and current heading is within the tolerance
     * @param targetHeading the desired heading
     * @param angleUnit     the unit of measure to use for the angles
     * @return              whether or not the current heading is approximately equal to the target heading
     */
    public boolean isAtHeading(double targetHeading, @NonNull AngleUnit angleUnit) {
        return isAtHeading(targetHeading, angleUnit.fromDegrees(tolerance), angleUnit);
    }

    /**
     * Returns true if the difference between the target heading and current heading is within the tolerance
     * @param targetHeading the desired heading
     * @param tolerance     the tolerance to allow for in differences between angles
     * @param angleUnit     the unit of measure to use for the angles
     * @return              whether or not the current heading is approximately equal to the target heading
     */
    public boolean isAtHeading(double targetHeading, double tolerance, @NonNull AngleUnit angleUnit) {
        return Math.abs(angularDisplacement(getRelativeHeading(angleUnit), targetHeading, angleUnit)) <= tolerance;
    }

    /**
     * Calls the callback once the gyro heading is approximately equal to the target heading
     * @param targetHeading the target heading in radians
     * @param callback      the callback
     */
    public void onceAtHeading(double targetHeading, Runnable callback) {
        onceAtHeading(targetHeading, Math.toRadians(tolerance), AngleUnit.RADIANS, callback);
    }

    /**
     * Calls the callback once the gyro heading is approximately equal to the target heading
     * @param targetHeading the target heading in radians
     * @param tolerance     the tolerance to allow for in differences between angles in radians
     * @param callback      the callback
     */
    public void onceAtHeading(double targetHeading, double tolerance, @NonNull Runnable callback) {
        onceAtHeading(targetHeading, tolerance, AngleUnit.RADIANS, callback);
    }

    /**
     * Calls the callback once the gyro heading is approximately equal to the target heading
     * @param targetHeading the target heading
     * @param angleUnit     the unit of measure to use for the angles
     * @param callback      the callback
     */
    public void onceAtHeading(double targetHeading, @NonNull AngleUnit angleUnit, @NonNull Runnable callback) {
        onceAtHeading(targetHeading, angleUnit.fromDegrees(tolerance), angleUnit, callback);
    }

    /**
     * Calls the callback once the gyro heading is approximately equal to the target heading
     * @param targetHeading the target heading
     * @param tolerance     the tolerance to allow for in differences between angles
     * @param angleUnit     the unit of measure to use for the angles
     * @param callback      the callback
     */
    public void onceAtHeading(double targetHeading, double tolerance, @NonNull AngleUnit angleUnit, @NonNull Runnable callback) {
        listener.once(gyro, gyro -> isAtHeading(targetHeading, tolerance, angleUnit), callback);
    }

    /**
     * Calls the callback whenever the gyro heading is approximately equal to the target heading
     * @param targetHeading the target heading in radians
     * @param callback      the callback
     */
    public void onAtHeading(double targetHeading, @NonNull Runnable callback) {
        onAtHeading(targetHeading, Math.toRadians(tolerance), AngleUnit.RADIANS, callback);
    }

    /**
     * Calls the callback whenever the gyro heading is approximately equal to the target heading
     * @param targetHeading the target heading in radians
     * @param tolerance     the tolerance to allow for in differences between angles in radians
     * @param callback      the callback
     */
    public void onAtHeading(double targetHeading, double tolerance, @NonNull Runnable callback) {
        onAtHeading(targetHeading, tolerance, AngleUnit.RADIANS, callback);
    }

    /**
     * Calls the callback whenever the gyro heading is approximately equal to the target heading
     * @param targetHeading the target heading
     * @param angleUnit     the tolerance to allow for in differences between angles
     * @param callback      the callback
     */
    public void onAtHeading(double targetHeading, @NonNull AngleUnit angleUnit, @NonNull Runnable callback) {
        onAtHeading(targetHeading, angleUnit.fromDegrees(tolerance), angleUnit, callback);
    }

    /**
     * Calls the callback whenever the gyro heading is approximately equal to the target heading
     * @param targetHeading the target heading
     * @param tolerance     the tolerance to allow for in differences between angles
     * @param angleUnit     the unit of measure to use for the angles
     * @param callback      the callback
     */
    public void onAtHeading(double targetHeading, double tolerance, @NonNull AngleUnit angleUnit, @NonNull Runnable callback) {
        listener.on(gyro, gyro -> isAtHeading(targetHeading, tolerance, angleUnit), callback);
    }

    /**
     * Calls the update function repeatedly with the current angular displacement in radians.
     * @param targetHeading the target heading in radians
     * @param update        the update function
     */
    public void untilAtHeading(double targetHeading, @NonNull Update<Double> update) {
        untilAtHeading(targetHeading, update, null);
    }

    /**
     * Calls the update function repeatedly with the current angular displacement in radians and calls the
     * callback when the target heading is reached.
     * @param targetHeading the target heading in radians
     * @param update        the update function
     * @param callback      the callback
     */
    public void untilAtHeading(double targetHeading, @NonNull Update<Double> update, Runnable callback) {
        untilAtHeading(targetHeading, Math.toRadians(tolerance), AngleUnit.RADIANS, update, callback);
    }

    /**
     * Calls the update function repeatedly with the current angular displacement in radians.
     * @param targetHeading the target heading in radians
     * @param tolerance     the tolerance to allow for in differences between angles in radians
     * @param update        the update function
     */
    public void untilAtHeading(double targetHeading, double tolerance, @NonNull Update<Double> update) {
        untilAtHeading(targetHeading, tolerance, update, null);
    }

    /**
     * Calls the update function repeatedly with the current angular displacement in radians and calls the
     * callback when the target heading is reached.
     * @param targetHeading the target heading in radians
     * @param tolerance     the tolerance to allow for in differences between angles in radians
     * @param update        the update function
     * @param callback      the callback
     */
    public void untilAtHeading(double targetHeading, double tolerance, @NonNull Update<Double> update, Runnable callback) {
        untilAtHeading(targetHeading, tolerance, AngleUnit.RADIANS, update, callback);
    }

    /**
     * Calls the update function repeatedly with the current angular displacement.
     * @param targetHeading the target heading
     * @param angleUnit     the tolerance to allow for in differences between angles
     * @param update        the update function
     */
    public void untilAtHeading(double targetHeading, @NonNull AngleUnit angleUnit, @NonNull Update<Double> update) {
        untilAtHeading(targetHeading, angleUnit, update, null);
    }

    /**
     * Calls the update function repeatedly with the current angular displacement and calls the
     * callback when the target heading is reached.
     * @param targetHeading the target heading
     * @param angleUnit     the tolerance to allow for in differences between angles
     * @param update        the update function
     * @param callback      the callback
     */
    public void untilAtHeading(double targetHeading, @NonNull AngleUnit angleUnit, @NonNull Update<Double> update, Runnable callback) {
        untilAtHeading(targetHeading, angleUnit.fromDegrees(tolerance), angleUnit, update, callback);
    }

    /**
     * Calls the update function repeatedly with the current angular displacement.
     * @param targetHeading the target heading
     * @param tolerance     the tolerance to allow for in differences between angles
     * @param angleUnit     the tolerance to allow for in differences between angles
     * @param update        the update function
     */
    public void untilAtHeading(double targetHeading, double tolerance, @NonNull AngleUnit angleUnit, @NonNull Update<Double> update) {
        untilAtHeading(targetHeading, tolerance, angleUnit, update, null);
    }

    /**
     * Calls the update function repeatedly with the current angular displacement and calls the
     * callback when the target heading is reached.
     * @param targetHeading the target heading
     * @param tolerance     the tolerance to allow for in differences between angles
     * @param angleUnit     the tolerance to allow for in differences between angles
     * @param update        the update function
     * @param callback      the callback
     */
    public void untilAtHeading(double targetHeading, double tolerance, @NonNull AngleUnit angleUnit, @NonNull Update<Double> update, Runnable callback) {
        listener.once(gyro, gyro -> {
          double dis = angularDisplacement(getRelativeHeading(angleUnit), targetHeading, angleUnit);

          update.update(dis);

          return Math.abs(dis) <= tolerance;
        }, () -> {
            if (callback != null) callback.run();
        });
    }

    /**
     * Calls the update function repeatedly with the current angular displacement in radians synchronously
     * (blocks thread) until the target heading is reached
     * @param targetHeading the target heading in radians
     * @param update        the update function
     */
    public void untilAtHeadingSync(double targetHeading, @NonNull Update<Double> update) throws InterruptedException {
        untilAtHeadingSync(targetHeading, Math.toRadians(tolerance), AngleUnit.RADIANS, update);
    }

    /**
     * Calls the update function repeatedly with the current angular displacement in radians synchronously
     * (blocks thread) until the target heading is reached
     * @param targetHeading the target heading in radians
     * @param tolerance     the tolerance to allow for in differences between angles in radians
     * @param update        the update function
     */
    public void untilAtHeadingSync(double targetHeading, double tolerance, @NonNull Update<Double> update) throws InterruptedException {
        untilAtHeadingSync(targetHeading, tolerance, AngleUnit.RADIANS, update);
    }

    /**
     * Calls the update function repeatedly with the current angular displacement synchronously
     * (blocks thread) until the target heading is reached
     * @param targetHeading the target heading
     * @param angleUnit     the tolerance to allow for in differences between angles
     * @param update        the update function
     */
    public void untilAtHeadingSync(double targetHeading, @NonNull AngleUnit angleUnit, @NonNull Update<Double> update) throws InterruptedException {
        untilAtHeadingSync(targetHeading, angleUnit.fromDegrees(tolerance), angleUnit, update);
    }

    /**
     * Calls the update function repeatedly with the current angular displacement synchronously
     * (blocks thread) until the target heading is reached
     * @param targetHeading the target heading
     * @param tolerance     the tolerance to allow for in differences between angles
     * @param angleUnit     the tolerance to allow for in differences between angles
     * @param update        the update function
     */
    public void untilAtHeadingSync(double targetHeading, double tolerance, @NonNull AngleUnit angleUnit, @NonNull Update<Double> update) throws InterruptedException {
        double dis = angularDisplacement(getRelativeHeading(angleUnit), targetHeading, angleUnit);

        while (Math.abs(dis) > tolerance) {
            update.update(dis);
            Thread.sleep(10);
            dis = angularDisplacement(getRelativeHeading(angleUnit), targetHeading, angleUnit);
        }
    }

    /**
     * Stops the gyro handler
     */
    @Override
    public void stop() {
        listener.removeAllListeners(gyro);
    }
}
