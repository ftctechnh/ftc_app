package org.firstinspires.ftc.teamcode.debugging;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ImprovedOpModeBase;
import org.firstinspires.ftc.teamcode.enhancements.ConsoleManager;
import org.firstinspires.ftc.teamcode.enhancements.SimplisticThread;
import org.firstinspires.ftc.teamcode.MainRobotBase;

@Autonomous(name = "Thread Debugging", group = "Utility Group")

/**
 * HAVE TO USE BOOLEAN
 */

public class ThreadTesting extends ImprovedOpModeBase
{
    public void initializeHardware() {}

    public void driverStationSaysGO() throws InterruptedException
    {
        //Create new debugging thread.
        SimplisticThread t1 = new SimplisticThread (2000)
        {
            long i = 0;
            @Override
            public void actionPerUpdate ()
            {
                ConsoleManager.outputNewLineToDrivers ("Thread update " + i);
                i++;
            }
        };

        //Create new debugging thread.
        SimplisticThread t2 = new SimplisticThread (3000)
        {
            long i = 0;
            @Override
            public void actionPerUpdate ()
            {
                ConsoleManager.outputNewLineToDrivers ("Thread update 2 " + i);
                i++;
            }
        };

        sleep(4000);

        t1.stop ();

        sleep (4000);

//        t2.stop ();
        t1.start ();

        sleep (4000);

//        t2.start ();

        while (true)
            idle();
    }
}
