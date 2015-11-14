package com.qualcomm.ftcrobotcontroller.opmodes;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by Jerry on 10/10/2015.
 */


public class CalibrationCheck extends OpMode {

    OpticalDistanceSensor opticalDistanceSensor;

    double redValue;
    double blueValue;
    double whiteValue;
    double matValue;
    String date;

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void init() {
        try
        {
            File file = new File("/sdcard/FIRST/calibration.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            date = br.readLine();
            redValue = Double.parseDouble(br.readLine());
            blueValue = Double.parseDouble(br.readLine());
            whiteValue = Double.parseDouble(br.readLine());
            matValue = Double.parseDouble(br.readLine());
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loop() {

        telemetry.addData("timestamp", date);
        telemetry.addData("blueValue", blueValue);
        telemetry.addData("redValue", redValue);
        telemetry.addData("whiteValue", whiteValue);
        telemetry.addData("matValue", matValue);
    }

    public void stop(){

    }

}
