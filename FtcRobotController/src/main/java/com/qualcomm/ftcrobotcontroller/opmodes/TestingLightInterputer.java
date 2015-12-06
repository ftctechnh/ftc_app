package com.qualcomm.ftcrobotcontroller.opmodes;
/*
created by Jeremy and Andrew on 11/22/15
*/

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.LightSensor;

public class TestingLightInterputer extends OpMode
{
        LightSensor redSensor;
        LightSensor blueSensor;
        LightSensor clearSensor;

    @Override
    public void init()
        {
        redSensor = hardwareMap.lightSensor.get("redSensor");
        blueSensor = hardwareMap.lightSensor.get("blueSensor");
        clearSensor = hardwareMap.lightSensor.get("clearSensor");
        }

public void loop()
    {
        int clear = clearSensor.getLightDetectedRaw();
        int redDiff = Math.abs(clear - redSensor.getLightDetectedRaw());
        int blueDiff = Math.abs(clear - blueSensor.getLightDetectedRaw());

        if (blueDiff >= (2 * redDiff))
        {
            telemetry.addData("Beacon is red", redSensor.getLightDetectedRaw());
        }
        else if (redDiff >= (2 * blueDiff))
        {
            telemetry.addData("Beacon is blue", blueSensor.getLightDetectedRaw());
        }
        else
        {
            telemetry.addData("Beacon is undefined", 0);

        }
    }
public void stop()
        {

        }
}



