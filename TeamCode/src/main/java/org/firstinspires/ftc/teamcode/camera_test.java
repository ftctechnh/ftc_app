package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@TeleOp(name="  camera_test", group="Testing")
public class camera_test extends LinearOpMode
{
    Camera camera;

    @Override
    public void runOpMode()
    {
        camera = new Camera(hardwareMap, telemetry);
        waitForStart();

        while (opModeIsActive())
        {
            Double wallHeading = camera.headingToWall();

            if(wallHeading != null)
            {
                telemetry.addData("wall heading", wallHeading);
                if(Math.abs(wallHeading) < 2)
                    telemetry.addLine("Done!");
                else
                {
                    if(wallHeading > 0)
                        telemetry.addData("rotate", .08);
                    else
                        telemetry.addData("rotate", -.08);
                }
            }
            telemetry.update();
            idle();
        }
    }
}

