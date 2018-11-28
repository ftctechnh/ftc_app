package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class UserControl {
    /* Declare private class objects
     */
    private Telemetry telemetry;            /* local copy of telemetry object from opmode class */
    private String name = "User Control";

    /* Declare extended gamepad */
    GamepadWrapper egamepad1 = null;
    GamepadWrapper egamepad2 = null;

    /* Declare public class objects
     */
    public enum DPAD {
        UP, RIGHT, DOWN, LEFT;
    }

    /* Constructor */
    public UserControl() {
    }

    public void init(LinearOpMode opMode) {
        /* Set local copies from opmode class */
        telemetry = opMode.telemetry;

        telemetry.addLine(name + " initialize");

        /* Instantiate extended gamepad */
        egamepad1 = new GamepadWrapper(opMode.gamepad1);
        egamepad2 = new GamepadWrapper(opMode.gamepad2);
    }

    public boolean getRedBlue(String prompt) {
        boolean isRed = false;

        telemetry.addLine(prompt);
        telemetry.addLine("[X = blue, B = red]");
        telemetry.update();
        egamepad1.updateEdge();
        do {
            egamepad1.updateEdge();
        } while (!egamepad1.x.pressed && !egamepad1.b.pressed);
        if (egamepad1.b.pressed)
            isRed = true;
        egamepad1.updateEdge();
        return isRed;
    }

    public boolean getYesNo(String prompt) {
        boolean isYes = false;

        telemetry.addLine(prompt);
        telemetry.addLine("[A = yes, B = no]");
        telemetry.update();
        egamepad1.updateEdge();
        do {
            egamepad1.updateEdge();
        } while (!egamepad1.a.pressed && !egamepad1.b.pressed);
        if (egamepad1.a.pressed)
            isYes = true;
        egamepad1.updateEdge();
        return isYes;
    }

    public boolean getLeftRight(String prompt) {
        boolean isLeft = false;

        telemetry.addLine(prompt);
        telemetry.addLine("[X = left, B = right]");
        telemetry.update();
        egamepad1.updateEdge();
        do {
            egamepad1.updateEdge();
        } while (!egamepad1.x.pressed && !egamepad1.b.pressed);
        if (egamepad1.x.pressed)
            isLeft = true;
        egamepad1.updateEdge();
        return isLeft;
    }

    public DPAD getDPad(String prompt) {
        DPAD dpad = null;

        telemetry.addLine(prompt);
        telemetry.addLine("[dpad]");
        telemetry.update();
        egamepad1.updateEdge();
        do {
            egamepad1.updateEdge();
            if (egamepad1.dpad_up.pressed) dpad = DPAD.UP;
            if (egamepad1.dpad_right.pressed) dpad = DPAD.RIGHT;
            if (egamepad1.dpad_down.pressed) dpad = DPAD.DOWN;
            if (egamepad1.dpad_left.pressed) dpad = DPAD.LEFT;
        }
        while (dpad == null);
        return dpad;
    }
}
