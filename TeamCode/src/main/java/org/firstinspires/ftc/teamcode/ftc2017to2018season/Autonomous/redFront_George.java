/*============================================================================================================================================
                                                            EDIT HISTORY



when                                      who                       Purpose/Change
-----------------------------------------------------------------------------------------------------------------------------------------------
4/6/18                                    Steven                  By looking at test programs, fix CV to recognize jewel order. OpenCVInit has to be put before waitForStart and after initiate
=============================================================================================================================================*/
package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.teamcode.ftc2017to2018season.Constants.Constants_for_blueBack_George;

//10-28-17
@Autonomous(name = "Red Front George Worlds")
//@Disabled
public class redFront_George extends Autonomous_General_George_ {
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
            case RED_BLUE:
                jewelServo.setPosition(0.2);
                sleep(500);
                //move the jewel manipulator to the right to knock off the ball
                jewelServoRotate.setPosition(1);
                sleep(150);
                jewelServoRotate.setPosition(0.79);
                jewelServo.setPosition(0.8);
                sleep(200);
                //move the jewel manipulator to the original position
                //      sleep(500);
                break;

            case BLUE_RED:
                jewelServo.setPosition(0.2);
                sleep(500);
                //move the jewel manipulator to the left to knock off the ball
                telemetry.addLine("Jewels Seen Red Blue");
                telemetry.update();

                jewelServoRotate.setPosition(0.5);
                sleep(150);
                jewelServoRotate.setPosition(0.79);
                jewelServo.setPosition(0.8);
                sleep(200);
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
        sleep(75);
        toggleLight(false);

        //closeGlyphManipulator();
        sleep(75);
        encoderMecanumDrive(0.5,-37,-37,5000,0);
        sleep(75);
        gyroTurnREV(0.6,0,0.4);
        sleep(75);
        //wallAlign(0.3,28, 1);//since the columns of the cryptobox are protruding,
                                                    // the range sensor is actually using the distance from the protruding columns
                                                    //the last value is 0 for the blue auto and 1 for the red auto
        wallAlign(0.5, 28, 1);
        sleep(75);
        gyroTurnREV(0.5, -90,1.5);

        if (vuMark == RelicRecoveryVuMark.RIGHT){//should be 20 cm away from wall for left
            //goes to given distance away from the wall
            //wallAlign(0.3, 35, 1);
            encoderMecanumDrive(0.3, -15, -15, 5000, 0);
        }
        else if (vuMark == RelicRecoveryVuMark.CENTER || vuMark == RelicRecoveryVuMark.UNKNOWN){
            encoderMecanumDrive(0.3,-3,-3,5000,0);
            //wallAlign(0.4, 35, 1);
            //encoderMecanumDrive(0.5, 33, 33, 5000, 0);
        }
        else if(vuMark == RelicRecoveryVuMark.LEFT){
            encoderMecanumDrive(0.3,6,6,5000,0);
            //wallAlign(0.4, 50, 1);
            //encoderMecanumDrive(0.5, 48, 48, 5000, 0);

        }

        gyroTurnREV(0.5, -135,1.2);//turn 135 degrees to the right of origin (actually turning left to reach it, be 32 cm away from wall

        sleep(200);

        encoderMecanumDrive(0.6, 19, 19, 1000, 0);
        sleep(75);

        moveDownGlyph(1.35);
        intakeLeft.setPower(-1);
        intakeRight.setPower(1);
        sleep(500);
        intakeLeft.setPower(0);
        intakeRight.setPower(0);
        sleep(100);
        encoderMecanumDrive(0.6, -8, 8, 1000, 0);

        encoderMecanumDrive(0.6, -8, -8, 1000, 0);
        sleep(100);



        if(System.currentTimeMillis() - opModeStart > 5000) {
            //two block auto from here:
            gyroTurnREV(0.5, -90, 1);
            sleep(250);

            if (vuMark == RelicRecoveryVuMark.RIGHT) {
                encoderMecanumDrive(0.3, 15, 15, 1000, 0);
            }

            encoderMecanumDrive(0.7, (45 - wallAlignBack.getDistance(DistanceUnit.INCH)), (45 - wallAlignBack.getDistance(DistanceUnit.INCH)), 1000, 0);
            sleep(75);
            gyroTurnREV(0.6, -20, 1.4);

        /*if(vuMark == RelicRecoveryVuMark.LEFT){
            gyroTurnREV(0.6, -18, 1.4);
        }*/

            sleep(75);
            middleGlyphManipulator();
            sleep(75);
            intakeLeft.setPower(1);
            intakeRight.setPower(-1);
            encoderMecanumDrive(0.7, 23, 23, 1000, 0);
            sleep(150);
            encoderMecanumDrive(0.4, 15, 15, 1000, 0);
            sleep(75);
            closeGlyphManipulator();
            intakeLeft.setPower(0);
            intakeRight.setPower(0);
            sleep(75);
            moveUpGlyph(3.2);
            sleep(75);
            encoderMecanumDrive(0.7, -40, -40, 1000, 0);
            sleep(75);
            gyroTurnREV(0.5, 170, 2.15);
            sleep(75);
            encoderMecanumDrive(0.7, 21, 21, 1000, 0);
            sleep(75);
            glyphOuttakeRolly(0.5);
            sleep(75);
            encoderMecanumDrive(0.7, -5, -5, 1000, 0);
        }
    }


}
