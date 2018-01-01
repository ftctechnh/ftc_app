package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by ftc6347 on 12/29/17.
 */

public class JsonTesting {

    Gson gson = new Gson();
    Random rand = new Random();
    JSONParser parser = new JSONParser();

    try{
        Object obj = parser.parse(new FileReader("Testing.json"));
        JSONObject jsonObject = (JSONObject) obj;
        String name = (String) jsonObject.get("name");
        //print name in test-op
        JSONArray memberNames = (JSONArray) jsonObject.get("memberNames");
        String randomMemberName = (String) memberNames[rand.nextInt(9)];


    }catch(FileNotFoundException e){
        e.printStackTrace();
    }catch(IOException e){
        e.printStackTrace();
    }catch(ParseException e){
        e.printStackTrace();
    }catch(Exception e){
        e.printStackTrace();
    }

}
