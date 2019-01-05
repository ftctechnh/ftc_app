package org.firstinspires.ftc.teamcode.Utilities;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/* Sub Assembly Class
 */
public class UserControl {
    /* Declare private class objects */
    private LinearOpMode opmode = null;     /* local copy of opmode class */
    private Telemetry telemetry = null;     /* local copy of telemetry object from opmode class */

    /* Declare extended gamepad */
    private GamepadWrapper egamepad1 = null;
    private GamepadWrapper egamepad2 = null;

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
        opmode = opMode;
        telemetry = opMode.telemetry;

        telemetry.addLine("User Control" + " initialize");
        telemetry.update();

        /* Instantiate extended gamepad */
        egamepad1 = new GamepadWrapper(opMode.gamepad1);
        egamepad2 = new GamepadWrapper(opMode.gamepad2);
    }

    public boolean getRedBlue(String prompt) {
        boolean isRed = false;

        telemetry.addData("[X = blue, B = red] ",prompt);
        telemetry.update();
        egamepad1.updateEdge();
        do {
            egamepad1.updateEdge();
        } while (!egamepad1.x.pressed && !egamepad1.b.pressed && !opmode.isStopRequested());
        if (egamepad1.b.pressed)
            isRed = true;
        egamepad1.updateEdge();
        return isRed;
    }

    public boolean getYesNo(String prompt) {
        boolean isYes = false;

        telemetry.addData("[A = yes, B = no] ",prompt);
        telemetry.update();
        egamepad1.updateEdge();
        do {
            egamepad1.updateEdge();
        } while (!egamepad1.a.pressed && !egamepad1.b.pressed && !opmode.isStopRequested());
        if (egamepad1.a.pressed)
            isYes = true;
        egamepad1.updateEdge();
        return isYes;
    }

    public boolean getLeftRight(String prompt) {
        boolean isLeft = false;

        telemetry.addData("[X = left, B = right] ", prompt);
        telemetry.update();
        egamepad1.updateEdge();
        do {
            egamepad1.updateEdge();
        } while (!egamepad1.x.pressed && !egamepad1.b.pressed && !opmode.isStopRequested());
        if (egamepad1.x.pressed)
            isLeft = true;
        egamepad1.updateEdge();
        return isLeft;
    }

    public DPAD getDPad(String prompt) {
        DPAD dpad = null;

        telemetry.addData("[dpad] ",prompt);
        telemetry.update();
        egamepad1.updateEdge();
        do {
            egamepad1.updateEdge();
            if (egamepad1.dpad_up.pressed) dpad = DPAD.UP;
            if (egamepad1.dpad_right.pressed) dpad = DPAD.RIGHT;
            if (egamepad1.dpad_down.pressed) dpad = DPAD.DOWN;
            if (egamepad1.dpad_left.pressed) dpad = DPAD.LEFT;
        }
        while ((dpad == null) && !opmode.isStopRequested());
        return dpad;
    }

    public int getInt(String prompt) {
        int Integer = 0;

        telemetry.addData("[A = confirm, B = 0, Up = +1, Down = -1] ", prompt);
        telemetry.update();
        egamepad1.updateEdge();
        do {
            do {
                egamepad1.updateEdge();
            }
            while (!egamepad1.a.pressed && !egamepad1.b.pressed && !egamepad1.dpad_up.released && !egamepad1.dpad_down.released && !opmode.isStopRequested());
            if (!egamepad1.dpad_up.released)
                Integer ++;
            if (egamepad1.dpad_down.released)
                Integer --;
            telemetry.addData(prompt, " = ", Integer);
            telemetry.update();
        } while (!egamepad1.a.pressed && !egamepad1.b.pressed && !opmode.isStopRequested());
        if (egamepad1.b.pressed)
            Integer = 0;
        egamepad1.updateEdge();
        return Integer;
    }
}
