package org.firstinspires.ftc.team6417;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.lynx.LynxEmbeddedIMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import detectors.FoundationPipeline.SkyStone;
import detectors.ImageDetector;
import detectors.OpenCvDetector;

@Autonomous(name="Wafflywaffle", group="Autonomous")
public class Waffle extends LinearOpMode {

    Hardware6417 robot = new Hardware6417();
    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;

    Boolean exit = false;

    public void runOpMode(){

        ImageDetector detector = new ImageDetector(this, false);
        detector.start();

        detector.getPosition();


        /***
        OpenCvDetector cam = new OpenCvDetector(this);
        cam.loop(); // update
        SkyStone[] stones = cam.getObjectsSkyStones();
         ***/

    }

}
