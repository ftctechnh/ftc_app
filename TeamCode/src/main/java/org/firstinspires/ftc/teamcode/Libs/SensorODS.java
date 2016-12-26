package org.firstinspires.ftc.teamcode.Libs;

/**
 * Created by caseyzandbergen on 10/12/16.
 */

import org.firstinspires.ftc.teamcode.HardwareProfiles.HardwareTestPlatform;

/**
 * Class abstracts the ODS sensor
 */
public class SensorODS {
    private HardwareTestPlatform robot = null;

    /**
     * Class constructor
     */
    public SensorODS(HardwareTestPlatform myRobot) {

        robot = myRobot;

    }

    /**
     * Get the amount of light detected by the sensor, scaled and cliped to a range which is a
     * pragmatically useful sensitivity. Note that returned values INCREASE as the light energy INCREASES.
     *
     * @return amount of light, on a scale of 0.0 to 1.0
     */
    public double getLight() {
        return robot.ods.getLightDetected();
    }

    /**
     * Returns a signal whose strength is proportional to the intensity of the light measured.
     * Note that returned values INCREASE as the light energy INCREASES. The units in which this signal is returned are unspecified.
     *
     * @return a value proportional to the amount of light detected, in unspecified units
     */
    public double getRawLight() {
        return robot.ods.getRawLightDetected();
    }

    /**
     * Returns the maximum value that can be returned by getRawLightDetected().
     *
     * @return the maximum value that can be returned by getRawLightDetected
     */
    public double getMaxLight() {
        return robot.ods.getRawLightDetectedMax();
    }

    /**
     * Enable the LED light
     *
     * @param enable enable - true to enable; false to disabl
     */
    public void enableLed(boolean enable) {
        robot.ods.enableLed(enable);
    }

    /**
     * Status of this sensor, in string form
     *
     * @return status
     */
    public String status() {
        return robot.ods.status();
    }
}
