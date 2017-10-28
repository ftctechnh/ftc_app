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
    double duration = 0;
    double rising_time;   // time of the rising edge
    double falling_time;

    public void microSecondWait(int microseconds) {
        initialMicroTime = System.nanoTime(); //reset initial time
        while (System.nanoTime() - initialMicroTime < microseconds*1000) {;} //do nothing while current time is less than goal
    }

    public void restartTimer() {
        initialTimeSeconds = System.currentTimeMillis() * 1000;
    }

    public boolean timer(int seconds) {
        if (System.currentTimeMillis() * 1000 < initialTimeSeconds + seconds)
            return true; //returns true if timer hasn't run out yet
        else return false;
    }

    void read_pulse() {
        duration = 0;
        // On rising edge: record current time.
        if (hwMap.ultrasonicEcho.getState() == true)
            rising_time = System.nanoTime();
        while (hwMap.ultrasonicEcho.getState() == true) {;}
            falling_time = System.nanoTime();
            duration = (falling_time - rising_time) / 1000;
    }

    public String getDistance() { //Call THIS function to get distance
        hwMap.ultrasonicTrigger.setState(false);
        microSecondWait(1);
        hwMap.ultrasonicTrigger.setState(true);
        microSecondWait(9);
        hwMap.ultrasonicTrigger.setState(false);

        restartTimer();
        while(duration==0 && timer(1)){ //continues to get pulse until value is received/timeout
            read_pulse();
        }
        if (duration == 0) return "Cannot read distance.";
        Double distance = Math.floor(duration / 29.1 / 2);
        return distance.toString();
    }




}
