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
        final ElapsedTime elapsed = new ElapsedTime();
        final int loopCountStart = loopCount.get();

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
            this.telemetry.dashboard.item("mean rate: ", new IFunc<Object>() { @Override public Object value()
                {
                return format(elapsed.time() / (loopCount.get()-loopCountStart) * 1000) + "ms";
                }})
            );

        while (!this.stopRequested())
            {
            if (this.newGamePadInputAvailable())
                {
                if (this.gamepad1.left_bumper())
                    {
                    this.telemetry.log.add(format(elapsed) + ": left bumper pressed");
                    }

                this.telemetry.dashboard.update();
                }
            else
                {
                this.telemetry.dashboard.update();
                this.idle();
                }
            }
        }

    String format(ElapsedTime elapsed)
        {
        return String.format("%.1f", elapsed.time());
        }
    String format(double d)
        {
        return String.format("%.1f", d);
        }
    }
