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

    public void debug(Exception e) {
        StackTraceElement stackTrace = e.getStackTrace()[0];
        int line = stackTrace.getLineNumber();
        String file = stackTrace.getFileName();
        exceptionLogger.log("Exception", "{0} line:{1} file:{2}", e.toString(), line, file);
        exceptionLogger.write();
    }
}
