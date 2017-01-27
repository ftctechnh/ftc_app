package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Autonomous - Red (No Shoot) Edition", group = "Autonomous Group")

public class AutonomousRedNoShoot extends _AutonomousBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        driveForTime(-0.5, 1000);

        driveForTime(-0.5, 1200);

        turnToHeading(800, TurnMode.BOTH); //Doesn't use gyro.
        turnToHeading(-800, TurnMode.BOTH);
        driveForTime(-0.3, 1000);
    }
}