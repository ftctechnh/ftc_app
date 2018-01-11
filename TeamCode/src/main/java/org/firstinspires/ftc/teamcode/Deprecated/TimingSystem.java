package org.firstinspires.ftc.teamcode.Deprecated;

/**
 * Created by Peter on 9/5/2017.
 */

import java.util.Timer;
import java.util.TimerTask;


public class TimingSystem{
    public void newTimer(TimerTask function, long ms, long delay) {
            Timer timer = new Timer("Timer");
            timer.scheduleAtFixedRate(function, delay, ms);
    }
}