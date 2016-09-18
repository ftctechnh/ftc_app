package com.borsch;

import java.util.Arrays;

/**
 * Created by Max on 9/10/2016.
 */
public class TelemetryCodec {

    /*
    * TelemetryData (int timestamp, DataType type, int id, String data)
    * /\
    * ||
    * \/
    * "timestamp type.ordinal() id data[0]~data[1]~data[n]"
    */

    public static TelemetryData Decode (String line) {
        String[] parts = line.split(" ");

        for (short i=2; i<parts.length; i++) {
            parts[i] = parts[i].replaceAll("~", " ");
        }

        int timestamp = Integer.parseInt(parts[0]);
        DataType type = DataType.values()[Integer.parseInt(parts[1])];
        int id = Integer.parseInt(parts[2]);
        String[] data = Arrays.copyOfRange (parts, 3, parts.length);

        return new TelemetryData(timestamp, type, id, data);
    }

    public static String Encode (TelemetryData data) {
        String s = "";
        s += data.timestamp + " ";
        s += data.type.ordinal() + " ";
        s += data.id + " ";

        for (String var : data.data) {
            s += var.replaceAll(" ", "~") + " ";
        }

        return s;
    }
}
