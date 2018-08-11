package org.firstinspires.ftc.teamcode.Year_2018_19;

import android.content.Context;
import android.media.MediaPlayer;
import org.firstinspires.ftc.teamcode.R;
import java.io.IOException;

public class SongPlayer {

    private static MediaPlayer mediaPlayer = null;

    public static void playMusic(Context context) {
        if (mediaPlayer == null) mediaPlayer = MediaPlayer.create(context, R.raw.music);
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            try { mediaPlayer.prepare(); }
            catch (IOException e) {}
        }
    }

    public static void playBB8Sound(Context context) {
        if (mediaPlayer == null) mediaPlayer = MediaPlayer.create(context, R.raw.BB8);
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
    }

    public static void playR2D2Sound (Context context) {
        if (mediaPlayer == null) mediaPlayer = MediaPlayer.create(context, R.raw.R2D2);
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
    }
}
