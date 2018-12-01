package org.firstinspires.ftc.teamcode.systems.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;

/**
 * A writer used to write to the log file on the phone
 */
public class LogWritter
{
    private StringBuffer buffer;
    private File logFile;

    /**
     * Creates a new Log Writter given a log file name
     * @param logFile the name of the log file
     */
    public LogWritter(File logFile) {
        this.buffer = new StringBuffer();
        this.logFile = logFile;
    }

    /**
     * appends line to the writter
     * @param line the line to be added
     */
    public void appendLine(String line) {
        buffer.ensureCapacity(line.length() + 1);
        buffer.append(line + "\n");
    }

    /**
     * Flushes the string buffer
     */
    public void flush() {
        try {
            writeToFile();
        } catch (Exception e) {
            throw new IllegalStateException("Could not close to file", e);
        }
    }

    /**
     * Writes to the file
     * @throws Exception IOException is thrown if there is an error appending the buffer to the file
     */
    private void writeToFile() throws Exception {
        FileWriter fileWriter = getFileWriter();
        appendBufferToFile(fileWriter);
        buffer = new StringBuffer();
    }

    /**
     * Gets the file writer need to write to the log files
     * @return Returns the file writer used to write to log files
     * @throws Exception IOException if the file writer causes
     * @see FileWriter
     */
    private FileWriter getFileWriter() throws Exception {
        return new FileWriter(logFile);
    }

    /**
     * Appends the content of the string buffer to the file writter
     * @param fileWriter writer to the log files
     * @throws Exception IOException is thrown if an error occurs while handling the file
     * @see FileWriter
     */
    private void appendBufferToFile(FileWriter fileWriter) throws Exception {
        fileWriter.append(buffer.toString());
        fileWriter.flush();
        fileWriter.close();
    }
}
