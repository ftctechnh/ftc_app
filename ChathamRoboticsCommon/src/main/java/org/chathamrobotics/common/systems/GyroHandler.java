package org.chathamrobotics.common.systems;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 11/18/2017
 */


import com.qualcomm.robotcore.hardware.GyroSensor;

import org.chathamrobotics.common.hardware.utils.HardwareListener;
import org.chathamrobotics.common.robot.Robot;
import org.chathamrobotics.common.robot.RobotLogger;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


/**
 * Handles gyro operations
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class GyroHandler implements System {
    /**
     * The default tolerance for differences in angles
     */
    public static final int DEFAULT_TOLERANCE = 5;

    private static final String TAG = "GyroHandler";
    private static final double TWO_PI = Math.PI * 2;
    private static final HardwareListener.HardwareCondition<GyroSensor> GYRO_CALIBRATION_FINISHED =
            (GyroSensor gyro) -> ! gyro.isCalibrating();
    private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(1);

    /**
     * Called repeatedly
     */
    public interface UpdateCallback {
        void run(double currentVal);
    }

    /**
     * Calculates the relative angle based off the given initial angle
     * @param initial   the initial angle
     * @param angle     the angle to correct
     * @return          the corrected angle
     */
    public static double calcRelativeAngle(double initial, double angle) {
        return (initial > Math.PI ? TWO_PI - initial : initial) + angle;
    }

    /**
     * Calculates the relative angle based off the given initial angle in the given units
     * @param initial   the initial angle in the given units
     * @param angle     the angle to correct in the given units
     * @param angleUnit the units to measure the angles in
     * @return          the corrected angle in the given units
     */
    public static double calcRelativeAngle(double initial, double angle, AngleUnit angleUnit) {
        if (angleUnit == AngleUnit.RADIANS)
            return calcRelativeAngle(initial, angle);

        return (initial > 180 ? 360 - initial : initial) + angle;
    }

    /**
     * Builds a new instance of {@link GyroHandler} from the given robot
     * @param robot the robot to use to build the GyroHandler
     * @return      the built gyro handler
     */
    public static GyroHandler build(Robot robot) {
        return  new GyroHandler(
                robot.getHardwareMap().gyroSensor.get("Gyro"),
                robot,
                robot.log.systemLogger(TAG)
        );
    }

    private final GyroSensor gyro;
    private final HardwareListener listener;
    private final RobotLogger logger;

    // STATE
    private boolean isCalibrated = false;
    private boolean isInitialized = false;
    private volatile double initialHeading = 0;
    private double tolerance = DEFAULT_TOLERANCE;

    public GyroHandler(GyroSensor gyro, HardwareListener listener, RobotLogger logger) {
        this.gyro = gyro;
        this.listener = listener;
        this.logger = logger;
    }

    /**
     * Calibrates the gyro
     */
    public void calibrate() {
        logger.debug("calibrating...");
        gyro.calibrate();

        listener.once(gyro, GYRO_CALIBRATION_FINISHED, () -> {
            isCalibrated = true;
            logger.debug("finished calibrating");
        });
    }

    /**
     * Calibrates the gyro and calls the callback when it is finished
     * @param callback  the callback function
     */
    public void calibrate(Runnable callback) {
        logger.debug("calibrating...");
        gyro.calibrate();

        listener.once(gyro, GYRO_CALIBRATION_FINISHED, () -> {
            isCalibrated = true;

            callback.run();

            logger.debug("finished calibrating");
        });
    }

    /**
     * Calibrates the gyro synchronously (blocks)
     * @throws InterruptedException thrown if the thread is interrupted while calibrating
     */
    public void calibrateSync() throws InterruptedException {
        logger.debug("calibrating...");
        if (! gyro.isCalibrating()) gyro.calibrate();

        while (! gyro.isCalibrating()) Thread.sleep(10);

        isCalibrated = true;
        logger.debug("finished calibrating");
    }

    /**
     * Checks whether or not the gyro is calibrated
     * @return  whether or not the gyro is calibrated
     */
    public boolean isCalibrated() {return isCalibrated;}

    /**
     * Initializes the gyro
     */
    public void init() {
        logger.debug("initializing...");

        calibrate(() -> {
            initialHeading = getHeading();
            isInitialized = true;
            logger.debug("finished initializing");
        });
    }

    /**
     * Initializes the gyro and runs the callback when finished
     * @param callback  the callback to run when the gyro has been initialized
     */
    public void init(Runnable callback) {
        logger.debug("initializing...");

        calibrate(() -> {
            initialHeading = getHeading();
            isInitialized = true;
            callback.run();
            logger.debug("finished initializing");
        });
    }

    /**
     * Initializes the gyro synchronously (blocks)
     * @throws InterruptedException thrown if the thread is interrupted while initializing
     */
    public void initSync() throws InterruptedException {
        logger.debug("initializing...");
        calibrateSync();

        initialHeading = gyro.getHeading();
        isInitialized = true;
        logger.debug("finished initializing");
    }

    /**
     * Checks whether or not the gyro has been initialized
     * @return  whether or not the gyro has been initialized
     */
    public boolean isInitialized() {return isInitialized;}

    /**
     * Sets the tolerance for differences in angles
     * @param tolerance the tolerance for differences in angles
     */
    public void setTolerance(double tolerance) {this.tolerance = tolerance;}

    /**
     * Returns the gyro's current heading in radians
     * @return  the gyro's current heading in radians
     */
    public double getHeading() {return Math.toRadians(gyro.getHeading());}

    /**
     * Returns the gyro's current heading in the given units
     * @param angleUnit the units to measure the heading with
     * @return          the gyro's current heading in the given units
     */
    public double getHeading(AngleUnit angleUnit) {return angleUnit.fromDegrees(gyro.getHeading());}

    /**
     * Returns the gyro's current heading relative to the initial heading in radians
     * @return  the gyro's current heading relative to the initial heading in radians
     */
    public double getRelativeHeading() {
        return Math.toRadians(calcRelativeAngle(initialHeading, gyro.getHeading(), AngleUnit.DEGREES));
    }

    /**
     * Returns the gyro's current heading relative to the initial heading in the given angle units
     * @param angleUnit     the angle units to measure the heading with
     * @return              the gyro's current heading relative to the initial heading in the given angle units
     */
    public double getRelativeHeading(AngleUnit angleUnit) {
        return getRelativeHeading(initialHeading, angleUnit);
    }

    /**
     * Returns the gyro's current heading relative to the initial heading in radians
     * @param initial   the initial heading
     * @return          the gyro's current heading relative to the initial heading in radians
     */
    public double getRelativeHeading(double initial) {
        return  calcRelativeAngle(
                initial,
                Math.toRadians(gyro.getHeading())
        );
    }

    /**
     * Returns the gyro's current heading relative to the initial heading in the given angle units
     * @param initial   the initial heading in the given angle units
     * @return          the gyro's current heading relative to the initial heading in the given angle units
     */
    public double getRelativeHeading(double initial, AngleUnit angleUnit) {
        return calcRelativeAngle(
                angleUnit.fromDegrees(initial),
                angleUnit.fromDegrees(gyro.getHeading()),
                angleUnit
        );
    }

    /**
     * Checks whether or not the gyro heading is at the target heading
     * @param target    the target heading in radians
     * @return          whether or not the gyro heading is at the target heading
     */
    public boolean isAtTarget(double target) {
        return calcAngleDiff(gyro.getHeading(), Math.toDegrees(target)) < tolerance;
    }

    /**
     * Checks whether or not the gyro heading is at the target heading
     * @param target    the target heading in the given units
     * @param angleUnit the angle units
     * @return          whether or not the gyro heading is at the target heading
     */
    public boolean isAtTarget(double target, AngleUnit angleUnit) {
        return calcAngleDiff(gyro.getHeading(), angleUnit.toDegrees(target)) < tolerance;
    }

    /**
     * Checks whether or not the gyro heading is at the target heading
     * @param target    the target heading in radians
     * @param tolerance the tolerance in the difference between the angles
     * @return          whether or not the gyro heading is at the target heading
     */
    public boolean isAtTarget(double target, double tolerance) {
        return calcAngleDiff(gyro.getHeading(), Math.toDegrees(target)) < tolerance;
    }

    /**
     * Checks whether or not the gyro heading is at the target heading
     * @param target    the target heading in radians
     * @param tolerance the tolerance in the difference between the angles
     * @param angleUnit the angle units
     * @return          whether or not the gyro heading is at the target heading
     */
    public boolean isAtTarget(double target, double tolerance, AngleUnit angleUnit) {
        return calcAngleDiff(gyro.getHeading(), angleUnit.toDegrees(target)) < angleUnit.toDegrees(tolerance);
    }

    /**
     * Calls the callback once the gyro is at the target angle
     * @param target    the target angle in radians
     * @param callback  called once the gyro is at the target angle
     */
    public void onceAtTarget(double target, Runnable callback) {
        listener.once(gyro, (GyroSensor gyro) -> isAtTarget(target), callback);
    }

    /**
     * Calls the callback once the gyro is at the target angle
     * @param target    the target angle in radians
     * @param angleUnit the units for the angles
     * @param callback  called once the gyro is at the target angle
     */
    public void onceAtTarget(double target, AngleUnit angleUnit, Runnable callback) {
        listener.once(gyro, (GyroSensor gyro) -> isAtTarget(target, angleUnit), callback);
    }

    /**
     * Calls the callback once the gyro is at the target angle
     * @param target    the target angle in radians
     * @param tolerance the tolerance in the difference between the angles
     * @param angleUnit the units for the angles
     * @param callback  called once the gyro is at the target angle
     */
    public void onceAtTarget(double target, double tolerance, AngleUnit angleUnit, Runnable callback) {
        listener.once(gyro, (GyroSensor gyro) -> isAtTarget(target, tolerance, angleUnit), callback);
    }

    /**
     * Calls the update callback until the target angle is reached
     * @param target            the target angle in radians
     * @param updateCallback    called repeatedly until the target angle is reached
     */
    public void untilAtTarget(double target, UpdateCallback updateCallback) {
        final double tag = Math.toDegrees(target);
        listener.once(gyro, (GyroSensor gyro) -> {
            double diff = calcAngleDiff(getRelativeHeading(AngleUnit.DEGREES), tag);

            updateCallback.run(Math.toRadians(diff));

            return diff < tolerance;
        }, () -> {});
    }

    /**
     * Calls the update callback until the target angle is reached
     * @param target            the target angle in radians
     * @param angleUnit         the unit for the angles
     * @param updateCallback    called repeatedly until the target angle is reached
     */
    public void untilAtTarget(double target, AngleUnit angleUnit, UpdateCallback updateCallback) {
        final double tag = angleUnit.toDegrees(target);
        listener.once(gyro, (GyroSensor gyro) -> {
            double diff = calcAngleDiff(getRelativeHeading(AngleUnit.DEGREES), tag);

            updateCallback.run(angleUnit.fromDegrees(diff));

            return diff < tolerance;
        }, () -> {});
    }

    /**
     * Calls the update callback until the target angle is reached
     * @param target            the target angle in radians
     * @param tolerance         the tolerance in the difference between the angles
     * @param updateCallback    called repeatedly until the target angle is reached
     */
    public void untilAtTarget(double target, double tolerance, UpdateCallback updateCallback) {
        final double tag = Math.toDegrees(target);
        final double tol = Math.toDegrees(tolerance);
        listener.once(gyro, (GyroSensor gyro) -> {
            double diff = calcAngleDiff(getRelativeHeading(AngleUnit.DEGREES), tag);

            updateCallback.run(Math.toRadians(diff));

            return diff < tol;
        }, () -> {});
    }

    /**
     * Calls the update callback until the target angle is reached
     * @param target            the target angle in radians
     * @param tolerance         the tolerance in the difference between the angles
     * @param angleUnit         the unit for the angles
     * @param updateCallback    called repeatedly until the target angle is reached
     */
    public void untilAtTarget(double target, double tolerance, AngleUnit angleUnit, UpdateCallback updateCallback) {
        final double tag = angleUnit.toDegrees(target);
        final double tol = angleUnit.toDegrees(tolerance);
        listener.once(gyro, (GyroSensor gyro) -> {
            double diff = calcAngleDiff(getRelativeHeading(AngleUnit.DEGREES), tag);

            updateCallback.run(angleUnit.fromDegrees(diff));

            return diff < tol;
        }, () -> {});
    }

    /**
     * Calls the update callback until the target angle is reached
     * @param target            the target angle in radians
     * @param updateCallback    called repeatedly until the target angle is reached
     * @param callback          called when the target angle is reached
     */
    public void untilAtTarget(double target, UpdateCallback updateCallback, Runnable callback) {
        final double tag = Math.toDegrees(target);
        listener.once(gyro, (GyroSensor gyro) -> {
            double diff = calcAngleDiff(getRelativeHeading(AngleUnit.DEGREES), tag);

            updateCallback.run(Math.toRadians(diff));

            return diff < tolerance;
        }, callback);
    }

    /**
     * Calls the update callback until the target angle is reached
     * @param target            the target angle in radians
     * @param angleUnit         the unit for the angles
     * @param updateCallback    called repeatedly until the target angle is reached
     * @param callback          called when the target angle is reached
     */
    public void untilAtTarget(double target, AngleUnit angleUnit, UpdateCallback updateCallback, Runnable callback) {
        final double tag = angleUnit.toDegrees(target);
        listener.once(gyro, (GyroSensor gyro) -> {
            double diff = calcAngleDiff(getRelativeHeading(AngleUnit.DEGREES), tag);

            updateCallback.run(angleUnit.fromDegrees(diff));

            return diff < tolerance;
        }, callback);
    }

    /**
     * Calls the update callback until the target angle is reached
     * @param target            the target angle in radians
     * @param tolerance         the tolerance in the difference between the angles
     * @param updateCallback    called repeatedly until the target angle is reached
     * @param callback          called when the target angle is reached
     */
    public void untilAtTarget(double target, double tolerance, UpdateCallback updateCallback, Runnable callback) {
        final double tag = Math.toDegrees(target);
        final double tol = Math.toDegrees(tolerance);
        listener.once(gyro, (GyroSensor gyro) -> {
            double diff = calcAngleDiff(getRelativeHeading(AngleUnit.DEGREES), tag);

            updateCallback.run(Math.toRadians(diff));

            return diff < tol;
        }, callback);
    }

    /**
     * Calls the update callback until the target angle is reached
     * @param target            the target angle in radians
     * @param tolerance         the tolerance in the difference between the angles
     * @param angleUnit         the unit for the angles
     * @param updateCallback    called repeatedly until the target angle is reached
     * @param callback          called when the target angle is reached
     */
    public void untilAtTarget(double target, double tolerance, AngleUnit angleUnit, UpdateCallback updateCallback, Runnable callback) {
        final double tag = angleUnit.toDegrees(target);
        final double tol = angleUnit.toDegrees(tolerance);
        listener.once(gyro, (GyroSensor gyro) -> {
            double diff = calcAngleDiff(getRelativeHeading(AngleUnit.DEGREES), tag);

            updateCallback.run(angleUnit.fromDegrees(diff));

            return diff < tol;
        }, callback);
    }

    /**
     * Calls the update callback synchronously (blocks) until the target angle is reached
     * @param target            the target angle in radians
     * @param updateCallback    called repeatedly until the target angle is reached
     */
    public void untilAtTargetSync(double target, UpdateCallback updateCallback) throws InterruptedException {
        target = Math.toDegrees(target);
        double diff = calcAngleDiff(getRelativeHeading(AngleUnit.DEGREES), target);

        while (diff > tolerance) {
            updateCallback.run(Math.toRadians(diff));

            Thread.sleep(10);

            diff = calcAngleDiff(getRelativeHeading(AngleUnit.DEGREES), target);
        }
    }

    /**
     * Calls the update callback synchronously (blocks) until the target angle is reached
     * @param target            the target angle in radians
     * @param angleUnit         the unit for the angles
     * @param updateCallback    called repeatedly until the target angle is reached
     */
    public void untilAtTargetSync(double target, AngleUnit angleUnit, UpdateCallback updateCallback) throws InterruptedException {
        target = angleUnit.toDegrees(target);
        double diff = calcAngleDiff(getRelativeHeading(AngleUnit.DEGREES), target);

        while (diff > tolerance) {
            updateCallback.run(angleUnit.fromDegrees(diff));

            Thread.sleep(10);

            diff = calcAngleDiff(getRelativeHeading(AngleUnit.DEGREES), target);
        }
    }

    /**
     * Calls the update callback synchronously (blocks) until the target angle is reached
     * @param target            the target angle in radians
     * @param tolerance         the tolerance in the difference between the angles
     * @param updateCallback    called repeatedly until the target angle is reached
     */
    public void untilAtTargetSync(double target, double tolerance, UpdateCallback updateCallback) throws InterruptedException {
        target = Math.toDegrees(target);
        tolerance = Math.toDegrees(tolerance);

        double diff = calcAngleDiff(getRelativeHeading(AngleUnit.DEGREES), target);

        while (diff > tolerance) {
            updateCallback.run(Math.toRadians(diff));

            Thread.sleep(10);

            diff = calcAngleDiff(getRelativeHeading(AngleUnit.DEGREES), target);
        }
    }

    /**
     * Calls the update callback synchronously (blocks) until the target angle is reached
     * @param target            the target angle in radians
     * @param tolerance         the tolerance in the difference between the angles
     * @param angleUnit         the unit for the angles
     * @param updateCallback    called repeatedly until the target angle is reached
     */
    public void untilAtTargetSync(double target, double tolerance, AngleUnit angleUnit, UpdateCallback updateCallback) throws InterruptedException {
        target = angleUnit.toDegrees(target);
        tolerance = angleUnit.toDegrees(tolerance);

        double diff = calcAngleDiff(getRelativeHeading(AngleUnit.DEGREES), target);

        while (diff > tolerance) {
            updateCallback.run(angleUnit.fromDegrees(diff));

            Thread.sleep(10);

            diff = calcAngleDiff(getRelativeHeading(AngleUnit.DEGREES), target);
        }
    }

    @Override
    public void stop() {
        listener.removeAllListeners(gyro);
    }

    private double calcAngleDiff(double current, double target) {
        double diff = current - target;

        if (Math.abs(diff) > 180)
            diff += (current > 180 ? -360 : 360);

        return diff;
    }
}
