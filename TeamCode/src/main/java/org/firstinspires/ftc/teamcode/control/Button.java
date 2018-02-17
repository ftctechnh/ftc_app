package org.firstinspires.ftc.teamcode.control;

/**
 * Created by Derek on 2/13/2018.
 */

public class Button {
    private ButtonState state;

    public ButtonState getState() {
        return state;
    }

    public boolean isPressed() {
        return state.equals(ButtonState.PRESSED);
    }

    public void setState(ButtonState state) {
        this.state = state;
    }
}