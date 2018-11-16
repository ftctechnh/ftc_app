package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="ex")
public class BasicDrive extends OpMode{

    chassis test = new chassis();

    public void init() {
        test.init(hardwareMap);
    }

    public void loop() {

    }

    public void drive(Gamepad gamepad) {
        if(Math.abs(gamepad.left_stick_y) > Math.abs(gamepad.left_stick_x)) {
            test.forward(gamepad.left_stick_y);
        } else if(Math.abs(gamepad.left_stick_x) > Math.abs(gamepad.left_stick_y)) {
            test.turn(gamepad.left_stick_x);
        } else {
            test.move(0, 0,0 , 0);
        }
    }

}
