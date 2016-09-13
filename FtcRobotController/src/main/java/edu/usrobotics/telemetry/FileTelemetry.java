package edu.usrobotics.telemetry;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Max on 9/9/2016.
 */
public class FileTelemetry implements ITelemetryWriter {

    private static File outputFile = Environment.getExternalStorageDirectory();//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    private static FileOutputStream outputStream;
    private static PrintWriter outputWriter;

    @Override
    public void init () {
        try {
            outputFile = new File(outputFile.getPath(), "Telemetry.txt");
            outputStream = new FileOutputStream(outputFile, false);
            outputWriter = new PrintWriter (outputStream);

        } catch (IOException e) {
            System.out.println ("Failed to write to telemetry file.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean write (String line) {
        if (outputWriter == null) return false;

        outputWriter.print (line);

        return true;
    }

    @Override
    public void stop () {
        if (outputWriter != null) {
            outputWriter.close();
        }

        /*if (simulated) { return; }

            Intent intent =
                    new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            FtcRobotControllerActivity.instance.sendBroadcast(intent);*/
    }


}
