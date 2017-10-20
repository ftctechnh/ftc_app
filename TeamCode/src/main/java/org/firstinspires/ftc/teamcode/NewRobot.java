package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Jeremy on 10/18/2017.
 */

public class NewRobot
{
    ColorSensor topColorSens = null;
    ColorSensor forwardColorSens = null;
    ColorSensor floorColorSens = null;

    public NewRobot(HardwareMap hMap)
    {
        topColorSens = hMap.colorSensor.get("topColorSens");
        forwardColorSens = hMap.colorSensor.get("forwardColorSens");
        floorColorSens = hMap.colorSensor.get("floorColorSens");

    }

    public float getHueValue(ColorSensor in_ColorSens)
    {
        float hsvValues[] = {0F,0F,0F};
        Color.RGBToHSV(in_ColorSens.red(), in_ColorSens.green(), in_ColorSens.blue(), hsvValues);
        return hsvValues[0];
    }

    public ColorSensor getTopColorSens()
    {
        return topColorSens;
    }

    public ColorSensor getForwardColorSens()
    {
        return forwardColorSens;
    }

    public ColorSensor getFloorColorSens()
    {
        return floorColorSens;
    }

}
