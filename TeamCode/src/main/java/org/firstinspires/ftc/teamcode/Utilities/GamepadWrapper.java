package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Created by ablauch on 11/3/2017.
 * this class detects the button press and release
 * call the update method when you want to check for new press/release
 */

public class GamepadWrapper {

    public class ButtonEdge {
        public boolean state = false;
        public boolean pressed = false;
        public boolean released = false;

        /* Call this method when you want to update the detection of the button edges pressed/released */
        public void updateEdge(boolean new_state) {
            /* check if button has just been pressed */
            if (new_state && !state) pressed = true;
            else pressed = false;

            /* check if button has just been released */
            if (!new_state && state) released = true;
            else released = false;

            state = new_state;
        }
    }

    private Gamepad mygamepad;

    /* actual boolean buttons */
    public ButtonEdge dpad_up = new ButtonEdge();
    public ButtonEdge dpad_down = new ButtonEdge();
    public ButtonEdge dpad_left = new ButtonEdge();
    public ButtonEdge dpad_right = new ButtonEdge();
    public ButtonEdge a = new ButtonEdge();
    public ButtonEdge b = new ButtonEdge();
    public ButtonEdge x = new ButtonEdge();
    public ButtonEdge y = new ButtonEdge();
    public ButtonEdge guide = new ButtonEdge();
    public ButtonEdge start = new ButtonEdge();
    public ButtonEdge back = new ButtonEdge();
    public ButtonEdge left_bumper = new ButtonEdge();
    public ButtonEdge right_bumper = new ButtonEdge();
    public ButtonEdge left_stick_button = new ButtonEdge();
    public ButtonEdge right_stick_button = new ButtonEdge();

    /* boolean button interpreted from analog signals */
    public ButtonEdge left_trigger = new ButtonEdge();
    public ButtonEdge right_trigger = new ButtonEdge();

    /* Constructor */
    public GamepadWrapper(Gamepad gp) {
        mygamepad = gp;
    }

    /* Call this method when you want to update the detection of the button edges pressed/released */
    public void updateEdge() {

        dpad_up.updateEdge(mygamepad.dpad_up);
        dpad_down.updateEdge(mygamepad.dpad_down);
        dpad_left.updateEdge(mygamepad.dpad_left);
        dpad_right.updateEdge(mygamepad.dpad_right);
        a.updateEdge(mygamepad.a);
        b.updateEdge(mygamepad.b);
        x.updateEdge(mygamepad.x);
        y.updateEdge(mygamepad.y);
        guide.updateEdge(mygamepad.guide);
        start.updateEdge(mygamepad.start);
        back.updateEdge(mygamepad.back);
        left_bumper.updateEdge(mygamepad.left_bumper);
        right_bumper.updateEdge(mygamepad.right_bumper);
        left_stick_button.updateEdge(mygamepad.left_stick_button);
        right_stick_button.updateEdge(mygamepad.right_stick_button);

        /* interpreted from analog signals */
        left_trigger.updateEdge(mygamepad.left_trigger > 0.5);
        right_trigger.updateEdge(mygamepad.right_trigger > 0.5);
    }
}
