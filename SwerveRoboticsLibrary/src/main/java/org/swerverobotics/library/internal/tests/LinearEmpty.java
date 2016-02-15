package org.swerverobotics.library.internal.tests;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.swerverobotics.library.interfaces.Disabled;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * A linear OpMode that just executes, signifying nothing.
 */
@TeleOp(name="Empty (Linear)", group="Swerve Tests")
@Disabled
public class LinearEmpty extends LinearOpMode
    {
    @Override
    public void runOpMode() throws InterruptedException
        {
        waitForStart();
        DbgLog.msg("executed Linear Empty OpMode");
        }
    }
