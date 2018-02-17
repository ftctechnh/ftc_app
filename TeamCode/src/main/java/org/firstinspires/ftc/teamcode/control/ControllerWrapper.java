package org.firstinspires.ftc.teamcode.control;

import android.util.Log;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.general.Updatable;

import java.util.Objects;

import static org.firstinspires.ftc.teamcode.control.ButtonState.HELD;
import static org.firstinspires.ftc.teamcode.control.ButtonState.PRESSED;

/**
 * Created by Derek on 2/13/2018.
 */

public class ControllerWrapper implements Updatable{
    private Gamepad gamepad;
    public Button A,B,X,Y,DPAD_UP,DPAD_DOWN,DPAD_LEFT,DPAD_RIGHT,LEFT_STICK_BUTTON,RIGHT_STICK_BUTTON,START,GUIDE,LEFT_BUMPER,RIGHT_BUMPER;

    public ControllerWrapper(Gamepad gamepad) {
        this.gamepad = gamepad;

        A = new Button();
        B = new Button();
        X = new Button();
        Y = new Button();
        DPAD_UP = new Button();
        DPAD_DOWN = new Button();
        DPAD_LEFT = new Button();
        DPAD_RIGHT = new Button();
        LEFT_STICK_BUTTON = new Button();
        RIGHT_STICK_BUTTON = new Button();
        START = new Button();
        GUIDE = new Button();
        LEFT_BUMPER = new Button();
        RIGHT_BUMPER = new Button();
    }

    @Override
    public void update() {
        setState(A,gamepad.a);
        setState(B,gamepad.b);
        setState(X,gamepad.x);
        setState(Y,gamepad.y);
        setState(DPAD_UP,gamepad.dpad_up);
        setState(DPAD_DOWN,gamepad.dpad_down);
        setState(DPAD_LEFT,gamepad.dpad_left);
        setState(DPAD_RIGHT,gamepad.dpad_right);
        setState(LEFT_STICK_BUTTON,gamepad.left_stick_button);
        setState(RIGHT_STICK_BUTTON,gamepad.right_stick_button);
        setState(START,gamepad.start);
        setState(GUIDE,gamepad.guide);
        setState(LEFT_BUMPER,gamepad.left_bumper);
        setState(RIGHT_BUMPER,gamepad.right_bumper);
    }

    private void setState(Button button,boolean pressed) {

        if (pressed) {
            if (button.getState() == PRESSED || button.getState() == HELD) {
                button.setState(HELD);
            } else {
                button.setState(PRESSED);

            }
        } else {
            button.setState(ButtonState.UNPRESSED);
        }


    }
}


