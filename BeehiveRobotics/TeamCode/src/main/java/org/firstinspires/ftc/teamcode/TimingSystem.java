package org.firstinspires.ftc.teamcode;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Peter on 9/5/2017.
 */

public class TimingSystem{
    public void newTimer(TimerTask function, long ms, long delay) {
            Timer timer = new Timer("Timer");

        timer.scheduleAtFixedRate(function, delay, ms);
    }
}