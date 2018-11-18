package org.firstinspires.ftc.teamcode.opmodes;

import android.util.Log;

public class Logger {
    String tag;

    public Logger(String tag){
        this.tag = tag;
    }

    public void log(String text){
        Log.i("[INTERSECT]", "[" + this.tag +  "] " +  text);
    }
}
