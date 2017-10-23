package edu.usrobotics.telemetry;

import com.borsch.TelemetryCodec;
import com.borsch.TelemetryData;

/**
 * Created by Max on 9/9/2016.
 */
public class TelemetryWriter {
    private static ITelemetryWriter[] writers = new ITelemetryWriter[2];
    private static long startTime = System.currentTimeMillis();

    public static void init() {
        ITelemetryWriter bluetoothWriter = new BluetoothTelemetry ();
        bluetoothWriter.init();

        if (BluetoothTelemetry.isSupported) {
            System.out.println ("Launching Bluetooth Telemetry!");
            ((Thread) bluetoothWriter).start();
        } else { writers[0] = bluetoothWriter; }

        System.out.println ("Launching File Telemetry!");
        ITelemetryWriter fileWriter = new FileTelemetry ();
        fileWriter.init();
        writers[1] = fileWriter;
    }

    public static int getTimestamp () {
        return (int) (System.currentTimeMillis() - startTime);
    }

    public static boolean writeData(TelemetryData data) {
        return writeLine(TelemetryCodec.Encode(data));
    }

    public static boolean writeLine(String line) {
        if (writers.length < 1) { return false; }

        for (int i=0; i<writers.length; i++) {
            if (writers[i] != null) {
                writers[i].write (line + "\n");
            }
        }

        return true;
    }

    public static void stop() {
        for (int i=0; i<writers.length; i++) {
            if (writers[i] != null) {
                writers[i].close();
            }
        }
    }
}
