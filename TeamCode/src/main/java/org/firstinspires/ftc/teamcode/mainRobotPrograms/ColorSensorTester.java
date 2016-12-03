package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by makiah on 11/29/16.
 */

@Autonomous(name="Color Sensor Tester", group = "Autonomous Group")

public class ColorSensorTester extends AutonomousBase
{
    protected void driverStationSaysGO()
    {
        bottomColorSensor.enableLed(true);
        leftColorSensor.enableLed(true);
//        rightColorSensor.enableLed(true);

        while (opModeIsActive())
        {
            OutputRealTimeData(new String[]{
                    "Left Color Sensor",
                    "--------------------",
                    "ARGB: " + bottomColorSensor.argb() + " Alpha: " + bottomColorSensor.alpha(),
                    "Blue: " + bottomColorSensor.blue() + " Red: " + bottomColorSensor.red(),
                    "Green: " + bottomColorSensor.green()
            });
            idle();
        }
    }
}
