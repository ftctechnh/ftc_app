package org.overlake.ftc.team_7330.Testing;

import java.io.*;
import com.google.gson.*;
import com.google.gson.annotations.*;

/**
 * Created by leeac on 1/6/2016.
 */
public class ColorSensorData
{
    @Expose public HueData grayTile;
    @Expose public HueData redTape;
    @Expose public HueData blueTape;
    @Expose public HueData whiteTape;
    @Expose public HueData redBeacon;
    @Expose public HueData blueBeacon;

    public ColorSensorData()
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

    public static ColorSensorData[] fromJson(String json)
    {
        return getGson().fromJson(json, ColorSensorData[].class);
    }

    public static String toJson(ColorSensorData[] values)
    {
        return getGson().toJson(values);
    }

    public static ColorSensorData[] fromFile(String fileName)
    {
        try
        {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);

            String json = reader.readLine();

            reader.close();
            fileReader.close();

            return ColorSensorData.fromJson(json);

        }
        catch (IOException exception)
        {
            String s = exception.getMessage();
            String s2 = s;
        }

        return null;
    }

    public static void toFile(String fileName, ColorSensorData[] values)
    {
        try
        {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            String json = ColorSensorData.toJson(values);

            writer.write(json);
            writer.write("\n");
            writer.close();

            fileWriter.close();
        }
        catch (IOException exception)
        {
            String s = exception.getMessage();
            String s2 = s;
        }
    }
}
