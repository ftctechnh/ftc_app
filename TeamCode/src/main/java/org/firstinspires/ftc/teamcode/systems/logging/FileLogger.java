package org.firstinspires.ftc.teamcode.systems.logging;

public class FileLogger implements ILogger
{
    private static final String LoggingPath = "";

    private LogWritter logWritter;

    public FileLogger() {
        logWritter = new LogWritter();
    }

    public void log(String name, String data, Object... args)
    {

    }

    public void write() {
        logWritter.write();
    }
}
