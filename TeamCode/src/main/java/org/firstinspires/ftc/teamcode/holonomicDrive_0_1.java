package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="holonomicDrive_0_1", group="Testing")
public class holonomicDrive_0_1 extends LinearOpMode
{
    Bogg robot;

    @Override
    public void runOpMode()
    {
        robot = new Bogg(hardwareMap, gamepad1, telemetry);
        waitForStart();
        double x = .6;

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
            if(gamepad1.dpad_up)
            {
                x+=.001;
            }

            if(gamepad1.dpad_down)
            {
                x-=.001;
            }
            robot.setBrake(x);



            robot.lift();

            // Display the current value
            telemetry.addData("leftx: ", gamepad1.left_stick_x);
            telemetry.addData("lefty: ", gamepad1.left_stick_y);
            telemetry.addData("rightx: ", gamepad1.right_stick_x);
            telemetry.addData("righty: ", gamepad1.right_stick_y);
            telemetry.addData("spinAve: ", robot.getSmoothSpin());
            telemetry.update();
            idle();
        }
    }
}

