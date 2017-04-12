package org.firstinspires.ftc.teamcode.debugging;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class ConsoleManager
{
    private static Telemetry mainTelemetry;
    public static void initializeWith (Telemetry someTelemetry)
    {
        mainTelemetry = someTelemetry;
        sequentialConsoleData = new ArrayList<> ();
        privateProcessConsoles = new ArrayList<> ();
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

        rebuildConsole ();
    }
    public static void appendToLastSequentialLine(String toAppend)
    {
        String result = sequentialConsoleData.get (0) + toAppend;
        sequentialConsoleData.remove (0);
        sequentialConsoleData.add (0, result);

        rebuildConsole ();
    }

    //Private process data.
    private static ArrayList <ProcessConsole> privateProcessConsoles;
    public static class ProcessConsole
    {
        public final String processName;
        public String[] processData;

        public ProcessConsole(String processName)
        {
            this.processName = processName;
        }

        public void updateWith(String... processData)
        {
            this.processData = processData;
            rebuildConsole ();
        }

        public void destroy()
        {
            privateProcessConsoles.remove(this);
        }
    }
    public static ProcessConsole getPrivateConsole(String processName)
    {
        ProcessConsole newProcessConsole = new ProcessConsole (processName);
        privateProcessConsoles.add(newProcessConsole);
        return newProcessConsole;
    }
    public static void rebuildConsole()
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
}
