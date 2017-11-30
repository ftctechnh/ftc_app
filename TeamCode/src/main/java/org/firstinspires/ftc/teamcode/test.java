package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Preston on 11/25/17.
 */

@TeleOp(name="test", group="Testing")
public class test extends LinearOpMode
{
    DcMotor front;
    DcMotor back;
    DcMotor left;
    DcMotor right;

    @Override
    public void runOpMode()
    {
        front = hardwareMap.dcMotor.get("front");
        back = hardwareMap.dcMotor.get("back");
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");

        front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        front.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        front.setDirection(DcMotor.Direction.FORWARD);
        back.setDirection(DcMotor.Direction.REVERSE);
        left.setDirection(DcMotor.Direction.REVERSE);
        right.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();
        while (opModeIsActive())
        {
            if(gamepad1.dpad_up)
            {
                front.setPower(.5);
            }
            else if(gamepad1.dpad_down)
            {
                back.setPower(.5);
            }
            else if(gamepad1.dpad_left)
            {
                left.setPower(.5);
            }
            else if(gamepad1.dpad_right)
            {
                right.setPower(.5);
            }
            else
            {
                front.setPower(0);
                back.setPower(0);
                left.setPower(0);
                right.setPower(0);
            }
            idle();
        }

    }
}