package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by makiah on 9/23/16.
 */

@Autonomous(name="Beta Test Code", group = "Beta Testing")
public class BetaTestRunner extends AutonomousBase
{
    protected void driverStationSaysGO() throws InterruptedException
    {
        PlayAudio(DownloadedSongs.values()[(int) (Math.random() * DownloadedSongs.values().length)]);
        while (true)
            idle();
    }
}
