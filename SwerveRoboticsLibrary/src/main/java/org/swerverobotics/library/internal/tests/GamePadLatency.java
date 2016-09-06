package org.swerverobotics.library.internal.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.swerverobotics.library.SynchronousOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Logs
 */
@TeleOp(name="Gamepad Latency (Synch)", group="Swerve Tests")
@Disabled
public class GamePadLatency extends SynchronousOpMode
    {
    @Override
    protected void main() throws InterruptedException
        {
        waitForStart();
        while (opModeIsActive())
            {
            if (updateGamepads())
                {
                RobotLog.d("gamepads updated");
                }
            }
        }
    }
