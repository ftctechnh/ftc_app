package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by guberti on 11/3/2017.
 */
@Autonomous(name="Pictograph seer", group="Diagnostics")
public class PictographSeer extends CompleteOldAutonomous {

    @Override
    public void runOpMode() {

        robot.init(hardwareMap, this, gamepad1, gamepad2);
        initializeVuforia();

        waitForStart();

        while (opModeIsActive()) {

            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
                telemetry.addData("VuMark", "%s visible", vuMark);
            } else {
                telemetry.addData("VuMark", "not visible");
            }
            telemetry.update();
        }
    }
}
