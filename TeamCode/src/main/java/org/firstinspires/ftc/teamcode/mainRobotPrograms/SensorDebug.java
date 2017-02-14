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
                            "Option 2 Color Sensor",
                            "--------------------",
                            "ARGB: " + option2ColorSensor.argb() + " Alpha: " + option2ColorSensor.alpha(),
                            "Blue: " + option2ColorSensor.blue() + " Red: " + option2ColorSensor.red(),
                            "Green: " + option2ColorSensor.green(),
                            "Option 1 Color Sensor",
                            "--------------------",
                            "ARGB: " + option2ColorSensor.argb() + " Alpha: " + option2ColorSensor.alpha(),
                            "Blue: " + option2ColorSensor.blue() + " Red: " + option2ColorSensor.red(),
                            "Green: " + option2ColorSensor.green(),
                            "Heading: " + getValidGyroHeading(),
                            "Front Range Sensor: " + frontRangeSensor.cmUltrasonic(),
                            "Back Range Sensor: " + sideRangeSensor.cmUltrasonic()
                    }
            );
            idle();
        }
    }
}