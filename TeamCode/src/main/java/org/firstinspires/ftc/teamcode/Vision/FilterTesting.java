package org.firstinspires.ftc.teamcode.Vision;


import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldDetector;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name="Just Filter Testing", group="Vision")

public class FilterTesting extends LinearOpMode {
    private GoldDetector detector;


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "DogeCV 2018.0 - Gold Align Example");

        detector = new GoldDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();

        // Optional Tuning
        detector.downscale = 1; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005;

        detector.ratioScorer.weight = 5;
        detector.ratioScorer.perfectRatio = 1.0;

        detector.enable();

        waitForStart();

        while(opModeIsActive()) {
            if (detector.isFound()) {
                telemetry.addData("Position" , detector.getFoundRect().toString()); // Gold X pos
            }
            telemetry.addData("Position" , "Unknown"); // Gold X pos
        }

        detector.disable();
    }

}
