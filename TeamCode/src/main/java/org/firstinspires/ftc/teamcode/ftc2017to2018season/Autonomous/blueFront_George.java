/*============================================================================================================================================
                                                            EDIT HISTORY



when                                      who                       Purpose/Change
-----------------------------------------------------------------------------------------------------------------------------------------------

=============================================================================================================================================*/
package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Constants.Constants_for_blueBack_George;


//10-28-17
@Autonomous(name = "Blue Front George Worlds")
//@Disabled
public class blueFront_George extends Autonomous_General_George_ {
    Constants_for_blueBack_George constants = new Constants_for_blueBack_George();

    /*-------------------------------------------------------
    Aditya (3/31/18) - Fix basic errors,like the jewel not moved down and the moving glyph manipulator
    Steven (4/6/18) - By looking at test programs, fix CV to recognize jewel order. OpenCVInit has to be put before waitForStart and after initiate
    -------------------------------------------------------*/
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

        jewelServo.setPosition(0.6);

        moveUpGlyph(0.7);//change distances once we lower the stress of the glyph manipulator
        sleep(150);
        middleGlyphManipulator();
        sleep(150);
        moveDownGlyph(1.25);
        sleep(150);
        closeGlyphManipulator();
        sleep(150);
        moveUpGlyph(1.50);
        sleep(250);

        switch (jewelDetector.getCurrentOrder()) {
            case BLUE_RED:
                jewelServo.setPosition(0.2);
                sleep(1400);
                //move the jewel manipulator to the right to knock off the ball
                jewelServoRotate.setPosition(1);
                sleep(250);
                jewelServoRotate.setPosition(0.79);
                jewelServo.setPosition(0.8);
                sleep(400);
                //move the jewel manipulator to the original position
                //      sleep(500);
                break;

            case RED_BLUE:
                jewelServo.setPosition(0.2);
                sleep(1400);
                //move the jewel manipulator to the left to knock off the ball
                telemetry.addLine("Jewels Seen Red Blue");
                telemetry.update();

                jewelServoRotate.setPosition(0.5);
                sleep(250);
                jewelServoRotate.setPosition(0.79);
                jewelServo.setPosition(0.8);
                sleep(400);
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
        jewelServo.setPosition(0.8);
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
        sleep(75);
        toggleLight(false);

        encoderMecanumDrive(0.5,32,32,5000,0);
        sleep(75);
        gyroTurnREV(0.5,0,0.4);
        sleep(75);
        wallAlign(0.5,28, 0);//since the columns of the cryptobox are protruding,
                                                    // the range sensor is actually using the distance from the protruding columns
                                                    //the last value is 0 for the blue auto and 1 for the red auto
        sleep(100);
        gyroTurnREV(0.55, -90,1.5);
        sleep(75);



        if (vuMark == RelicRecoveryVuMark.LEFT){//should be 20 cm away from wall for left
            //goes to given distance away from the wall
            //wallAlign(0.3, 35, 1);
            encoderMecanumDrive(0.4, -11, -11, 5000, 0);
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER || vuMark == RelicRecoveryVuMark.UNKNOWN){
            encoderMecanumDrive(0.4,-1.4,-1.4,5000,0);
            //wallAlign(0.4, 35, 1);
            //encoderMecanumDrive(0.5, 33, 33, 5000, 0);
        }
        else if(vuMark == RelicRecoveryVuMark.RIGHT){
            encoderMecanumDrive(0.4,6.5,6.5,5000,0);
            //wallAlign(0.4, 50, 1);
            //encoderMecanumDrive(0.5, 48, 48, 5000, 0);
        }
        //if we didn't detect the image, automatically put the glyph in the center

        gyroTurnREV(0.5,-45,2.0);

        sleep(75);

        encoderMecanumDrive(0.3, 16, 16, 1000, 0);
        sleep(75);

        moveDownGlyph(1.35);
        intakeLeft.setPower(-1);
        intakeRight.setPower(1);
        sleep(500);
        intakeLeft.setPower(0);
        intakeRight.setPower(0);
        sleep(100);
        encoderMecanumDrive(0.3, 7, -7, 1000, 0);

        encoderMecanumDrive(0.6, -8, -8, 1000, 0);

        //after this is work on a second glyph auto!
        if(System.currentTimeMillis() - opModeStart > 5000) {
            gyroTurnREV(0.5, -90, 1);
            sleep(250);

            if (vuMark == RelicRecoveryVuMark.LEFT) {
                encoderMecanumDrive(0.4, 15, 15, 1000, 0);//this is because if the range sensor gets too close to the clear wall, it might not detect the wall, so move forward a little bit
            }

            encoderMecanumDrive(0.7, (41 - wallAlignBack.getDistance(DistanceUnit.INCH)), (41 - wallAlignBack.getDistance(DistanceUnit.INCH)), 1000, 0);
            sleep(75);
            gyroTurnREV(0.5, -151, 1.4);
            sleep(75);
            middleGlyphManipulator();
            sleep(75);
            intakeLeft.setPower(1);
            intakeRight.setPower(-1);
            encoderMecanumDrive(0.8, 40, 40, 1000, 0);
            sleep(150);
            encoderMecanumDrive(0.4, 23, 23, 1000, 0);
            sleep(75);
            closeGlyphManipulator();
            intakeLeft.setPower(0);
            intakeRight.setPower(0);
            sleep(75);
            moveUpGlyph(2.5);
            sleep(75);
            encoderMecanumDrive(0.8, -60, -60, 1000, 0);
            sleep(75);
            gyroTurnREV(0.55, 43, 2.3);
            sleep(75);
            encoderMecanumDrive(0.7, 14, 14, 1000, 0);
            sleep(75);
            glyphOuttakeRolly(0.5);
            sleep(75);
            encoderMecanumDrive(0.65, -5, -5, 1000, 0);

        }

        if (!opModeIsActive()) {
            jewelDetector.disable();
        }
    }


}
