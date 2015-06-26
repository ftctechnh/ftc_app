package com.fellowshipoftheloosescrews.opmodes;

import android.os.Debug;
import android.util.Log;

/**
 * Created by Thomas on 6/25/2015.
 * Tests the ThreadedOpMode class
 */
public class ThreadingTest extends ThreadedOpMode {

    public long cyclecount = 0;

    @Override
    public void onStart() {
        Log.d("Status", "Starting");
    }

    @Override
    public void run() {
        while(isRunning) {
            cyclecount++;
            Log.d("Status", "Looping");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoop() {
        telemetry.addData("cycle count", cyclecount);
    }

    @Override
    public void onStop() {
        Log.d("Status", "Stopped");
    }
}
