package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="  test alpha values", group="Testing")
public class holonomicDrive_test_alpha extends LinearOpMode
{
    Bogg robot;

    Gamepad g1;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        g1 = gamepad1;
        waitForStart();

        while (opModeIsActive())
        {
            if(!robot.manualRotate(g1.right_stick_button,g1.right_stick_x))
            {
                robot.manualDrive(g1.left_stick_button,g1.left_stick_x,g1.left_stick_y);
            }

            robot.manualLift(g1.y,g1.a);


            if(g1.left_bumper)
            {
                robot.incAlpha();
            }

            if(g1.right_bumper)
            {
                robot.decAlpha();
            }

            // Display the current value
            telemetry.addData("alpha: ", robot.getAlpha());
            telemetry.addData("g x: ", g1.left_stick_x);
            telemetry.addData("g y", g1.left_stick_y);
            telemetry.update();
            idle();
        }
    }
}

