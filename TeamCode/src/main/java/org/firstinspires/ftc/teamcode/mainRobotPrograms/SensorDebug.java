package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Sensor Debug", group = "Autonomous Group")

public class SensorDebug extends _AutonomousBase
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