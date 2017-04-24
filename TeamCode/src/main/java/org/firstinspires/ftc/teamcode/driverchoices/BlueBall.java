package org.firstinspires.ftc.teamcode.driverchoices;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.maintypes.BallAuto;

@Autonomous(name="Blue Ball", group = "Auto Group")

public class BlueBall extends BallAuto
{
    public Alliance getAlliance ()
    {
         return Alliance.BLUE;
    }
}