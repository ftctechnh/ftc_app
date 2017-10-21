package org.firstinspires.ftc.teamcode;

/**
 * Created by Owner on 10/20/2017.
 */

public class HardwareRobot {
    private static final HardwareRobot ourInstance = new HardwareRobot();

    public static HardwareRobot getInstance() {
        return ourInstance;
    }

    private HardwareRobot() {
    }
}
