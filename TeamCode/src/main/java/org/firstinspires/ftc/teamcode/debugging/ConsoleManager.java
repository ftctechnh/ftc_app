package org.firstinspires.ftc.teamcode.debugging;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

public class ConsoleManager
{
    private static Telemetry mainTelemetry;
    public static void setMainTelemetry(Telemetry someTelemetry)
    {
        mainTelemetry = someTelemetry;
        currentConsoleDisplay = new ArrayList<> ();
    }

    /*** USE TO OUTPUT DATA IN A SLIGHTLY BETTER WAY THAT LINEAR OP MODES PROVIDE ***/
    private static ArrayList<String> currentConsoleDisplay;
    private static int maxLines = 13;

    public static void outputNewLineToDrivers(String newLine)
    {
        //Add new line at beginning of the lines.
        currentConsoleDisplay.add(0, newLine);
        //If there is more than 5 lines there, remove one.
        if (currentConsoleDisplay.size() > maxLines)
            currentConsoleDisplay.remove(maxLines);

        refreshConsole ();
    }

    public static void appendToLastOutputtedLine(String toAppend)
    {
        String result = currentConsoleDisplay.get (0) + toAppend;
        currentConsoleDisplay.remove (0);
        currentConsoleDisplay.add (0, result);

        refreshConsole ();
    }

    //Allows for more robust output of actual data instead of line by line without wrapping.  Used for driving and turning.
    public static void outputConstantDataToDrivers(String[] data)
    {
        mainTelemetry.update();
        for (String s : data)
            mainTelemetry.addLine(s);
        mainTelemetry.update();
    }

    private static void refreshConsole()
    {
        //Output every line in order.
        mainTelemetry.update(); //Empty the output
        for (String s : currentConsoleDisplay)
            mainTelemetry.addLine(s); //add all lines
        mainTelemetry.update(); //updateMotorPowerWithPID the output with the added lines.
    }
}
