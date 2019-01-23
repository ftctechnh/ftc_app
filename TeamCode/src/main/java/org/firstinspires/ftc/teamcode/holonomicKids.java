package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="holonomicKids", group="Testing")
public class holonomicKids extends LinearOpMode
{
    MiniBogg robot;

    @Override
    public void runOpMode()
    {
        robot = new MiniBogg(hardwareMap, telemetry);
        robot.alpha *= 5;

        waitForStart();

        while (opModeIsActive())
        {

            if(gamepad1.right_stick_x != 0 )
            {
                robot.manualRotate();
            }
            else
            {
                robot.manualDrive();
            }

            // Display the current value

            telemetry.update();
            idle();
        }
    }
}

