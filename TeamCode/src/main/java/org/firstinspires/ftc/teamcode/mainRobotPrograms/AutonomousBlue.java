package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Autonomous - Blue Edition", group = "Autonomous Group")
//@Disabled

public class AutonomousBlue extends _AutonomousBase
{
    //Autonomous code for the Blue alliance

    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO()
    {
        sleep(0);
        driveForTime(0.8, 1500);
        turnToHeading (-110);
        driveForTime(0.8, 2400);
        harvester.setPower(0.5);
        sleep(3000);
    }
}