package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Tony_Air on 10/1/15.
 */
public class TeleGamePad extends OpMode{


    public void init(){


    }
    public void loop(){


        telemetry.addData ("01", "A: " + gamepad1.a);
        telemetry.addData ("02", "B: " + gamepad1.b);
        telemetry.addData ("03", "X: " + gamepad1.x);
        telemetry.addData ("04", "Y: " + gamepad1.y);

        telemetry.addData ("05", "LB: " + gamepad1.left_bumper);
        telemetry.addData ("06", "LT: " + gamepad1.left_trigger);

        telemetry.addData ("07", "RB: " + gamepad1.right_bumper);
        telemetry.addData ("08", "RT: " + gamepad1.right_trigger);

        telemetry.addData ("09", "Left X: " + gamepad1.left_stick_x);
        telemetry.addData ("10", "Left Y: " + gamepad1.left_stick_y);
        telemetry.addData ("11", "Left Button: " + gamepad1.left_stick_button);


        telemetry.addData ("12", "Right X: " + gamepad1.right_stick_x);
        telemetry.addData ("13", "Right Y: " + gamepad1.right_stick_y);
        telemetry.addData ("14", "Right Button: " + gamepad1.right_stick_button);

        telemetry.addData("15", "DPad Up: " + gamepad1.dpad_up);
        telemetry.addData("16", "DPad Down: " + gamepad1.dpad_down);
        telemetry.addData("17", "DPad Left: " + gamepad1.dpad_left);
        telemetry.addData("18", "DPad Right: " + gamepad1.dpad_right);

        telemetry.addData("19", "Start: " + gamepad1.start);
        telemetry.addData("20", "Back: " + gamepad1.back);
        telemetry.addData("21", "Guide: " + gamepad1.guide);




    }


}
