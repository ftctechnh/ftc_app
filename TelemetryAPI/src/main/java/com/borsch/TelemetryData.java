package com.borsch;

/**
 * Created by Max on 9/10/2016.
 */
public class TelemetryData implements java.io.Serializable {
    public int timestamp;
    public DataType type;
    public int id;
    public String[] data;

    public TelemetryData (int timestamp, DataType type, int id, String[] data) {
        this.timestamp = timestamp;
        this.type = type;
        this.id = id;
        this.data = data;
    }
}
