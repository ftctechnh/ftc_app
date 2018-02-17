package org.firstinspires.ftc.teamcode.general.action;

import java.util.concurrent.Callable;

/**
 * Created by Derek on 2/15/2018.
 */

public class Actions {
    public static final int ENCODER_COUNTS_PER_REVOLUTION = 1440;
    public static LoopAction turnAction(double degrees) {
        return new LoopAction(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }
}
