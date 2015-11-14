package io.github.thunderbots.robotcontroller.logging;

import android.util.Log;

/**
 * Contains delegates to Android's standard Log methods, but inserts necessary tag information.
 */
public class ThunderLog {

    public static final String THUNDERBOTS_TAG = "Thunderbots";

    public static void d(String msg) {
        Log.d(THUNDERBOTS_TAG, msg);
    }

    public static void w(String msg) {
        Log.w(THUNDERBOTS_TAG, msg);
    }

    public static void e(String msg) {
        Log.e(THUNDERBOTS_TAG, msg);
    }

    public static void i(String msg) {
        Log.i(THUNDERBOTS_TAG, msg);
    }

    public static void v(String msg) {
        Log.v(THUNDERBOTS_TAG, msg);
    }

}
