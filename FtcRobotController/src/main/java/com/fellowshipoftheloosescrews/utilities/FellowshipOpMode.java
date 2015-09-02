package com.fellowshipoftheloosescrews.utilities;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by FTC7123A on 9/1/2015.
 *
 * This is the master OpMode for our main robot. This adds methods to the standard
 * LinearOpMode specific to Legoless, like sensor setups and motor controls. This also
 * helps control the many threads we'll use for sensors.
 *
 * This corresponds to a hardware config file on the phones, it must be used with
 * the correct hardware.
 */
public class FellowshipOpMode extends LinearOpMode {

    /**
     * Run method from LinearOpMode, don't override.
     * @throws InterruptedException
     */
    @Override
    public final void runOpMode() throws InterruptedException
    {
        thisOpMode = this;
    }

    /**
     * A reference to the currently running OpMode
     */
    private static FellowshipOpMode thisOpMode = null;

    /**
     * Getter for the currently running OpMode,
     * @return The currently running FellowshipOpMode reference
     */
    public static FellowshipOpMode getOpMode()
    {
        return thisOpMode;
    }

    /**
     * Lets any thread delay one hardware cycle
     * NOTE: Different from waitOneHardwareCycle()
     */
    public static void delayOneHardware()
    {
        try {
            FellowshipOpMode currentOM = getOpMode();
            if(currentOM == null) {
                new IllegalAccessException("getOpMode() returned null").printStackTrace();
                return;
            }
            currentOM.waitOneHardwareCycle();
        } catch (Exception e) {
            Log.e("FellowshipOpMode", "Couldn't wait one hardware cycle");
        }
    }
}
