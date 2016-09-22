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

    public static String QueryUserInput()
    {
        String result = "";
        try {
            //Create the InputStream
            InputStream inputStream = hardwareContext.openFileInput("FTCRobotConfig-" + Time.HOUR + ":" + Time.MINUTE + ":" + Time.SECOND + ".txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                result = bufferedReader.readLine();

                inputStream.close();
            }
        }
        catch (Exception e) {
            output.addData("Exception", "Error reading config file: " + e.toString());
        }

        return result;
    }

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
        final MediaPlayer mediaPlayer = MediaPlayer.create(hardwareContext, selectedSong);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer1) {
                mediaPlayer1.release();
            }
        });
    }
}
