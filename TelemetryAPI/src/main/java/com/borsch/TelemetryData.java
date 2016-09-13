package com.borsch;

/**
 * Created by Max on 9/10/2016.
 */
public class TelemetryData implements java.io.Serializable {
    public float timestamp;
    public DataType type;
    public String[] data;

    public TelemetryData (float timestamp, DataType type, String[] data) {
        this.timestamp = timestamp;
        this.type = type;
        this.data = data;
    }
}
