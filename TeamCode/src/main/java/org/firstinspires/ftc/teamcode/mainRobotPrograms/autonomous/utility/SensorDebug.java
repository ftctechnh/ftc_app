package org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.utility;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.AutoBase;

@Autonomous(name = "Sensor Debug", group = "Utility Group")

public class SensorDebug extends AutoBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        while (true) {
            outputConstantDataToDrivers(
                    new String[]
                    {
                            "Option 1 Color Sensor",
                            "--------------------",
                            "ARGB: " + option1ColorSensor.argb() + " Alpha: " + option1ColorSensor.alpha(),
                            "Blue: " + option1ColorSensor.blue() + " Red: " + option1ColorSensor.red(),
                            "Green: " + option1ColorSensor.green(),
                            "Option 2 Color Sensor",
                            "--------------------",
                            "ARGB: " + option2ColorSensor.argb() + " Alpha: " + option2ColorSensor.alpha(),
                            "Blue: " + option2ColorSensor.blue() + " Red: " + option2ColorSensor.red(),
                            "Green: " + option2ColorSensor.green(),
                            "Bottom Color Sensor",
                            "--------------------",
                            "ARGB: " + bottomColorSensor.argb() + " Alpha: " + bottomColorSensor.alpha(),
                            "Blue: " + bottomColorSensor.blue() + " Red: " + bottomColorSensor.red(),
                            "Green: " + bottomColorSensor.green(),
                            "Heading: " + getValidGyroHeading(),
                            "Front Range Sensor: " + frontRangeSensor.cmUltrasonic(),
                            "Back Range Sensor: " + sideRangeSensor.cmUltrasonic()
                    }
            );
            idle();
        }
    }
}