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
        telemetry.addData("The color is ", getColor.colorDetected());

    }
    public void stop()
    {

    }
}
