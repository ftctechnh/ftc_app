package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.robotplus.autonomous.VuforiaWrapper;

@Disabled
@Autonomous(name = "VuforiaImageTester", group = "Testing")
public class AutonomousVuforiaPatternTester extends LinearOpMode {
    private VuforiaWrapper identifier;

    public void runOpMode() {
        identifier = new VuforiaWrapper(hardwareMap);

        identifier.getLoader().getTrackables().activate();

        waitForStart();

        telemetry.addData("Status", "Ready");
        telemetry.update();

        RelicRecoveryVuMark relicRecoveryVuMark;
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            // run the opMode
             relicRecoveryVuMark = RelicRecoveryVuMark.from(this.identifier.getLoader().getRelicTemplate());

            if (relicRecoveryVuMark != RelicRecoveryVuMark.UNKNOWN) {
                telemetry.addData("VuMark Column", relicRecoveryVuMark.name());
            }

            telemetry.update();
        }
    }
}
