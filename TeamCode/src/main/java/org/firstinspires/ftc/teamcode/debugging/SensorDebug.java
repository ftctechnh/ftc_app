package org.firstinspires.ftc.teamcode.debugging;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.enhancements.ConsoleManager;
import org.firstinspires.ftc.teamcode.enhancements.ProgramFlow;

@Autonomous(name = "Sensor Debug", group = "Utility Group")

public class SensorDebug extends AutoBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        while (true) {
            ConsoleManager.outputConstantDataToDrivers(
                    new String[]
                    {
                            "Option 1 Color Sensor",
                            "ARGB: " + option1ColorSensor.argb() + " Alpha: " + option1ColorSensor.alpha(),
                            "Blue: " + option1ColorSensor.blue() + " Red: " + option1ColorSensor.red(),
                            "Option 2 Color Sensor",
                            "ARGB: " + option2ColorSensor.argb() + " Alpha: " + option2ColorSensor.alpha(),
                            "Blue: " + option2ColorSensor.blue() + " Red: " + option2ColorSensor.red(),
                            "Bottom Color Sensor",
                            "ARGB: " + bottomColorSensor.argb() + " Alpha: " + bottomColorSensor.alpha(),
                            "Blue: " + bottomColorSensor.blue() + " Red: " + bottomColorSensor.red(),
                            "Particle Color Sensor",
                            "ARGB: " + particleColorSensor.argb() + " Alpha: " + particleColorSensor.alpha(),
                            "Blue: " + particleColorSensor.blue() + " Red: " + particleColorSensor.red(),
                            "Heading: " + getValidGyroHeading(),
                            "Front Range Sensor: " + frontRangeSensor.cmUltrasonic(),
                            "Back Range Sensor: " + sideRangeSensor.cmUltrasonic(),
                            "Harvester encoder: " + harvester.encoderMotor.getCurrentPosition (),
                            "Flywheel encoder: " + flywheels.encoderMotor.getCurrentPosition (),
                            "L motor encoder: " + leftDrive.encoderMotor.getCurrentPosition (),
                            "R motor encoder: " + rightDrive.encoderMotor.getCurrentPosition ()
                    }
            );
            ProgramFlow.pauseForSingleFrame ();
        }
    }
}