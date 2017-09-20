package org.firstinspires.ftc.teamcode.VelocityVortex;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by team on 7/18/2017.
 */
@TeleOp(name = "Metal Monstrisities MiniBertha TeleOp" , group = "TeleOp")
public class MiniBertha extends OpMode{

    DcMotor right;
    DcMotor left;
    DcMotor lift;
    DcMotor arm;
    double rightPow;
    double leftPow;
    double liftPow;
    double armPow;

    @Override
    public void init() {
        right = hardwareMap.dcMotor.get("right");
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        left = hardwareMap.dcMotor.get("left");
        lift = hardwareMap.dcMotor.get("lift");
        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        arm = hardwareMap.dcMotor.get("arm");
    }

    @Override
    public void loop() {
        rightPow = gamepad1.right_stick_y;
        leftPow = gamepad1.left_stick_y;
        liftPow = gamepad1.right_trigger - gamepad1.left_trigger;
        double armConstant = .5;
        if (gamepad1.right_bumper) {
            armPow = armConstant;
        }
        else if (gamepad1.left_bumper) {
            armPow = -armConstant;
        }
        else {
            armPow = 0;
        }
        right.setPower(rightPow);
        left.setPower(leftPow);
        lift.setPower(liftPow);
        arm.setPower(armPow);
    }
}
