package org.firstinspires.ftc.teamcode.programflow;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class ProgramFlow
{
    private static LinearOpMode program;
    public static void setProgram(LinearOpMode theProgram)
    {
        program = theProgram;
    }

    public static void pauseForMS(long msDelay)
    {
        long startTime = System.currentTimeMillis ();
        while (!RunState.stopRequested () && System.currentTimeMillis () - startTime <= msDelay)
            program.idle();
    }
}
