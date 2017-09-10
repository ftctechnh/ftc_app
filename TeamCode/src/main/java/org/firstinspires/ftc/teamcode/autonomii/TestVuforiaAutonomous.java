package org.firstinspires.ftc.teamcode.autonomii;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robotplus.autonomoushelper.ImageIdentifier;

/**
 * Created by amigala on 9/9/2017.
 */

public class TestVuforiaAutonomous extends LinearOpMode {
    private ImageIdentifier identifier;

    public void runOpMode() {
        identifier = new ImageIdentifier(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            // run the opMode
            RelicRecoveryVuMark relicRecoveryVuMark = RelicRecoveryVuMark.from(identifier.getLoader().getTrackables().get(0));

            telemetry.addData("Relic Visibility", relicRecoveryVuMark);
        }
    }
}
