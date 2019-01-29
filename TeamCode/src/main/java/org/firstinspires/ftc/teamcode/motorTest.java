
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.Math.*;

/**
 * Created by Wake Robotics Member on 11/2/2017.
 */


@TeleOp(name = "MotorTest", group = "Tank")
public class motorTest extends OpMode {

    DcMotor back_right;
    DcMotor back_left;
    DcMotor front_left;
    DcMotor front_right;
    DcMotor plow;

    @Override
    public void init() {
        back_right = hardwareMap.dcMotor.get("back_right");
        back_left = hardwareMap.dcMotor.get("back_left");
        front_left = hardwareMap.dcMotor.get("front_left");
        front_right = hardwareMap.dcMotor.get("front_right");
        plow = hardwareMap.dcMotor.get("plow");
    }

    @Override
    public void loop() {
        double frontBackPower = gamepad1.left_stick_y;  //power to spin holonomic
        double leftRightPower = gamepad1.left_stick_x;  //power to spin holonomic
        double spinPower = -gamepad1.right_stick_x; //power to spin holonomic

        float rightStick = gamepad2.left_stick_y;   //plow power

        back_right.setPower(gamepad1.left_stick_y);
        back_left.setPower(gamepad1.left_stick_x);
        front_left.setPower(gamepad2.left_stick_x);
        front_right.setPower(gamepad2.left_stick_y);

        telemetry.addData("back_right", "%.2f",  gamepad1.left_stick_y);
        telemetry.addData("back_left", "%.2f",  gamepad1.left_stick_x);
        telemetry.addData("front_left", "%.2f",  gamepad2.left_stick_x);
        telemetry.addData("front_right", "%.2f",  gamepad2.left_stick_y);

    }
}
