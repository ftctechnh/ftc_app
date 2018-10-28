package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.HardwareBruinBot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;

@Autonomous (name = "AutoMineral", group = "Rohan")
public class AutoMineral extends LinearOpMode {

    HardwareBruinBot hwMap = new HardwareBruinBot();

    private GoldAlignDetector detector;

    private ElapsedTime     runtime = new ElapsedTime();
    //public boolean found() { return GoldAlignExample.isFound(); }
    //public boolean isAligned() { return detector.getAligned(); }
    //public double getX() { return detector.getXPosition(); }
    public void runOpMode () {

        telemetry.addData("Status", "DogeCV 2018.0 - Gold Align Example");

        detector = new GoldAlignDetector();
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        detector.useDefaults();

        // Optional Tuning
        detector.alignSize = 100; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        detector.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        detector.downscale = 0.4; // How much to downscale the input frames

        detector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //detector.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        detector.maxAreaScorer.weight = 0.005;

        detector.ratioScorer.weight = 5;
        detector.ratioScorer.perfectRatio = 1.0;

        detector.enable();
        //Variable setting rotation angle;


        //Initialize hardware;
        hwMap.init(hardwareMap);
        while (!isStarted()) {

        }
        double fwdSpeed=0.0;  // Normally 0.1
        double rotate = 0.1; // Rotation Speed
        //This loop runs until the gold mineral is found;
        //Need to change this to "while not detected" like in the GoldAlignExample program;
        while (detector.isFound()) {
        //boolean isAligned = false;
        //!! This is where we need to poll the detector
           telemetry.addData("IsAligned" , detector.getAligned()); // Is the bot aligned with the gold mineral
           telemetry.addData("X Pos" , detector.getXPosition()); // Gold X pos.
           telemetry.addLine("testing");
           if(detector.getAligned())
           {
               fwdSpeed = 0.1;
           }
           else
           {
               fwdSpeed = 0.0;
           }

            //For a 0.5 second period, move the robot forward away from the lander;
            // I don't think we need this - "sleep" takes care of this while (runtime.seconds() < 0.5)
            if(detector.getXPosition() < 270)
            {
                //double rotate = 0.2;
                hwMap.leftFrontDrive.setPower(fwdSpeed + rotate); //= drive + strafe + rotate;
                hwMap.leftRearDrive.setPower(fwdSpeed + rotate); //= drive - strafe + rotate;
                hwMap.rightFrontDrive.setPower(fwdSpeed - rotate); //= drive - strafe - rotate;
                hwMap.rightRearDrive.setPower(fwdSpeed - rotate); //= drive + strafe - rotate;

            }
            else if(detector.getXPosition() > 330)
            {
                //double rotate = -0.2;
                hwMap.leftFrontDrive.setPower(fwdSpeed - rotate); //= drive + strafe + rotate;
                hwMap.leftRearDrive.setPower(fwdSpeed - rotate); //= drive - strafe + rotate;
                hwMap.rightFrontDrive.setPower(fwdSpeed + rotate); //= drive - strafe - rotate;
                hwMap.rightRearDrive.setPower(fwdSpeed + rotate); //= drive + strafe - rotate;
            }
            else
            {
                hwMap.leftFrontDrive.setPower(fwdSpeed); //= drive + strafe + rotate;
                hwMap.leftRearDrive.setPower(fwdSpeed); //= drive - strafe + rotate;
                hwMap.rightFrontDrive.setPower(fwdSpeed); //= drive - strafe - rotate;
                hwMap.rightRearDrive.setPower(fwdSpeed); //= drive + strafe - rotate;
            }

            sleep(50);
            /*hwMap.leftFrontDrive.setPower(0.00); //= drive + strafe + rotate;
            hwMap.leftRearDrive.setPower(0.00); //= drive - strafe + rotate;
            hwMap.rightFrontDrive.setPower(0.00); //= drive - strafe - rotate;
            hwMap.rightRearDrive.setPower(0.00); //= drive + strafe - rotate;*/

            //Keep moving and aligning so long as you can see the gold mineral
            /*while (!detector.getAligned()) {

                //Detect the rotation angle to the mineral;
                rotate = detector.getXPosition()/480;

                //The motors will be set to rotate based on the magnitude of misalignment;
                //!! We actually need to scale the "0.1" and scale the "rotate" so that the robot moves and the rotation angle equals "rotation". Could use gyro here;

                hwMap.leftFrontDrive.setPower(0.1 + rotate); //= drive + strafe + rotate;
                hwMap.leftRearDrive.setPower(0.1 + rotate); //= drive - strafe + rotate;
                hwMap.rightFrontDrive.setPower(0.1 - rotate); //= drive - strafe - rotate;
                hwMap.rightRearDrive.setPower(0.1 - rotate); //= drive + strafe - rotate;

            }*/
            //This is where we no longer see the mineral and stop the robot
           telemetry.update();
        }

        //Now we move the robot forward slightly to push the mineral.
           /* hwMap.leftFrontDrive.setPower(0.1); //= drive + strafe + rotate;
            hwMap.leftRearDrive.setPower(0.1); //= drive - strafe + rotate;
            hwMap.rightFrontDrive.setPower(0.1); //= drive - strafe - rotate;
            hwMap.rightRearDrive.setPower(0.1); //= drive + strafe - rotate;
        sleep(1000);*/
        hwMap.leftFrontDrive.setPower(0);
        hwMap.leftRearDrive.setPower(0);
        hwMap.rightFrontDrive.setPower(0);
        hwMap.rightRearDrive.setPower(0);

    }
}
