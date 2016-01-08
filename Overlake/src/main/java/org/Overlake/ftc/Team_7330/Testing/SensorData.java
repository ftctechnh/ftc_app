package org.Overlake.ftc.Team_7330.Testing;

import java.io.*;
import com.google.gson.*;
import com.google.gson.annotations.*;

/**
 * Created by leeac on 1/6/2016.
 */
public class SensorData
{
    @Expose public HueData grayTile;
    @Expose public HueData redTape;
    @Expose public HueData blueTape;
    @Expose public HueData whiteTape;
    @Expose public HueData redBeacon;
    @Expose public HueData blueBeacon;

    public SensorData()
    {
        grayTile = new HueData();
        redTape = new HueData();
        blueTape = new HueData();
        whiteTape = new HueData();
        redBeacon = new HueData();
        blueBeacon = new HueData();
    }

    public String toString()
    {
        return "gt: " + grayTile.hasData +
               ", rt: " + redTape.hasData +
               ", bt: " + blueTape.hasData +
               ", wt: " + whiteTape.hasData +
               ", rb: " + redBeacon.hasData +
               ", bb: " + blueBeacon.hasData;
    }

    private static Gson gson;

    private static Gson getGson()
    {
        if (gson == null)
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        return gson;
    }

    public static SensorData[] fromJson(String json)
    {
        return getGson().fromJson(json, SensorData[].class);
    }

    public static String toJson(SensorData[] values)
    {
        return getGson().toJson(values);
    }

    public static SensorData[] fromFile(String fileName)
    {
        try
        {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);

            String json = reader.readLine();

            fileReader.close();

            return SensorData.fromJson(json);

        }
        catch (IOException exception)
        {
        }

        return null;
    }

    public static void toFile(String fileName, SensorData[] values)
    {
        try
        {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            String json = SensorData.toJson(values);

            writer.write(json);

            fileWriter.close();
        }
        catch (IOException exception)
        {
        }
    }
}
