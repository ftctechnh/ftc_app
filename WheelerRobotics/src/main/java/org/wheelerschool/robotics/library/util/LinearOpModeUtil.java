package org.wheelerschool.robotics.library.util;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.concurrent.Callable;

/**
 * Created by luciengaitskell on 11/29/16.
 */

public class LinearOpModeUtil {
    public static void runWhileWait(LinearOpMode opMode, Callable<Void> myFunc) {
        while (!opMode.isStarted()) {
            synchronized (opMode) {
                try {
                    try {
                        myFunc.call();
                    } catch(Exception e) {
                        throw new InterruptedException();
                    }
                    opMode.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}
