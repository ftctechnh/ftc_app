package org.firstinspires.ftc.teamcode.systems.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class LogWritter
{
    private static final String LoggingFilePath = "";

    private StringBuffer buffer;
    private File logFile;

    public LogWritter() {
        this.buffer = new StringBuffer();
    }

    public void appendLine(String line) {
        buffer.ensureCapacity(line.length() + 1);
        buffer.append(line + "\n");
    }

    public void write() {
        try {
            writeToFile();
        } catch (Exception e) {
            throw new IllegalStateException("Could not write to file", e);
        }
    }

    private void writeToFile() throws Exception {
        FileWriter fileWriter = getFileWriter();
        appendBufferToFile(fileWriter);
        buffer = new StringBuffer();
    }

    private FileWriter getFileWriter() throws Exception {
        return new FileWriter(logFile);
    }

    private void appendBufferToFile(FileWriter fileWriter) throws Exception {
        fileWriter.append(buffer.toString());
        fileWriter.flush();
        fileWriter.close();
    }
}
