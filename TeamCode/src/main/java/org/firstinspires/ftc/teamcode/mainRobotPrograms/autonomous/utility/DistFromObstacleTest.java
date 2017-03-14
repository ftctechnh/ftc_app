package org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.utility;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.AutonomousBase;

@Autonomous(name="Dist From Obstacle Test", group = "Utility Group")

public class DistFromObstacleTest extends AutonomousBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        driveUntilDistanceFromObstacle (40);
    }
}