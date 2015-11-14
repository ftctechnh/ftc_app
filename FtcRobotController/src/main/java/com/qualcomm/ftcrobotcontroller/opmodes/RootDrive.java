package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.bamboo.Motor;
import com.qualcomm.ftcrobotcontroller.bamboo.Point;
import com.qualcomm.ftcrobotcontroller.bamboo.Root;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by alex on 11/10/15.
 */
public class RootDrive extends Root {

    public Point sides;
    public Motor right, left;
    public Motor ext, rot;

    public RootDrive() {
        sides = new Point(0, 0);

    }

    @Override
    public void init()
    {
        right = new Motor("right", hardwareMap);
        left = new Motor("left", hardwareMap, true);
        ext = new Motor("rightext", hardwareMap);
        rot = new Motor("rightrot", hardwareMap);
    }

    @Override
    public void onJoy1_left()
    {
        left.scale(joy1.left.y + joy1.left.x);
        right.scale(joy1.left.y - joy1.left.x);
    }

    @Override
    public void onJoy1_right()
    {
        rot.scale(joy1.right.y);
    }

    @Override
    public void onJoy1_rt()
    {
        ext.scale(gp_rt);
    }

    @Override
    public void onJoy1_rb_press()
    {
        ext.set(-0.5);
    }

    @Override
    public void onJoy1_rb_release()
    {
        ext.set(0);
    }
}
