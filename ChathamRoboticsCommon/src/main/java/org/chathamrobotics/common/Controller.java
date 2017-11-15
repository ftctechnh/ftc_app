package org.chathamrobotics.common;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 10/29/2017
 */

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * The representation of a controller
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Controller extends Gamepad {
    public enum ButtonState {
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

    private final Gamepad _gamepad;

    /**
     * Creates a new instance of {@link Gamepad}
     * @param gamepad   the _gamepad to base the controller off
     */
    public Controller(Gamepad gamepad) {
        this._gamepad = gamepad;
    }

    /**
     * Updates the controllers values
     */
    public void update() {
        try {
            copy(_gamepad);
        } catch (RobotCoreException e) {
            RobotLog.e("Gamepads could not be copied");
            throw new IllegalStateException("Gamepads could not be copied");
        }


        aState = updateButtonState(a, aState);
        bState = updateButtonState(b, bState);
        xState = updateButtonState(x, xState);
        yState = updateButtonState(y, yState);
        padUpState = updateButtonState(dpad_up, padUpState);
        padDownState = updateButtonState(dpad_down, padDownState);
        padLeftState = updateButtonState(dpad_left, padLeftState);
        padRightState = updateButtonState(dpad_right, padRightState);

        leftBumperState = updateButtonState(left_bumper, leftBumperState);
        rightBumperState = updateButtonState(right_bumper, rightBumperState);
    }

    /**
     * Inverts the value. Useful for the sticks' y values
     * @param val   the value to invert
     * @return      the inverted value
     */
    public float invert(float val) {
        return val * -1;
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
