package org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.choices;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.BeaconAuto;

@Autonomous(name="Red Redemption", group = "Auto Group")

public class RedRedemption extends BeaconAuto
{
    public void driverStationSaysGO() throws InterruptedException
    {
        runBeaconAutonomous (Alliance.RED);
    }
}