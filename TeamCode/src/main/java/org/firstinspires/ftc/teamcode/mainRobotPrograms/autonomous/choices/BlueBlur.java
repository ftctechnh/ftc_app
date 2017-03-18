package org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.choices;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.BeaconAuto;

@Autonomous(name="Blue Blur", group = "Auto Group")

public class BlueBlur extends BeaconAuto
{
    public void driverStationSaysGO() throws InterruptedException
    {
        runBeaconAutonomous (Alliance.BLUE);
    }
}