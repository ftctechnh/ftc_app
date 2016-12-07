package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Autonomous - Red Edition", group = "Autonomous Group")
//@Disabled

public class AutonomousRed extends _AutonomousBase
{
    //Autonomous code for the Red alliance

    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO()
    {
        sleep(0);
        driveForTime(0.8, 1500); // Drive to hit cap ball
        turnToHeading(110); // Turn towards corner vortex
        driveForTime(1, 2000); // Drive onto corner vortex
        harvester.setPower(0.5); // Score pre-loaded balls
        sleep(3000);
    }
}