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
        //Play a random song.
        PlayAudio(DownloadedSongs.values()[(int) (Math.random() * DownloadedSongs.values().length)]);
        //Idle indefinitely.
        while (true)
            idle();
    }
}
