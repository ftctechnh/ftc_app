package org.firstinspires.ftc.teamcode.makiahsLab;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by makiah on 9/22/16.
 */

@Autonomous(name="Beta Functions", group="Makiah's Lab")

public class CodeRun extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        CompletedUtilities.InitializeUtilityComponents(hardwareMap.appContext, telemetry);

        String response = "3";
        if (response == null || response.equals(""))
            response = "3";
        int answer = 3;//Integer.parseInt(response);
        CompletedUtilities.DownloadedSongs desiredSong = CompletedUtilities.DownloadedSongs.JOHN_CENA_INTRO;
        switch (answer)
        {
            case 1:
                desiredSong = CompletedUtilities.DownloadedSongs.JOHN_CENA_INTRO;
                break;
            case 2:
                desiredSong = CompletedUtilities.DownloadedSongs.MISSION_IMPOSSIBLE;
                break;
            case 3:
                desiredSong = CompletedUtilities.DownloadedSongs.RUSSIAN_NATIONAL_ANTHEM;
                break;
            default:
                telemetry.addData("ERROR IN INPUT", "");
                break;
        }
        CompletedUtilities.PlaySong(desiredSong);

        waitForStart();
    }
}
