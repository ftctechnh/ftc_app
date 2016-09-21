package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by FTCGearedUP on 5/7/2016.
 */
public class FirstTeleOp extends OpMode {

    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;

    @Override
    public void init() {
        backLeftMotor = hardwareMap.dcMotor.get("blmotor");
        backRightMotor = hardwareMap.dcMotor.get("brmotor");
        frontLeftMotor = hardwareMap.dcMotor.get("flmotor");
        frontRightMotor = hardwareMap.dcMotor.get("frmotor");

        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    @Override
    public void loop() {
        if (gamepad1.right_stick_y > 0.2) {
            backLeftMotor.setPower(gamepad1.right_stick_y);
            backRightMotor.setPower(gamepad1.right_stick_y);
            frontLeftMotor.setPower(gamepad1.right_stick_y);
            frontLeftMotor.setPower(gamepad1.right_stick_y);
        }
    }
}
