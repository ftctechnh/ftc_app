package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by makiah on 11/17/16.
 */

@Autonomous(name = "Autonomous - Drive Forward Edition", group = "Autonomous Group")

public class AutonomousDriveForward extends AutonomousBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO()
    {
        while (opModeIsActive()) {
            OutputRealTimeData(new String[]{
                    "Bottom Color Sensor",
                    "--------------------",
                    "ARGB: " + bottomColorSensor.argb() + " Alpha: " + bottomColorSensor.alpha(),
                    "Blue: " + bottomColorSensor.blue() + " Red: " + bottomColorSensor.red(),
                    "Green: " + bottomColorSensor.green(),
                    "Left Color Sensor",
                    "--------------------",
                    "ARGB: " + leftColorSensor.argb() + " Alpha: " + leftColorSensor.alpha(),
                    "Blue: " + leftColorSensor.blue() + " Red: " + leftColorSensor.red(),
                    "Green: " + leftColorSensor.green()
            });
            idle();
        } // alpha > 10 = white
//        drive(0.8, 2000);
    }
}