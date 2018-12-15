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
    Bogg robot;

    @Override
    public void runOpMode()
    {
        camera = new Camera(hardwareMap, telemetry);
        robot = new Bogg(hardwareMap,gamepad1,telemetry);

        waitForStart();
        VuforiaTrackable target = null;
        double[] targetLocation = new double[2];
        double[] unitTargetLocation = new double[2];
        double target_x = 0, target_y = 0;
        double deltaRight = 0, deltaUp = 0;

        while (opModeIsActive())
        {
            double[] location = camera.getLocation();
            double heading =  camera.getHeading();
            if(location != null) {
                telemetry.addData("x", location[0]);
                telemetry.addData("y", location[1]);
                telemetry.addData("z", location[2]);
                telemetry.addData("heading", heading);

                if(camera.targetVisible(0))
                    target = camera.allTrackables.get(0);
                else if(camera.targetVisible(1))
                    target = camera.allTrackables.get(1);
                else if(camera.targetVisible(2))
                    target = camera.allTrackables.get(2);
                else if(camera.targetVisible(3))
                    target = camera.allTrackables.get(3);

                targetLocation[0] = Math.round(target.getLocation().getTranslation().get(0) / 25.4);
                targetLocation[1] = Math.round(target.getLocation().getTranslation().get(1) / 25.4);

                unitTargetLocation[0] = Math.signum(targetLocation[0]);
                unitTargetLocation[1] = Math.signum(targetLocation[1]);

                double[] wallTarget = new double[]{(5*12 + 8) * unitTargetLocation[0], (5*12 + 8) * unitTargetLocation[1]};
                if(gamepad1.dpad_up)
                    deltaUp += 2;
                else if(gamepad1.dpad_down)
                    deltaUp -= 2;

                if(gamepad1.dpad_right)
                    deltaRight += 2;
                else if(gamepad1.dpad_right)
                    deltaRight -= 2;

                double theta = Math.atan2(wallTarget[1],wallTarget[0]);
                double deltaX = Math.cos(theta) * deltaRight - Math.sin(theta) * deltaUp;
                double deltaY = Math.sin(theta) * deltaRight + Math.cos(theta) * deltaUp;

                target_x = wallTarget[0] + deltaX;
                target_y = wallTarget[1] + deltaY;

                double robot_x = location[0];
                double robot_y = location[1];

                double delta_x = target_x - robot_x;
                double delta_y = target_y - robot_y;

                if(Math.abs(delta_x) < 4 && Math.abs(delta_y) < 4)
                {
                    telemetry.addData("drive", "0");
                }

                double target_heading = (heading - Math.atan2(target_y - robot_y, target_x - robot_x));

                double wall_target_heading = (heading - Math.atan2(wallTarget[1] - robot_y, wallTarget[0] - robot_x));

                robot.rotateToTarget(targetLocation[0], targetLocation[1], 5);
                robot.driveToTarget(wallTarget[0], wallTarget[1], .3, 3);

                if (Math.abs(wall_target_heading) < 5 * Math.PI/180)
                    telemetry.addLine("Good on rotation");
                else
                    telemetry.addData("Rotation:",.2 * Math.signum(wall_target_heading));


                telemetry.addData("x",Math.sin(target_heading) * .3);
                telemetry.addData("y",Math.cos(target_heading) * .3);
                telemetry.addData("target x:", targetLocation[0]);
                telemetry.addData("target y:", targetLocation[1]);
                telemetry.addData("destination x:", wallTarget[0]);
                telemetry.addData("destination y:", wallTarget[1]);
                telemetry.addData("target heading", target_heading * 180/Math.PI);
            }

            telemetry.update();
            idle();
        }
    }
}


