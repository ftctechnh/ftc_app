package org.swerverobotics.library;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import org.swerverobotics.library.internal.Util;

/**
 * A class containing a small selection of handy utilities.
 */
public class SwerveUtil
    {
    /**
     * Plays a sound given the sounds identity as a (raw) resource.
     * @param context       the application context in which to play the sound
     * @param resourceId    the resource number of the sound to play
     */
    public static void playSound(Context context, int resourceId)
        {
        try {
            // Get ourselves a media player for the resource
            MediaPlayer mediaPlayer = MediaPlayer.create(context, resourceId);

            // Start the sound a-playing and wait until it's done
            mediaPlayer.start();
            while (mediaPlayer.isPlaying())
                Thread.yield();

            // Shutdown the media player entirely and cleanly. reset() is needed to
            // have the internal media player event handler remove it's callbacks so
            // it won't later get notifications. release() is good citizenship, and is
            // certainly a good thing to do, but it might not strictly be needed.
            mediaPlayer.reset();
            mediaPlayer.release();
            }
        catch (Exception e)
            {
            Log.e(SynchronousOpMode.LOGGING_TAG, String.format("exception ignored in playSound: %s", Util.getStackTrace(e)));
            }
        }
    }
