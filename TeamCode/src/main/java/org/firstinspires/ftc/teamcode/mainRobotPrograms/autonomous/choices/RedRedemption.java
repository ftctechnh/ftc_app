package org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.choices;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.BeaconAuto;

@Autonomous(name="Red Redemption", group = "Auto Group")

public class RedRedemption extends BeaconAuto
{
    protected Alliance setAlliance()
    {
        return Alliance.RED;
    }
}