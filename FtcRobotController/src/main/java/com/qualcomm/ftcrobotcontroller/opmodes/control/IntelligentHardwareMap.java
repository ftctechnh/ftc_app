package com.qualcomm.ftcrobotcontroller.opmodes.control;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by tdoylend on 2016-01-02.
 *
 * This class provides an 'intelligent' hardware map.
 * Instead of having to perform calls like
 *
 * hardwareMap.whatever.get("thingy")
 *
 * you can now just do
 *
 * ihwm.get("thingy")
 */

public class IntelligentHardwareMap {

    HardwareMap hwm;

    public IntelligentHardwareMap(HardwareMap hwm) {
        this.hwm  = hwm;
    }

    public HardwareDevice get(String name) {
        try { return this.hwm.dcMotor.get(name); }
        catch (Exception e) { }
        try { return this.hwm.servo.get(name); }
        catch (Exception e) { }
        try { return this.hwm.colorSensor.get(name); }
        catch (Exception e) { }
        throw new Error("Unable to find device with name "+name);
    }

}
