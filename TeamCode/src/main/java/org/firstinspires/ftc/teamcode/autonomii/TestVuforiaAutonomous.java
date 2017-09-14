package org.firstinspires.ftc.teamcode.autonomii;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robotplus.autonomoushelper.VuforiaWrapper;

@Autonomous(name = "VuforiaTester", group = "Autononomousous")
public class TestVuforiaAutonomous extends LinearOpMode {
    private VuforiaWrapper identifier;

    public void runOpMode() {
        identifier = new VuforiaWrapper(hardwareMap);

        identifier.getLoader().getTrackables().activate();

        waitForStart();

        telemetry.addData("Status", "Ready");
        telemetry.update();

        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            // run the opMode
            RelicRecoveryVuMark relicRecoveryVuMark = RelicRecoveryVuMark.from(this.identifier.getLoader().getRelicTemplate());

            if (relicRecoveryVuMark != RelicRecoveryVuMark.UNKNOWN) {
                telemetry.addData("Relic Visibility", relicRecoveryVuMark);
                telemetry.addData("VuMark Column", relicRecoveryVuMark.name());
            }

            telemetry.update();
        }
    }
}
