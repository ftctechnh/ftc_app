package org.firstinspires.ftc.teamcode.opmodes.debuggers;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.systems.logging.FileLogger;
import org.firstinspires.ftc.teamcode.systems.logging.ILogger;

public class Debugger implements IOpModeDebugger
{
    private ILogger exceptionLogger;

    public Debugger() {
        exceptionLogger = new FileLogger();
    }

    public void debug(Exception e)
    {
        for (int i = 0; i < e.getStackTrace().length; i++)
        {
            StackTraceElement stackTrace = e.getStackTrace()[i];
            int line = stackTrace.getLineNumber();
            String file = stackTrace.getFileName();
            exceptionLogger.log(
                "Stack Trace Element",
                "{0} line:{1} file:{2}\ndepth: {3}",
                e.toString(),
                line,
                file,
                i
            );
            exceptionLogger.write();
        }
    }
}
