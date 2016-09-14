package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.modules.GamepadV2;


/**
 * Created by billcipher1344 on 9/13/2016.
 */

@TeleOp
public class yayayayayayay extends OpMode {

    GamepadV2 pad1 = new GamepadV2();
    DcMotor motor;

    public void init() {

    motor = hardwareMap.dcMotor.get("motor");
    }

    public void loop(){

        pad1.update(gamepad1);
        motor.setPower(pad1.left_stick_y_exponential(1));

    }
}

