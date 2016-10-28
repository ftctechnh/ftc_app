package org.firstinspires.ftc.robotcontroller.internal.Devices;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by abnaveed on 10/13/2016.
 */
public class TeleOp extends OpMode {
    // TeleOp class
    public DcMotor  leftMotor   = null;
    public DcMotor  rightMotor  = null;

    @Override
    public void init() {

    }
    @Override
    public void loop() {
        double Left_Joystick = -gamepad1.left_stick_y;
        double Right_Joystick= -gamepad1.left_stick_y;
        //converting joystick values to motor power values
        rightMotor.setPower(Right_Joystick);
        leftMotor.setPower(Left_Joystick);
    }
}
