package org.swerverobotics.library.examples;

import java.text.*;
import java.util.*;
import org.swerverobotics.library.*;
import com.qualcomm.robotcore.util.*;

/**
 * An example that illustrates use of the telemetry dashboard and log
 */
public class TelemetryOp extends SynchronousOpMode
    {
    @Override protected void main() throws InterruptedException
        {
        // Declare variables that will be used as our dashboard is updated. The variables
        // are declared 'final' so that they will be accessible inside the item expressions
        // of the dashboard.
        final ElapsedTime elapsed = new ElapsedTime();
        final int loopCountStart = loopCount.get();

        // Configure the dashboard. Here, it will have one line, which will contain three items
        this.telemetry.dashboard.line
            (
            this.telemetry.dashboard.item("time: ",  new IFunc<Object>() { @Override public Object value()
                {
                return format(elapsed);
                }}),
            this.telemetry.dashboard.item("count: ", new IFunc<Object>() { @Override public Object value()
                {
                return loopCount.get() - loopCountStart;
                }}),
            this.telemetry.dashboard.item("rate: ", new IFunc<Object>() { @Override public Object value()
                {
                return format(elapsed.time() / (loopCount.get()-loopCountStart) * 1000) + "ms";
                }})
            );

        // Go go gadget robot!
        while (!this.stopRequested())
            {
            if (this.newGamePadInputAvailable())
                {
                // There is (likely) new gamepad input available.
                
                // Put out a log message if the left bumper is pressed
                if (this.gamepad1.left_bumper())
                    {
                    this.telemetry.log.add(format(elapsed) + ": left bumper pressed");
                    }

                // Emit telemetry with the freshest possible values
                this.telemetry.dashboard.update();
                }
            else
                {
                // There's no new gamepad input available.

                // Emit any telemetry that hasn't been sent in a while
                this.telemetry.dashboard.update();

                // Let the rest of the system run until there's a stimulus from the robot controller runtime.
                this.idle();
                }
            }
        }

    // A couple of handy functions for formatting data for the dashboard
    String format(ElapsedTime elapsed)
        {
        return String.format("%.1f", elapsed.time());
        }
    String format(double d)
        {
        return String.format("%.1f", d);
        }
    }
