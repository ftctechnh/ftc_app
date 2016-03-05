package org.swerverobotics.library.internal.tests;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.MovingStatistics;

import org.swerverobotics.library.SynchronousOpMode;
import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.TeleOp;

@TeleOp(name="Time the Timer (Synch)", group="Swerve Tests")
@Disabled
public class TimeTheTimer extends SynchronousOpMode
    {
    MovingStatistics stats = new MovingStatistics(1000);

    @Override
    protected void main() throws InterruptedException
        {
        waitForStart();
        telemetry.log.add("starting");

        while (opModeIsActive())
            {
            int cloop = 100;
            int perLoop = 100 * 10;
            long start = System.nanoTime();
            for (int i=0; i < cloop; i++)
                {
                read100();
                read100();
                read100();
                read100();
                read100();

                read100();
                read100();
                read100();
                read100();
                read100();
                }
            long end = System.nanoTime();

            int count = cloop * perLoop;
            long nsDuration = (end - start);
            stats.add(nsDuration / count);

            telemetry.addData("ns/read count", format(stats.getCount()));
            telemetry.addData("ns/read mean", format(stats.getMean()));
            telemetry.addData("ns/read sd", format(stats.getStandardDeviation()));
            telemetry.update();
            idle();
            }

        telemetry.log.add("done");
        }

    String format(double f) { return String.format("%.2f", f); }
    String format(long l) { return String.format("%d", l); }
    String format(int i) { return String.format("%d", i); }

    void read100()
        {
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();

        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();

        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();

        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();

        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();

        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();

        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();

        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();

        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();

        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        System.nanoTime();
        }
    }

