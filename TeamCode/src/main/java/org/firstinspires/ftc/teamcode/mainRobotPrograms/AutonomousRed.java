package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Autonomous - Red Edition", group = "Autonomous Group")

public class AutonomousRed extends _AutonomousBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        driveForTime(0.5, 1000);

        flywheels.setPower(0.4);
        sleep(2000);
        harvester.setPower(1.0);

        sleep(4000);

        harvester.setPower(0);
        flywheels.setPower(0);

        driveForTime(0.5, 1200);

        turnToHeading(800, TurnMode.BOTH, 3000); //Doesn't use gyro.
        turnToHeading(-800, TurnMode.BOTH, 3000);
        driveForTime(0.3, 1000);
    }
}