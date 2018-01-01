package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.*;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Random;

/**
 * Created by ftc6347 on 12/29/17.
 */

public class JsonTesting {

    Gson gson = new Gson();
    Random rand = new Random();
    JsonParser parser = new JsonParser();

    Object obj = parser.parse(new FileReader("Testing.json"));
    JSONObject jsonObject = (JSONObject) obj;
    String name = (String) jsonObject.get("name");
    //print name in test-op
    JSONArray memberNames = (JSONArray) jsonObject.get("memberNames");
    String randomMemberName = (String) memberNames.get(rand.nextInt(9));

    public JsonTesting() throws FileNotFoundException, JSONException {
    }
}
