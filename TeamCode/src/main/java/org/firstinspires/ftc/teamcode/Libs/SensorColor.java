package org.firstinspires.ftc.teamcode.Libs;

/**
 * Created by caseyzandbergen on 10/12/16.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.I2cAddr;

import org.firstinspires.ftc.teamcode.HardwareProfiles.HardwareTestPlatform;

/**
 * Abstracts the color sensor hardware
 */
public class SensorColor {
    LinearOpMode opMode;
    private HardwareTestPlatform robot = null;

    /**
     * Class constructor
     *
     * @param myRobot hardwaremap input
     */
    public SensorColor(HardwareTestPlatform myRobot) {

        robot = myRobot;
    }

    /**
     * Get the amount of light detected by the sensor as an int.
     *
     * @return reading, unscaled.
     */
    public int alpha() {
        return robot.colorSensorRight.alpha();
    }

    /**
     * Get the "hue"
     *
     * @return unscaled int
     */
    public int argb() {
        return robot.colorSensorRight.argb();
    }

    /**
     * Get the blue value
     * @return unscaled int
     */
    public int blue() {
        return robot.colorSensorRight.blue();
    }

    /**
     * get the red value
     * @return unscaled int
     */
    public int red() {
        return robot.colorSensorRight.red();
    }

    /**
     * Get the green value
     * @return unscaled int
     */
    public int green() {
        return robot.colorSensorRight.green();
    }

    /**
     * Enable the LED light
     * @param enable Requires boolean input
     */
    public void enableLed(boolean enable) {
        robot.colorSensorRight.enableLed(enable);
    }

    /**
     * Gets the devices I2c Address
     * @return current I2c Address
     */
    public I2cAddr getI2cAddr() {
        return robot.colorSensorRight.getI2cAddress();
    }

    /**
     * Set the I2C address to a new value.
     * @param addr requires I2cAddress as input.  DO NOT CHANGE THIS!
     */
    public void setI2c(I2cAddr addr) {
        robot.colorSensorRight.setI2cAddress(addr);
    }

}
