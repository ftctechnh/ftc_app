package org.firstinspires.ftc.teamcode.debugging;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.maintypes.ResetAuto;

@Autonomous(name="Drive Backward", group = "Utility Group")

public class DriveBackward extends ResetAuto
{
    @Override
    protected int getSign ()
    {
        return -1;
    }
}