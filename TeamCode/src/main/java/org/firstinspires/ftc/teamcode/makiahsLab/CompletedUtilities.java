package org.firstinspires.ftc.teamcode.makiahsLab;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.format.Time;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by makiah on 9/22/16.
 */
public class CompletedUtilities
{

    private static Context hardwareContext;
    private static Telemetry output;
    private static boolean initialized = false;

    public static void InitializeUtilityComponents(Context hardwareContext1, Telemetry output1)
    {
        hardwareContext = hardwareContext1;
        output = output1;
        initialized = true;
    }

    public enum DownloadedSongs
    {
        JOHN_CENA_INTRO,
        MISSION_IMPOSSIBLE,
        RUSSIAN_NATIONAL_ANTHEM
    }

    private  static MediaPlayer mediaPlayer = null;
    public static void PlaySong(DownloadedSongs song)
    {
        int selectedSong = com.qualcomm.ftcrobotcontroller.R.raw.jcena;
        switch (song)
        {
            case JOHN_CENA_INTRO:
                selectedSong = com.qualcomm.ftcrobotcontroller.R.raw.jcena;
                break;
            case MISSION_IMPOSSIBLE:
                selectedSong = com.qualcomm.ftcrobotcontroller.R.raw.missionimpossible;
                break;
            case RUSSIAN_NATIONAL_ANTHEM:
                selectedSong = com.qualcomm.ftcrobotcontroller.R.raw.nationalanthem;
                break;
        }
        mediaPlayer = MediaPlayer.create(hardwareContext, selectedSong);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer1) {
                mediaPlayer1.release();
            }
        });
        output.addData("Playing ", song.toString());
    }

    public static void OnExit()
    {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
