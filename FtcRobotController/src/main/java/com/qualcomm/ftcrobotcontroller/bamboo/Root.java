package com.qualcomm.ftcrobotcontroller.bamboo;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by chsrobotics on 11/9/2015.
 */
public class Root extends OpMode {

    public Console console;
    public Joystick joy1, joy2;
    public HardwareMap map;
    public float gp1_rt=0, gp1_lt=0, gp2_rt=0,gp2_lt=0;
    public boolean gp1_rb=false, gp1_lb=false, gp2_rb=false,gp2_lb=false;

    public Root()
    {
        console = new Console(telemetry);
        joy1 = new Joystick();
        joy2 = new Joystick();
        map = hardwareMap;
    }

    public void onJoy1_right() {}
    public void onJoy1_left() {}
    public void onJoy2_right() {}
    public void onJoy2_left() {}

    public void onJoy1_lt() {}
    public void onJoy1_rt() {}
    public void onJoy1_rb_press() {}
    public void onJoy1_lb_press() {}
    public void onJoy1_rb_release() {}
    public void onJoy1_lb_release() {}

    public void onJoy2_lt() {}
    public void onJoy2_rt() {}
    public void onJoy2_rb_press() {}
    public void onJoy2_lb_press() {}
    public void onJoy2_rb_release() {}
    public void onJoy2_lb_release() {}

    public void update() {};


    @Override
    public void init()
    {
        console.log("init", "Root has initialized");
    }


    @Override
    public void loop()
    {
        // joystick one bumpers and triggers
        update();

        if(gp1_rb != gamepad1.right_bumper)
        {
            if(gamepad1.right_bumper) onJoy1_rb_press();
            else onJoy1_rb_release();
            gp1_rb = !gp1_rb;
        }

        if(gp1_lb != gamepad1.left_bumper)
        {
            if(gamepad1.left_bumper) onJoy1_lb_press();
            else onJoy1_lb_release();
            gp1_lb = !gp1_lb;
        }

        if(gp1_rt != gamepad1.right_trigger)
        {
            gp1_rt = gamepad1.right_trigger;
            onJoy1_rt();
        }

        if(gp1_lt != gamepad1.left_trigger)
        {
            gp1_lt = gamepad1.left_trigger;
            onJoy1_lt();
        }

        // now joystick number two.

        if(gp2_rb != gamepad2.right_bumper)
        {
            if(gamepad2.right_bumper) onJoy2_rb_press();
            else onJoy2_rb_release();
            gp2_rb = !gp2_rb;
        }

        if(gp2_lb != gamepad2.left_bumper)
        {
            if(gamepad2.left_bumper) onJoy2_lb_press();
            else onJoy2_lb_release();
            gp2_lb = !gp2_lb;
        }

        if(gp2_rt != gamepad2.right_trigger)
        {
            gp2_rt = gamepad2.right_trigger;
            onJoy2_rt();
        }

        if(gp2_lt != gamepad2.left_trigger)
        {
            gp2_lt = gamepad2.left_trigger;
            onJoy2_lt();
        }

        // check the joystick modifiers
        if(gamepad1.right_stick_x != joy1.right.x ||
                gamepad1.right_stick_y != joy1.right.y)
        {
            joy1.right.x = gamepad1.right_stick_x;
            joy1.right.y = gamepad1.right_stick_y;
            onJoy1_right();
        }

        if(gamepad1.left_stick_x != joy1.left.x ||
                gamepad1.left_stick_y != joy1.left.y)
        {
            joy1.left.x = gamepad1.left_stick_x;
            joy1.left.y = gamepad1.left_stick_y;
            onJoy1_left();
        }

        if(gamepad2.right_stick_x != joy2.right.x ||
                gamepad2.right_stick_y != joy2.right.y)
        {
            joy2.right.x = gamepad2.right_stick_x;
            joy2.right.y = gamepad2.right_stick_y;
            onJoy2_right();
        }

        if(gamepad2.left_stick_x != joy2.left.x ||
                gamepad2.left_stick_y != joy2.left.y)
        {
            joy2.left.x = gamepad2.left_stick_x;
            joy2.left.y = gamepad2.left_stick_y;
            onJoy2_left();
        }

    }

}
