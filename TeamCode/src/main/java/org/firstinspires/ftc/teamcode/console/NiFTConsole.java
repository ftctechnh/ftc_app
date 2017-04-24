/**
 * This console supports private process consoles and sequential data, which is super important for programmers to observe (believe me i know)
 */

package org.firstinspires.ftc.teamcode.console;

import android.os.AsyncTask;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.threads.NiFTFlow;

import java.util.ArrayList;

public class NiFTConsole
{
    private static Telemetry mainTelemetry;
    public static void initializeWith (Telemetry someTelemetry)
    {
        mainTelemetry = someTelemetry;
        sequentialConsoleData = new ArrayList<> ();
        privateProcessConsoles = new ArrayList<> ();

        startConsoleUpdater ();
    }

    /*** USE TO OUTPUT DATA IN A SLIGHTLY BETTER WAY THAT LINEAR OP MODES PROVIDE ***/
    private static ArrayList<String> sequentialConsoleData; //Lines being added and removed.
    private static int maxSequentialLines = 13;
    public static void outputNewSequentialLine(String newLine)
    {
        //Add new line at beginning of the lines.
        sequentialConsoleData.add(0, newLine);
        //If there is more than 5 lines there, remove one.
        if (sequentialConsoleData.size() > maxSequentialLines)
            sequentialConsoleData.remove(maxSequentialLines);
    }
    public static void appendToLastSequentialLine(String toAppend)
    {
        String result = sequentialConsoleData.get (0) + toAppend;
        sequentialConsoleData.remove (0);
        sequentialConsoleData.add (0, result);
    }

    //Private process data.
    private static ArrayList <ProcessConsole> privateProcessConsoles;
    public static class ProcessConsole
    {
        private final String processName;
        private String[] processData;

        public ProcessConsole(String processName)
        {
            this.processName = processName;
            processData = new String[0];

            privateProcessConsoles.add (this);
        }

        public void updateWith(String... processData)
        {
            this.processData = processData;
        }

        public void destroy()
        {
            privateProcessConsoles.remove(this);
        }
        public void revive() { privateProcessConsoles.add(this); }
    }

    //The console automatically updates itself, so that rebuild() isn't called every 10 ms.
    private static class ConsoleUpdater extends AsyncTask <Void, Void, Void>
    {
        @Override
        protected Void doInBackground (Void... params)
        {
            consoleUpdaterInstance = this;

            try
            {
                while (true)
                {
                    rebuildConsole ();
                    NiFTFlow.pauseForMS (50);
                }
            }
            catch (InterruptedException e)
            {
                outputNewSequentialLine ("Got end of program: ending console updates!");
                cancel (true);
            }

            return null;
        }

        @Override
        protected void onCancelled ()
        {
            consoleUpdaterInstance = null;
        }
    }
    private static ConsoleUpdater consoleUpdaterInstance;
    public static void startConsoleUpdater()
    {
        if (consoleUpdaterInstance == null)
        {
            new ConsoleUpdater ().executeOnExecutor (AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
    public static void stopConsoleUpdater()
    {
        if (consoleUpdaterInstance != null)
        {
            consoleUpdaterInstance.cancel (true);
        }
    }

    public static void rebuildConsole()
    {
        if (mainTelemetry != null)
        {
            //Clear all lines.
            mainTelemetry.update ();

            //Add all private console data.
            for (ProcessConsole pConsole : privateProcessConsoles)
            {
                mainTelemetry.addLine ("----- " + pConsole.processName + " -----");

                for (String line : pConsole.processData)
                    mainTelemetry.addLine (line);

                mainTelemetry.addLine ("");
            }

            mainTelemetry.addLine ("----- Sequential Data -----");
            for (String line : sequentialConsoleData)
            {
                mainTelemetry.addLine (line);
            }

            //Refresh the console with this new data.
            mainTelemetry.update ();
        }
        //Otherwise it just gets queued in the ArrayList.
    }
}
