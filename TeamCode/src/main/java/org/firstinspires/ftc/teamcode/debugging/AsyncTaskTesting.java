package org.firstinspires.ftc.teamcode.debugging;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ImprovedOpModeBase;
import org.firstinspires.ftc.teamcode.threading.ProgramFlow;
import org.firstinspires.ftc.teamcode.threading.EasyAsyncTask;

@Autonomous(name = "AsyncTask Debugging", group = "Utility Group")

/**
 * HAVE TO USE BOOLEAN
 */

public class AsyncTaskTesting extends ImprovedOpModeBase
{
    public void initializeHardware() {}

    public void driverStationSaysGO() throws InterruptedException
    {
        new EasyAsyncTask ()
        {
            @Override
            public void taskToAccomplish () throws InterruptedException
            {
                ConsoleManager.outputNewLineToDrivers ("Starting AsyncTask");
                ProgramFlow.pauseForMS (3000);
                ConsoleManager.outputNewLineToDrivers ("Ended AsyncTask");
            }
        };

        while (true)
            ProgramFlow.pauseForSingleFrame ();
    }
}
