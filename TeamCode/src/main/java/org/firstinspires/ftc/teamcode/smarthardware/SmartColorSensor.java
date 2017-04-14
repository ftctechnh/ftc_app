package org.firstinspires.ftc.teamcode.smarthardware;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;

import org.firstinspires.ftc.teamcode.threads.ProgramFlow;

public class SmartColorSensor
{
    public final ColorSensor sensor;

    public SmartColorSensor(ColorSensor colorSensor, int i2cAddress, boolean enableLED) throws InterruptedException
    {
        this.sensor = colorSensor;

        sensor.setI2cAddress (I2cAddr.create8bit (i2cAddress));

        sensor.enableLed (false);
        if (enableLED)
        {
            ProgramFlow.pauseForMS (200);
            sensor.enableLed (true);
        }
    }
}
