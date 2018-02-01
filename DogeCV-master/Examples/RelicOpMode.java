
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


@Autonomous(name="DogeCV Relic/Generic Detector", group="DogeCV")

public class JewelOpMode extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();


    private GenericDetector genericDetector = null;
    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");


        genericDetector = new GenericDetector();
        genericDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        genericDetector.colorFilter = new LeviColorFilter(LeviColorFilter.ColorPreset.YELLOW);
        //genericDetector.colorFilter = new HSVColorFilter(new Scalar(30,200,200), new Scalar(15,50,50));
        genericDetector.debugContours = false;
        genericDetector.minArea = 700;
        genericDetector.perfectRatio = 1.8;
        genericDetector.stretch = true;
        genericDetector.stretchKernal = new Size(2,50);
        genericDetector.enable();


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
        if(genericDetector.getFound() == true){
             telemetry.addData("Location", genericDetector.getLocation().toString());
             telemetry.addData("Rect", genericDetector.getRect().toString());
        }
    }

    @Override
    public void stop() {
        genericDetector.disable();
    }


}
