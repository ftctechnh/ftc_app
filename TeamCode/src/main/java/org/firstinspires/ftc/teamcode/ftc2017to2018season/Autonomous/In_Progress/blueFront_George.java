/*============================================================================================================================================
                                                            EDIT HISTORY



when                                      who                       Purpose/Change
-----------------------------------------------------------------------------------------------------------------------------------------------

=============================================================================================================================================*/
package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.In_Progress;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.JewelDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous.Autonomous_General_George;
import com.disnodeteam.dogecv.detectors.JewelDetector;
import com.vuforia.Vuforia;

import static com.disnodeteam.dogecv.detectors.JewelDetector.JewelOrder.BLUE_RED;
import static com.disnodeteam.dogecv.detectors.JewelDetector.JewelOrder.RED_BLUE;


//10-28-17
@Autonomous(name = "Blue Front George Worlds")
//@Disabled
public class blueFront_George extends Autonomous_General_George_ {

    public double rsBuffer = 20.00;
    private ElapsedTime runtime = new ElapsedTime();
    JewelDetector jewelDetector = new JewelDetector();

    @Override
    public void runOpMode() {

        vuforiaInit(true, true);
        telemetry.addData("","Vuforia Initiated");
        telemetry.update();
        initiate(false);
        sleep(500);
        telemetry.addData("","GOOD TO GO! :)");
        telemetry.update();

        waitForStart();
//reseting gyro sensor
        jewelServoRotate.setPosition(0.74);
        sleep(100);
        toggleLight(true);
        //light.setPower(0.5);
        startTracking();
        telemetry.addData("","READY TO TRACK");
        telemetry.update();

        double begintime= runtime.seconds();
        while(!vuMarkFound() && runtime.seconds() - begintime <= waitTime){


        }
        toggleLight(false);

        telemetry.addData("Vumark" , vuMark);
        telemetry.update();
        sleep(250);
        relicTrackables.deactivate();
        jewelDetector.areaWeight = 0.02;
        jewelDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        jewelDetector.detectionMode = JewelDetector.JewelDetectionMode.MAX_AREA; // PERFECT_AREA
        jewelDetector.debugContours = true;
        jewelDetector.maxDiffrence = 15;
        jewelDetector.ratioWeight = 15;
        jewelDetector.minArea = 700;
        jewelDetector.enable();
        sleep(750);
        //Used to make sure the jewels are recognized
telemetry.addData("Jewel order is ", jewelDetector.getCurrentOrder());
telemetry.update();

        switch (jewelDetector.getCurrentOrder()){
            case BLUE_RED:
                //move the jewel manipulator to the left to knock off the ball
                jewelServoRotate.setPosition(1);
                sleep(300);
                jewelServo.setPosition(0.8);
                sleep(750);
                //move the jewel manipulator to the original position
                jewelServoRotate.setPosition(0.79);
                sleep(1000);
                break;

            case RED_BLUE:
                //move the jewel manipulator to the right to knock off the ball
                jewelServoRotate.setPosition(0.5);
                sleep(300);
                jewelServo.setPosition(0.8);
                sleep(750);
                //move it back to the original posititon
                jewelServoRotate.setPosition(0.79);
                //Add code to swing the jwele arm
                break;
            case UNKNOWN:
                telemetry.addData("Balls not seen", "Solution TBD   :/");
                telemetry.update();
                readColorRev();
                KnockjewelSensor(ballColor);
                break;
        }
        //Used to make sure the jewels are recognized
        telemetry.addData("Jewel order is ", jewelDetector.getCurrentOrder());
        telemetry.update();
        jewelServo.setPosition(1);

        moveUpGlyph(0.7);//change distances once we lower the stress of the glyph manipulator        middleGlyphManipulator();
        sleep(250);
        moveDownGlyph(1.45);
        sleep(250);
        closeGlyphManipulator();
        sleep(250);
        moveUpGlyph(1.45);
        sleep(250);


        sleep(200);

        encoderMecanumDrive(0.4,40,40,5000,0);
        sleep(100);
        gyroTurnREV(0.4,0);
        sleep(100);
        wallAlign(0.5,28, 0);//since the columns of the cryptobox are protruding,
                                                    // the range sensor is actually using the distance from the protruding columns
                                                    //the last value is 0 for the blue auto and 1 for the red auto
        sleep(200);
        gyroTurnREV(0.5, -84);
        sleep(100);



        if (vuMark == RelicRecoveryVuMark.LEFT){//should be 20 cm away from wall for left
            //goes to given distance away from the wall
            //wallAlign(0.3, 35, 1);
            encoderMecanumDrive(0.3, -14, -14, 5000, 0);
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER || vuMark == RelicRecoveryVuMark.UNKNOWN){
            encoderMecanumDrive(0.3,-5,-5,5000,0);
            //wallAlign(0.4, 35, 1);
            //encoderMecanumDrive(0.5, 33, 33, 5000, 0);
        }
        else if(vuMark == RelicRecoveryVuMark.RIGHT){
            encoderMecanumDrive(0.3,3,3,5000,0);
            //wallAlign(0.4, 50, 1);
            //encoderMecanumDrive(0.5, 48, 48, 5000, 0);

        }
        //if we didn't detect the image, automatically put the glyph in the center

        gyroTurnREV(0.5,-45);

        sleep(750);

        moveDownGlyph(1.05);
        sleep(100);
        /*encoderMecanumDrive(0.3, 5, 5, 1000, 0);
        sleep(250);*/
        openGlyphManipulator();
        sleep(250);

        encoderMecanumDrive(0.3,20,20,1000,0);
        sleep(250);
        encoderMecanumDrive(0.3,10,-10,1000,0);
        sleep(500);
        encoderMecanumDrive(0.3, -10, -10, 1000, 0);

        if(vuMark == RelicRecoveryVuMark.LEFT){
            encoderMecanumDrive(0.3, 10, 10, 1000, 1);
            encoderMecanumDrive(0.3,2,2,1000,0);
        }
        /*sleep(100);
        gyroTurnREV(0.3, 179);
        sleep(100);*/
    }


}
