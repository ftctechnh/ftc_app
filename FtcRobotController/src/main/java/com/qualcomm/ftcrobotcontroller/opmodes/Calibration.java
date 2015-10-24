package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by Jerry on 10/10/2015.
 */


public class Calibration extends OpMode {
    OpticalDistanceSensor opticalDistanceSensor;
    double reflectance = 0;
    private String path;


    @Override
    public void init() {
        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");
    }
    public void loop() {
        reflectance = opticalDistanceSensor.getLightDetected();
        telemetry.addData("Reflectance Value", reflectance);

    }
    public void stop() {
        //edit by Samuel on 10/24/15
        try {
            File statText = new File("/FIRST/calibration.txt");
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write(String.valueOf(reflectance));
            w.close();
        } catch (IOException e) {
            System.err.println("Problem writing");
        }
    }

}
