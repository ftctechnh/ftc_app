package com.lasarobotics.library.monkeyc;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lasarobotics.library.android.Util;
import com.lasarobotics.library.controller.Controller;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * MonkeyUtil handles reading and writing text files with instructions created by MonkeyC
 */
public class MonkeyUtil {
    public final static String FILE_DIR = "/MonkeyC/";
    static final long MONKEYC_STARTING_CONSTANT = -1000;

    private static JsonObject getDeltas(Controller current, Controller previous, boolean allowNull) throws JSONException {
        Gson g = getGson();
        JSONObject currentjson = new JSONObject(g.toJson(current));
        JSONObject previousjson = new JSONObject(g.toJson(previous));
        JsonObject out = new JsonObject();
        //Test if anything was changed
        boolean changed = false;
        Iterator<?> keys = previousjson.keys();
        while (keys.hasNext()) {

            String key = (String) keys.next();
            double cur = currentjson.getDouble(key);
            double prev = previousjson.getDouble(key);
            if (!(cur == prev)) {
                changed = true;
                out.addProperty(key, cur);
            }
        }

        if (!changed && !allowNull)
            return null;
        return out;
    }

    public static MonkeyData createDeltas(Controller current1, Controller previous1, Controller current2, Controller previous2, long time, boolean allowNull) {
        //Get controller deltas
        JsonObject one = null;
        JsonObject two = null;

        try {
            one = getDeltas(current1, previous1, allowNull);
            two = getDeltas(current2, previous2, allowNull);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new MonkeyData(one, two, time);
    }


    public static void writeFile(String fileName, ArrayList<MonkeyData> commands, boolean overwrite) {
        try {
            Type listOfTestObject = new TypeToken<List<MonkeyData>>() {
            }.getType();

            File file = Util.createFileOnDevice(FILE_DIR, fileName, overwrite);
            Gson g = getGson();
            String out = g.toJson(commands, listOfTestObject);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(out);
            bw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<MonkeyData> readFile(String filename) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + FILE_DIR, filename);
        String out = "";
        ArrayList<MonkeyData> commands = new ArrayList<>();
        try {
            Scanner s = new Scanner(file);
            while (s.hasNextLine()) {
                out += s.nextLine();
            }
            Type listOfTestObject = new TypeToken<List<MonkeyData>>() {
            }.getType();
            commands = new Gson().fromJson(out, listOfTestObject);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return commands;
    }

    private static Gson getGson() {
        GsonBuilder gb = new GsonBuilder();
        return gb.create();
    }
}
