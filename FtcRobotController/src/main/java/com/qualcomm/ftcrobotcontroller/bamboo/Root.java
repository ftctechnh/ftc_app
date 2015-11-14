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
    public float gp_rt=0, gp_lt=0;
    public boolean gp_rb=false, gp_lb=false;

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


    @Override
    public void init()
    {
        console.log("Root has initialized");
    }


    @Override
    public final void loop()
    {
        // some ugly stuff here....

        if(gp_rb != gamepad1.right_bumper)
        {
            if(gamepad1.right_bumper) onJoy1_rb_press();
            else onJoy1_rb_release();
            gp_rb = !gp_rb;
        }

        if(gp_lb != gamepad1.left_bumper)
        {
            if(gamepad1.left_bumper) onJoy1_lb_press();
            else onJoy1_lb_release();
            gp_lb = !gp_lb;
        }

        if(gp_rt != gamepad1.right_trigger)
        {
            gp_rt = gamepad1.right_trigger;
            onJoy1_rt();
        }

        if(gp_lt != gamepad1.left_trigger)
        {
            gp_lt = gamepad1.left_trigger;
            onJoy1_lt();
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
