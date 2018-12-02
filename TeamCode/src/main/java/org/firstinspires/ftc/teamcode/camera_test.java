package org.firstinspires.ftc.teamcode;

import android.graphics.drawable.GradientDrawable;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

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
            double[] location = camera.getLocation();
            double heading =  camera.getHeading();
            if(location != null) {
                telemetry.addData("x", location[0]);
                telemetry.addData("y", location[1]);
                telemetry.addData("z", location[2]);
                telemetry.addData("heading", heading);
            }

            telemetry.update();
            idle();
        }
    }
}

