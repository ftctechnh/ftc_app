package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by ftc6347 on 9/22/16.
 */
@TeleOp(name = "Holonomic Drive", group = "Tests")
public class HolonomicDriveOp extends OpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    @Override
    public void init() {
        frontLeft = hardwareMap.dcMotor.get("fl");
        frontRight = hardwareMap.dcMotor.get("fr");
        backLeft = hardwareMap.dcMotor.get("bl");
        backRight = hardwareMap.dcMotor.get("br");
    }

    @Override
    public void loop() {
        if(gamepad1.dpad_right) {
            frontLeft.setPower(-1);
            frontRight.setPower(-1);
            backLeft.setPower(-1);
            backRight.setPower(-1);
        } else if(gamepad1.dpad_left) {
            frontLeft.setPower(1);
            frontRight.setPower(1);
            backLeft.setPower(1);
            backRight.setPower(1);
        } else if(gamepad1.dpad_up) {
             frontLeft.setPower(0.5);
             backLeft.setPower(0.5);
             frontRight.setPower(-0.5);
             backRight.setPower(-0.5);
        } else if(gamepad1.dpad_down) {
            frontLeft.setPower(-0.5);
            backLeft.setPower(-0.5);
            frontRight.setPower(0.5);
            backRight.setPower(0.5);
        } else {
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
        }
    }
}
