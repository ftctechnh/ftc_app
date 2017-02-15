package org.firstinspires.ftc.teamcode.legacycode;

import android.util.Log;

import com.qualcomm.ftccommon.DbgLog;

/**
 * Created by 292486 on 12/2/2015.
 */
public class TMUtil {

    public static void formatDLog(String from, String string, Object... args)
    {
        String info = String.format(string, args);
        Log.d(from, info);
    }

    public static void RCLog(String string){
        DbgLog.msg(string);
    }
}
