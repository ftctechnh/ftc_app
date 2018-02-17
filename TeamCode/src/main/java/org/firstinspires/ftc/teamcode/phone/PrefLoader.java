package org.firstinspires.ftc.teamcode.phone;

import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Derek on 2/9/2018.
 */

public class PrefLoader {
    private SharedPreferences sharedPreferences;
    private String[] attributes;
    private Map<String,Double> tunemap;

    public PrefLoader(SharedPreferences preferences, String[] attributes) {
        this.sharedPreferences = preferences;
        this.attributes = attributes;
        tunemap = new HashMap<>();
    }

    public PrefLoader loadAll() {
        for(String s : attributes) {
            tunemap.put(s,Double.parseDouble(load(s)));  //eew
        }
        return this;
    }

    //private methods

    /*
    * This could use sharedPreferences.getFloat, or something like that,
    * but this makes it more expandable for later
    * */

    private String load(String attribName) {
        return sharedPreferences.getString(attribName,"1.00");
    }

    //getters and setters

    public Map<String,Double> getTunemap() {
        return new HashMap<>(tunemap);
    }
}
