
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Wake Robotics Member on 11/2/2017.
 */


@TeleOp(name = "holonomicDrive", group = "Tank")
public class holonomicDrive extends OpMode {

    DcMotor m1Front;
    DcMotor m2Right;
    DcMotor m3Back;
    DcMotor m4Left;
    DcMotor plow;

    @Override
    public void init() {
        m1Front = hardwareMap.dcMotor.get("m1");
        m2Right = hardwareMap.dcMotor.get("m2");
        m3Back = hardwareMap.dcMotor.get("m3");
        m4Left = hardwareMap.dcMotor.get("m4");
        plow = hardwareMap.dcMotor.get("plow");
    }

    @Override
    public void loop() {
        double frontBackPower = gamepad1.left_stick_y;
        double leftRightPower = gamepad1.left_stick_x;
        float rightStick = gamepad2.left_stick_y;

        m1Front.setPower(leftRightPower);
        m2Right.setPower(frontBackPower);
        m3Back.setPower(leftRightPower);
        m4Left.setPower(frontBackPower);
        plow.setPower(-rightStick * 0.1);
    }
}
