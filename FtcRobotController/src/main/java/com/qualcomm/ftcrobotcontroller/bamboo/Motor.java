package com.qualcomm.ftcrobotcontroller.bamboo;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by alex on 11/11/15.
 */
public class Motor {

    public DcMotor _motor;
    public String name;
    public boolean reversed;

    public Motor(String nm, HardwareMap hwm)
    {
        this(nm, hwm, false);
    }

    public Motor(String nm, HardwareMap hwm, boolean rev)
    {
        name = nm;
        _motor = hwm.dcMotor.get(name);
        reversed = false;
    }

    public void reverse()
    {
        reversed = !reversed;
    }

    public void set(double amt)
    {
        if(amt > 1) amt = 1;
        else if(amt < -1) amt = -1;
        _motor.setPower(reversed ? -amt : amt);
    }

    public double get()
    {
        return _motor.getPower();
    }

    public void stop()
    {
        _motor.setPower(0);
    }

    public void scale(double amt)
    {
        if(amt > 1) amt = 1;
        else if(amt < -1) amt = -1;
        amt = amt*amt*amt;

        set(amt);
    }


}
