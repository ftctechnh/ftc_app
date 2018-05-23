package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by shivaan on 19/5/18.
 */

@TeleOp(name = "Teach1ng", group = "agroup")

public class debug_motor extends LinearOpMode
{
    private DcMotor motorFrontLeft;
    private DcMotor motorBackLeft;
    private DcMotor motorFrontRight;
    private DcMotor motorBackRight;

    int mode = 0;

    @Override
    public void runOpMode() throws InterruptedException
    {
        motorFrontLeft = hardwareMap.dcMotor.get("MC1M1");
        motorBackLeft = hardwareMap.dcMotor.get("MC1M2");
        motorFrontRight = hardwareMap.dcMotor.get("MC2M1");
        motorBackRight = hardwareMap.dcMotor.get("MC2M2");

        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while(opModeIsActive()){

            if (!gamepad1.a && !gamepad1.b && !gamepad1.x && !gamepad1.y) {
                motorBackRight.setPower(0);
            }

            if (gamepad1.dpad_up) {
                mode = 1;
            }

            if (gamepad1.dpad_down) {
                mode = 2;
            }

            if (mode == 1) {
                if (gamepad1.a && !gamepad1.b && !gamepad1.x && !gamepad1.y) {
                    motorBackRight.setPower(gamepad1.right_trigger);
                    break;
                }

                if (!gamepad1.a && gamepad1.b && !gamepad1.x && !gamepad1.y) {
                    motorBackLeft.setPower(gamepad1.right_trigger);
                    break;
                }

                if (!gamepad1.a && !gamepad1.b && gamepad1.x && !gamepad1.y) {
                    motorFrontRight.setPower(gamepad1.right_trigger);
                    break;
                }

                if (!gamepad1.a && !gamepad1.b && !gamepad1.x && gamepad1.y) {
                    motorFrontRight.setPower(gamepad1.right_trigger);
                    break;
                }
            }

            if (mode == 2) {
                if (gamepad1.a && !gamepad1.b && !gamepad1.x && !gamepad1.y) {
                    motorBackRight.setPower(-gamepad1.right_trigger);
                    break;
                }

                if (!gamepad1.a && gamepad1.b && !gamepad1.x && !gamepad1.y) {
                    motorBackLeft.setPower(-gamepad1.right_trigger);
                    break;
                }

                if (!gamepad1.a && !gamepad1.b && gamepad1.x && !gamepad1.y) {
                    motorFrontRight.setPower(-gamepad1.right_trigger);
                    break;
                }

                if (!gamepad1.a && !gamepad1.b && !gamepad1.x && gamepad1.y) {
                    motorFrontLeft.setPower(-gamepad1.right_trigger);
                    break;
                }
            }

            idle();
        }
    }
}
