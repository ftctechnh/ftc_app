package org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.choices;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.BallAuto;

@Autonomous(name="Blue Ball", group = "Auto Group")

public class BlueBall extends BallAuto
{
    public void driverStationSaysGO() throws InterruptedException
    {
        runBallAutonomous (Alliance.BLUE);
    }
}