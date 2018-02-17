package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

/**
 * Created by Derek on 11/21/2017.
 */
@TeleOp(group = "Test",name = "WheelTest")
public class WheelTest extends OpMode {
    private DcMotor A,B,C,D;
    int reg = 0;
    boolean b = false;
    @Override
    public void init() {
        try {
            A = hardwareMap.dcMotor.get("frontLeft");
            C = hardwareMap.dcMotor.get("frontRight");
            B = hardwareMap.dcMotor.get("backLeft");
            D = hardwareMap.dcMotor.get("backRight");

            C.setDirection(REVERSE);
            D.setDirection(REVERSE);
        } catch (Exception e) {
            telemetry.addData("",e.getLocalizedMessage());
        }


    }

    @Override
    public void loop() {
        if (reg >3) reg = 0;

        switch (reg) {
            case 0:
                A.setPower(gamepad1.left_stick_y / 2);
                break;

            case 1:
                B.setPower(gamepad1.left_stick_y / 2);
                break;

            case 2:
                C.setPower(gamepad1.left_stick_y / 2);
                break;

            case 3:
                D.setPower(gamepad1.left_stick_y / 2);
                break;
        }

        if (gamepad1.a && !b) {
            reg++;
            b = true;
        }

        if (!gamepad1.a) {
            b = false;
        }
    }
}
