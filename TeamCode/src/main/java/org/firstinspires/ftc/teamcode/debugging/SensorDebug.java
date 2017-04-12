package org.firstinspires.ftc.teamcode.debugging;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.threading.ProgramFlow;

@Autonomous(name = "Sensor Debug", group = "Utility Group")

public class SensorDebug extends AutoBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        ConsoleManager.ProcessConsole processConsole = new ConsoleManager.ProcessConsole ("Sensor Debugging");

        while (true) {
            processConsole.updateWith (
                    "Option 1 Color Sensor",
                    "ARGB: " + option1ColorSensor.sensor.argb() + " Alpha: " + option1ColorSensor.sensor.alpha(),
                    "Blue: " + option1ColorSensor.sensor.blue() + " Red: " + option1ColorSensor.sensor.red(),
                    "Option 2 Color Sensor",
                    "ARGB: " + option2ColorSensor.sensor.argb() + " Alpha: " + option2ColorSensor.sensor.alpha(),
                    "Blue: " + option2ColorSensor.sensor.blue() + " Red: " + option2ColorSensor.sensor.red(),
                    "Bottom Color Sensor",
                    "ARGB: " + bottomColorSensor.sensor.argb() + " Alpha: " + bottomColorSensor.sensor.alpha(),
                    "Blue: " + bottomColorSensor.sensor.blue() + " Red: " + bottomColorSensor.sensor.red(),
                    "Particle Color Sensor",
                    "ARGB: " + particleColorSensor.sensor.argb() + " Alpha: " + particleColorSensor.sensor.alpha(),
                    "Blue: " + particleColorSensor.sensor.blue() + " Red: " + particleColorSensor.sensor.red(),
                    "Heading: " + gyroscope.getValidGyroHeading(0),
                    "Front Range Sensor: " + frontRangeSensor.getVALIDDistCM (),
                    "Back Range Sensor: " + sideRangeSensor.getVALIDDistCM (),
                    "Harvester encoder: " + harvester.encoderMotor.getCurrentPosition (),
                    "Flywheel encoder: " + flywheels.encoderMotor.getCurrentPosition (),
                    "L motor encoder: " + leftDrive.encoderMotor.getCurrentPosition (),
                    "R motor encoder: " + rightDrive.encoderMotor.getCurrentPosition ()
            );

            ProgramFlow.pauseForSingleFrame ();
        }
    }
}