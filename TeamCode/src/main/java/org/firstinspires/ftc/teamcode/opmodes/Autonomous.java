package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robotutil.Direction;
import org.firstinspires.ftc.teamcode.robotutil.DriveTrainNew;
import org.firstinspires.ftc.teamcode.robotutil.GoldCV;
import org.firstinspires.ftc.teamcode.robotutil.RotateMethod;
import org.firstinspires.ftc.teamcode.robotutil.Utils;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous",group="FinalShit")

public class Autonomous extends LinearOpMode {

    DriveTrainNew dt;
    GoldCV cv;

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();

        if (opModeIsActive()) {
            alignWithGold(1);
            // Drive forward
        }
    }

    private void initialize() {
        dt = new DriveTrainNew(this);
        cv = new GoldCV(this);
    }

    // UNTESTED
    private void alignWithGold(double power) {
        // Random values, needs tuning
        double alignPos = 100;
        double allowedAlignError = 100;
        double maxError = 100;

        if (cv.isFound()) {
            double error = cv.getXPosition() - alignPos;
            while (error > allowedAlignError) {
                double proportionalPower = power * error / maxError;
                if (error > 0) {
                    dt.rotate(Direction.CCW, RotateMethod.SPIN, proportionalPower);
                } else {
                    dt.rotate(Direction.CW, RotateMethod.SPIN, proportionalPower);
                }

                error = cv.getXPosition() - alignPos;
            }
        }
        telemetry.addData("gold mineral found: ", cv.isFound());
        telemetry.addData("gold mineral aligned: ", cv.getAligned());
    }

    private void haltUntilPressStart() {
        while (!gamepad1.start  && !isStopRequested()) {
            Utils.waitFor(300);
        }
    }
}
