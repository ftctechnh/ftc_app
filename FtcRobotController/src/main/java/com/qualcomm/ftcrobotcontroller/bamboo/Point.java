package com.qualcomm.ftcrobotcontroller.bamboo;

/**
 * Created by chsrobotics on 11/9/2015.
 */
public class Point {

    public float x, y;
    public Point(float _x, float _y)
    {
        x = _x;
        y = _y;
    }

    public double mag()
    {
        return Math.sqrt(x*x+y*y);
    }

    public double dir()
    {
        return Math.atan2(y, x);
    }
}
