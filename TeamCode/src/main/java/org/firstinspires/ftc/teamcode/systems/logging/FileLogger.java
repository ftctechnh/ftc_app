package org.firstinspires.ftc.teamcode.systems.logging;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileLogger implements ILogger
{
    private final String ExceptionPath = "/FTC_Exceptions.txt";

    private LogWritter logWritter;
    private File logFile;

    public FileLogger() {
        logFile = new File(Environment.getExternalStorageDirectory() + ExceptionPath);
        logWritter = new LogWritter(logFile);
    }

    public void log(String name, String data, Object... args) {
        String line = StringFormatter.format("[{0}]: {1}", name, StringFormatter.format(data, args));
        logWritter.appendLine(line);
    }

    public void write() {
        logWritter.write();
    }
}
