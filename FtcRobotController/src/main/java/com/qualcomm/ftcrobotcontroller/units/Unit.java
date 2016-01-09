package com.qualcomm.ftcrobotcontroller.units;

/**
 * Created by tucker on 1/7/16.
 */
public abstract class Unit {

    long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

}
