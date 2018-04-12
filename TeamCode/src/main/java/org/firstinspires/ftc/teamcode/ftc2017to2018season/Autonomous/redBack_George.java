/*============================================================================================================================================
                                                            EDIT HISTORY



when                                      who                       Purpose/Change
-----------------------------------------------------------------------------------------------------------------------------------------------
4/6/18                                    Steven                  By looking at test programs, fix CV to recognize jewel order. OpenCVInit has to be put before waitForStart and after initiate
=============================================================================================================================================*/
package org.firstinspires.ftc.teamcode.ftc2017to2018season.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

//10-28-17
@Autonomous(name="Red Back George Worlds")
//3/2/18 edit by Steven Chen: getting rid of unnecessary turns (this is new version of the regional code)
public class redBack_George extends Autonomous_General_George_ {

    public double rsBuffer = 20.00;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        initiate(false);
        //intiate hardware

        openCVInit();
        //initiate dogeCV
        sleep(500);

        //enable vuforia and load its sdk and camera
        vuforiaInit(false, true);
        //tell driver that it loaded
        telemetry.addData("", "Vuforia Initiated");
        telemetry.update();

        //tell robot that robot is ready to go
        telemetry.addData("The robot is loaded", "You are ready to go! :D");
        telemetry.update();
        //wait for driver to click play
        waitForStart();
        opModeStart = System.currentTimeMillis();
        jewelServoRotate.setPosition(0.74);
        sleep(100);
        //turn on the phone flashlight
        toggleLight(true);

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
            case RED_BLUE:
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

            case BLUE_RED:
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
        encoderMecanumDrive(0.5, -25, -25, 5000, 0);
        sleep(100);


        gyroTurnREV(0.4, 0,0.5);


        if (vuMark == RelicRecoveryVuMark.RIGHT) {
            //if the right image was read we move back 4.25 cm
            encoderMecanumDrive(0.4, -20, -20, 5000, 0);
        } else if (vuMark == RelicRecoveryVuMark.CENTER || vuMark == RelicRecoveryVuMark.UNKNOWN) {
            //if the center or unkown image was read we move forward 4 cm
            encoderMecanumDrive(0.4, -12, -12, 5000, 0);

        } else if (vuMark == RelicRecoveryVuMark.LEFT) {
            //if the left image was read we move back 9.25 cm
            encoderMecanumDrive(0.4, -15, -15, 5000, 0);
        }


        sleep(75);

        if (vuMark == RelicRecoveryVuMark.RIGHT) {
            //if the image was right we turn to 60º counterclockwise from origin angle to push block in angled
            gyroTurnREV(0.5, 70,2.5);

        } else {
            //if the image was right we turn to 102º counterclockwise from origin angle to push block in angled
            gyroTurnREV(0.5, 117,1.5);
        }


        sleep(75);

        encoderMecanumDrive(0.3, 8, 8, 1000, 0);
        sleep(75);

        moveDownGlyph(1.05);
        glyphOuttakeRolly(0.5);

        if (vuMark == RelicRecoveryVuMark.RIGHT) {
            //to push the block in more, we turn left while pushing in forward
            encoderMecanumDrive(0.3, 10, -10, 1000, 0);

        } else {
            //to push the block in more, we turn right while pushing in forward
            encoderMecanumDrive(0.3, -10, 10, 1000, 0);
        }

        sleep(75);
        //we back up 10 cm to park

        //code to get second glyph
        encoderMecanumDrive(0.65, -10, -10, 1000, 0);
        sleep(75);
        if(System.currentTimeMillis() - opModeStart > 5000) {
            gyroTurnREV(0.6, -100, 2);//this will cause it to face the pile of glyphs at a 90 degree angle
            moreGlyphsRed(vuMark);
        }

        if (!opModeIsActive()) {
            jewelDetector.disable();
        }


    }

}
