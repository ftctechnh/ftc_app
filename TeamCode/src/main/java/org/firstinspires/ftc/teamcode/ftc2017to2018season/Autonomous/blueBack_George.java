/*============================================================================================================================================
                                                            EDIT HISTORY



when                                      who                       Purpose/Change
-----------------------------------------------------------------------------------------------------------------------------------------------
3/28/18                                   Rohan                   On line 65 I changed the function value of centimeters for the moveDownGlyph() functions as it was moving down to far causing the program to crash.
3/28/18                                   Rohan                   Added new object called jewelDectector1 which calls on DogeCV_JewelDetector. Added on line 88 jewelDectector1.init(); which initializes OpenCV. The other lines of initiation are deleted.  Added a sleep afterward as to give time for the initiation.
3/28/18                                   Rohan                   Deleted the call on init in line 88 as it caused numerous other problems in the program. Went back to old activations methods.
3/28/18                                   Rohan                   Added a getCurrentOrder function

=============================================================================================================================================*/
package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.detectors.JewelDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Testing_and_Calibrations.Computer_Vision.DogeCVJewelDetector;


//10-28-17
@Autonomous(name = "Blue Back George Worlds")
//@Disabled
//3/2/18 edit by Steven Chen: trying to see if going a slower speed off the platform makes it go more straight
//3/25/18 edit by Steven Chen: adding second block autonomous
public class blueBack_George extends Autonomous_General_George_ {

    public double rsBuffer = 20.00;
    private ElapsedTime runtime = new ElapsedTime();
    DogeCVJewelDetector jewelDetector = new DogeCVJewelDetector();
    JewelDetector jewelDetector1 = new JewelDetector();

    @Override
    public void runOpMode() {


        vuforiaInit(true, true);
        //intiates the vuforia sdk and camera
        telemetry.addData("","Vuforia Initiated");
        telemetry.update();
        //tell driver that vuforia is ready
        initiate(false);
        //intiate hardware
        sleep(500);
        telemetry.addData("","GOOD TO GO! :)");
        telemetry.update();
        //tell driver that we are good to go

        waitForStart();
//reseting gyro sensor

        jewelServoRotate.setPosition(0.74);
        //sleep(100);
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
        //sleep(250);

        moveUpGlyph(0.7);//change distances once we lower the stress of the glyph manipulator
        sleep(250);
        middleGlyphManipulator();
        sleep(250);
        //3/28/18 Changed to allow the program to crash as the manipulator can't move far down mechanically. Previous value was 1.45.
        moveDownGlyph(0.5);
        sleep(250);
        closeGlyphManipulator();
        sleep(250);
        moveUpGlyph(1.45);
        sleep(250);

        jewelServo.setPosition(0.2);
        telemetry.addData("jewelServo Position", jewelServo.getPosition());
        telemetry.update();
        //sleep(100);
        readColorRev();
        sleep(100);
        //light.setPower(0);
        telemetry.addData("right jewel color", ballColor);
        telemetry.update();
        //returnImage();


        relicTrackables.deactivate();
        jewelDetector1.init(hardwareMap.appContext, CameraViewDisplay.getInstance());

        //Jewel Detector Settings
        jewelDetector1.areaWeight = 0.02;
        jewelDetector1.detectionMode = JewelDetector.JewelDetectionMode.MAX_AREA; // PERFECT_AREA
        //jewelDetector.perfectArea = 6500; <- Needed for PERFECT_AREA
        jewelDetector1.debugContours = true;
        jewelDetector1.maxDiffrence = 15;
        jewelDetector1.ratioWeight = 15;
        jewelDetector1.minArea = 700;

        jewelDetector1.enable();

        jewelDetector1.getCurrentOrder();
       sleep(500);
        switch (jewelDetector1.getCurrentOrder()){
            case BLUE_RED:
                //move the jewel manipulator to the left to knock off the ball
                jewelServoRotate.setPosition(1);
                telemetry.addLine("Jewels Seen Blue Red");
                telemetry.update();
                sleep(300);
                jewelServo.setPosition(0.8);
                sleep(750);
                //move the jewel manipulator to the original position
                jewelServoRotate.setPosition(0.79);
                sleep(1000);
                break;

            case RED_BLUE:
                //move the jewel manipulator to the right to knock off the ball
               telemetry.addLine("Jewels Seen Red Blue");
                telemetry.update();

                jewelServoRotate.setPosition(0.5);
                sleep(300);
                jewelServo.setPosition(0.8);
                sleep(750);
                //move it back to the original posititon
                jewelServoRotate.setPosition(0.79);
                //Add code to swing the jwele arm
                break;
            case UNKNOWN:
//                telemetry.addData("Balls not seen", "Solution TBD   :/");
//                telemetry.update();
                readColorRev();
                KnockjewelSensor(ballColor);
                sleep(1000);
                break;
        }
        //Used to make sure the jewels are recognized
        telemetry.addData("Jewel order is ", jewelDetector1.getCurrentOrder());
        telemetry.update();
        jewelServo.setPosition(1);
/*
        if(ballColor.equals("blue")){
            //move the jewel manipulator to the right to knock off the ball
            jewelServoRotate.setPosition(0.5);
            sleep(300);
            jewelServo.setPosition(0.8);
            sleep(750);
            //move it back to the original posititon
            jewelServoRotate.setPosition(0.79);
            //Add code to swing the jwele arm

        }
        else if(ballColor.equals("red")){
            //move the jewel manipulator to the left to knock off the ball
            jewelServoRotate.setPosition(1);
            sleep(300);
            jewelServo.setPosition(0.8);
            sleep(750);
            //move the jewel manipulator to the original position
            jewelServoRotate.setPosition(0.79);
            sleep(500);
        }
        else if (ballColor.equals("blank")){
            jewelServo.setPosition(1);
            sleep(300);
            jewelServo.setPosition(0.2);
            sleep(750);
            readColorRev();
            sleep(500);
            if(ballColor.equals("blue")){
                //move the jewel manipulator to the right to knock off the ball
                jewelServoRotate.setPosition(0.5);
                sleep(300);
                jewelServo.setPosition(0.8);
                sleep(750);
                //move it back to the original posititon
                jewelServoRotate.setPosition(0.79);
                //Add code to swing the jwele arm
            }
            else if(ballColor.equals("red")) {
                //move the jewel manipulator to the left to knock off the ball
                jewelServoRotate.setPosition(1);
                sleep(300);
                jewelServo.setPosition(0.8);
                sleep(750);
                //move the jewel manipulator to the original position
                jewelServoRotate.setPosition(0.79);
                sleep(500);
            }
        }
        jewelServo.setPosition(1);
        */
        sleep(500);
        encoderMecanumDrive(0.4,50,50,5000,0);
        sleep(100);
        gyroTurnREV(0.4,0);
        sleep(100);




        if (vuMark == RelicRecoveryVuMark.LEFT){
            encoderMecanumDrive(0.4,4.25,4.25,5000,0);
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER || vuMark == RelicRecoveryVuMark.UNKNOWN){
            encoderMecanumDrive(0.4,-4,-4,5000,0);

        }
        else if (vuMark == RelicRecoveryVuMark.RIGHT){
            encoderMecanumDrive(0.4,9.25,9.25,5000,0);
        }


        sleep(100);

        if (vuMark == RelicRecoveryVuMark.LEFT){
            gyroTurnREV(0.5, 112);//turn 45 degrees to the right of origin (actually turning left to reach it, be 32 cm away from wall

        }
        else {
            gyroTurnREV(0.5, 60);//turn 45 degrees to the right of origin (actually turning left to reach it, be 32 cm away from wall
        }


        sleep(200);

        moveDownGlyph(1.05);
        sleep(100);
        /*encoderMecanumDrive(0.3, 5, 5, 1000, 0);
        sleep(250);*/
        openGlyphManipulator();
        sleep(250);

        encoderMecanumDrive(0.3,16,16,1000,0);
        sleep(250);

        if (vuMark == RelicRecoveryVuMark.LEFT){
            encoderMecanumDrive(0.3,-10,10,1000,0);

        }
        else {
            encoderMecanumDrive(0.3,10,-10,1000,0);
        }

        //code to get second glyph
        encoderMecanumDrive(0.3, -20, -20, 1000, 0);
        gyroTurnREV(0.5,-90);//this will cause it to face the pile of glyphs at a 90 degree angle
        encoderMecanumDrive(0.6,20,20,5000,0);
        closeGlyphManipulator();
        glyphIntakeRolly(1);
        moveUpGlyph(3.4);
        gyroTurnREV(0.5,90);
        //moveDownGlyph(2);
        encoderMecanumDrive(0.5,35,35,5000,0);
        glyphOuttakeRolly(1);
        openGlyphManipulator();
        encoderMecanumDrive(0.3,-10,-10,5000,0);

    }


}
