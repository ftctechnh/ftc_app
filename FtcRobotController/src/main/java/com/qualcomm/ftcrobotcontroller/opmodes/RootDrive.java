package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.bamboo.BServo;
import com.qualcomm.ftcrobotcontroller.bamboo.Motor;
import com.qualcomm.ftcrobotcontroller.bamboo.Point;
import com.qualcomm.ftcrobotcontroller.bamboo.Root;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by alex on 11/10/15.
 */
public class RootDrive extends Root {

    public final double SERVO_STEP = 0.001;

    public Point sides;
    public Motor right, left;
    public Motor extr, rotr, extl, rotl;
    public BServo cdr, cdl;

    public RootDrive() {
        sides = new Point(0, 0);

    }

    @Override
    public void init()
    {
        right = new Motor("right", hardwareMap, true);
        left = new Motor("left", hardwareMap);
        extr = new Motor("rightext", hardwareMap);
        rotr = new Motor("rightrot", hardwareMap, true);
        extl = new Motor("leftext", hardwareMap, true);
        rotl = new Motor("leftrot", hardwareMap);

        cdr = new BServo("cdsRight", hardwareMap);
        cdl = new BServo("cdsLeft", hardwareMap);
    }

    @Override
    public void loop()
    {
        super.loop();
        if(cdr.get() > 0 && gp2_rt < 0.2) cdr.inc(-SERVO_STEP);
        if(cdl.get() > 0 && gp2_lt < 0.2) cdl.inc(-SERVO_STEP);
    }

    @Override
    public void onJoy2_left()
    {
        left.scale(joy2.left.y + joy2.left.x);
        right.scale(joy2.left.y - joy2.left.x);
    }

    @Override
    public void onJoy1_right()
    {
        rotr.set(joy1.right.y/2);
    }

    @Override
    public void onJoy1_rt()
    {
        extr.scale(-gp1_rt);
    }

    @Override
    public void onJoy1_rb_press()
    {
        extr.set(1);
    }

    @Override
    public void onJoy1_rb_release()
    {
        extr.set(0);
    }

    @Override
    public void onJoy1_left()
    {
        rotl.set(joy1.left.y/2);
    }

    @Override
    public void onJoy1_lt()
    {
        extl.scale(-gp1_lt);
    }

    @Override
    public void onJoy1_lb_press()
    {
        extl.set(1);
    }

    @Override
    public void onJoy1_lb_release()
    {
        extl.set(0);
    }

    @Override
    public void onJoy2_rt()
    {
        if(gp2_rt > 0.2)
        {
            cdr.inc(SERVO_STEP);
        }
    }

    public void onJoy2_lt()
    {
        if(gp2_rt > 0.2)
        {
            cdl.inc(SERVO_STEP);
        }
    }


}
