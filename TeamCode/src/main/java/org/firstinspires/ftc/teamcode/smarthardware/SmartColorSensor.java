package org.firstinspires.ftc.teamcode.smarthardware;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;

public class SmartColorSensor
{
    public final ColorSensor sensor;

    public SmartColorSensor(ColorSensor colorSensor, int i2cAddress, boolean enableLED)
    {
        this.sensor = colorSensor;

        sensor.setI2cAddress (I2cAddr.create8bit (i2cAddress));

        sensor.enableLed (enableLED);
    }
}
