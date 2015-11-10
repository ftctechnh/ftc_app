package com.qualcomm.ftcrobotcontroller.bamboo;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by chsrobotics on 11/9/2015.
 */
public class Root extends OpMode{

    public Console console;
    public Joystick joy1, joy2;

    public Root()
    {
        console = new Console(telemetry);
        joy1 = new Joystick();
        joy2 = new Joystick();
    }

    public void onJoystick1()
    {

    }

    public void onJoystick2()
    {

    }

    @Override
    public final void init()
    {
        console.log("Root has initialized");
    }


    @Override
    public final void loop()
    {
        // some ugly stuff here....

        // check the joystick modifiers
        if(gamepad1.right_stick_x != joy1.right.x ||
                gamepad1.right_stick_y != joy1.right.y ||
                gamepad1.left_stick_y != joy1.right.x ||
                gamepad1.left_stick_y != joy1.right.x)
        {
            joy1.right.x = gamepad1.right_stick_x;
            joy1.right.y = gamepad1.right_stick_y;
            joy1.left.x = gamepad1.left_stick_x;
            joy1.left.x = gamepad1.left_stick_x;
            onJoystick1();
        }

        if(gamepad2.right_stick_x != joy2.right.x ||
                gamepad2.right_stick_y != joy2.right.y ||
                gamepad2.left_stick_y != joy2.right.x ||
                gamepad2.left_stick_y != joy2.right.x)
        {
            joy2.right.x = gamepad2.right_stick_x;
            joy2.right.y = gamepad2.right_stick_y;
            joy2.left.x = gamepad2.left_stick_x;
            joy2.left.x = gamepad2.left_stick_x;
            onJoystick2();
        }

    }

}
