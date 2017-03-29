package org.firstinspires.ftc.teamcode.debugging;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.enhancements.ConsoleManager;
import org.firstinspires.ftc.teamcode.enhancements.SimplisticThread;
import org.firstinspires.ftc.teamcode.MainRobotBase;

@Autonomous(name = "Thread Debugging", group = "Utility Group")

public class ThreadTesting extends MainRobotBase
{
    public void driverStationSaysGO() throws InterruptedException
    {
        //Create new debugging thread.
        new SimplisticThread (2000)
        {
            @Override
            public void actionPerUpdate ()
            {
                ConsoleManager.outputNewLineToDrivers ("Thread update");
            }
        };

        while (true)
            idle();
    }
}
