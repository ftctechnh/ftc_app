package com.qualcomm.ftcrobotcontroller.opmodes;

//import com.qualcomm.hardware.HiTechnicNxtLightSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;

/**
 * Created by Peter on 10/25/2015.
 */
public class LightSensorTest extends OpMode
{
    LightSensor beaconColor;

    public void init()
    {
        beaconColor = hardwareMap.lightSensor.get("beaconColor");
    }

    public void loop()
    {
        telemetry.addData("beaconColor", beaconColor.getLightDetectedRaw());
    }

    public void stop()
    {

    }

}

