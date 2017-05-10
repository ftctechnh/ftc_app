package org.firstinspires.ftc.teamcode.Libs;

import android.annotation.SuppressLint;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


/**
 * Created by caseyzandbergen on 9/21/16.
 */

@SuppressWarnings({"SameParameterValue", "ResultOfMethodCallIgnored", "unused", "EmptyCatchBlock"})
public class DataLogger {

    private final long msBase;
    //Declare datalogger variables
    private Writer writer;
    private StringBuffer lineBuffer;
    private long nsBase;

    public DataLogger(String fileName) {
        String directoryPath = Environment.getExternalStorageDirectory().getPath();   //Path to log file
        String filePath         = directoryPath + "/" + fileName + ".csv"; //File name

        new File(directoryPath).mkdir();        // Make sure that the directory exists

        try {
            writer = new FileWriter(filePath);
            lineBuffer = new StringBuffer(128);
        } catch (IOException e) {
        }
        msBase = System.currentTimeMillis();
        nsBase = System.nanoTime();
        addField("sec");
        addField("d ms");
    }

    @SuppressLint("DefaultLocale")
    private void flushLineBuffer(){
        long milliTime,nanoTime;

        try {
            lineBuffer.append('\n');
            writer.write(lineBuffer.toString());
            lineBuffer.setLength(0);
        }
        catch (IOException e){
        }
        milliTime   = System.currentTimeMillis();
        nanoTime    = System.nanoTime();
        addField(String.format("%.3f",(milliTime - msBase) / 1.0E3));
        addField(String.format("%.3f",(nanoTime - nsBase) / 1.0E6));
        nsBase      = nanoTime;
    }

    public void closeDataLogger() {
        try {
            writer.close();
        }
        catch (IOException e) {
        }
    }

    public void addField(String s) {
        if (lineBuffer.length()>0) {
            lineBuffer.append(',');
        }
        lineBuffer.append(s);
    }

    private void addField(char c) {
        if (lineBuffer.length()>0) {
            lineBuffer.append(',');
        }
        lineBuffer.append(c);
    }

    public void addField(boolean b) {
        addField(b ? '1' : '0');
    }

    public void addField(byte b) {
        addField(Byte.toString(b));
    }

    public void addField(short s) {
        addField(Short.toString(s));
    }

    public void addField(long l) {
        addField(Long.toString(l));
    }

    public void addField(float f) {
        addField(Float.toString(f));
    }

    public void addField(double d) {
        addField(Double.toString(d));
    }

    public void newLine() {
        flushLineBuffer();
    }

    @Override
    protected void finalize() throws Throwable {
        closeDataLogger();
        super.finalize();
    }
}