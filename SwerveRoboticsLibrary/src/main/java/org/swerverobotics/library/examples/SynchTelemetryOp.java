package org.swerverobotics.library.examples;

import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.Autonomous;
import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.TeleOp;

import com.qualcomm.robotcore.util.*;

/**
 * An example that illustrates use of the telemetry dashboard and log
 */
@TeleOp(name="Telemetry (sync)", group="Swerve Examples")
@Disabled
public class SynchTelemetryOp extends SynchronousOpMode
    {
    @Override protected void main() throws InterruptedException
        {
        // Declare variables that will be used as our dashboard is updated. The variables
        // are declared 'final' so that they will be accessible inside the item expressions
        // of the dashboard.
        final ElapsedTime elapsed = new ElapsedTime();
        final int loopCountStart = getLoopCount();

        this.telemetry.log.setDisplayOldToNew(false);   // And we show the log in new to old order, just because we want to
        this.telemetry.log.setCapacity(10);             // We can control the number of lines used by the log

        // Wait until we've been given the ok to go
        this.waitForStart();
        
        // Go go gadget robot!
        while (this.opModeIsActive())
            {
            if (this.updateGamepads())
                {
                // There is new gamepad input available. We choose to log some of its state.
                if (this.gamepad1.left_bumper)  this.telemetry.log.add(format(elapsed) + ": left bumper pressed");
                if (this.gamepad1.right_bumper) this.telemetry.log.add(format(elapsed) + ": right bumper pressed");
                }

            // Update the telemetry dashboard with fresh values
            this.telemetry.addData("time",  format(elapsed));
            this.telemetry.addData("count", getLoopCount() - loopCountStart);
            this.telemetry.addData("rate",  format(elapsed.time()*1000.0 / (getLoopCount() - loopCountStart)) + "ms");

            // Update driver station and wait until there's something useful to do
            this.telemetry.update();
            this.idle();
            }
        }

    // A couple of handy functions for formatting data for the dashboard
    String format(ElapsedTime elapsed)
        {
        return String.format("%.1fs", elapsed.time());
        }
    String format(double d)
        {
        return String.format("%.1f", d);
        }
    }
