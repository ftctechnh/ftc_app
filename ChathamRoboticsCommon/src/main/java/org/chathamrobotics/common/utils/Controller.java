package org.chathamrobotics.common.utils;

import com.qualcomm.robotcore.hardware.Gamepad;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 10/29/2017
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Controller {
    enum ButtonState {
        TAPPED,
        HELD,
        STATIONARY,
        RELEASED,
    }

    public ButtonState aState = ButtonState.STATIONARY;
    public ButtonState bState = ButtonState.STATIONARY;
    public ButtonState xState = ButtonState.STATIONARY;
    public ButtonState yState = ButtonState.STATIONARY;

    public ButtonState padUpState = ButtonState.STATIONARY;
    public ButtonState padDownState = ButtonState.STATIONARY;
    public ButtonState padLeftState = ButtonState.STATIONARY;
    public ButtonState padRightState = ButtonState.STATIONARY;

    public ButtonState leftBumperState = ButtonState.STATIONARY;
    public ButtonState rightBumperState = ButtonState.STATIONARY;

    public boolean a = false;
    public boolean b = false;
    public boolean x = false;
    public boolean y = false;

    public boolean dpad_up = false;
    public boolean dpad_down = false;
    public boolean dpad_left = false;
    public boolean dpad_right = false;

    public boolean left_bumper = false;
    public boolean right_bumper = false;

    public boolean guide = false;
    public boolean start = false;
    public boolean back = false;

    public boolean left_stick_button = false;
    public boolean right_stick_button = false;

    public float left_stick_x = 0;
    public float left_stick_y = 0;

    public float right_stick_x = 0;
    public float right_stick_y = 0;

    public float left_trigger = 0;
    public float right_trigger = 0;

    private final Gamepad gamepad;

    /**
     * Creates a new instance of {@link Gamepad}
     * @param gamepad   the gamepad to base the controller off
     */
    public Controller(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    /**
     * Updates the controllers values
     */
    public void update() {
        a = gamepad.a;
        aState = updateButtonState(a, aState);

        b = gamepad.b;
        bState = updateButtonState(b, bState);

        x = gamepad.x;
        xState = updateButtonState(x, xState);

        y = gamepad.y;
        yState = updateButtonState(y, yState);

        dpad_up = gamepad.dpad_up;
        padUpState = updateButtonState(dpad_up, padUpState);

        dpad_down = gamepad.dpad_down;
        padDownState = updateButtonState(dpad_down, padDownState);

        dpad_left = gamepad.dpad_left;
        padLeftState = updateButtonState(dpad_left, padLeftState);

        dpad_right = gamepad.dpad_right;
        padRightState = updateButtonState(dpad_right, padRightState);

        left_bumper = gamepad.left_bumper;
        leftBumperState = updateButtonState(left_bumper, leftBumperState);

        right_bumper = gamepad.right_bumper;
        rightBumperState = updateButtonState(right_bumper, rightBumperState);

        guide = gamepad.guide;
        start = gamepad.start;
        back = gamepad.back;

        left_stick_button = gamepad.left_stick_button;
        right_stick_button = gamepad.right_stick_button;

        left_stick_x = gamepad.left_stick_x;
        left_stick_y = gamepad.left_stick_y;

        right_stick_x = gamepad.right_stick_x;
        right_stick_y = gamepad.right_stick_y;

        left_trigger = gamepad.left_trigger;
        right_trigger = gamepad.right_trigger;
    }

    /**
     * Inverts the value. Useful for the sticks' y values
     * @param val   the value to invert
     * @return      the inverted value
     */
    public float invert(float val) {
        return val * -1;
    }

    /**
     * Returns the vector representation of the left stick's position
     * @return the vector representation of the left stick's position
     */
    public Vector getLeftStickVector() {
        return new Vector(gamepad.left_stick_x, invert(gamepad.left_stick_y));
    }

    /**
     * Returns the vector representation of the right stick's position
     * @return the vector representation of the right stick's position
     */
    public Vector getRightStickVector() {
        return new Vector(gamepad.right_stick_x, invert(gamepad.right_stick_y));
    }

    private ButtonState updateButtonState(boolean pressed, ButtonState currentState) {
        if (pressed) {
            if (currentState == ButtonState.TAPPED || currentState == ButtonState.HELD) return ButtonState.HELD;

            return ButtonState.TAPPED;
        }

        if (currentState == ButtonState.RELEASED || currentState == ButtonState.STATIONARY) return ButtonState.STATIONARY;

        return ButtonState.RELEASED;
    }
}
