package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by FTCGearedUP on 5/15/2016.
 */
public class BuildBotOp extends OpMode {
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotorController drive;

    @Override
    public void init() {
        leftMotor = hardwareMap.dcMotor.get("lw");
        rightMotor = hardwareMap.dcMotor.get("rw");
        drive = hardwareMap.dcMotorController.get("c1");

        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD); // motors are facing opposite direction, needs to be reversed
    }

    @Override
    public void loop() {

        if (gamepad1.y) {
            leftMotor.setPower(-.2);  //Slow Forward
            rightMotor.setPower(-.2);
        }

        else if (gamepad1.a) {
            leftMotor.setPower(.2);  //Slow Back
            rightMotor.setPower(.2);
        }

        else if (gamepad1.x) {
            leftMotor.setPower(-.2);  //Slow Left
            rightMotor.setPower(.2);
        }

        else if (gamepad1.b) {
            leftMotor.setPower(.2);  //Slow Right
            rightMotor.setPower(-.2);
        }

        else{
            if (Math.abs(gamepad1.right_stick_y) > 0.2) {
                leftMotor.setPower(gamepad1.right_stick_y); // Right motor main drive
            } else {
                leftMotor.setPower(0);
            }

            if (Math.abs(gamepad1.left_stick_y) > 0.2) {
                rightMotor.setPower(gamepad1.left_stick_y); // Left motor main drive
            } else {
                rightMotor.setPower(0);
            }
        }

    }
}
