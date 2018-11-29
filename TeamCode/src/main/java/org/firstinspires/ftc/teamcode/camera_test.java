package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.ArrayList;
@Disabled
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
            Double dist = camera.alignToTarget(0);
            telemetry.addData("found:", dist != null);
            telemetry.addData("distance:", dist);
            telemetry.update();
            idle();
        }
    }
}

