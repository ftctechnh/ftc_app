package com.fellowshipoftheloosescrews.utilities.data;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Thomas on 10/9/2015.
 */
public class DataLog {

    public static final String LOG_TAG = "DataLog";

    private DataState state = DataState.NOT_READY;

    // this is set up as <Time, <Variable, Value>>
    private HashMap<Double, HashMap<String, Double>> data;

    // holds data for the current Hardware Cycle
    private HashMap<String, Double> tempData;

    // holds the names of current variables
    private ArrayList<String> variables;

    public DataLog()
    {
        data = new HashMap<Double, HashMap<String, Double>>();
        tempData = new HashMap<String, Double>();
        variables = new ArrayList<String>();
    }

    public void init()
    {
        state = DataState.REGISTER_VARIABLES;
    }

    public void addVariable(String variableName)
    {
        if(state == DataState.REGISTER_VARIABLES)
        {
            variables.add(variableName);
        }
        else
        {
            Log.e(LOG_TAG, "Not ready to register variables");
            new Exception().printStackTrace();
        }
    }

    public void start()
    {
        state = DataState.RUNNING;
    }

    public void addData(String variable, double data)
    {
        if(!variables.contains(variable)) {
            new RuntimeException("Variable not found in registered data").printStackTrace();
        }
        else {
            tempData.put(variable, data);
        }
    }

    public void saveTempData(Double time)
    {
        data.put(time, new HashMap<String, Double>());
        for(String var : variables)
        {
            data.get(time).put(var, tempData.get(var));
        }
    }

    private enum DataState
    {
        NOT_READY,
        REGISTER_VARIABLES,
        RUNNING,
        SAVING
    }

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

    public String toString()
    {
        String header = "time,";
        for(String s : variables)
        {
            header += s + ",";
        }

        String dataString = "";

        for(Double time : data.keySet())
        {
            dataString += time + ",";
            for(String v : variables)
            {
                dataString += data.get(time).get(v) + ",";
            }
            dataString += "\n";
        }

        return header + "\n" + dataString;
    }
}
