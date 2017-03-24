package org.firstinspires.ftc.teamcode.programflow;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class RunState
{
    public enum DriverSelectedState {INIT, RUNNING, STOP}

    private static LinearOpMode program;

    public static void setProgram (LinearOpMode givenProgram)
    {
        program = givenProgram;
    }

    public static DriverSelectedState getState()
    {
        if (program != null)
        {
            //Will only return one state.
            if (program.isStopRequested () || Thread.interrupted ())
                return DriverSelectedState.STOP;

            if (program.isStarted ())
                return DriverSelectedState.RUNNING;

            return DriverSelectedState.INIT;
        }
        else
        {
            ConsoleManager.outputNewLineToDrivers ("No program provided!");
            return DriverSelectedState.STOP;
        }
    }

    public static boolean stopRequested()
    {
        return getState () == DriverSelectedState.STOP;
    }
}
