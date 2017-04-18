package org.firstinspires.ftc.teamcode.music;

import android.content.Context;
import android.media.MediaPlayer;

import org.firstinspires.ftc.teamcode.console.NiFTConsole;
import org.firstinspires.ftc.teamcode.threads.NiFTFlow;

//Singleton class: only allows playing one file at a time (currently)
public class NiFTMusic
{
    //Required components in order to create a MediaPlayer instance.
    private static Context opModeAppContext;
    public static void initializeWith (Context appContext)
    {
        opModeAppContext = appContext;
    }

    /**
     * The DownloadedSongs enum allows easy access to provided MP3 files, so that you just have to call play (DownloadedSongs.song_name_here);
     */
    public enum DownloadedSongs
    {
        IMPERIAL_MARCH
    }
    private static MediaPlayer mediaPlayer = null;
    public static void play (DownloadedSongs choice)
    {
        try
        {
            int selectedSong = com.qualcomm.ftcrobotcontroller.R.raw.imperialmarch;

            //Add new mp3s here.
            switch (choice)
            {
                case IMPERIAL_MARCH:
                    selectedSong = com.qualcomm.ftcrobotcontroller.R.raw.imperialmarch;
                    break;
            }

            mediaPlayer = MediaPlayer.create(opModeAppContext, selectedSong);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                public void onCompletion(MediaPlayer mediaPlayer1)
                {
                    mediaPlayer1.release();
                }
            });

            NiFTConsole.outputNewSequentialLine ("Playing " + choice.toString());

            NiFTFlow.pauseForMS (1000); //Give the MediaPlayer some time to initialize, and register that a song is being played.
        }
        catch (Exception e)
        {
            NiFTConsole.outputNewSequentialLine ("Music error: " + e.getMessage ());
        }
    }

    //Used to make the media player stopEasyTask playing audio, and also to prevent excess memory allocation from being taken up.
    public static void quit ()
    {
        if (mediaPlayer != null)
        {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop(); //stopEasyTask playing
            mediaPlayer.release(); //prevent resource allocation
            mediaPlayer = null; //nullify the reference.
        }
    }
}
