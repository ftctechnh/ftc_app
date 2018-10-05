package org.firstinspires.ftc.teamcode.systems.logging;

public class FileLogger implements ILogger
{
    private LogWritter logWritter;

    public FileLogger() {
        logWritter = new LogWritter();
    }

    public void log(String name, String data, Object... args) {
        String line = StringFormatter.format("[{0}]: {1}", name, StringFormatter.format(data, args));
        logWritter.appendLine(line);
    }

    public void write() {
        logWritter.write();
    }
}
