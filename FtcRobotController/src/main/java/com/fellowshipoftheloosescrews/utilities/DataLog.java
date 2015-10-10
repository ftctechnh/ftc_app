package com.fellowshipoftheloosescrews.utilities;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.fellowshipoftheloosescrews.utilities.opmode.FellowshipOpMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Thomas on 10/9/2015.
 */
public class DataLog {

    public static final String LOG_TAG = "DataLog";

    public void saveFile(String folderName, String fileName, String data)
    {
        if(data.equals(""))
        {
            Toast.makeText(FellowshipOpMode.getOpMode().hardwareMap.appContext,
                    "Data cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File externalPath = new File(Environment.getExternalStorageDirectory() + "/Fellowship");
            externalPath.mkdirs();
            File folder = new File(externalPath, folderName);
            folder.mkdirs();
            //externalPath.setReadable(true);
            File file = new File(folder, fileName);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
            //file.setReadable(true);
            MediaScannerConnection.scanFile(
                    FellowshipOpMode.getOpMode().hardwareMap.appContext, new String[]{file.getAbsolutePath()}, null,
                    new MediaScannerConnection.MediaScannerConnectionClient() {
                        @Override
                        public void onMediaScannerConnected() {

                        }

                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.d(LOG_TAG, path);
                            sendEmail("ftc7123data@gmail.com", "Data from " + getDateAndTimeString(), "", path);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendEmail(String email, String subject, String body, String path) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("plain/text");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.putExtra(Intent.EXTRA_TEXT, body);
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        FellowshipOpMode.getOpMode().hardwareMap.appContext.startActivity(sendIntent);
    }

    public static String getDateString()
    {
        return DateFormat.getDateInstance().format(new Date());
    }

    public static String getDateAndTimeString()
    {
        return DateFormat.getDateTimeInstance().format(new Date());
    }
}
