package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.LightSensor;

import java.util.Scanner;

/**
 * Created by grempa on 11/27/15
 */

public class ThreeLightColorSensor
{
    public final int blueDetected = -1;
    public final int indeterminate = 0;
    public final int redDetected = 1;
   // public final int farFromBeacon = 2;

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
        int totalDiffofClearRed = 0, totalDiffofClearBlue = 0;


        //   if (blueLightDetected <= redLightDetected / 2 || redLightDetected <= blueLightDetected / 2) {
     // may need beacon detection code as it requires the color sensors to be close to the beacon

            if (diffOfClearRed > diffOfClearBlue * 5)
            {
                for (int i = 0; i < 5; i++)
                {
                    totalDiffofClearBlue += diffOfClearBlue;
                    totalDiffofClearRed += diffOfClearRed;
                }

                if (totalDiffofClearBlue > totalDiffofClearRed * 5) // originally a algorithm used for finding the aveerage but if we divide, they cancel each other out
                {
                    return redDetected;//returns 1, meaning it's red
                }
            }
            else if (diffOfClearBlue > diffOfClearRed * 5)
            {
                for (int i = 0; i < 5; i++)
                {
                    totalDiffofClearBlue += diffOfClearBlue;
                    totalDiffofClearRed += diffOfClearRed;
                }

                if (totalDiffofClearRed > totalDiffofClearBlue * 5)
                {
                    return blueDetected;//returns -1, meaning blue is detected
                }
            }
            else
            {
                return indeterminate;//0 means undefined color
            }


       // }




    }


}
