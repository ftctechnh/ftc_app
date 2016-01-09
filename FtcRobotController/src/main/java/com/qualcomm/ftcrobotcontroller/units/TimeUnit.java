package com.qualcomm.ftcrobotcontroller.units;

/**
 * Created by tucker on 1/7/16.
 */
public class TimeUnit extends Unit {

    public TimeUnit(long msecs) {
        setValue(msecs);
    }

    public TimeUnit(float seconds) {
        setValue((long) (seconds * 1000));
    }

}
