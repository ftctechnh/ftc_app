package org.firstinspires.ftc.teamcode.libraries.hardware;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Noah on 2/24/2018.
 */

public class StupidColor extends AdafruitI2cColorSensor {
    protected boolean LEDEn = true;

    public StupidColor(AdafruitI2cColorSensor sensor) {
        super(sensor.getDeviceClient());
    }

    @Override
    public synchronized void enableLed(boolean enable) {
        //read the enable bits, then OR them by the AIEN value, and send them back
        if(LEDEn != enable) {
            byte val = readEnable();
            if(!enable) val |= Enable.AIEN.bVal;
            else val &= ~Enable.AIEN.bVal;
            write8(Register.ENABLE, val);
            LEDEn = enable;
        }
    }

    @Override public boolean isLightOn()
    {
        return LEDEn;
    }
}
