package org.swerverobotics.library.examples;

import android.util.Log;

import java.text.*;
import java.util.*;
import org.swerverobotics.library.*;
import com.qualcomm.robotcore.util.*;

/**
 * An OpMode that gives more examples of how telemetry can be used
 */
public class TelemetryOp extends SynchronousOpMode
    {
    int count;
    ElapsedTime runtime;
    Random random;

    @Override protected void main() throws InterruptedException
        {
        this.random = new Random();
        this.count = 0;
        this.runtime = new ElapsedTime();

        this.dashboard.clear();
        this.dashboard.line(
            this.dashboard.item("count: ", new ITelemetryValue() { @Override public Object value() { return count; }}),
            this.dashboard.item("time: ",  new ITelemetryValue() { @Override public Object value() { return runtime.time(); }})
            );

        while (!this.stopRequested())
            {
            count++;

            if (this.random.nextInt(5) == 0)
                {
                this.log.add("logging jackpot at count = " + this.count);
                }

            this.dashboard.update();
            this.idle();
            }
        }
    }
