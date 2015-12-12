package com.qualcomm.ftcrobotcontroller.bamboo;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by chsrobotics on 12/11/2015.
 */
public class BServo {

    private Servo _servo;
    private double current;

    public BServo(String name, HardwareMap hwm)
    {
        _servo = hwm.servo.get(name);
    }

    public void set(double amt)
    {
        _servo.setPosition(amt);
        current = amt;
    }

    public void inc(double step)
    {
        current += step;
        if(current < 0) current = 0;
        if(current > 1) current = 1;
        _servo.setPosition(current);
    }

    public double get()
    {
        return _servo.getPosition();
    }
}
