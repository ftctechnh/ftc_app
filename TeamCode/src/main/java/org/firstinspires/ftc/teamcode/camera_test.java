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
        VuforiaTrackable target = null;
        double[] targetLocation = new double[2];
        double[] unitTargetLocation = new double[2];
        double target_x, target_y;

        while (opModeIsActive())
        {
            double[] location = camera.getLocation();
            if(location != null) {

                double heading =  camera.getHeading();

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
                target_x = wallTarget[0];
                target_y = wallTarget[1];

                double robot_x = location[0];
                double robot_y = location[1];

                double delta_x = target_x - robot_x;
                double delta_y = target_y - robot_y;


                //the direction a compass would tell us
                double heading_of_robot_on_field = heading;

                //where a map would tell us a mountain is, relative to us
                double heading_of_target_from_robot_location = Math.atan2(delta_y,delta_x);

                //where compass would say the mountain is located considering our compass isn't pointed north
                double target_heading = heading - heading_of_target_from_robot_location;

                double wall_target_heading = heading - Math.atan2(wallTarget[1] - robot_y, wallTarget[0] - robot_x);

                double wall_heading = heading - Math.round(heading / (Math.PI /2)) * Math.PI/2;

                if(Math.abs(wall_target_heading) > 5 * Math.PI/180)
                    telemetry.addData("rotation",.2 * Math.signum(wall_target_heading));

                else if(Math.abs(delta_x) > 4 || Math.abs(delta_y) > 4) {
                    telemetry.addData("drive x",Math.sin(target_heading) * .3);
                    telemetry.addData("drive y",Math.cos(target_heading) * .3);
                }
                else if(Math.abs(delta_x) > .5 || Math.abs(delta_y) > .5){
                    telemetry.addData("drive x2",Math.sin(target_heading) * .2);
                    telemetry.addData("drive y2",Math.cos(target_heading) * .2);
                }
                else if(Math.abs(wall_heading) > 4 * Math.PI/180)
                    telemetry.addData("rotation2", .2 * Math.signum(wall_heading));
                else
                    telemetry.addLine("Move to wall complete!");



                telemetry.addData("Loc. x", location[0]);
                telemetry.addData("Loc. y", location[1]);
                telemetry.addData("Loc. z", location[2]);
                telemetry.addData("heading", heading * 180 /Math.PI);

                telemetry.addData("target x:", targetLocation[0]);
                telemetry.addData("target y:", targetLocation[1]);
                telemetry.addData("destination x:", wallTarget[0]);
                telemetry.addData("destination y:", wallTarget[1]);
                telemetry.addData("heading from location", heading_of_target_from_robot_location);
                telemetry.addData("target heading", target_heading * 180/Math.PI);

                telemetry.update();
            }
            idle();
        }
    }
}

