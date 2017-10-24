package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Hardware9330;

import java.util.Timer;

/**
 * Created by robot on 10/23/2017.
 */

public class Ultrasonic9330 {

    private Hardware9330 hwMap = null;
    public Ultrasonic9330(Hardware9330 robotMap) { hwMap = robotMap; }
    Long initialMicroTime;

    public void microTimerReset() {
        initialMicroTime = System.nanoTime()/1000;
    }

    public Long getMicroTime() {
        return System.nanoTime()/1000 - initialMicroTime;
    }

    public void microSecondWait(int microseconds) {
        microTimerReset();
        while (getMicroTime() < microseconds) {}
    }

    public Long returnDistance() {
        Long initialTimeSeconds = System.currentTimeMillis() * 1000;
        while (System.currentTimeMillis() * 1000 < initialTimeSeconds + 1) {
            if (hwMap.ultrasonicEcho.getState() == true) return getMicroTime();
        }
        return null;
    }

    public Long getDistance() { //Call THIS function to get distance
        hwMap.ultrasonicTrigger.setState(true);
        microSecondWait(10);
        hwMap.ultrasonicTrigger.setState(false);
        return returnDistance();
    }




}
