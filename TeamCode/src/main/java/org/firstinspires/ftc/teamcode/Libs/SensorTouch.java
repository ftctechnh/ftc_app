package org.firstinspires.ftc.teamcode.Libs;

/**
 * Created by caseyzandbergen on 10/12/16.
 */

import org.firstinspires.ftc.teamcode.HardwareProfiles.HardwareTestPlatform;

/**
 * Class abstracts the touch sensor hardware
 */
public class SensorTouch {
    private HardwareTestPlatform robot = null;

    /**
     * Class Constructor
     *
     * @param myRobot hardwaremap input
     */
    public SensorTouch(HardwareTestPlatform myRobot) {

        robot = myRobot;

    }

    /**
     * Returns the force the sensor has registered.  Not available on all sensors, will be 0 or 1
     * on sensors that don't have force sense.
     *
     * @return force
     */
    public double force() {
        return robot.touchSensor.getValue();
    }

    /**
     * Returns the state of the button.
     *
     * @return pressed
     */
    public boolean pressed() {
        return robot.touchSensor.isPressed();
    }

}
