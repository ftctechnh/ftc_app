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
        int redDiff = Math.abs(clearSensor.getLightDetectedRaw() - redSensor.getLightDetectedRaw());
        int blueDiff = Math.abs(clearSensor.getLightDetectedRaw() - blueSensor.getLightDetectedRaw());

        if (redDiff > blueDiff)
        {
            telemetry.addData("Beacon is red", redSensor.getLightDetectedRaw());
        }
        if (redDiff < blueDiff)
        {
            telemetry.addData("Beacon is blue", blueSensor.getLightDetectedRaw());
        }
    }
public void stop()
        {

        }
        }



        }