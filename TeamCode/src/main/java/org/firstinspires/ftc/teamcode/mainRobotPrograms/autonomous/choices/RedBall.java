package org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.choices;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.BallAuto;

@Autonomous(name="Red Ball", group = "Auto Group")

public class RedBall extends BallAuto
{
    public void driverStationSaysGO() throws InterruptedException
    {
        runBallAutonomous (Alliance.RED);
    }
}