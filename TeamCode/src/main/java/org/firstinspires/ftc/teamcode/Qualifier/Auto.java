package org.firstinspires.ftc.teamcode.Qualifier;

/**
 *
 */


import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


@Autonomous(name = "TheAuto", group = "8045")  // @Autonomous(...) is the other common choice
//@Disabled
public class Auto extends LinearOpMode {

    RobotRR gromit;

    private ElapsedTime runtime = new ElapsedTime();
    private MenuFileHandler menuFile;
    public static final String TAG = "Vuforia VuMark Sample";
    public boolean redjewelisright;

    OpenGLMatrix lastLocation = null;

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    VuforiaLocalizer vuforia;

    @Override
    public void runOpMode() {
        //Init Hardware map
        gromit = new RobotRR();
        gromit.init(hardwareMap);
        menuFile = new MenuFileHandler(telemetry, gamepad1);

                // get a reference to the RelativeLayout so we can change the background  for Edit mode
                // color of the Robot Controller app to match the hue detected by the RGB sensor.
                int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
                final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);


        /**********************************************************************************************\
         |--------------------------------- Vuforia Setup ----------------------------------------------|
         \**********************************************************************************************/

        /*
         * To start up Vuforia, tell it the view that we wish to use for camera monitor (on the RC phone);
         * If no camera monitor is desired, use the parameterless constructor instead (commented out below).
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // OR...  Do Not Activate the Camera Monitor View, to save power
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();


        parameters.vuforiaLicenseKey = "AWfr4/T/////AAAAGRMg80Ehu059mDMJI2h/y+4aBmz86AidOcs89UScq+n+QQyGFT4cZP+rzg1M9B/CW5bgDoVf16x6x3WlD5wYKZddt0UWQS65VIFPjZlM9ADBWvWJss9L1dj4X2LZydWltdeaBhkXTXFnKBkKLDcdTyC2ozJlcAUP0VnLMeI1n+f5jGx25+NdFTs0GPJYVrPQRjODb6hYdoHsffiOCsOKgDnzFsalKuff1u4Z8oihSY9pvv3me2gJjzrQKqp2gCRIZAXDdYzln28Z/8vNSU+aXr6eoRrNXPpYdAwyYI+fX2V9H04806eSUKsNYcPBSbVlhe2KoUsSD7qbOsBMagcEIdMZxo010kVCHHhnhV3IFIs8";

        /*
         * We also indicate which camera on the RC that we wish to use.
         * Here we chose the back (HiRes) camera (for greater range), but
         * for a competition robot, the front camera might be more convenient.
         */
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        /**
         * Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
         * in this data set: all three of the VuMarks in the game were created from this one template,
         * but differ in their instance id information.
         * @see VuMarkInstanceId
         */
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary



        /**********************************************************************************************\
         |--------------------------------- Pre Init Loop ----------------------------------------------|
         \**********************************************************************************************/
        telemetry.addLine("Back Button reverts to code values");
        telemetry.update();
        //sleep(1000);
        menuFile.initArrays();
        menuFile.initializeNTransferValues( true );       // initialize variables & values

        // update them from the file if the back button is not pressed

        if (!gamepad1.back) {
            telemetry.addLine("Reading Data from File");
            telemetry.update();
            menuFile.readDataFromTxtFile(hardwareMap.appContext);
            telemetry.addLine("Transferring Data to Variables");
            telemetry.update();
            menuFile.initializeNTransferValues(false);            // transfer the array to variables with useable names
            //sleep(1000);
        } else {
            telemetry.addLine("Reverting to Initial Values");
            telemetry.update();
            //sleep(1000);
        }

        //sleep(3000);
        //menuFile.editParameters();                            // edit parameters
        //menuFile.writeDataToTxtFile(hardwareMap.appContext);  // write the current parameters to the file for next time


        /**********************************************************************************************\
         |--------------------------------------Init Loop-----------------------------------------------|
         \**********************************************************************************************/
        //Activate Vuforia
        relicTrackables.activate();

        //Run using encoders
        gromit.driveTrain.runUsingEncoders();

        // Actual Init loop
        while (!opModeIsActive()) {
            telemetry.addData("IMU", "Heading: %4.2f ", gromit.driveTrain.getheading());
            telemetry.addLine("************ READY TO RUN *************");
            telemetry.addLine("Press START button to enter EDIT mode");
            telemetry.addLine(" ");

            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.BLACK);
                }
            });

            //
            //
            if (gamepad1.start) {             // edit parameters  & write the new file
                // change the background color to match the color detected by the RGB sensor.
                // pass a reference to the hue, saturation, and value array as an argument
                // to the HSVToColor method.
                relativeLayout.post(new Runnable() {
                    public void run() {
                        relativeLayout.setBackgroundColor(Color.YELLOW);
                    }
                });


                menuFile.editParameters();
                menuFile.writeDataToTxtFile(hardwareMap.appContext);  // write the current parameters to the file for next time

            }

            for (int i = 0; i < menuFile.menulabel.length; i++) {
                     if (menuFile.menuvaluetoken[i][1] != "" ) {                           // menu items that need tokens should be less than 5
                //           if (menuFile.menuupperlimit[i] < 5) {                           // menu items that need tokens should be less than 5
                         telemetry.addLine().addData(menuFile.menulabel[i], menuFile.menuvalue[i] + "  " + menuFile.menuvaluetoken[i][menuFile.menuvalue[i]]);
                     } else {
                         telemetry.addData(menuFile.menulabel[i], menuFile.menuvalue[i]);
                     }

            }
            telemetry.update();
            idle();
        }

        /**********************************************************************************************\
         |----------------------------------- Run the Autonomous --------------------------------------|
         \*********************************************************************************************/


        waitForStart();
        telemetry.clearAll();
        //Search for Vumark
        /**
         * See if any of the instances of {@link relicTemplate} are currently visible.
         * {@link RelicRecoveryVuMark} is an enum which can have the following values:
         * UNKNOWN, LEFT, CENTER, and RIGHT. When a VuMark is visible, something other than
         * UNKNOWN will be returned by {@link RelicRecoveryVuMark#from(VuforiaTrackable)}.
         */
        gromit.jewelArm.jewelflickerCenter();
        sleep(2000);
        gromit.jewelArm.jewelflickerForward();
        sleep(2000);


        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
//        while ( RelicRecoveryVuMark.from(relicTemplate) == RelicRecoveryVuMark.UNKNOWN && opModeIsActive()) {//Add timeout
//            // vuMark = RelicRecoveryVuMark.from(relicTemplate);
//            telemetry.addData("VuMark", "%s not visible", vuMark);
//            telemetry.update();
//        }
        relicTrackables.deactivate();
        //VUMARK FOUND OR TIMEOUT
        //Grip Block
            gromit.glyphTrain.glyphclamp("close");
        //Raise Block
        // do the jewel should be a method in the jewelArm
        gromit.jewelArm.jewelArmDown();
            while(opModeIsActive() && gromit.jewelArm.jewelArmServo.getPosition() < 0.5) { idle();}
        gromit.jewelArm.solveJewelPuzzle(menuFile.teamIsRed);
        gromit.jewelArm.jewelArmUp();
        //        //Put Jewel Arm down
//        gromit.jewelArm.jewelArmDown();
//        sleep(750);
//        // pretend we're red for now...
//        // determine if Red to the left, the sensor reads in the left direction.
//        if (gromit.jewelArm.sensorColor.red() < gromit.jewelArm.sensorColor.blue()) {
//            redjewelisright = true;
//        }  else {
//            redjewelisright = false;
//        }
//
//        if (redjewelisright) {               //Left Jewel is Blue, right is red
//            //Backward
//            gromit.driveTrain.mecanumDrive(0.2,-2,0,0);
//        }
//        else {                               //Left Jewel is RED, right is BLUE                                                                          //Left Jewel is Red
//            //Forward
//            gromit.driveTrain.mecanumDrive(0.2,2,0,0);
//        }
//
//        gromit.jewelArm.jewelArmUp();
//        //gromit.driveTrain.resetencoders();

        //clamp glyph
        //lift glyph

        if (menuFile.startPositionIsFront  && !menuFile.teamIsRed){/** Front Blue  */
            gromit.driveTrain.mecanumDrive(0.25, menuFile.FrontBlueDistance1, menuFile.FrontBlueHeading1,0);
            gromit.driveTrain.mecanumTurn(0.25,menuFile.FrontBlueTurn1);
            gromit.driveTrain.mecanumDrive(0.25, menuFile.FrontBlueDistance2, menuFile.FrontBlueHeading2,0);

        } else if (menuFile.startPositionIsFront  && menuFile.teamIsRed){                   /** Front RED  */
            gromit.driveTrain.mecanumDrive(0.25, menuFile.FrontRedDistance1, menuFile.FrontRedHeading1,0);
            gromit.driveTrain.mecanumTurn(0.25,menuFile.FrontRedTurn1);
            gromit.driveTrain.mecanumDrive(0.25, menuFile.FrontRedDistance2, menuFile.FrontRedHeading2,0);

        } else if (!menuFile.startPositionIsFront  && !menuFile.teamIsRed){                 /** Back Blue  */
            gromit.driveTrain.mecanumDrive(0.25, menuFile.BackBlueDistance1, menuFile.BackBlueHeading1,0);
//            gromit.driveTrain.mecanumDrive(0.5, menuFile.BackBlueDistance2, menuFile.BackBlueHeading2,90);
            gromit.driveTrain.mecanumDrive(0.25, menuFile.BackBlueDistance3, menuFile.BackBlueHeading3,0);

        } else if (!menuFile.startPositionIsFront  && menuFile.teamIsRed) {                 /** Back RED  */
            gromit.driveTrain.mecanumDrive(0.25, menuFile.BackRedDistance1, menuFile.BackRedHeading1,0);
 //           gromit.driveTrain.mecanumDrive(0.5, menuFile.BackRedDistance2, menuFile.BackRedHeading2,-90);
            gromit.driveTrain.mecanumDrive(0.25, menuFile.BackRedDistance3, menuFile.BackRedHeading3,0);}

            sleep(2000);

        gromit.driveTrain.mecanumDrive(0.5,-10,0,0);
        gromit.driveTrain.mecanumTurn(0.5,0.0);
//        sleep(2000);
//            // drive forward some Left Center Right
//            //  vuMark
//            if (vuMark == RelicRecoveryVuMark.LEFT) {
//               //Turn Right Away
//                //drive more
//                sleep(200);
//                gromit.driveTrain.mecanumTurn(0.5,90);
//
//            } else if (vuMark == RelicRecoveryVuMark.CENTER) {
//                //drive more
//                gromit.driveTrain.mecanumDrive(0.5,-10,0,0);
//                sleep(2000);
//                gromit.driveTrain.mecanumTurn(0.5,90);
//
//            } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
//                //drive more
//                gromit.driveTrain.mecanumDrive(0.5,-20,0,0);
//                sleep(200);
//                gromit.driveTrain.mecanumTurn(0.5,90);
//            }


//        gromit.driveTrain.mecanumDrive(.5,20, 0, -20);
        //gromit.driveTrain.mecanumDrive(.5,20, 0, 0);
        //gromit.driveTrain.mecanumTurn(1,45);
        /*gromit.driveTrain.mecanumDrive(0.5, 20, 0,0);
        gromit.driveTrain.mecanumDrive(0.85, -10, 0,-90);
        sleep(800);
        gromit.driveTrain.mecanumDrive(0.85, 10, 0, -90);
        gromit.driveTrain.mecanumDrive(0.5, -20, 0,0);
        gromit.driveTrain.mecanumDrive(0.5, 10, 0,-45);*/
        gromit.driveTrain.stopMotors();
        while (opModeIsActive()) {
            telemetry.addData("", "Heading: %4.2f ", gromit.driveTrain.getheading());
            telemetry.update();
        }

    }
}
    

