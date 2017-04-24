package org.firstinspires.ftc.teamcode.driverchoices;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.maintypes.BeaconAuto;

@Autonomous(name="Blue Blur", group = "Auto Group")

public class BlueBlur extends BeaconAuto
{
    public Alliance getAlliance ()
    {
        return Alliance.BLUE;
    }
}