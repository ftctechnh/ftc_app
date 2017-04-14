package org.firstinspires.ftc.teamcode.threads;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class ProgramFlow
{
    //TODO: Now that this is the standard for stopping the program, perhaps it would be worthwhile to update the program's SDK.

    private static LinearOpMode mainLinOpMode;
    public static void initializeWithOpMode(LinearOpMode opMode)
    {
        mainLinOpMode = opMode;
    }

    public static void pauseForMS(long ms) throws InterruptedException
    {
        long startTime = System.currentTimeMillis ();
        while (System.currentTimeMillis () - startTime <= ms)
            pauseForSingleFrame ();
    }

    public static void pauseForSingleFrame() throws InterruptedException
    {
        // Abort the OpMode if we've been asked to stopEasyTask
        if (mainLinOpMode.isStopRequested())
            throw new InterruptedException();

        //Copied from idle() in LinearOpMode.
        // Otherwise, yield back our thread scheduling quantum and give other threads at our priority level a chance to startEasyTask
        Thread.yield();
    }
}
