
package org.firstinspires.ftc.teamcode.testing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.io.IOException;


@Autonomous(name="DogeCV Jewel Detector", group="DogeCV")

public class JewelOpMode extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();


    private JewelDetector jewelDetector = null;
    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");


        jewelDetector = new JewelDetector();
        jewelDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());

        //Jewel Detector Settings
        jewelDetector.areaWeight = 0.02;
        jewelDetector.detectionMode = JewelDetector.JewelDetectionMode.MAX_AREA; // PERFECT_AREA
        //jewelDetector.perfectArea = 6500; <- Needed for PERFECT_AREA
        jewelDetector.debugContours = true;
        jewelDetector.maxDiffrence = 15;
        jewelDetector.ratioWeight = 15;
        jewelDetector.minArea = 700;

        jewelDetector.enable();


    }

    @Override
    public void init_loop() {
        telemetry.addData("Status", "Initialized.");
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {



        telemetry.addData("Status", "Run Time: " + runtime.toString());

        telemetry.addData("Current Order", "Jewel Order: " + jewelDetector.getCurrentOrder().toString()); // Current Result
        telemetry.addData("Last Order", "Jewel Order: " + jewelDetector.getLastOrder().toString()); // Last Known Result

    }

    @Override
    public void stop() {
        jewelDetector.disable();
    }

}
