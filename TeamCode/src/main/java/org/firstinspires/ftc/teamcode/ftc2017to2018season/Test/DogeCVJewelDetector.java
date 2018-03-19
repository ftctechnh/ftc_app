
package org.firstinspires.ftc.teamcode.ftc2017to2018season.Test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.io.IOException;

import static com.disnodeteam.dogecv.detectors.JewelDetector.JewelOrder.BLUE_RED;
import static com.disnodeteam.dogecv.detectors.JewelDetector.JewelOrder.RED_BLUE;


@Autonomous(name="DogeCV Jewel Detector", group="DogeCV")

public class DogeCVJewelDetector extends OpMode
{
public Servo jewelServo;
public Servo jewelServoRotate;
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();


    private JewelDetector jewelDetector = null;
    /*
     * Code to run ONCE when the driver hits INIT
     */

    @Override
    public void init() {
        jewelServo = hardwareMap.servo.get("jewelServo");
        jewelServoRotate = hardwareMap.servo.get("jewelServoRotate");
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
        switch (jewelDetector.getCurrentOrder()){
            case UNKNOWN:
                telemetry.addData("Balls not seen", "Solution TBD   :/");
                break;
            case BLUE_RED:
                //move the jewel manipulator to the left to knock off the ball
                jewelServoRotate.setPosition(1);
                jewelServo.setPosition(0.8);
                //move the jewel manipulator to the original position
                jewelServoRotate.setPosition(0.79);
                break;
            case RED_BLUE:
                //move the jewel manipulator to the right to knock off the ball
                jewelServoRotate.setPosition(0.5);
                jewelServo.setPosition(0.8);
                //move it back to the original posititon
                jewelServoRotate.setPosition(0.79);
                //Add code to swing the jwele arm
                break;

        }

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



        switch (jewelDetector.getCurrentOrder()){
            case UNKNOWN:
                telemetry.addData("Balls not seen", "Solution TBD   :/");
                break;
            case BLUE_RED:
                //move the jewel manipulator to the left to knock off the ball
                jewelServoRotate.setPosition(1);
                jewelServo.setPosition(0.8);
                //move the jewel manipulator to the original position
                jewelServoRotate.setPosition(0.79);
                break;
            case RED_BLUE:
                //move the jewel manipulator to the right to knock off the ball
                jewelServoRotate.setPosition(0.5);
                jewelServo.setPosition(0.8);
                //move it back to the original posititon
                jewelServoRotate.setPosition(0.79);
                //Add code to swing the jwele arm
                break;

        }


}
}
