package org.swerverobotics.library;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import org.swerverobotics.library.internal.Util;

import java.util.LinkedHashMap;
import java.util.concurrent.Semaphore;

/**
 * A class containing a small selection of handy utilities.
 */
public class SwerveUtil
    {
    static private class SimpleSoundPlayer implements SoundPool.OnLoadCompleteListener
        {
        //------------------------------------------------------------------------------------------
        // Types
        //------------------------------------------------------------------------------------------

        // Maps from resourceId to loaded soundId
        class LoadedSoundCache extends LinkedHashMap<Integer, Integer>
            {
            static final float loadFactor = 0.75f;

            final int capacity; // max number of cached sounds

            LoadedSoundCache(int capacity)
                {
                super((int)Math.ceil(capacity / loadFactor) + 1, loadFactor, true);
                this.capacity = capacity;
                }

            /** update the fact that this key has been just used, again */
            public void update(Integer key, Integer value)
                {
                put(key, value);
                }

            @Override
            protected boolean removeEldestEntry(Entry<Integer, Integer> eldest)
                {
                return size() > capacity;
                }

            @Override
            public Integer remove(Object key)
                {
                int soundId = super.remove(key);
                soundPool.unload(soundId);
                return soundId;
                }
            }

        //------------------------------------------------------------------------------------------
        // State
        //------------------------------------------------------------------------------------------

        Semaphore        semaphore;
        SoundPool        soundPool;
        LoadedSoundCache loadedSounds;

        //------------------------------------------------------------------------------------------
        // Construction
        //------------------------------------------------------------------------------------------

        SimpleSoundPlayer()
            {
            semaphore = new Semaphore(0);
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            soundPool.setOnLoadCompleteListener(this);
            loadedSounds = new LoadedSoundCache(4);
            }

        //------------------------------------------------------------------------------------------
        // Operations
        //------------------------------------------------------------------------------------------

        synchronized public void play(Context context, int resourceId)
            {
            try {
                int soundId = load(context, resourceId);
                soundPool.play(soundId, 1, 1, 1, 0, 1);
                }
            catch (Exception e)
                {
                Log.e(SynchronousOpMode.LOGGING_TAG, String.format("exception ignored in playSound: %s", Util.getStackTrace(e)));
                }
            }

        private int load(Context context, int resourceId)
            {
            Integer soundId = loadedSounds.get(resourceId);
            if (soundId == null)
                {
                // Request that the sound be loaded
                soundId = soundPool.load(context, resourceId, 1);

                // Wait until that's complete
                try {
                    semaphore.acquire();
                    }
                catch (InterruptedException e)
                    {
                    Thread.currentThread().interrupt();
                    }

                // Remember the resourceId -> soundId mapping
                loadedSounds.put(resourceId, soundId);
                }
            else
                {
                // Update the MRU notion of which sounds have been recently used
                loadedSounds.update(resourceId, soundId);
                }

            // Return the id by which the resource will be played
            return soundId;
            }

        @Override
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
            {
            semaphore.release();
            }
        }

    static private SimpleSoundPlayer simpleSoundPlayer = new SimpleSoundPlayer();

    /**
     * Plays a sound given the sound's identity as a (raw) resource.
     * @param context       the application context in which to play the sound
     * @param resourceId    the resource number of the sound to play
     */
    public static void playSound(Context context, int resourceId)
        {
        simpleSoundPlayer.play(context, resourceId);
        }
    }
