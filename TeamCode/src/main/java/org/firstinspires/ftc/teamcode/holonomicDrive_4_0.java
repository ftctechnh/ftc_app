package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.DriveEngine;

@TeleOp(name="holonomicDrive_4_0", group="Testing")
public class holonomicDrive_4_0 extends LinearOpMode
{
    DriveEngine engine;
    double x = 0;
    double y = 0;

    @Override
    public void runOpMode()
    {
        engine = new DriveEngine(hardwareMap);

        waitForStart();
        while (opModeIsActive())
        {
            if(gamepad1.right_stick_y != 0 ){
                engine.rotate(gamepad1.right_stick_y);
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
                engine.drive(x,y);
            }

            telemetry.addData("leftx: ", gamepad1.left_stick_x);
            telemetry.addData("lefty: ", gamepad1.left_stick_y);
            telemetry.addData("rightx: ", gamepad1.right_stick_x);
            telemetry.addData("righty: ", gamepad1.right_stick_y);
            telemetry.addData("x: ", x);
            telemetry.addData("y: ", y);
            telemetry.update();
            idle();
        }

    }
}