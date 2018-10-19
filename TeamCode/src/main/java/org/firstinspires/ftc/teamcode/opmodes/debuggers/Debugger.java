package org.firstinspires.ftc.teamcode.opmodes.debuggers;

import org.firstinspires.ftc.teamcode.systems.logging.FileLogger;
import org.firstinspires.ftc.teamcode.systems.logging.ILogger;

public class Debugger implements IOpModeDebugger
{
    private ILogger exceptionLogger;

    public Debugger() {
        exceptionLogger = new FileLogger("/FTC_Exceptions.txt");
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
                "{0} line:{1} file:{2} depth: {3}\n",
                e.toString(),
                line,
                file,
                i + 1
            );
        }
        exceptionLogger.close();
    }
}
