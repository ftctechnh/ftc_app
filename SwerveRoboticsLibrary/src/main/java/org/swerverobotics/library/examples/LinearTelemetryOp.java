package org.swerverobotics.library.examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An example that illustrates use of the telemetry dashboard and log in a linear opmode
 */
@TeleOp(name="Telemetry (linear)", group="Swerve Examples")
@Disabled
public class LinearTelemetryOp extends LinearOpMode
    {
    TelemetryDashboardAndLog telemetry;

    @Override public void runOpMode() throws InterruptedException
        {
        final ElapsedTime elapsed = new ElapsedTime();

        // Create a little helper utility that counts loops for us
        final IOpModeLoopCounter loopCounter = ClassFactory.createLoopCounter(this);

        try {
            this.telemetry = new TelemetryDashboardAndLog();
            this.telemetry.log.setDisplayOldToNew(false);   // And we show the log in new to old order, just because we want to
            this.telemetry.log.setCapacity(10);             // We can control the number of lines used by the log

            // Wait until we've been given the ok to go
            this.waitForStart();
            final int loopCountStart = loopCounter.getLoopCount();

            // Go go gadget robot!
            while (this.opModeIsActive())
                {
                if (this.gamepad1.left_bumper)  { this.telemetry.log.add(format(elapsed) + ": left bumper pressed");  while (opModeIsActive() && this.gamepad1.left_bumper) Thread.yield(); }
                if (this.gamepad1.right_bumper) { this.telemetry.log.add(format(elapsed) + ": right bumper pressed"); while (opModeIsActive() && this.gamepad1.right_bumper) Thread.yield(); }

                // Update the telemetry dashboard with fresh values
                this.telemetry.addData("time",  format(elapsed));
                this.telemetry.addData("count", loopCounter.getLoopCount() - loopCountStart);
                this.telemetry.addData("ms/loop", format(elapsed.time() * 1000.0 / (loopCounter.getLoopCount() - loopCountStart)) + "ms");

                // Update driver station and wait until there's something useful to do
                this.telemetry.update();
                this.waitOneFullHardwareCycle();
                }
            }
        finally
            {
            loopCounter.close();
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
