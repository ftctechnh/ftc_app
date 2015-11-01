package com.qualcomm.ftcrobotcontroller.opmodes;

import android.widget.EditText;
import android.widget.Toast;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
        DbgLog.msg("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!adb eopd qd qd ++++++++++++++++++++++++++++++++++++++++++++");
        /*try {
            File statText = new File("/FIRST/calibration.txt");
            DbgLog.msg("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + statText.getCanonicalPath());
            FileOutputStream is = new FileOutputStream(statText);
            DbgLog.msg("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!after creating fileoutputstream");
            OutputStreamWriter osw = new OutputStreamWriter(is);
            DbgLog.msg("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!after creating output strem writer");
            Writer w = new BufferedWriter(osw);
            DbgLog.msg("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + String.valueOf(reflectance));
            w.write(String.valueOf(reflectance));
            w.close();

            FileOutputStream calitxt = new FileOutputStream("/FIRST/cali.txt");
            OutputStreamWriter out = new OutputStreamWriter(calitxt);
            out.write(String.valueOf(reflectance));
            out.write('\n');
            out.close();



            DbgLog.msg("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + String.valueOf(reflectance));

        } catch (IOException e) {
            System.err.println("Problem writing");
        }*/


        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            File statText = new File("phone/FIRST/calibration.txt");
            DbgLog.msg("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + statText.getCanonicalPath());
            fileWriter = new FileWriter(statText);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("test");
        } catch (IOException e) {
            System.err.println("Error writing the file : ");
            e.printStackTrace();
        } finally {

            if (bufferedWriter != null && fileWriter != null) {
                try {
                    bufferedWriter.close();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
