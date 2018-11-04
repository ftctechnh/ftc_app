package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="holonomicDrive_test_alpha", group="Testing")
public class holonomicDrive_test_alpha extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        double x = .5;
        waitForStart();

        while (opModeIsActive())
        {
            if(gamepad1.right_stick_y != 0 )
            {
                robot.manualRotate();
            }
            else
            {
                robot.manualDrive();
            }

            robot.lift();


            if(gamepad1.dpad_up)
            {
                robot.incAlpha();
            }

            if(gamepad1.dpad_down)
            {
                robot.decAlpha();
            }

            // Display the current value
            telemetry.addData("leftx: ", robot.smoothX(gamepad1.left_stick_x));
            telemetry.addData("lefty: ", robot.smoothY(gamepad1.left_stick_y));
            telemetry.addData("servo x: ", x);
            telemetry.addData("leftx: ", gamepad1.left_stick_x);
            telemetry.addData("lefty: ", gamepad1.left_stick_y);
            telemetry.addData("alpha: ", robot.getAlpha());
            telemetry.update();
            idle();
        }
    }
}

