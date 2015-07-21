package org.swerverobotics.library.examples;

import android.util.Log;

import java.text.*;
import java.util.*;
import org.swerverobotics.library.*;
import com.qualcomm.robotcore.util.*;

/**
 * A synchronous version of the FTC-library-provided NullOp example.
 */
public class NullOp extends SynchronousOpMode
    {
    @Override protected void main() throws InterruptedException
        {
        final String startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        final ElapsedTime runtime = new ElapsedTime();

        this.dashboard.line(
            this.dashboard.item("count: ", new IFunc<Object>() { @Override public Object value() { return "Synch NullOp started at " + startDate; }}),
            this.dashboard.item("time: ",  new IFunc<Object>() { @Override public Object value() { return "running for " + runtime; }})
            );

        while (!this.stopRequested())
            {
            this.dashboard.update();
            this.idle();
            }
        }
    }
