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

<<<<<<< HEAD
        if (redDiff > blueDiff)
        {
            telemetry.addData("Beacon is red", redSensor.getLightDetectedRaw());
        }
        if (redDiff < blueDiff)
        {
            telemetry.addData("Beacon is blue", blueSensor.getLightDetectedRaw());
=======
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
>>>>>>> ad6e9a70de40b32d978aa1f2b9ca94ee31e90f89
        }
    }
public void stop()
        {

        }
}



