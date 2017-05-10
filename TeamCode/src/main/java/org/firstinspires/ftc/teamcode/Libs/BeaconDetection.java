package org.firstinspires.ftc.teamcode.Libs;

import org.firstinspires.ftc.teamcode.HardwareProfiles.HardwareTestPlatform;

/**
 * Created by caseyzandbergen on 11/30/16.
 */

public class BeaconDetection {
    private final static HardwareTestPlatform robot = new HardwareTestPlatform();

    private double red = 0;
    private double blue = 1;

    public String getColor() {
        String color = "unk";
        red = robot.colorSensorRight.red();
        blue = robot.colorSensorRight.blue();

        if (red == 1 && blue == 0) {
            color = "red";
        }
        if (blue == 1 && red == 0) {
            color = "blue";
        }
        if (red == 1 && blue == 1 || red == 0 & blue == 0) {
            color = "unk";
        }
        return color;
    }
}
