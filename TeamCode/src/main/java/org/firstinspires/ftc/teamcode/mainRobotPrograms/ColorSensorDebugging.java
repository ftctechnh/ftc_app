package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Color Sensor Debugger", group = "Autonomous Group")

public class ColorSensorDebugging extends _AutonomousBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO()
    {
        leftSensorServo.setPosition(LEFT_SERVO_OPEN);
        rightSensorServo.setPosition(RIGHT_SERVO_OPEN);

        while (opModeIsActive()) {
            outputConstantLinesToDriverStation(new String[]
                    {
                    "Bottom Color Sensor",
                    "--------------------",
                    "ARGB: " + bottomColorSensor.argb() + " Alpha: " + bottomColorSensor.alpha(),
                    "Blue: " + bottomColorSensor.blue() + " Red: " + bottomColorSensor.red(),
                    "Green: " + bottomColorSensor.green(),
                    "Left Color Sensor",
                    "--------------------",
                    "ARGB: " + leftColorSensor.argb() + " Alpha: " + leftColorSensor.alpha(),
                    "Blue: " + leftColorSensor.blue() + " Red: " + leftColorSensor.red(),
                    "Green: " + leftColorSensor.green(),
                    "Right Color Sensor",
                    "--------------------",
                    "ARGB: " + rightColorSensor.argb() + " Alpha: " + rightColorSensor.alpha(),
                    "Blue: " + rightColorSensor.blue() + " Red: " + rightColorSensor.red(),
                    "Green: " + rightColorSensor.green(),
                    "Heading: " + getValidGyroHeading()
            });
            idle();
        } // alpha > 10 = white
//        driveForTime(0.8, 2000);
    }
}