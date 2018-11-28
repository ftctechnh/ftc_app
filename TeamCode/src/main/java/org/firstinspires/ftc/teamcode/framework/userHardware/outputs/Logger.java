package org.firstinspires.ftc.teamcode.framework.userHardware.outputs;

import android.os.Environment;
import android.util.Log;

import com.qualcomm.robotcore.util.RobotLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    private final File file;

    private FileOutputStream fOut;
    private OutputStreamWriter myOutWriter;

    public Logger(String flieName) {
        file = new File(path, flieName);
        try {
            if (file.exists()) {
                //Don't create file
                RobotLog.i("ABCD Existing File");
            } else {
                //Create file
                RobotLog.i("ABCD Creating New File");
                file.createNewFile();
            }
            fOut = new FileOutputStream(file);
            myOutWriter = new OutputStreamWriter(fOut);
        } catch (IOException e) {
            Log.e("Exception", "File init failed " + e.toString());
        }
    }

    public void log(String text) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

        try {
            myOutWriter.append(timeStamp + " : " + text + (char) Character.LINE_SEPARATOR);
            myOutWriter.flush();
        } catch (IOException e) {
            Log.e("Exception", "File append failed: " + e.toString());
        }
    }

    public void stop() {
        try {
            myOutWriter.close();
            fOut.close();
        } catch (IOException e) {
            Log.e("Exception", "File close failed " + e.toString());
        }
    }
}
