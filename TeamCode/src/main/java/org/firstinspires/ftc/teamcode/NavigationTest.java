package org.firstinspires.ftc.teamcode;

import android.graphics.drawable.GradientDrawable;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;

import java.util.ArrayList;
@TeleOp(name="NavigationTest", group="Testing")
public class NavigationTest extends LinearOpMode
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
            Double heading = camera.getHeading();
            VuforiaTrackable target = null;
            if (camera.targetVisible(0))
                target = camera.allTrackables.get(0);
            else if (camera.targetVisible(1))
                target = camera.allTrackables.get(1);
            else if (camera.targetVisible(2))
                target = camera.allTrackables.get(2);
            else if (camera.targetVisible(3))
                target = camera.allTrackables.get(3);

            if(location != null && heading != null && target != null)
            {
                double[] drive = camera.getMoveToWall(location, heading, target);
                switch (drive.length)
                {
                    case 1:
                        telemetry.addData("rotate", drive[0]);
                        break;
                    case 2:
                        telemetry.addData("driveX", drive[0]);
                        telemetry.addData("driveY", drive[1]);
                        break;
                }
                double[] orbit = camera.getOrbit(location,0,0, true);
                telemetry.addData("orbitX", orbit[0]);
                telemetry.addData("orbitY", orbit[1]);
            }

            telemetry.update();

            idle();
        }
    }
}


