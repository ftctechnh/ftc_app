package org.firstinspires.ftc.teamcode.Year_2018_19;

import android.content.Context;
import android.media.MediaPlayer;
import org.firstinspires.ftc.teamcode.R;
import java.io.IOException;

public class SongPlayer {
    //The player handling the audio
    private static MediaPlayer mediaPlayer = null;
    //Start the wubs
    public static void start(Context context) {
        if (mediaPlayer == null) mediaPlayer = MediaPlayer.create(context, R.raw.music);
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
    }
    //Stop the wubs
    public static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            try { mediaPlayer.prepare(); }
            catch (IOException e) {}
        }
    }
}
