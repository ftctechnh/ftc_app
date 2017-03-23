package org.firstinspires.ftc.teamcode.driverchoices;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.maintypes.BeaconAuto;

@Autonomous(name="Red Redemption", group = "Auto Group")

public class RedRedemption extends BeaconAuto
{
    public void setAlliance()
    {
        alliance = Alliance.BLUE;
    }
}