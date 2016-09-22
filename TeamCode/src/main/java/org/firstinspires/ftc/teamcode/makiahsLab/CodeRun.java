package org.firstinspires.ftc.teamcode.makiahsLab;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by makiah on 9/22/16.
 */

@Autonomous(name="Play Random Song", group="Makiah's Lab")

public class CodeRun extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        waitForStart();
        //Initialize components.
        CompletedUtilities.InitializeUtilityComponents(hardwareMap.appContext, telemetry);

        int randomChosenValue = (int) (Math.random() * 3);
        CompletedUtilities.PlaySong(CompletedUtilities.DownloadedSongs.values()[randomChosenValue]);

        while (opModeIsActive())
        {
            idle();
        }

        CompletedUtilities.OnExit();

    }
}
