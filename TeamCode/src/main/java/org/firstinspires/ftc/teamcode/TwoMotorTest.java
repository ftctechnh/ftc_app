package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.modules.GamepadV2;

@TeleOp (group = "MotorTest")
public class TwoMotorTest extends OpMode {
    DcMotor motor, motor1;
    GamepadV2 pad1 = new GamepadV2();

    public void init() {
        motor = hardwareMap.dcMotor.get("motor");
        motor1 = hardwareMap.dcMotor.get("motor1");
        motor1.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void loop() {
        pad1.update(gamepad1);
        motor.setPower(pad1.left_stick_y_exponential(1.00));
        motor1.setPower(pad1.right_stick_y_exponential(1.00));
    }
}
