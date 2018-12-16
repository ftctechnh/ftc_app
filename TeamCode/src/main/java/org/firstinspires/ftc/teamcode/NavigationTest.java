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
            double[] drive = camera.getMoveToWall();
            switch (drive.length)
            {
                case 0:
                    break;
                case 1:
                    telemetry.addData("rotate", drive[0]);
                    break;
                case 2:
                    telemetry.addData("driveX", drive[0]);
                    telemetry.addData("driveY", drive[1]);
                    break;
            }

            double[] orbit = camera.getOrbit(0,0, true);
            switch (orbit.length)
            {
                case 0:
                    break;
                case 2:
                    telemetry.addData("orbitX", orbit[0]);
                    telemetry.addData("orbitY", orbit[1]);
                    break;
            }
            telemetry.update();
            idle();
        }
    }
}


