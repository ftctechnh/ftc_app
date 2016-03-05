package org.swerverobotics.library.internal.tests;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.TeleOp;

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
