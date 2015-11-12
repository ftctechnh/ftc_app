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

    public RootDrive() {
        sides = new Point(0, 0);

    }

    @Override
    public void init()
    {
        right = new Motor("right", hardwareMap, true);
        left = new Motor("left", hardwareMap);
    }

    @Override
    public void onJoy1_right()
    {
        left.scale(joy1.right.y - joy1.right.x);
        right.scale(joy1.right.y + joy1.right.x);
    }
}
