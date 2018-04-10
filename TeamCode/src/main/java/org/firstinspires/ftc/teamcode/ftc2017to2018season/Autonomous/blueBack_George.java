/*============================================================================================================================================
                                                            EDIT HISTORY



when                                      who                       Purpose/Change
-----------------------------------------------------------------------------------------------------------------------------------------------
3/28/18                                   Rohan                   On line 65 I changed the function value of centimeters for the moveDownGlyph() functions as it was moving down to far causing the program to crash.
3/28/18                                   Rohan                   Added new object called jewelDectector1 which calls on DogeCV_JewelDetector. Added on line 88 jewelDectector1.init(); which initializes OpenCV. The other lines of initiation are deleted.  Added a sleep afterward as to give time for the initiation.
3/28/18                                   Rohan                   Deleted the call on init in line 88 as it caused numerous other problems in the program. Went back to old activations methods.
3/28/18                                   Rohan                   Added a getCurrentOrder function
3/31/18                                   Rohan                   Replaced numerical values with an object reference to continue with having constants in one place.
3/31/18                                   Rohan                   Instead of having all the initiation lines for DogeCV in the program I created a function in Autonomous_General_George_ that performs the same function.
4/1/18                                    Rohan                   Moved the lift down farther because after vuforia reads image the lift didn't move far enough down.
4/1/18                                    Rohan                   Made it so that the jewel servo moves down before the jewel arm moves to knock of the jewel.
4/1/18                                    Rohan                   Removed a commented section of code that was obsolete due to the new old documented programs.
4/1/18                                    Rohan                   Changed values in the program when going back to get more blocks. Moves the robot farther into the pile.
4/1/18                                    Rohan                   The value to go down after Vuforia was to high so the robot got stuck in the loop.
4/6/18                                    Steven                  By looking at test programs, fix CV to recognize jewel order. OpenCVInit has to be put before waitForStart and after initiate
=============================================================================================================================================*/
package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.ftc2017to2018season.Constants.Constants_for_blueBack_George;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;


//10-28-17
@Autonomous(name = "Blue Back George Worlds")
//@Disabled
//3/2/18 edit by Steven Chen: trying to see if going a slower speed off the platform makes it go more straight
//3/25/18 edit by Steven Chen: adding second block autonomous
public class blueBack_George extends Autonomous_General_George_ {
    Constants_for_blueBack_George constants = new Constants_for_blueBack_George();

    public double rsBuffer = constants.rsBuffer;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {


        initiate(false);
        //intiate hardware

        openCVInit();
        //initiate dogeCV

        sleep(constants.initTimeMill);
        vuforiaInit(constants.vuforiaInitCameraView, constants.vuforiaInitRearCamera);// in order to get both openCVInit and vuforiaInit working, I initialized openCVInit first before waitForStart,
                                                                                        // did the jewel manipulator stuff stuff first, disabled jewel detector, then did vuforia stuff last
                                                                                        //need to do more testing to see exactly why CV stuff stops working when Vuforia stuff is done first
        //intiates the vuforia sdk and camera
        telemetry.addData("", "Vuforia Initiated");
        telemetry.update();
        //tell driver that vuforia is ready
        telemetry.addData("", "GOOD TO GO! :)");
        telemetry.update();
        //tell driver that we are good to go

        waitForStart();

        opModeStart = System.currentTimeMillis();
//reseting gyro sensor



        jewelServoRotate.setPosition(constants.jewelServoRotateInitValue);
        //sleep(100);
        toggleLight(constants.toogleLightVuforiaRead);
        //light.setPower(0.5);

        moveUpGlyph(0.7);//change distances once we lower the stress of the glyph manipulator
        sleep(250);
        middleGlyphManipulator();
        sleep(250);
        moveDownGlyph(1.0);
        sleep(250);
        closeGlyphManipulator();
        sleep(250);
        moveUpGlyph(1.50);
        sleep(250);

        switch (jewelDetector.getCurrentOrder()) {
            case BLUE_RED:
                jewelServo.setPosition(0.2);
                sleep(750);
                //move the jewel manipulator to the right to knock off the ball
                jewelServoRotate.setPosition(1);
                sleep(300);
                jewelServoRotate.setPosition(0.79);
                jewelServo.setPosition(0.8);
                sleep(750);
                //move the jewel manipulator to the original position
                //      sleep(500);
                break;

            case RED_BLUE:
                jewelServo.setPosition(0.2);
                sleep(750);
                //move the jewel manipulator to the left to knock off the ball
                telemetry.addLine("Jewels Seen Red Blue");
                telemetry.update();

                jewelServoRotate.setPosition(0.5);
                sleep(300);
                jewelServoRotate.setPosition(0.79);
                jewelServo.setPosition(0.8);
                sleep(750);
                //move it back to the original posititon
                break;
            case UNKNOWN:
                telemetry.addData("Balls not seen", "Solution TBD   :/");
                telemetry.update();
                /*jewelServo.setPosition(0.2);
                sleep(750);
                readColorRev();
                KnockjewelSensor(ballColor, "blue");
                sleep(100);*/
                break;
        }
        //Used to make sure the jewels are recognized
        telemetry.addData("Jewel order is ", jewelDetector.getCurrentOrder());
        telemetry.update();
        jewelServo.setPosition(1);
        jewelDetector.disable();

        startTracking();
        telemetry.addData("", "READY TO TRACK");
        telemetry.update();

        double begintime = runtime.seconds();
        while (!vuMarkFound() && runtime.seconds() - begintime <= waitTime) {


        }
        relicTrackables.deactivate();
        //toggleLight(true);

        telemetry.addData("Vumark", vuMark);
        telemetry.update();
        sleep(200);
        toggleLight(false);


        sleep(500);
        encoderMecanumDrive(0.5, 25, 25, 5000, 0);
        sleep(100);
        //Rohan: Is this necessary
        gyroTurnREV(0.4, 0, 0.5);
        sleep(100);


        if (vuMark == RelicRecoveryVuMark.LEFT) {
            encoderMecanumDrive(0.4, 18.25, 18.25, 5000, 0);
        } else if (vuMark == RelicRecoveryVuMark.CENTER || vuMark == RelicRecoveryVuMark.UNKNOWN) {
            encoderMecanumDrive(0.4, 4, 4, 5000, 0);

        } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
            encoderMecanumDrive(0.4, 15, 15, 5500, 0);
        }


        sleep(75);

        if (vuMark == RelicRecoveryVuMark.LEFT) {
            gyroTurnREV(0.5, 112,2.5);//turn 45 degrees to the right of origin (actually turning left to reach it, be 32 cm away from wall

        } else {
            gyroTurnREV(0.5, 60,1.5);//turn 45 degrees to the right of origin (actually turning left to reach it, be 32 cm away from wall
        }


        sleep(75);

        encoderMecanumDrive(0.3, 8, 8, 1000, 0);
        sleep(75);

        moveDownGlyph(1.05);
        glyphOuttakeRolly(0.5);

        if (vuMark == RelicRecoveryVuMark.LEFT) {
            encoderMecanumDrive(0.3, -10, 10, 1000, 0);

        } else {
            encoderMecanumDrive(0.3, 10, -10, 1000, 0);
        }

        sleep(75);
        encoderMecanumDrive(0.65,-10,-10,1000,0);
        sleep(75);
        gyroTurnREV(0.6,-85,2);
        moreGlyphsBlue(vuMark);


        if (!opModeIsActive()) {
            jewelDetector.disable();
        }


    }
}
