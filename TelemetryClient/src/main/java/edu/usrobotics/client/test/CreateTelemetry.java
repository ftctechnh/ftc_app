package edu.usrobotics.client.test;

import com.borsch.DataType;
import com.borsch.TelemetryCodec;
import com.borsch.TelemetryData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Max on 9/17/2016.
 */
public class CreateTelemetry {

    static long time = System.currentTimeMillis();

    static int getTime () {
        return (int) (System.currentTimeMillis() - time);
    }

    public static void main (String[] args) {
        PrintWriter outputWriter;

        try {
            File outputFile = new File("Yolometry.txt");
            FileOutputStream outputStream = new FileOutputStream(outputFile, false);
            outputWriter = new PrintWriter(outputStream);

        } catch (IOException e) {
            System.out.println("Failed to write to telemetry file.");
            e.printStackTrace();
            return;
        }

        outputWriter.println(TelemetryCodec.Encode(new TelemetryData(0, DataType.ROBOT_SPECS, 0, new String[]{"18", "18", "18"})));

        TelemetryData data = new TelemetryData(getTime(), DataType.UPDATE_MAP, 0, new String[]{
                "0", "0", "0",
                "0", "0", "0"
        });

        int num = 60;
        for (int i=0; i<num; i++) {
            data.timestamp = i * 100;
            float decrease = ((float)i/(float)num);

            if (i == 0) {
                data.id = 1;
                data.data[0] = Float.toString(1800f);
                data.data[2] = Float.toString(1800f);
            } else {
                data.id = 0;
                data.data[0] = Float.toString(800f + 500 * decrease);
                int b = -30 + i;
                data.data[2] = Float.toString(400 + 2*b*b + 20*b);
                data.data[4] = Float.toString(i);
            }

            outputWriter.println(TelemetryCodec.Encode(data));
            /*try {
                Thread.sleep((long) (1000f/20f));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }

        outputWriter.println(TelemetryCodec.Encode(new TelemetryData(num*100 + 1, DataType.STOP, 0, new String[]{})));

        outputWriter.close();
    }
}
