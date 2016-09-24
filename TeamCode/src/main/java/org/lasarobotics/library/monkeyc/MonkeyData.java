package org.lasarobotics.library.monkeyc;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import org.lasarobotics.library.controller.Controller;

import org.json.JSONObject;

import java.util.Iterator;

/**
 * Contains a single time-stamped patched state of one Controller
 */
public class MonkeyData {
    //TODO can improve this to combine everything into one data set

    @SerializedName("g1")
    JsonObject deltasGamepad1;  //Changes to gamepad 1 from previous state
    @SerializedName("g2")
    JsonObject deltasGamepad2;  //Changes to gamepad 2 from previous state
    @SerializedName("t")
    long time;                  //Time from initial delta

    /**
     * Initialize a MonkeyData instance
     */
    MonkeyData() {
        deltasGamepad1 = null;
        deltasGamepad2 = null;
        time = -1;
    }

    MonkeyData(long time) {
        this.deltasGamepad1 = new JsonObject();
        this.deltasGamepad2 = new JsonObject();
        this.time = time;
    }

    /**
     * Initialize a MonkeyData instance using data points
     *
     * @param deltasGamepad1 Deltas for Gamepad 1
     * @param deltasGamepad2 Deltas
     * @param time           The current time, in milliseconds
     */
    MonkeyData(JsonObject deltasGamepad1, JsonObject deltasGamepad2, long time) {
        this.deltasGamepad1 = deltasGamepad1;
        this.deltasGamepad2 = deltasGamepad2;
        this.time = time;
    }

    public Controller updateControllerOne(Controller previous) {
        if (deltasGamepad1 != null) {
            Gson g = new Gson();
            try {
                JSONObject previousjson = new JSONObject(g.toJson(previous));
                JSONObject patch = new JSONObject(deltasGamepad1.toString());
                Iterator<?> keys = patch.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    previousjson.remove(key);
                    previousjson.put(key, patch.get(key));
                }
                return g.fromJson(previousjson.toString(), Controller.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return previous;
        } else {
            return previous;
        }
    }

    public Controller updateControllerTwo(Controller previous) {
        if (deltasGamepad2 != null) {
            Gson g = new Gson();
            try {
                JSONObject previousjson = new JSONObject(g.toJson(previous));
                JSONObject patch = new JSONObject(deltasGamepad2.toString());
                Iterator<?> keys = patch.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    previousjson.remove(key);
                    previousjson.put(key, patch.get(key));
                }
                return g.fromJson(previousjson.toString(), Controller.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return previous;
        } else {
            return previous;
        }
    }

    public boolean hasUpdate() {
        return deltasGamepad1 != null || deltasGamepad2 != null || time == MonkeyUtil.MONKEYC_STARTING_CONSTANT;
    }

    public JsonObject getDeltasGamepad1() {
        return deltasGamepad1;
    }

    void setDeltasGamepad1(JsonObject deltasGamepad1) {
        this.deltasGamepad1 = deltasGamepad1;
    }

    public JsonObject getDeltasGamepad2() {
        return deltasGamepad2;
    }

    void setDeltasGamepad2(JsonObject deltasGamepad2) {
        this.deltasGamepad2 = deltasGamepad2;
    }

    public long getTime() {
        return time;
    }

    void setTime(long time) {
        this.time = time;
    }
}
