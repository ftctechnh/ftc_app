package com.borsch;

import java.util.Arrays;

/**
 * Created by Max on 9/10/2016.
 */
public class TelemetryCodec {

    /*
    * TelemetryData (Float timestamp, DataType type, String data)
    * /\
    * ||
    * \/
    * "timestamp type.ordinal() data"
    */

    public static TelemetryData Decode (String line) {
        String[] parts = line.split(" ");

        for (short i=2; i<parts.length; i++) {
            parts[i] = parts[i].replaceAll("~", " ");
        }

        float timestamp = Float.parseFloat(parts[0]);
        DataType type = DataType.values()[Integer.parseInt(parts[1])];
        String[] data = Arrays.copyOfRange (parts, 2, parts.length);

        return new TelemetryData(timestamp, type, data);
    }

    public static String Encode (TelemetryData data) {
        String s = "";
        s += System.currentTimeMillis()/1000f + " ";
        s += data.type.ordinal() + " ";

        for (String var : data.data) {
            s += var.replaceAll(" ", "~") + " ";
        }

        return s;
    }
}
