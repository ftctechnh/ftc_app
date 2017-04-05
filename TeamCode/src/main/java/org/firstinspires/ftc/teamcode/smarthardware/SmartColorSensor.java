package org.firstinspires.ftc.teamcode.smarthardware;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;

public class SmartColorSensor
{
    public final ColorSensor colorSensor;

    public SmartColorSensor(ColorSensor colorSensor, int i2cAddress, boolean enableLED)
    {
        this.colorSensor = colorSensor;

        colorSensor.setI2cAddress (I2cAddr.create8bit (i2cAddress));

        colorSensor.enableLed (enableLED);
    }
}
