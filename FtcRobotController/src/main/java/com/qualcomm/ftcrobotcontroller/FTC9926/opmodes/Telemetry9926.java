package com.qualcomm.ftcrobotcontroller.FTC9926.opmodes;

/**
 * Created by Nicolas Bravo on 11/1/15.
 * Sends telemetry information to driver phone
 * based on AutoProgram
 */
public class Telemetry9926 extends HardwareMap9926
{
    public void UpdateTelemetry ()
    {
        // servo position
         telemetry.addData
                ( "01",
                "a_hand_position()"
                );
        // timer?
        telemetry.addData
                ("02",
                getRuntime()
                );
    }

}

