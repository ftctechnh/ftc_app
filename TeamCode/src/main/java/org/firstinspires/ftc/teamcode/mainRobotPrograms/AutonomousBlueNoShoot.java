package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Autonomous - Blue (No Shoot) Edition", group = "Autonomous Group")

public class AutonomousBlueNoShoot extends _AutonomousBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        driveForTime(-0.5, 1000);

        driveForTime(-0.5, 1200);

        turnToHeading(-400, TurnMode.BOTH); //Doesn't use gyro.
        turnToHeading(1000, TurnMode.BOTH);
        driveForTime(-0.3, 1000);
    }
}