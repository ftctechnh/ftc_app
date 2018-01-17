package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.*;

/**
 * Created by Derek on 11/14/2017.
 */

@TeleOp(group = "Test",name = "MeccanumTest")
public class TestMeccanumOp extends OpMode{
    private DcMotor A,B,C,D;
    @Override
    public void init() {
        try {
            B = hardwareMap.dcMotor.get("frontLeft");
            C = hardwareMap.dcMotor.get("frontRight");
            A = hardwareMap.dcMotor.get("backLeft");
            D = hardwareMap.dcMotor.get("backRight");

            C.setDirection(REVERSE);
            D.setDirection(REVERSE);
        } catch (Exception e) {
            telemetry.addData("",e.getLocalizedMessage());
        }


    }

    @Override
    public void loop() {
        double side = - gamepad1.left_stick_x/2;
        double angle = gamepad1.right_stick_x / 4;
        double foreward = gamepad1.left_stick_y/2 ;

        A.setPower((foreward - side) + angle);
        B.setPower((foreward + side) + angle);
        C.setPower((foreward - side) - angle);
        D.setPower((foreward + side) - angle);
    }
}
