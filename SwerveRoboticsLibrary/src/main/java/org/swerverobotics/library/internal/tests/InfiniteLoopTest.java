package org.swerverobotics.library.internal.tests;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * This simple OpMode exercises the logic that deals with runaway OpModes
 */
@TeleOp(name="Infinite Loop (Linear)", group="Swerve Tests")
@Disabled
public class InfiniteLoopTest extends LinearOpMode
    {
    @Override
    public void runOpMode() throws InterruptedException
        {
        waitForStart();

        DbgLog.msg("started infinite loop");
        for (;;)
            {
            Thread.yield();
            }
        }
    }
