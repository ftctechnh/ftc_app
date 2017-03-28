package org.firstinspires.ftc.teamcode.debugging;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.enhancements.EZThread;
import org.firstinspires.ftc.teamcode.MainRobotBase;

@Autonomous(name = "Thread Debugging", group = "Utility Group")

public class ThreadTesting extends MainRobotBase
{
    public void driverStationSaysGO() throws InterruptedException
    {
        //Create new debugging thread.
        new EZThread (2000)
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
