package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="  test alpha values", group="Testing")
public class holonomicDrive_test_alpha extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        waitForStart();

        while (opModeIsActive())
        {
            if(gamepad1.right_stick_y != 0 )
            {
                robot.manualRotate(gamepad1);
            }
            else
            {
                robot.manualDrive(gamepad1);
            }

            robot.manualLift();


            if(gamepad1.left_bumper)
            {
                robot.incAlpha();
            }

            if(gamepad1.right_bumper)
            {
                robot.decAlpha();
            }

            // Display the current value
            telemetry.addData("alpha: ", robot.getAlpha());
            telemetry.addData("x Power: ", robot.xAve);
            telemetry.addData("y Power: ", robot.yAve);
            telemetry.addData("gamepad x: ", gamepad1.left_stick_x);
            telemetry.addData("gamepad y", gamepad1.left_stick_y);
            telemetry.update();
            idle();
        }
    }
}

