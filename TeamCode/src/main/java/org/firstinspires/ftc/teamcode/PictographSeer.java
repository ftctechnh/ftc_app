package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;

import java.util.List;

/**
 * Created by guberti on 11/3/2017.
 */
@Autonomous(name="Pictograph seer", group="Autonomous")
public class PictographSeer extends LinearOpMode {

    NullbotHardware robot = new NullbotHardware();
    VuforiaLocalizer vuforia;
    List<VuforiaTrackable> trackables;

    public void runOpMode() {

        robot.init(hardwareMap, this, false, gamepad2);


        while (opModeIsActive()) {

            for (VuforiaTrackable trackable : trackables) {
                boolean seen = ((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible();
                telemetry.addData(trackable.getName(), seen ? "Visible" : "Not Visible");
            }
            telemetry.update();
        }
    }
}
