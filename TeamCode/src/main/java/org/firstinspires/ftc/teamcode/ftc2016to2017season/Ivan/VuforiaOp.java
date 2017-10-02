package org.firstinspires.ftc.teamcode.ftc2016to2017season.Ivan;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.R;

/**
 * Created by Ivan on 10/27/2016.
 */

@Autonomous(name="VuforiaExample", group="Vuforia")
@Disabled

public class VuforiaOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.vuforiaLicenseKey = "AW4nWA3/////AAAAGV+8hBh6UEjlrukZl7GnOgoQ6sWiB4nIVSVpUyyEJRnjqoXVr2kcSck/kgOjf63y5ipfm2LWD7nl8k4GxtH1dBb28a35yW6m3VWF0TilnYyq5d39bCFt890Vp32o01yc+vW3XZiw9o9jKbbrJfZk64Wg1fPko7+E3SaiIxheqciTXqKis4NHTgV2fyYFfm4vZc+F6CGSKz30Q9ELXFSrwon0Ng5xFwNPY9zEFQREjBdAbAUEz6Ijo/Bc8DZDTec4JGgT0XS9JrwH+lEm2tmJMaIeQqyhqs84XM+dF1xaluvsEeDZ9iZAh9RDyk70qNiLbyahD9Zw04wSzbj2d0MIAn77XpwzY4vTn2a6NcwLOBth";
        params.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(params);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);

        VuforiaTrackables beacons = vuforia.loadTrackablesFromAsset("FTC_2016-17");
        beacons.get(0).setName("Wheels");
        beacons.get(1).setName("Tools");
        beacons.get(2).setName("Lego");
        beacons.get(3).setName("Gears ");

        waitForStart();

        beacons.activate();

        while (opModeIsActive()){
            for(VuforiaTrackable beac : beacons){ //for(int i = 0; i < beacons.size(); i++){ VuforiaTrackable beac = beacons.get(i);
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beac.getListener()).getPose();

                if(pose != null){
                    VectorF translation = pose.getTranslation();

                    telemetry.addData(beac.getName() + "-Translation", translation);

                    double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2)));

                    telemetry.addData(beac.getName() + "-Degrees", degreesToTurn);
                }
            }
            telemetry.update();
        }
    }
}
