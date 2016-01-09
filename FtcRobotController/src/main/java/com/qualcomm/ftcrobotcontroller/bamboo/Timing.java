package com.qualcomm.ftcrobotcontroller.bamboo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.Vector;

/**
 * Created by alex on 12/3/15.
 */
public class Timing {

    private Vector<Motor> motors;
    private Vector<Integer> starts;
    private Vector<Double> powers;
    private Vector<Integer> ends;
    private Vector<Boolean> isRunning;

    public Timing()
    {
        motors = new Vector<Motor>();
        starts = new Vector<Integer>();
        ends = new Vector<Integer>();
        powers = new Vector<Double>();
        isRunning = new Vector<Boolean>();
    }

    public void append(Motor mot, double power, int delay, int length)
    {
        motors.add(mot);
        starts.add(delay);
        powers.add(power);
        ends.add(delay+length);
        isRunning.add(false);
    }

    public void execute()
    {
        int current = 0;

        int moves = motors.size()*2;
        for(int j=0;j<moves;j++)
        {
            long min = 999999999;
            int minnum = 0;
            for(int i=0;i<motors.size();i++)
            {
                if(!isRunning.get(i) && starts.get(i) - current < min && starts.get(i) - current > 0)
                {
                    min = starts.get(i) - current;
                    minnum = i;
                }
                else if(isRunning.get(i) && ends.get(i) - current < min && ends.get(i) - current > 0)
                {
                    min = starts.get(i) - current;
                    minnum = i;
                }
            }

            try {
                Thread.sleep(min);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            current += min;
            if(isRunning.get(minnum))
            {
                motors.get(minnum).set(0);
            }
            else
            {
                isRunning.set(minnum, true);
                motors.get(minnum).set(powers.get(minnum));
            }
        }
    }

}
