package org.firstinspires.ftc.teamcode.debugging;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ImprovedOpModeBase;
import org.firstinspires.ftc.teamcode.enhancements.ConsoleManager;
import org.firstinspires.ftc.teamcode.enhancements.ProgramFlow;
import org.firstinspires.ftc.teamcode.enhancements.EasyAsyncTask;

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
            public String taskToAccomplish () throws InterruptedException
            {
                ConsoleManager.outputNewLineToDrivers ("Starting AsyncTask");
                ProgramFlow.pauseForMS (3000);
                ConsoleManager.outputNewLineToDrivers ("Ended AsyncTask");

                return "Success!";
            }
        };

        while (true)
            ProgramFlow.pauseForSingleFrame ();
    }
}
