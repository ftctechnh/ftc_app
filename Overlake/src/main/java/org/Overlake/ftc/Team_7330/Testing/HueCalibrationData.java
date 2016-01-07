package org.Overlake.ftc.Team_7330.Testing;

import java.io.*;
import com.google.gson.*;
import com.google.gson.annotations.*;

/**
 * Created by leeac on 1/6/2016.
 */
public class HueCalibrationData
{
    @Expose public HueCalibration grayTile;
    @Expose public HueCalibration redTape;
    @Expose public HueCalibration blueTape;
    @Expose public HueCalibration whiteTape;
    @Expose public HueCalibration redBeacon;
    @Expose public HueCalibration blueBeacon;

    public HueCalibrationData()
    {
    }

    private static Gson gson;

    private static Gson getGson()
    {
        if (gson == null)
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        return gson;
    }

    public static HueCalibrationData[] fromJson(String json)
    {
        return getGson().fromJson(json, HueCalibrationData[].class);
    }

    public static String toJson(HueCalibrationData [] values)
    {
        return getGson().toJson(values);
    }

    public static HueCalibrationData[] fromFile(String fileName)
    {
        try
        {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);

            String json = reader.readLine();

            fileReader.close();

            return HueCalibrationData.fromJson(json);

        }
        catch (IOException exception)
        {
        }

        return null;
    }

    public static void toFile(String fileName, HueCalibrationData[] values)
    {
        try
        {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            String json = HueCalibrationData.toJson(values);

            writer.write(json);

            fileWriter.close();
        }
        catch (IOException exception)
        {
        }
    }

    public static void SampleWrite()
    {

    }
}
