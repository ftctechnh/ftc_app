package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Samuel on 11/1/2015.
 */
public class ResQAutoDelayRed extends _ResQAuto{
    @Override
    protected int getDelay() {
        return 10000;
    }

    @Override
    protected int getRedAlliance() {
        return 1;
    }
}
