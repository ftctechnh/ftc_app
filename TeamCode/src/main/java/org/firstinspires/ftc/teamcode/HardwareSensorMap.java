package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by thund on 8/14/2017.
 */

public class HardwareSensorMap {
    HardwareMap hwMap = null;
    TouchSensor ts = null;

    public HardwareSensorMap() {
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;
        ts = hwMap.touchSensor.get("touch_sensor");

    }


}