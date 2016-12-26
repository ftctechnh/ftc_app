package org.firstinspires.ftc.teamcode.Libs;

import org.firstinspires.ftc.teamcode.HardwareProfiles.HardwareTestPlatform;

/**
 * Created by caseyzandbergen on 10/12/16.
 */

/**
 * Class abstracts the Gyro hardware
 */


public class SensorGyro {
    private HardwareTestPlatform robot = null;

    /**
     * Class Constructor
     *
     * @param myRobot input the map to the hardware.
     */
    public SensorGyro(HardwareTestPlatform myRobot) {

        robot = myRobot;
    }

    /**
     * Calibrate the Gyro
     *
     */
    public void calibrate() {

        robot.mrGyro.calibrate();
    }

    /**
     * Get the cartesion heading
     *
     * @return Cartesion heading
     */
    public int getHeading() {
        return robot.mrGyro.getHeading();
    }

    /**
     * Returns the heading relative to the calibrated heading.
     * Left = positive numbers, Right = negative numbers
     *
     * @return zVale
     */
    public int getIntegratedZ() {
        return robot.mrGyro.getIntegratedZValue();
    }

    /**
     * Return TRUE while calibrating and FALSE when not calibrating
     *
     * @return True / Flase
     */
    public boolean isCalibrating() {
        return robot.mrGyro.isCalibrating();
    }

    /**
     * Returns the raw X value
     *
     * @return X
     */
    public int getX() {
        return robot.mrGyro.rawX();
    }

    /**
     * Returns the raw Y value
     *
     * @return Y
     */
    public int getY() {
        return robot.mrGyro.rawY();
    }

    /**
     * Returns the raw Z value
     *
     * @return Z
     */
    public int getZ() {
        return robot.mrGyro.rawZ();
    }

    /**
     * Set the Z axis to 0
     */
    public void resetZAxis() {
        robot.mrGyro.resetZAxisIntegrator();
    }

    /**
     * Returns the status of the gyro
     *
     * @return status
     */
    public String status() {
        return robot.mrGyro.status();
    }
}
