package com.lasarobotics.library.sensor.legacy.hitechnic;

import com.lasarobotics.library.util.Timers;
import com.qualcomm.robotcore.hardware.GyroSensor;

import java.util.concurrent.TimeUnit;


/**
 * Implements additional Gyroscopic control methods and events
 */
public class Gyroscope {
    private final String clockName = "gyro";
    //IMPORTANT DELTAS
    private GyroSensor gyroSensor;
    private double velPrevious = 0.0D;
    private double velCurr = 0.0D;
    private double dt = 0.0D;
    private double offset = 0; //the offset for the GYROSCOPE RATE
    private double heading = 0;
    private Timers timers;

    public Gyroscope(GyroSensor g) {
        timers = new Timers();
        timers.startClock(clockName);
        gyroSensor = g;
        reset();
    }

    /**
     * Normalize Gyroscope bounds to within 0 and 360
     *
     * @param heading The current Gyroscope value
     * @return The normalized Gyroscope value, between 0 and 360.
     */
    public static double normalize(double heading) {
        if (heading < 0) {
            return 360 - (Math.abs(heading) % 360);
        } else {
            return (heading % 360);
        }
    }

    /* Run this method on every loop() event.
    * Only to be run once calibration has ended.
    * Propagates values from the GyroSensor to the more advanced Gyroscope implementation.
    * @param g GyroSensor retrieved from the hardwareMap
    */
    public void update(GyroSensor g) {
        //update raw gyro rotation
        gyroSensor = g;
        //store new values
        velPrevious = velCurr;
        velCurr = getRotation();
        dt = timers.getClockValue(clockName, TimeUnit.SECONDS);

        heading += (velPrevious + velCurr) * .5D * dt;

        //prepare for next values
        timers.resetClock(clockName);
    }

    /**
     * Resets the gyroscope to a value of zero.
     */
    public void reset() {
        heading = 0;
    }

    /**
     * Gets the gyroscope rotation rate in degrees per second
     *
     * @return The offset gyroscope rotation in degrees per second
     */
    public double getRate() {
        return gyroSensor.getHeading() - offset;
    }

    /**
     * Gets the gyroscope heading in degrees, between 0 and 360
     *
     * @return The gyro heading, between 0 and 360s
     */
    public double getHeading() {
        return normalize(heading);
    }

    /**
     * Gets the gyroscope rotation in degrees
     *
     * @return The gyroscope rotation in degrees
     */
    public double getRotation() {
        return heading;
    }

    /**
     * Gets the time difference between the last readings.
     *
     * @return The current time delay in seconds.
     */
    public double getTimeDifference() {
        return this.dt;
    }

    /**
     * Gets the gyroscope offset, in degrees per second.
     *
     * @return The offset, in degrees per second.
     */
    public double getOffset() {
        return this.offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    /**
     * Gets the status of the gyroscope
     *
     * @return The gyroscope status as a string
     */
    @Override
    public String toString() {
        return String.format("Gyroscope - rotation: %3.1f deg, rate: %3.1f deg/s, offset: %3.1f deg, over %1.4f sec\n" +
                this.getHeading(), this.getRate(), this.getOffset(), this.getTimeDifference());
    }
}
