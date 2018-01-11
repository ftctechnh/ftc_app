package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class JSONParsing {

    /**
     * This method returns the file data you want to call. Please set this method up to be like this.
     *
     * JsonObject nameHere = JSONParsing.findFile("Name of File");
     *
     * @param fileName the name of the JSON file you want to find. DO NOT INCLUDE THE .json AT THE END
     * @param robot this is used to print a telemetry error message if something goes wrong
     *
     * @return a JsonObject that contains the contents of the JSON file you requested
     *
     */

    public JsonObject findFile(String fileName, RelicRecoveryRobot robot){
        Object obj = null;

        JsonParser parser = new JsonParser();


        try {
            obj = parser.parse(new FileReader("/storage/emulated/0/FIRST/" + fileName + ".json"));
        } catch (FileNotFoundException e) {

        }
        if(obj == null){
            return null;
        }
        JsonObject jsonObject = (JsonObject) obj;

        return jsonObject;
    }
    /**
     * This method returns the data you want to retreive from a JsonObject.
     *
     * @param dataName the name of the data you want to find. THIS IS CASE SENSITIVE
     * @param jsonFile the JsonObject that contains the data you want to pull
     *
     * @return a data value in the form of a String parameter.
     *
     */
    public String jsonDataString(String dataName, JsonObject jsonFile){
        String parameter = jsonFile.get(dataName).toString();

        return parameter;
    }

    /**
     * This method returns the data you want to retreive from a JsonObject.
     *
     * @param dataName the name of the data you want to find. THIS IS CASE SENSITIVE
     * @param jsonFile the JsonObject that contains the data you want to pull
     *
     * @return a data value in the form of a Integer parameter. For Strings or Doubles, please use {@link #jsonDataString(String, JsonObject)}
     */
    public int jsonDataNotString(String dataName, JsonObject jsonFile){
        int parameter = jsonFile.get(dataName).getAsInt();

        return parameter;
    }
    /**
     * This method returns the data you want to retreive from a JsonObject.
     *
     * @param dataName the name of the data you want to find. THIS IS CASE SENSITIVE
     * @param jsonFile the JsonObject that contains the data you want to pull
     * @param isString type the kind of data type you want to return it as
     *
     * @return a data value in the form of the specified parameter.
     */
    public void jsonDataPull(String dataName, JsonObject jsonFile, boolean isString){

    }




}