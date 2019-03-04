package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

class Button {
    private Gamepad g;
    private Location location;
    private Type type;
    private boolean lastPressed = false;
    private boolean on = false;
    private int count = 0;

    enum Location
    {
        X,
        A,
        B,
        Y,
        D_Up,
        D_Down,
        D_Left,
        D_Right,
        Left_Stick,
        Right_Stick,
        Left_Bumper,
        Right_Bumper,
        Start
    }

    enum Type
    {
        Simple,
        Perpetual,
        Click,
        Increment
    }

    Button(Gamepad gamepad, Location location, Type type)
    {
        g = gamepad;
        this.location = location;
        this.type = type;
    }

    private boolean isPressed()
    {
        switch (location)
        {
            case X:
                return g.x;
            case A:
                return g.a;
            case B:
                return g.b;
            case Y:
                return g.y;
            case D_Up:
                return g.dpad_up;
            case D_Down:
                return g.dpad_down;
            case D_Left:
                return g.dpad_left;
            case D_Right:
                return g.dpad_right;
            case Left_Stick:
                return g.left_stick_button;
            case Right_Stick:
                return g.right_stick_button;
            case Left_Bumper:
                return g.left_bumper;
            case Right_Bumper:
                return g.right_bumper;
            case Start:
                return g.start;
            default:
                return false;
        }
    }

    boolean isOn()
    {
        switch (type)
        {
            case Simple:
                return isPressed();
            case Perpetual:
                if(isPressed())
                    on = true;
                return on;
            case Click:
                if(isPressed() ^ lastPressed)
                    on = !on;
                lastPressed = isPressed();
                return on;
            case Increment:
                return count() > 0;
        }
        return false;
    }

    int count()
    {
        switch (type)
        {
            case Increment:
                if(isPressed() ^ lastPressed) {
                    on = !on;
                    if(on)
                        count ++;
                }
                lastPressed = isPressed();
                return count;
            default:
                return isOn()? 1:0;
        }
    }

}
