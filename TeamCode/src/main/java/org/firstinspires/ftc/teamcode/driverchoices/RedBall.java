package org.firstinspires.ftc.teamcode.driverchoices;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.maintypes.BallAuto;

@Autonomous(name="Red Ball", group = "Auto Group")

public class RedBall extends BallAuto
{
    public void driverStationSaysGO() throws InterruptedException
    {
        runBallAutonomous (Alliance.RED);
    }
}