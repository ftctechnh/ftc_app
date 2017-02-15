package org.firstinspires.ftc.teamcode.legacycode;

import android.util.Log;

import com.qualcomm.ftccommon.DbgLog;

import java.util.Objects;

/**
 * Created by 292486 on 12/1/2015.
 */
public class Debugger {

    public static void debugFormat(String income, String string, Objects... args)
    {
        String info = String.format(string, args);
        Log.d(income, info);
    }
}
