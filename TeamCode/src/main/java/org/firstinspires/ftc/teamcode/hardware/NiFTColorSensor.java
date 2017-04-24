/**
 * An enhancement on the original color sensor which enables quick i2c address setting and led enabling in the constructor (not much else for now)
 */

package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;

import org.firstinspires.ftc.teamcode.threads.NiFTFlow;

public class NiFTColorSensor
{
    public final ColorSensor sensor;

    public NiFTColorSensor (String colorSensorName, int i2cAddress, boolean enableLED) throws InterruptedException
    {
        this.sensor = NiFTInitializer.initialize (ColorSensor.class, colorSensorName);

        sensor.setI2cAddress (I2cAddr.create8bit (i2cAddress));

        sensor.enableLed (false);
        if (enableLED)
        {
            NiFTFlow.pauseForMS (200);
            sensor.enableLed (true);
        }
    }
}
