package com.lasarobotics.library.controller;

import com.google.gson.annotations.SerializedName;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Implements a functional controller with an event API
 */
public class Controller {

    //BUTTONS
    //Buttons use an integer to identify state, found in controller.ButtonState
    @SerializedName("du")
    public int dpad_up;         //Directional pad UP
    @SerializedName("dd")
    public int dpad_down;       //Directional pad DOWN
    @SerializedName("dl")
    public int dpad_left;       //Directional pad LEFT
    @SerializedName("dr")
    public int dpad_right;      //Directional pad RIGHT
    @SerializedName("a")
    public int a;               //A button
    @SerializedName("b")
    public int b;               //B button
    @SerializedName("x")
    public int x;               //X button
    @SerializedName("y")
    public int y;               //Y button
    @SerializedName("g")
    public int guide;           //Guide button
    @SerializedName("s")
    public int start;           //START button
    @SerializedName("bk")
    public int back;            //BACK button
    @SerializedName("bl")
    public int left_bumper;     //left bumper
    @SerializedName("br")
    public int right_bumper;    //right bumper

    //TRIGGERS
    //Triggers use a float for how much they are pressed
    @SerializedName("tl")
    public float left_trigger;  //left trigger
    @SerializedName("tr")
    public float right_trigger; //right trigger

    //JOYSTICKS
    //Joysticks don't have any events, just values
    @SerializedName("lx")
    public float left_stick_x;  //left joystick X axis
    @SerializedName("ly")
    public float left_stick_y;  //left joystick Y axis
    @SerializedName("rx")
    public float right_stick_x; //right joystick X axis
    @SerializedName("ry")
    public float right_stick_y; //right joystick Y axis

    /**
     * Initialize a blank controller
     */
    public Controller() {
        reset();
    }

    /**
     * Initialize a controller from another (cloning)
     *
     * @param another Another Controller
     */
    public Controller(Controller another) {
        this.dpad_up = another.dpad_up;
        this.dpad_down = another.dpad_down;
        this.dpad_left = another.dpad_left;
        this.dpad_right = another.dpad_right;
        this.a = another.a;
        this.b = another.b;
        this.x = another.x;
        this.y = another.y;
        this.guide = another.guide;
        this.start = another.start;
        this.back = another.back;
        this.left_bumper = another.left_bumper;
        this.right_bumper = another.right_bumper;

        this.left_trigger = another.left_trigger;
        this.right_trigger = another.right_trigger;

        this.left_stick_x = another.left_stick_x;
        this.left_stick_y = another.left_stick_y;
        this.right_stick_x = another.right_stick_x;
        this.right_stick_y = another.right_stick_y;
    }

    /**
     * Initialize a controller from a Gamepad (FIRST library underlayer)
     */
    public Controller(Gamepad g) {
        dpad_up = g.dpad_up ? ButtonState.HELD : ButtonState.NOT_PRESSED;
        dpad_down = g.dpad_down ? ButtonState.HELD : ButtonState.NOT_PRESSED;
        dpad_left = g.dpad_left ? ButtonState.HELD : ButtonState.NOT_PRESSED;
        dpad_right = g.dpad_right ? ButtonState.HELD : ButtonState.NOT_PRESSED;
        a = g.a ? ButtonState.HELD : ButtonState.NOT_PRESSED;
        b = g.b ? ButtonState.HELD : ButtonState.NOT_PRESSED;
        x = g.x ? ButtonState.HELD : ButtonState.NOT_PRESSED;
        y = g.y ? ButtonState.HELD : ButtonState.NOT_PRESSED;
        guide = g.guide ? ButtonState.HELD : ButtonState.NOT_PRESSED;
        start = g.start ? ButtonState.HELD : ButtonState.NOT_PRESSED;
        back = g.back ? ButtonState.HELD : ButtonState.NOT_PRESSED;
        left_bumper = g.left_bumper ? ButtonState.HELD : ButtonState.NOT_PRESSED;
        right_bumper = g.right_bumper ? ButtonState.HELD : ButtonState.NOT_PRESSED;
        right_trigger = g.right_trigger;
        left_stick_x = g.left_stick_x;
        left_stick_y = g.left_stick_y;
        right_stick_x = g.right_stick_x;
        right_stick_y = g.right_stick_y;
    }

    public static Controller getZeroController() {
        Controller a = new Controller();
        a.reset();
        return a;
    }

    public static Controller getPressedController() {
        Controller a = new Controller();
        a.dpad_up = ButtonState.HELD;
        a.dpad_down = ButtonState.HELD;
        a.dpad_left = ButtonState.HELD;
        a.dpad_right = ButtonState.HELD;
        a.a = ButtonState.HELD;
        a.b = ButtonState.HELD;
        a.x = ButtonState.HELD;
        a.y = ButtonState.HELD;
        a.guide = ButtonState.HELD;
        a.start = ButtonState.HELD;
        a.back = ButtonState.HELD;
        a.left_bumper = ButtonState.HELD;
        a.right_bumper = ButtonState.HELD;

        a.left_trigger = 1.0f;
        a.right_trigger = 1.0f;

        a.left_stick_x = 1.0f;
        a.left_stick_y = 1.0f;
        a.right_stick_x = 1.0f;
        a.right_stick_y = 1.0f;
        return a;
    }

    public void reset() {
        dpad_up = ButtonState.NOT_PRESSED;
        dpad_down = ButtonState.NOT_PRESSED;
        dpad_left = ButtonState.NOT_PRESSED;
        dpad_right = ButtonState.NOT_PRESSED;
        a = ButtonState.NOT_PRESSED;
        b = ButtonState.NOT_PRESSED;
        x = ButtonState.NOT_PRESSED;
        y = ButtonState.NOT_PRESSED;
        guide = ButtonState.NOT_PRESSED;
        start = ButtonState.NOT_PRESSED;
        back = ButtonState.NOT_PRESSED;
        left_bumper = ButtonState.NOT_PRESSED;
        right_bumper = ButtonState.NOT_PRESSED;

        left_trigger = 0.0f;
        right_trigger = 0.0f;

        left_stick_x = 0.0f;
        left_stick_y = 0.0f;
        right_stick_x = 0.0f;
        right_stick_y = 0.0f;
    }

    /**
     * Update the Controller states from a Gamepad.
     * CALL THIS METHOD ON EVERY EVENT LOOP!
     */
    //FIXME is Gamepad g necessary?
    public void update(Gamepad g) {
        dpad_up = handleUpdate(dpad_up, g.dpad_up);
        dpad_down = handleUpdate(dpad_down, g.dpad_down);
        dpad_left = handleUpdate(dpad_left, g.dpad_left);
        dpad_right = handleUpdate(dpad_right, g.dpad_right);
        a = handleUpdate(a, g.a);
        b = handleUpdate(b, g.b);
        x = handleUpdate(x, g.x);
        y = handleUpdate(y, g.y);
        guide = handleUpdate(guide, g.guide);
        start = handleUpdate(start, g.start);
        back = handleUpdate(back, g.back);
        left_bumper = handleUpdate(left_bumper, g.left_bumper);
        right_bumper = handleUpdate(right_bumper, g.right_bumper);
        left_trigger = g.left_trigger;
        right_trigger = g.right_trigger;
        left_stick_x = g.left_stick_x;
        left_stick_y = g.left_stick_y;
        right_stick_x = g.right_stick_x;
        right_stick_y = g.right_stick_y;
    }

    /**
     * Update an individual button or bumper
     *
     * @param b             Variable from Controller
     * @param updatedstatus Boolean from Gamepad
     * @return The new state
     */
    private int handleUpdate(Integer b, boolean updatedstatus) {
        if (updatedstatus) {
            if (b == ButtonState.NOT_PRESSED || b == ButtonState.RELEASED)
                return ButtonState.PRESSED;
            else
                return ButtonState.HELD;
        } else {
            if (b == ButtonState.PRESSED || b == ButtonState.HELD)
                return ButtonState.RELEASED;
            else
                return ButtonState.NOT_PRESSED;
        }
    }

    @Override
    public String toString() {
        return "Controller{" +
                "dpad_up=" + dpad_up +
                ", dpad_down=" + dpad_down +
                ", dpad_left=" + dpad_left +
                ", dpad_right=" + dpad_right +
                ", a=" + a +
                ", b=" + b +
                ", x=" + x +
                ", y=" + y +
                ", guide=" + guide +
                ", start=" + start +
                ", back=" + back +
                ", left_bumper=" + left_bumper +
                ", right_bumper=" + right_bumper +
                ", left_trigger=" + left_trigger +
                ", right_trigger=" + right_trigger +
                ", left_stick_x=" + left_stick_x +
                ", left_stick_y=" + left_stick_y +
                ", right_stick_x=" + right_stick_x +
                ", right_stick_y=" + right_stick_y +
                '}';
    }
}
