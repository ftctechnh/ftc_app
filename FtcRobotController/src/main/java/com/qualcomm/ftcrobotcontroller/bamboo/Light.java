package com.qualcomm.ftcrobotcontroller.bamboo;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;

/**
 * Created by alex on 12/22/15.
 */
public class Light {

    LightSensor _light;

    public Light(String name, HardwareMap hwm)
    {
        _light = hwm.lightSensor.get(name);
    }

    public void enable()
    {
        _light.enableLed(true);
    }

    public void disable()
    {
        _light.enableLed(false);
    }

    public int get()
    {
        return (int) (_light.getLightDetected()*100);
    }

}
