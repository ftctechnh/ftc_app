package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * ControllerTest
 * Prints out controller data to the Driver Station so we can tell what the Robot is seeing.
 */
public class ControllerTest extends OpMode {


    private ElapsedTime runtime = new ElapsedTime();
    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void start() {
        runtime.reset();
    }

    public static String toBinaryString(final Boolean input){
        if(input == null) {
            return null;
        } else {
            return (input ? "1" : "0");
        }

    }

    /*
     * Comment goes here
     */
    public static String dpadDirectionString(final Gamepad gamepad) {
        if (gamepad == null){
            return null;
        } else {
            if(gamepad.dpad_down)
                return "Down";
            if(gamepad.dpad_left)
                return "Left";
            if(gamepad.dpad_right)
                return "Right";
            if(gamepad.dpad_up)
                return "Up";
            return "None";
        }
    }
    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void loop() {
        telemetry.addData("Timer", "Running for " + runtime.toString());
        telemetry.addData("Buttons", "A B X Y: " +
                toBinaryString(gamepad1.a) + " " +
                toBinaryString(gamepad1.b) + " " +
                toBinaryString(gamepad1.x) + " " +
                toBinaryString(gamepad1.y));
        telemetry.addData("Joysticks", "Left: (" + String.format("%.2f", gamepad1.left_stick_x) + "," + String.format("%.2f", gamepad1.left_stick_y) +
                ") Right: (" + String.format("%.2f", gamepad1.right_stick_x) + "," + String.format("%.2f", gamepad1.right_stick_y) + ")");
        telemetry.addData("Dpad", "Dpad: " + dpadDirectionString(gamepad1));
        telemetry.addData("Bumpers", "LB LT RB RT: " +
                toBinaryString(gamepad1.left_bumper) + " " +
                String.format("%.2f",gamepad1.left_trigger) + " " +
                toBinaryString(gamepad1.right_bumper) + " " +
                String.format("%.2f",gamepad1.right_trigger));
        telemetry.addData("Middle", "Back, Start: " + toBinaryString(gamepad1.back) + "," + toBinaryString(gamepad1.start));
    }

    /*
     * Code to run when the op mode is first disabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }
}
