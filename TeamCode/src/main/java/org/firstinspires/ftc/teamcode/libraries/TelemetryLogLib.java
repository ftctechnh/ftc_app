package org.firstinspires.ftc.teamcode.libraries;

import android.os.Environment;
import android.provider.Settings;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.Util;

import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;

import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Noah on 4/2/2017.
 */

public class TelemetryLogLib {

    public static class TelemetryLog extends TelemetryImpl {
        private FileWriter mLog;
        private long startTime = 0;

        public static final String fileTAG = "TelemetryLog";

        public TelemetryLog(OpMode mode){
            super(mode);
            try {
                mLog = new FileWriter(Environment.getExternalStorageDirectory() + "/FIRST/" + mode.getClass().getSimpleName() + "_Log.txt");
            }
            catch (IOException e){
                RobotLog.e(fileTAG + " Opening log file failed!");
            }
        }
        public TelemetryLog(OpMode mode, String filePath){
            super(mode);
            try {
                mLog = new FileWriter(filePath);
            }
            catch (IOException e){
                RobotLog.e(fileTAG + " Opening log file failed!");
            }
        }

        //shoehorn into the main telemetry updating function
        //steal all of the data
        //profit
        @Override
        protected void saveToTransmitter(boolean recompose, TelemetryMessage transmitter) {
            //make sure it still does its thing
            super.saveToTransmitter(recompose, transmitter);

            //if(startTime == 0) startTime = System.currentTimeMillis();
            //double currentTimeSeconds = (System.currentTimeMillis() - startTime) / 1000.0;
            //then take one of the varibles left over from it
            //and log the crap out of it
            if(mLog != null && recompose){
                try {
                    mLog.write("+---------------------+\n");
                    mLog.write(String.format(Locale.US, "Current Time : %1$.2f s %n", super.opMode.getRuntime()));
                    for(int i = 0; i < super.composedLines.size(); i++){
                        mLog.write(super.composedLines.get(i) + '\n');
                    }
                    mLog.flush();
                }
                catch (IOException e){
                    RobotLog.e(fileTAG + " Logging to file failed!");
                }

            }
        }
    }
}
