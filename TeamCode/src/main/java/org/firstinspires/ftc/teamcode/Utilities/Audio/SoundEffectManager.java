package org.firstinspires.ftc.teamcode.Utilities.Audio;

import android.content.Context;

import com.qualcomm.ftccommon.SoundPlayer;

import org.firstinspires.ftc.ftccommon.external.SoundPlayingRobotMonitor;
import org.firstinspires.ftc.teamcode.R;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class SoundEffectManager {

    public static final Map<String, Integer> PACMAN_AUDIO = new TreeMap<>();
    static {
        PACMAN_AUDIO.put("begin", R.raw.pacman_begin);
        PACMAN_AUDIO.put("mineral", R.raw.pacman_mineral);
        PACMAN_AUDIO.put("move", R.raw.pacman_move);
        PACMAN_AUDIO.put("end", R.raw.pacman_end);

    }

    Context context;
    Map<String, Integer> audioFiles;
    boolean playing;

    public SoundEffectManager (Context context, Map<String, Integer> audioFiles) {
        this.context = context;
        this.audioFiles = audioFiles;
        this.playing = false;
        SoundPlayer.getInstance().setMasterVolume(1.0F);

        for (Map.Entry<String, Integer> entry : audioFiles.entrySet()) {
            SoundPlayer.getInstance().prefillSoundCache(entry.getValue());
        }
    }

    public void playEffect(String effect) {
        SoundPlayer.getInstance().startPlaying(context, audioFiles.get(effect));
        this.playing = true;
    }

    public void stopAll() {
        if (this.playing) {
            this.playing = false;
            SoundPlayer.getInstance().stopPlayingAll();
        }
    }
}
