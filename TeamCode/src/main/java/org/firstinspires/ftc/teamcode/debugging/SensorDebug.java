/**
 * This code is used to debug the sensors (to see if they are operational).
 */

package org.firstinspires.ftc.teamcode.debugging;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.console.NiFTConsole;
import org.firstinspires.ftc.teamcode.threads.NiFTFlow;

@Autonomous(name = "Sensor Debug", group = "Utility Group")

public class SensorDebug extends AutoBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        NiFTConsole.ProcessConsole processConsole = new NiFTConsole.ProcessConsole ("Sensor Debugging");
        NiFTConsole.outputNewSequentialLine ("created sensor debugging console");

        while (true)
        {
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
                    "Heading: " + gyroscope.getValidGyroHeading(),
                    "Front Range Sensor: " + frontRangeSensor.sensor.cmUltrasonic (),
                    "Back Range Sensor: " + sideRangeSensor.sensor.cmUltrasonic (),
                    "Harvester encoder: " + harvester.ENCODER_MOTOR.getCurrentPosition (),
                    "Flywheel encoder: " + flywheels.ENCODER_MOTOR.getCurrentPosition (),
                    "L motor encoder: " + leftDrive.ENCODER_MOTOR.getCurrentPosition (),
                    "R motor encoder: " + rightDrive.ENCODER_MOTOR.getCurrentPosition ()
            );

            NiFTFlow.pauseForSingleFrame ();
        }
    }
}