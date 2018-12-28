package org.firstinspires.ftc.teamcode;

import android.graphics.drawable.GradientDrawable;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;

import java.util.ArrayList;
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
            telemetry.addData("wall heading", wallHeading);

            if(wallHeading != null)
            {
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
            idle();
        }
    }
}

