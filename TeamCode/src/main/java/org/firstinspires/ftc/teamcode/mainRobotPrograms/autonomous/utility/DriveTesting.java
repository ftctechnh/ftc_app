package org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.utility;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.AutonomousBase;

@Autonomous(name="Drive Testing", group = "Test Group")

public class DriveTesting extends AutonomousBase
{
    //Called after runOpMode() has finished initializing by BaseFunctions.
    protected void driverStationSaysGO() throws InterruptedException
    {
        int sign = 1;
        //Keep driving back and forth.  Use this to determine if the robot can drive straight for long periods of time.
        while (opModeIsActive ())
        {
            driveForDistance (sign * (Math.random () * 0.7 + 0.3), 2000);
            sign *= -1;
        }
    }
}