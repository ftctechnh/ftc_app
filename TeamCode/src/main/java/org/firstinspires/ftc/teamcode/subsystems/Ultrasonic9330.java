package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.Hardware9330;

import java.util.Timer;

/**
 * Created by robot on 10/23/2017.
 */

public class Ultrasonic9330 {

    private Hardware9330 hwMap = null;
    public Ultrasonic9330(Hardware9330 robotMap) { hwMap = robotMap; }
    long initialMicroTime;
    long initialTimeSeconds;
    long duration = 0;
    long rising_time;   // time of the rising edge
    long falling_time;
    boolean last_state; // previous pin state

    public void microSecondWait(int microseconds) {
        initialMicroTime = System.nanoTime()/1000; //reset initial time
        while (System.nanoTime()/1000 - initialMicroTime < microseconds) {} //do nothing while current time is less than goal
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
        restartTimer();
        while (timer(1)) { //try for one second
            boolean state = hwMap.ultrasonicEcho.getState();  // current pin state

            // On rising edge: record current time.
            if (last_state == false && state == true)
                rising_time = System.nanoTime() / 1000;
            while (state == true) {state = hwMap.ultrasonicEcho.getState();}
            falling_time = System.nanoTime() / 1000;
            duration  =  falling_time - rising_time;
            return;
        } duration = 0;
    }

    public double getDistance() { //Call THIS function to get distance
        hwMap.ultrasonicTrigger.setState(false);
        microSecondWait(2);
        hwMap.ultrasonicTrigger.setState(true);
        microSecondWait(10);
        hwMap.ultrasonicTrigger.setState(false);
            read_pulse();
            if(duration!=0){
                return duration/29.1/2;
            } return 0.0;
    }




}
