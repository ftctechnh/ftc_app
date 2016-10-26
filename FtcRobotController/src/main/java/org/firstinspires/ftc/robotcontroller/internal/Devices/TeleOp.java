package org.firstinspires.ftc.robotcontroller.internal.Devices;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by abnaveed on 10/13/2016.
 */
public class TeleOp extends OpMode {
    // TeleOp class



    @Override
    public void init() {

    }

    @Override
    public void loop() {
        double Left_Joystick = -gamepad1.left_stick_y;
        double Right_Joystick= -gamepad1.left_stick_y;
    }
}
