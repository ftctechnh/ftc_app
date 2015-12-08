package com.qualcomm.ftcrobotcontroller.bamboo;

import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Created by chsrobotics on 11/9/2015.
 */
public class Console {

    Telemetry telemetry;
    public Console(Telemetry tele)
    {
        telemetry = tele;
    }

    public void log(String ref, String msg)
    {
        telemetry.addData(ref,  msg);
    }
}
