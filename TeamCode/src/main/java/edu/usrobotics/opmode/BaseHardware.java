package edu.usrobotics.opmode;

import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by mborsch19 on 10/13/16.
 */
public abstract class BaseHardware {

    public HardwareMap hardwareMap;

    public void init (HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

        getDevices();
    }

    public abstract void getDevices ();
}
