package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name="Autonomous - Drive Along Wall", group = "Autonomous Group")

public class AutonomousDriveAlongWall extends _AutonomousBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        setMovementPower(.3);
        while(opModeIsActive())
            adjustMotorPowersBasedOnRangeSensors();
    }
}