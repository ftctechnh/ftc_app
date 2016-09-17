package org.lasarobotics.library.sensor.modernrobotics;

import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Implements the Core Optical OpticalDistance Sensor with advanced methods
 * <p/>
 * This sensor is only fully accurate UP TO 5 CM
 * Different lighting conditions greatly affect distance read after 5 cm away from the object
 */
public class OpticalDistance {
    OpticalDistanceSensor o;

    public OpticalDistance(OpticalDistanceSensor sensor) {
        this.o = sensor;
    }

    public void update(OpticalDistanceSensor sensor) {
        this.o = sensor;
    }

    /**
     * Gets the raw light reflected as a decimal
     *
     * @return The raw light reflected as a decimal
     */
    public double getLightDetected() {
        return o.getLightDetected();
    }

    /**
     * Returns true if an object is detected within the sensor's absolute maximum range (25 cm)
     *
     * @return True if an object is detected
     */
    public Boolean objectDetected() {
        return (getDistance() <= 25);
    }

    /**
     * Returns true if an object is near the sensor (within 5-10 cm)
     *
     * @return True if an object is near the sensor
     */
    public Boolean objectNear() {
        return (getDistance() <= 10);
    }

    /**
     * Returns true if an object is close enough to get an accurate distance measurement of +- 1
     * cm,
     * assuming light object
     *
     * @return True if an object is close enough to get an accurate distance measurement
     */
    public Boolean objectClose() {
        return (getDistance() <= 4);
    }

    /**
     * Gets an approximate distance from the object in centimeters
     * Formula based on empirical measurements in 2700K lighting at room temperature with a white
     * semi-reflective object perpendicular to the beam
     * <p/>
     * Please note that these values are only SOMEWHAT ACCURATE between 0.5 and 5 cm!
     *
     * @return An approximate distance in centimeters
     */
    public double getDistance() {
        return 0.8028 * Math.pow(getLightDetected(), -0.999d);
    }
}