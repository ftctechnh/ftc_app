package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qualcomm.robotcore.hardware.usb.serial.SerialPort;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JSONParser {

    /**
     * This method returns a map that contains all the data of a Json file.
     *
     * @param file the file to parse the JSON data from
     *
     * @return A Map<String, Object> filled with all the data from the JSON file you requested
     */

    public Map<String, Object> parseFile(File file){

        JsonObject obj = null;
        Map<String, Object> result = new HashMap<>();
        JsonParser parser = new JsonParser();

        try {
            obj = (JsonObject) parser.parse(new FileReader(file));
        } catch (FileNotFoundException | ClassCastException e) {
            return null;
        }
        for(Map.Entry<String, JsonElement> e: obj.entrySet()){

            result.put(e.getKey(), e.getValue());

        }
        return result;
    }

}