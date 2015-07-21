package org.swerverobotics.library.examples;

import android.util.Log;

import java.text.*;
import java.util.*;
import org.swerverobotics.library.*;
import com.qualcomm.robotcore.util.*;

/**
 * A synchronous version of the FTC-library-provided NullOop example.
 */
public class NullOp extends SynchronousOpMode
    {
    @Override protected void main() throws InterruptedException
        {
        String startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        ElapsedTime runtime = new ElapsedTime();

        while (!this.stopRequested())
            {
            // Print telemetry messages
            this.telemetry.addData("1 Start", "Synch NullOp started at " + startDate);
            this.telemetry.addData("2 Status", "running for " + runtime.toString());

            // For illustration purposes, print similar messages to the Android
            // Studio Logcat window.
            Log.i("1 Start", "Synch NullOp started at " + startDate);
            Log.i("2 Status", "running for " + runtime.toString());

            // Go to sleep for a while so that we don't flood the world with output.
            Thread.sleep(1000);
            }
        }
    }
