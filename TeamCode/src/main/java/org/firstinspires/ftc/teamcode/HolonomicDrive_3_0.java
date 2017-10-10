package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="HolonomicDrive_3_0", group="Testing")
public class HolonomicDrive_3_0 extends LinearOpMode
{
    double RadiansPerTicks = (527.05 * Math.PI * 2 * Math.PI)/(1120 * 100 * Math.PI);
    double offset = Math.PI / 4;
    double theta = offset;

    @Override
    public void runOpMode()
    {
        DcMotor front = hardwareMap.dcMotor.get("front");
        DcMotor back = hardwareMap.dcMotor.get("back");
        DcMotor left = hardwareMap.dcMotor.get("left");
        DcMotor right = hardwareMap.dcMotor.get("right");

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

        double x = 0;
        double y = 0;

        waitForStart();
        while (opModeIsActive())
        {
            if(gamepad1.right_stick_y != 0 ){
                x = gamepad1.right_stick_y / 2;
                y = -1 * gamepad1.right_stick_y / 2;

                front.setPower(x);
                back.setPower(-1*x);
                left.setPower(y);
                right.setPower(-1*y);

                theta = offset + front.getCurrentPosition()*RadiansPerTicks;

            }
            else {

                if(gamepad1.dpad_up)
                {
                    x = 0;
                    y = 1;
                }
                else if(gamepad1.dpad_down)
                {
                    x = 0;
                    y = -1;
                }
                else if(gamepad1.dpad_left)
                {
                    x = -1;
                    y = 0;
                }
                else if(gamepad1.dpad_right)
                {
                    x = 1;
                    y = 0;
                }
                else
                {
                    x = gamepad1.left_stick_x;
                    y = gamepad1.left_stick_y;
                }

                double xprime = x * Math.cos(theta) - y * Math.sin(theta);
                double yprime = x * Math.sin(theta) + y * Math.cos(theta);

                front.setPower(xprime);
                back.setPower(xprime);
                left.setPower(yprime);
                right.setPower(yprime);
            }

            telemetry.addData("leftx: ", gamepad1.left_stick_x);
            telemetry.addData("lefty: ", gamepad1.left_stick_y);
            telemetry.addData("rightx: ", gamepad1.right_stick_x);
            telemetry.addData("righty: ", gamepad1.right_stick_y);
            telemetry.addData("x: ", x);
            telemetry.addData("y: ", y);
            telemetry.addData("theta: ", (theta*180/Math.PI) % 360);
            telemetry.addData("radians: ", theta);
            telemetry.addData("actual: ", front.getCurrentPosition());
            telemetry.update();
            idle();
        }

    }
}
