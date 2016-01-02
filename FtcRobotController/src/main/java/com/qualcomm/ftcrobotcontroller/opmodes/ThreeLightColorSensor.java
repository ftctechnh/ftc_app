package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.LightSensor;

import java.util.Scanner;

/**
 * Created by grempa on 11/27/15.
 */

public class ThreeLightColorSensor
{
    public final int blueDetected = -1;
    public final int indeterminate = 0;
    public final int redDetected = 1;
    public final int farFromBeacon = 2;

    private LightSensor red;
    private LightSensor blue;
    private LightSensor clear;

    public ThreeLightColorSensor(LightSensor redSensor, LightSensor blueSensor, LightSensor clearSensor)
    {
        red = redSensor;
        blue = blueSensor;
        clear = clearSensor;
    }

    public int colorDetected()
    {
        int redLightDetected = red.getLightDetectedRaw();
        int blueLightDetected = blue.getLightDetectedRaw();
        int clearLightDetected = clear.getLightDetectedRaw();

        int diffOfClearRed = Math.abs(clearLightDetected - redLightDetected);
        int diffOfClearBlue = Math.abs(clearLightDetected - blueLightDetected);


     //   if (blueLightDetected <= redLightDetected / 2 || redLightDetected <= blueLightDetected / 2) {
     // may need beacon detection code as it requires the color sensors to be close to the beacon

            if (diffOfClearRed > diffOfClearBlue * 2) {
                return blueDetected; //-1 means the light is blue
            } else if (diffOfClearBlue < diffOfClearRed * 2) {
                return redDetected; // returns 1, meaning that the light is red
            } else {
                return indeterminate;//0 means undefined color
            }


       // }




    }
}
