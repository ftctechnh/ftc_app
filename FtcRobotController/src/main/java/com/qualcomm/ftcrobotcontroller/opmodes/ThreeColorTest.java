package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.LightSensor;

/**
 * Created by grempa on 11/27/15.
 */

public class ThreeColorTest extends OpMode{
    LightSensor redSensor;
    LightSensor clearSensor;
    LightSensor blueSensor;
    ThreeLightColorSensor getColor;

    public void init()
    {
        redSensor = hardwareMap.lightSensor.get("redSensor");
        blueSensor = hardwareMap.lightSensor.get("blueSensor");
        clearSensor = hardwareMap.lightSensor.get("clearSensor");
        getColor = new ThreeLightColorSensor(redSensor, blueSensor, clearSensor);
    }

    public void loop()
    {
        int colorDetected = getColor.colorDetected();

        if (colorDetected == -1 )
        {
            telemetry.addData("The color is blue" , 0);
        }

        else if (colorDetected == 1 )
        {
            telemetry.addData("The color is red" , 0);
        }

        else
        {
            telemetry.addData("The color is undefined" , 0);
        }

        telemetry.addData("redSensor " , redSensor.getLightDetectedRaw());
        telemetry.addData("clearSensor" , clearSensor.getLightDetectedRaw());
        telemetry.addData("blueSensor" , blueSensor.getLightDetectedRaw());
        telemetry.addData("Diff of clear and red ",Math.abs(clearSensor.getLightDetectedRaw() - redSensor.getLightDetectedRaw()));
        telemetry.addData("Diff of clear and blue ",Math.abs(clearSensor.getLightDetectedRaw() - blueSensor.getLightDetectedRaw()));

    }
    public void stop()
    {

    }
}
