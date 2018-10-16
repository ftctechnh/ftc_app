package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.debuggers.LinearOpModeDebugger;
import org.firstinspires.ftc.teamcode.systems.logging.ILogger;
import org.firstinspires.ftc.teamcode.systems.logging.PhoneLogger;

import java.util.Scanner;

@Autonomous(name = "LoggingOpMode")
public class LoggerTestOpMode extends LinearOpModeDebugger
{
    @Override
    public void run()
    {
        ILogger logger = new PhoneLogger(telemetry);
        logger.log("1", "1");
        logger.write();
        sleepThread(1000);
        logger.log("1", "2");
        logger.log("2", "1");
        logger.write();
        sleepThread(1000);
        logger.log("1", "3");
        logger.log("2", "2");
        logger.write();
        sleepThread(1000);
        Scanner s = null;
        s.close();
    }

    private void sleepThread(int miliseconds) {
        try
        {
            Thread.sleep(miliseconds);
        }
        catch (InterruptedException e)
        {
            throw new IllegalStateException("Failed to sleep thread", e);
        }
    }
}
