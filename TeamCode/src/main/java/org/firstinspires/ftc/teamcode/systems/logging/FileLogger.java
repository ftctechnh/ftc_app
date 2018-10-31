package org.firstinspires.ftc.teamcode.systems.logging;

import android.os.Environment;

import java.io.File;

public class FileLogger implements ILogger
{
    private LogWritter logWritter;
    private File logFile;

    public FileLogger(String path) {
        logFile = new File(Environment.getExternalStorageDirectory() + path);
        logWritter = new LogWritter(logFile);
    }

    public void log(String name, Object data, Object... args) {
        String line = StringFormatter.format("[{0}]: {1}", name, StringFormatter.format(data.toString(), args));
        logWritter.appendLine(line);
    }

    public void write() {
        logWritter.flush();
    }
}
