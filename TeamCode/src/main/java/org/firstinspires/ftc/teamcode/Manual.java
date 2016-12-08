package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Michael on 10/14/2016.
 */

@TeleOp(name="Manual")
public class Manual extends RobotHardware {

    @Override public void loop() {

        //Dpads

        if(gamepad1.dpad_up) set_drive_power(1, 1);

        if(gamepad1.dpad_down) set_drive_power(-1, -1);

        if(gamepad1.dpad_left) set_drive_power(-1, 1);

        if(gamepad1.dpad_right) set_drive_power(1, -1);


        if(!(gamepad1.dpad_left || gamepad1.dpad_right || gamepad1.dpad_down || gamepad1.dpad_up)) set_drive_power(0, 0);

        ////////////////////

        //Joysticks1

        set_drive_power(gamepad1.left_stick_y, gamepad1.left_stick_x); //This may be wrong. Testing needed...

        ////////////////////
    }

}