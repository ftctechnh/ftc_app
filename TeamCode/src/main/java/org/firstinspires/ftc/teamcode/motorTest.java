
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

    DcMotor m1;
    DcMotor m2;
    DcMotor m3;
    DcMotor m4;
    DcMotor plow;

    @Override
    public void init() {
        m1 = hardwareMap.dcMotor.get("m1");
        m2 = hardwareMap.dcMotor.get("m2");
        m3 = hardwareMap.dcMotor.get("m3");
        m4 = hardwareMap.dcMotor.get("m4");
        plow = hardwareMap.dcMotor.get("plow");
    }

    @Override
    public void loop() {
        double frontBackPower = gamepad1.left_stick_y;  //power to spin holonomic
        double leftRightPower = gamepad1.left_stick_x;  //power to spin holonomic
        double spinPower = -gamepad1.right_stick_x; //power to spin holonomic

        float rightStick = gamepad2.left_stick_y;   //plow power

        m1.setPower(gamepad1.left_stick_y);
        m2.setPower(gamepad1.left_stick_x);
        m3.setPower(gamepad2.left_stick_x);
        m4.setPower(gamepad2.left_stick_y);

        telemetry.addData("m1", "%.2f",  gamepad1.left_stick_y);
        telemetry.addData("m2", "%.2f",  gamepad1.left_stick_x);
        telemetry.addData("m3", "%.2f",  gamepad2.left_stick_x);
        telemetry.addData("m4", "%.2f",  gamepad2.left_stick_y);

    }
}
