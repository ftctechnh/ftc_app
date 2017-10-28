package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 10/23/2017.
 */

public class Ultrasonic9330 {

    private Hardware9330 hwMap = null;
    public Ultrasonic9330(Hardware9330 robotMap) { hwMap = robotMap; }
    double initialMicroTime;
    double initialTimeSeconds;
    Double distance;
    long duration;
    long rising_time;   // time of the rising edge
    long falling_time;

    public void microSecondWait(int microseconds) {
        initialMicroTime = System.nanoTime(); //reset initial time
        while (System.nanoTime() - initialMicroTime < microseconds*1000) {;} //do nothing while current time is less than goal
    }

    public void restartTimer() {
        initialTimeSeconds = System.currentTimeMillis() / 1000;
    }

    public boolean timer(int seconds) {
        if (System.currentTimeMillis() / 1000 < initialTimeSeconds + seconds)
            return true; //returns true if timer hasn't run out yet
        else return false;
    }

    void read_pulse() {
        restartTimer();
        while(hwMap.ultrasonicEcho.getState() == false && timer(1)) {rising_time = System.nanoTime();}
        if (!timer(1)) return;
        while(hwMap.ultrasonicEcho.getState() == true) {falling_time = System.nanoTime();}
        duration = (falling_time - rising_time)/1000000;
        distance = Math.floor((duration * 34300) / 2);
        /* duration = 0;
        // On rising edge: record current time.
        if (hwMap.ultrasonicEcho.getState() == true)
            rising_time = System.nanoTime();
        while (hwMap.ultrasonicEcho.getState() == true) {;}
            falling_time = System.nanoTime();
            duration = (falling_time - rising_time) / 1000;
            */
    }

    public String getDistance() { //Call THIS function to get distance
        hwMap.ultrasonicTrigger.setState(false);
        microSecondWait(2);
        hwMap.ultrasonicTrigger.setState(true);
        microSecondWait(10);
        hwMap.ultrasonicTrigger.setState(false);

        distance = 0.0;
        read_pulse();
        if (distance == 0.0) return "Cannot read distance.";
        return distance.toString();
    }




}
