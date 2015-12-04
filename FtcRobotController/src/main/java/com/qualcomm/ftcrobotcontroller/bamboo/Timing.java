package com.qualcomm.ftcrobotcontroller.bamboo;

import java.util.Vector;

/**
 * Created by alex on 12/3/15.
 */
public class Timing {

    private Vector<Motor> motors;
    private Vector<Integer> starts;
    private Vector<Integer> ends;

    public Timing()
    {
        motors = new Vector<Motor>();
        starts = new Vector<Integer>();
        ends = new Vector<Integer>();
    }

    public void append(Motor mot, int delay, int length)
    {
        motors.add(mot);
        starts.add(delay);
        ends.add(delay+length);
    }

    public void execute()
    {
        
    }

}
