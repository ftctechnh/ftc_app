package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

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
                            "Bottom Color Sensor",
                            "--------------------",
                            "ARGB: " + bottomColorSensor.argb() + " Alpha: " + bottomColorSensor.alpha(),
                            "Blue: " + bottomColorSensor.blue() + " Red: " + bottomColorSensor.red(),
                            "Green: " + bottomColorSensor.green(),
                            "Right Color Sensor",
                            "--------------------",
                            "ARGB: " + rightColorSensor.argb() + " Alpha: " + rightColorSensor.alpha(),
                            "Blue: " + rightColorSensor.blue() + " Red: " + rightColorSensor.red(),
                            "Green: " + rightColorSensor.green(),
                            "Heading: " + getValidGyroHeading(),
                            "Front Range Sensor: " + frontRangeSensor.cmUltrasonic(),
                            "Back Range Sensor: " + backRangeSensor.cmUltrasonic()
                    }
            );
            idle();
        }
    }
}