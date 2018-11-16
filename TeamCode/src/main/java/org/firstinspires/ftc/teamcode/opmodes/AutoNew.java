package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robotutil.Direction;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrainNew;
import org.firstinspires.ftc.teamcode.robotutil.Utils;
import org.firstinspires.ftc.teamcode.robotutil.Vision;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous", group="FinalShit")

public class AutoNew extends LinearOpMode {

    private DriveTrainNew dt;
    private Vision vision;

    private Telemetry.Item goldXTelem;

    // Tune these
    private final double DIST_TO_GOLD = 12;
    private final double DIST_TO_DEPOT = 36;
    private final double DIST_TO_CRATER = 36;
    private final int GOLD_ALIGN_LOC = 100;

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        if (opModeIsActive()) {
            vision.startVision();

            dt.rotateIMU(Direction.CCW, 70, 0.1, 3);

            // Land

            // Reset to 0 degrees
//            dt.rotateToHeading(180 , 0.5, 5);

            // Align with gold mineral
//            goldAlign(0.5, 5);
//
//            // Encoder drive forward
//            dt.move(Direction.FORWARD, 0.5, DIST_TO_GOLD, 10);
//
//            // Encoder drive back
//            dt.move(Direction.FORWARD, 0.5, DIST_TO_GOLD, 10);
//
//            // Reset to 0 degrees
//            dt.rotateToHeading(0, 0.5, 5);
//
//            // Drive to depot
//            dt.move(Direction.FORWARD, 0.5, DIST_TO_DEPOT, 10);
//
//            // Drop off marker
//            /* IMPLEMENT MARKER DROPPING */
//
//            // Rotate toward crater
//            dt.rotateIMU(Direction.CW, 135, 0.5, 5);
//
//            // Drive to crater
//            dt.move(Direction.FORWARD, 0.5, DIST_TO_CRATER, 10);
        }

        vision.shutDown();
    }

    private void goldAlign(double power, double timeoutS) {
        double minError = 10;

        if (vision.detect() == -1) {
            goldXTelem.setValue(-1);
        } else {
            Direction direction;
            int goldX;
            int error;

            double startTime = System.currentTimeMillis();
            do {
                goldX = vision.detect();
                error = GOLD_ALIGN_LOC - goldX;

                if (error > 0) {
                    direction = Direction.CW;
                } else {
                    direction = Direction.CCW;
                }

                dt.move(direction, power);
                goldXTelem.setValue(goldX);
            } while (goldX != -1 &&
                    Math.abs(error) > minError &&
                    (System.currentTimeMillis() - startTime) / 1000 < timeoutS);
        }
    }

    private void initialize() {
        dt = new DriveTrainNew(this);
        vision = new Vision(this);

        goldXTelem = telemetry.addData("Gold mineral position", -1);
    }

    private void haltUntilPressStart() {
        while (!gamepad1.start  && !isStopRequested()) {
            Utils.waitFor(300);
        }
    }

}
