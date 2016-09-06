package org.swerverobotics.library.internal.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Infinite Loop
 */
@TeleOp(name="Infinite Loop (Linear)", group="Swerve Tests")
@Disabled
public class LinearInfiniteLoop extends LinearOpMode
    {
    @Override
    public void runOpMode() throws InterruptedException
        {
        while (true)
            {
            Thread.yield();
            }
        }
    }
