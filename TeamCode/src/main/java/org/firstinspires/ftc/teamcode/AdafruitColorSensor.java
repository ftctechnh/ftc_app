package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Jeremy yaaaaoooo on 11/17/2016.
 */

public class AdafruitColorSensor
{
    ColorSensor beaconColorSensor;
    DeviceInterfaceModule cdim;
    boolean bPrevState;
    boolean bCurrState;
    boolean bLedOn;
    float hsvValues[] = {0F,0F,0F};
    final float values[] = hsvValues;
    static final int LED_CHANNEL = 5;

    public void init(HardwareMap hwMap)
    {
        final View relativeLayout = ((Activity) hwMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.RelativeLayout);
        bPrevState = false;
        bCurrState = false;

        bLedOn = true;

        cdim = hwMap.deviceInterfaceModule.get("dim");

        cdim.setDigitalChannelMode(LED_CHANNEL, DigitalChannelController.Mode.OUTPUT);

        beaconColorSensor = hwMap.colorSensor.get("beaconColorSensor");

        cdim.setDigitalChannelState(LED_CHANNEL, bLedOn);
    }

    public float returnHue()
    {
        Color.RGBToHSV((beaconColorSensor.red() * 255) / 800, (beaconColorSensor.green() * 255) / 800, (beaconColorSensor.blue() * 255) / 800, hsvValues);
        return hsvValues[0];
    }

    public boolean isDetectingBlue()
    {
        if(returnHue() > 180 && returnHue() < 265)
            return true;

        return false;
    }

    public boolean isDetectingRed()
    {
        if(returnHue() < 25 || (returnHue() > 330 && returnHue() < 360))
            return true;

        return false;
    }
}
