package org.firstinspires.ftc.teamcode.robotutil;

import android.util.Log;

public class Logger {
    String tag;

    public Logger(String tag){
        this.tag = tag;
    }

    public void log(String text){
        Log.i("[INTERSECT]", "[" + this.tag +  "] " +  text);
    }

    public void logData(String dataName, double data){
        Log.i("[INTERSECT]", "[" + this.tag +  "] " + dataName + ": " + String.valueOf(data));
    }

    public void lineBreak() {
        Log.i("[INTERSECT]", "");
        Log.i("[INTERSECT]", "============================================");
        Log.i("[INTERSECT]", "============================================");
        Log.i("[INTERSECT]", "");
    }
}
