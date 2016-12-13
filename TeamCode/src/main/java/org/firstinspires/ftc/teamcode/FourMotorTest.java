package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.modules.GamepadV2;

@TeleOp
public class FourMotorTest extends OpMode {
    private DcMotor frontLeft, backLeft, frontRight, backRight;

    private GamepadV2 pad1 = new GamepadV2();

    @Override
    public void init() {
        frontLeft = hardwareMap.dcMotor.get("FL");
        backLeft = hardwareMap.dcMotor.get("BL");
        frontRight = hardwareMap.dcMotor.get("FR");
        backRight = hardwareMap.dcMotor.get("BR");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        pad1.update(gamepad1);

        frontLeft.setPower(pad1.left_stick_y_exponential(1));
        backLeft.setPower(pad1.left_stick_y_exponential(1));
        frontRight.setPower(pad1.right_stick_y_exponential(1));
        backRight.setPower(pad1.right_stick_y_exponential(1));
    }
}
