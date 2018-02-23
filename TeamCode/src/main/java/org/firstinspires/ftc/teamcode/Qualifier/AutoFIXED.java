package org.firstinspires.ftc.teamcode.Qualifier;

/**
 *
 */


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static org.firstinspires.ftc.teamcode.Qualifier.DriveTrain.drive_COEF;
import static org.firstinspires.ftc.teamcode.Qualifier.DriveTrain.drive_THRESHOLD;
import static org.firstinspires.ftc.teamcode.Qualifier.DriveTrain.turn_COEF;


@Autonomous(name = "TheAutoFIXED", group = "8045")  // @Autonomous(...) is the other common choice
//@Disabled
public class AutoFIXED extends LinearOpMode {

    RobotRR gromit;

    private ElapsedTime runtime = new ElapsedTime();
    private MenuFileHandler menuFile;
    public static final String TAG = "Vuforia VuMark Sample";
    public boolean redjewelisright;
    public boolean blueJewelIsLeft;
    public int screenColor = Color.BLACK;
    public int newScreenColor = Color.BLACK;


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

        gromit.driveTrain.left_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        gromit.driveTrain.left_rear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        gromit.driveTrain.right_front.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        gromit.driveTrain.right_rear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // get a reference to the RelativeLayout so we can change the background  for Edit mode
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);


        /**********************************************************************************************\
         |--------------------------------- Pre Init Loop ----------------------------------------------|
         \**********************************************************************************************/

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
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

// jewel image grabbing

        this.vuforia.setFrameQueueCapacity(3);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
        VuforiaLocalizer.CloseableFrame frame = null;

        telemetry.addLine("Back/Start Buttons revert to code values");
        telemetry.update();
        sleep(500);
        menuFile.initArrays();
        menuFile.initializeNTransferValues(true);       // initialize variables & values

        // update them from the file if the back button is not pressed

        if (gamepad1.back || gamepad1.start) {
            telemetry.addLine("Reverting to Initial Values");
            telemetry.update();
            sleep(500);
        } else {
            telemetry.addLine("Reading Data from File");
            menuFile.readDataFromTxtFile(hardwareMap.appContext);
            telemetry.addLine("Transferring Data to Variables");
            telemetry.update();
            menuFile.initializeNTransferValues(false);            // transfer the array to variables with useable names
            sleep(500);

        }

        //sleep(3000);
        //menuFile.editParameters();                            // edit parameters
        //menuFile.writeDataToTxtFile(hardwareMap.appContext);  // write the current parameters to the file for next time


        /**********************************************************************************************\
         |--------------------------------------Init Loop-----------------------------------------------|
         \**********************************************************************************************/
        //Activate Vuforia
        relicTrackables.activate();

        // move first clamp up out of the way  (should move the clamps, but open is out of 18!
        gromit.glyphTrain.glyphliftupper("top");

        //Run using encoders
        gromit.driveTrain.runUsingEncoders();

        // AutoTransitioner used before waitForStart()
        AutoTransitioner.transitionOnStop(this, "zMoo");   // get ready for teleop at the end of auto

        // Actual Init loop
        while (!opModeIsActive()) {
            telemetry.addData("IMU", "Heading: %4.2f ", gromit.driveTrain.getheading());
            telemetry.addData("Blue/Red Ratio", " %4.2f ", gromit.jewelArm.BRRatio);
            telemetry.addData("VuMark", "%s is visible", vuMark);
            double sharpIRVoltage = gromit.driveTrain.sharpIRSensor.getVoltage();
            double IRdistance = 18.7754 * Math.pow(sharpIRVoltage, -1.51);
//            telemetry.addData("Sharp IR V ", sharpIRVoltage);
            telemetry.addData("Sharp IR ", "cm %4.1f ", IRdistance);//            telemetry.addData("IR Distance", gromit.glyphTrain.)

            telemetry.addLine("************ READY TO RUN *************");
            telemetry.addLine("Press BACK or START button to enter EDIT mode");
            telemetry.addLine(" ");

//            relativeLayout.post(new Runnable() {
//                public void run() {
//                    relativeLayout.setBackgroundColor(Color.BLACK);
//                }
//            });

            //
            //
            if (gamepad1.start || gamepad1.back) {             // edit parameters  & write the new file
                // change the background color to yellow

                relativeLayout.post(new Runnable() {
                    public void run() {
                        relativeLayout.setBackgroundColor(Color.YELLOW);
                    }
                });


                menuFile.editParameters();
                menuFile.writeDataToTxtFile(hardwareMap.appContext);  // write the current parameters to the file for next time

                relativeLayout.post(new Runnable() {    // change back to black
                    public void run() {
                        relativeLayout.setBackgroundColor(Color.BLACK);
                    }
                });

            }

            // should look for vumark here

            // check jewel
            try {
                frame = this.vuforia.getFrameQueue().take();
                long numImages = frame.getNumImages();
                for (int i = 4; i < numImages; i++) {    // 4 should be the rgb format we want
                    final Image img = frame.getImage(i);
                    int fmt = img.getFormat();
                    if (fmt == PIXEL_FORMAT.RGB565) {
//                        telemetry.addData("time   ",time);
//                        telemetry.addData("frame rgb565 ",i);
                        Bitmap rgbImage = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.RGB_565);
                        rgbImage.copyPixelsFromBuffer(img.getPixels());
                        blueJewelIsLeft = gromit.jewelArm.blueIsOnLeft(rgbImage);
                        break;
                    }
//                telemetry.update();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if ((gromit.jewelArm.BRRatio > 1.5) || (gromit.jewelArm.BRRatio < 0.67)) {
                if (blueJewelIsLeft) {
                    newScreenColor = Color.BLUE;
                } else {
                    newScreenColor = Color.RED;
                }
            } else {
                newScreenColor = Color.BLACK;
            }

            if (newScreenColor != screenColor) {    // if color has changed update the screen
                relativeLayout.post(new Runnable() {
                    public void run() {
                        relativeLayout.setBackgroundColor(newScreenColor);
                    }
                });
                screenColor = newScreenColor;
            }

            if (gamepad1.right_bumper) {
                gromit.glyphTrain.glyphclamp("close");
            }
            if (gamepad1.right_trigger > 0.1) {
                gromit.glyphTrain.glyphclamp("open");
            }

            //Search for Vumark
            /**
             * See if any of the instances of {@link relicTemplate} are currently visible.
             * {@link RelicRecoveryVuMark} is an enum which can have the following values:
             * UNKNOWN, LEFT, CENTER, and RIGHT. When a VuMark is visible, something other than
             * UNKNOWN will be returned by {@link RelicRecoveryVuMark#from(VuforiaTrackable)}.
             */

//            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
//            while ( RelicRecoveryVuMark.from(relicTemplate) == RelicRecoveryVuMark.UNKNOWN && !gamepad1.start && opModeIsActive()) {//Add timeout
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
//            }


            menuFile.displayValues();
            telemetry.update();
            idle();

        }

        /**********************************************************************************************\
         |----------------------------------- Run the Autonomous --------------------------------------|
         \*********************************************************************************************/


        waitForStart();
        runtime.reset();
        //telemetry.clearAll();

        // should start a timer to timeout for vumark...

        //telemetry.addData("VuMark", "%s is where we're heading", vuMark);
        //telemetry.update();


        if (menuFile.mode == 3) {   //  test mode
//            mecanumTurn (menuFile.DriveSpeed,menuFile.RedFrontTurn1);
//            mecanumDrive();
            RobotLog.vv("[Gromit] IR", "Begin");

            while (runtime.milliseconds() < 3000) {
                gromit.driveTrain.left_rear.setPower(0.5);
                gromit.driveTrain.left_front.setPower(0.5);
                gromit.driveTrain.right_rear.setPower(0.5);
                gromit.driveTrain.right_front.setPower(0.5);
                double sharpIRVoltage = gromit.driveTrain.sharpIRSensor.getVoltage();
                double IRdistance = 18.7754 * Math.pow(sharpIRVoltage, -1.51);
                RobotLog.vv("[Gromit] IR", Double.toString(IRdistance));
//                Log.d("[Gromi2] IR",Double.toString(IRdistance));
                idle();
            }
            RobotLog.vv("[Gromit] IR", "End");
            gromit.driveTrain.stopMotors();

            while (opModeIsActive()) {
                telemetry.addLine("TEST MODE 3 ");
                double sharpIRVoltage = gromit.driveTrain.sharpIRSensor.getVoltage();
                double IRdistance = 18.7754 * Math.pow(sharpIRVoltage, -1.51);
//                mecanumTurn(menuFile.DriveSpeed, menuFile.RedFrontTurn1);
                telemetry.addData("", "Distance: %4.2f ", IRdistance);
                telemetry.addData("", "Heading: %4.2f ", gromit.driveTrain.getheading());
                telemetry.update();
            }
        }


        //VUMARK FOUND OR TIMEOUT
        //Grip Block
        gromit.glyphTrain.glyphclamp("close");
        sleep(100);
        //Raise Block
        gromit.glyphTrain.liftGlyph(4);
        // do the jewel should be a method in the jewelArm
//sleep(3000);
        gromit.jewelArm.jewelArmDown();
        // while(opModeIsActive() && gromit.jewelArm.jewelArmServo.getPosition() < 0.5) { idle();}
        sleep(900);
//        gromit.jewelArm.solveJewelPuzzle(menuFile.teamIsRed);
        if (menuFile.DoTheWrongJewel){      // knock the wrong one off
            gromit.jewelArm.solveJewelPuzzleCameraRP(menuFile.teamIsRed, blueJewelIsLeft);
        }else{                              // do the correct jewel
            gromit.jewelArm.solveJewelPuzzleCamera(menuFile.teamIsRed, blueJewelIsLeft);
        }
        sleep(750);
        gromit.jewelArm.jewelArmUp();

/**  ------------------------- Here are the vumark choices ----------------------------------------------------  */
        relicTrackables.deactivate();


        if (menuFile.teamIsRed && menuFile.startPositionIsFront) {                   /** RED Front  */
            double distance1 = menuFile.RedFrontDistance1Right;
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                distance1 = menuFile.RedFrontDistance1Center;
            } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                distance1 = menuFile.RedFrontDistance1Left;
            }
            mecanumDrive(menuFile.DriveSpeed * 0.8, distance1, menuFile.RedFrontHeading1, 0);
            mecanumTurn(menuFile.DriveSpeed, menuFile.RedFrontTurn1);
            gromit.glyphTrain.lowerGlyph(2);

            mecanumDrive(menuFile.DriveSpeed * 0.5, menuFile.RedFrontDistance2, menuFile.RedFrontHeading2, 0);
            gromit.glyphTrain.glyphclamp("open");
            sleep(400);
            gromit.glyphTrain.startGlyphMotors(0.8);
            if (menuFile.mode == 1 || menuFile.mode == 2) {
                //goGetAnotherGlyphRedFront(vuMark);
                goGetAnotherGlyphBlueFront(vuMark,menuFile.RedFrontHeading2 );
            }
        }
        else if (!menuFile.teamIsRed && menuFile.startPositionIsFront) {               /** BLUE Front  */
            double distance1 = menuFile.BlueFrontDistance1Right;
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                distance1 = menuFile.BlueFrontDistance1Center;
            } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                distance1 = menuFile.BlueFrontDistance1Left;
            }
            mecanumDrive(menuFile.DriveSpeed * 0.8, distance1, menuFile.BlueFrontHeading1, 0);
            mecanumTurn(menuFile.DriveSpeed, menuFile.BlueFrontTurn1);
            gromit.glyphTrain.lowerGlyph(2);
            //sleep(400);
            mecanumDrive(menuFile.DriveSpeed * 0.5, menuFile.BlueFrontDistance2, menuFile.BlueFrontHeading2, 0);
            gromit.glyphTrain.glyphclamp("open");
            sleep(400);
            gromit.glyphTrain.startGlyphMotors(0.8);
            if (menuFile.mode == 1 || menuFile.mode == 2) {
                goGetAnotherGlyphBlueFront(vuMark,menuFile.BlueFrontHeading2 );
            }


        } else if (menuFile.teamIsRed && !menuFile.startPositionIsFront) {                 /** RED  Back  */    //  back  4, 14, 26
            mecanumDrive(menuFile.DriveSpeed * 0.6, menuFile.RedBackDistance1, menuFile.RedBackHeading1, 0);  //0
            double distance2 = menuFile.RedBackDistance2Right;
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                distance2 = menuFile.RedBackDistance2Center;
            } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                distance2 = menuFile.RedBackDistance2Left;
            }
            mecanumDrive(menuFile.DriveSpeed, distance2, menuFile.RedBackHeading2, -90);  //-90
            gromit.glyphTrain.lowerGlyph(2);

            mecanumDrive(menuFile.DriveSpeed * 0.5, menuFile.RedBackDistance3, menuFile.RedBackHeading3, 0);  //0
            gromit.glyphTrain.glyphclamp("open");
            gromit.glyphTrain.startGlyphMotors(0.8);
            sleep(400);
            if (menuFile.mode == 1 || menuFile.mode == 2) {
//                goGetAnotherGlyphRedBack(vuMark, menuFile.RedBackHeading3, -30);
                goGetAnotherGlyphRedBack(vuMark, menuFile.RedBackHeading3, menuFile.RedBackHeading4);
            }
        } else if (!menuFile.teamIsRed && !menuFile.startPositionIsFront) {                 /** BLUE Back  */     //  back  4, 14, 26
            mecanumDrive(menuFile.DriveSpeed * 0.6, menuFile.BlueBackDistance1, menuFile.BlueBackHeading1, 0);  // drive off stone
            double distance2 = menuFile.BlueBackDistance2Left;
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                distance2 = menuFile.BlueBackDistance2Center;
            } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
                distance2 = menuFile.BlueBackDistance2Right;
            }
            mecanumDrive(menuFile.DriveSpeed * 0.6, distance2, menuFile.RedBackHeading2, -90);  //-90    strafe
            sleep(400);
            mecanumTurn(menuFile.DriveSpeed, menuFile.BlueBackTurn3);   // about face 180.
            sleep(400);
            gromit.glyphTrain.lowerGlyph(2);

            mecanumDrive(menuFile.DriveSpeed * 0.5, menuFile.BlueBackDistance3, menuFile.BlueBackHeading3, 0);  // 0

            gromit.glyphTrain.glyphclamp("open");
            gromit.glyphTrain.startGlyphMotors(0.8);
            if (menuFile.mode == 1 || menuFile.mode == 2) {
//                goGetAnotherGlyphBlueBack(vuMark, menuFile.BlueBackHeading3, -150);
               goGetAnotherGlyphBlueBack(vuMark, menuFile.BlueBackHeading3, menuFile.BlueBackHeading4);
            }
        }
        //ZERO THE REAR LIFT
        gromit.glyphTrain.glyphclamp("wide");   // OPEN BOTH SEROVS
        gromit.glyphTrain.glyphclampupper("open");
        gromit.glyphTrain.glyphliftupper("bottom");//Lower second Stage
        gromit.glyphTrain.liftGlyphIndex(0,0.3);  //lower
        //        gromit.glyphTrain.liftGlyph(0);


        gromit.driveTrain.stopMotors();
        while (opModeIsActive()) {
            telemetry.addData("", "Heading: %4.2f ", gromit.driveTrain.getheading());
            telemetry.update();
        }

    }
    public void goGetAnotherGlyphBlueFront(RelicRecoveryVuMark vuMark, double headingcrypto) {
//        double drivedistance = 54;
        double drivedistance = menuFile.RedBlueFrontDistance3;
//Re-align
        double strafe = 0;
        if (vuMark == RelicRecoveryVuMark.CENTER) {
            //Strafe right
            strafe = -3;//strafe right
        } else if (vuMark == RelicRecoveryVuMark.LEFT) {
            //Strafe Right
            strafe = -3;//strafe right
        } else {///THIS IS DEFAULT CASE OF VUMARK/Right
            strafe = 3;//left
        }
        //Push in first block
        mecanumDrive(menuFile.DriveSpeed * 0.6, 6, headingcrypto, 0);
        mecanumDrive(menuFile.DriveSpeed * 0.6, strafe, headingcrypto, -90);  //-90    strafe (strafe is never as long)
        mecanumDrive(menuFile.DriveSpeed * 0.6, -5, headingcrypto, 0);    // forward
        mecanumDrive(menuFile.DriveSpeed * 0.6, 4, headingcrypto, 0);    // back up

        mecanumTurn(menuFile.DriveSpeed, menuFile.BlueFrontHeading2);
        /**ONE WHEEL TURNS WOULD BE GOOD HERE **/
//ZERO LIFTS
        gromit.glyphTrain.liftGlyphIndex(0,0.3);  //lower
            gromit.glyphTrain.startGlyphMotors(0.8);
            mecanumDriveBlockClamp(menuFile.DriveSpeed * 0.6, drivedistance, headingcrypto, 0); //HEad to center
            mecanumTurn(menuFile.DriveSpeed, menuFile.BlueFrontHeading2);
            //drive back towards glyph box
            mecanumDriveBlockClamp(menuFile.DriveSpeed * 0.7, -(drivedistance-2), headingcrypto, 0); //move back
        sleep(200);
        if (menuFile.mode == 2) {//Align to a different box to do a row
            // COnditional for where you are
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                //Strafe right
                mecanumDriveBlockClamp(menuFile.DriveSpeed * 0.6, -7.5, headingcrypto, -90);   //strafe right 3//Move right (from our point of view)
            } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                //Strafe Left
                mecanumDriveBlockClamp(menuFile.DriveSpeed * 0.6, -7.5, headingcrypto, -90);  //strafe left 3 //Move left if you are on the certain edge
            } else {///THIS IS DEFAULT CASE OF VUMARK
                //Strafe Right
                mecanumDriveBlockClamp(menuFile.DriveSpeed * 0.6, 7.5, headingcrypto, -90);  //THe column to the left, Our and robot's roight
            }
//if you have two blocks, lift
            /*if(!gromit.glyphTrain.seeMiddleBlock.getState() || gromit.driveTrain.sharpIRSensor.getVoltage() < 1){

                //Spit out second block
                gromit.glyphTrain.startGlyphMotors(1.0);
                while((!gromit.glyphTrain.seeMiddleBlock.getState() || gromit.driveTrain.sharpIRSensor.getVoltage() < 1)&& opModeIsActive()){//While it sees a block MUST ADD A TIMEOUT HERE FOR THE JAMS
                    //Keep waiting
                    idle();
                }
                gromit.glyphTrain.stopGlyphMotors();
                //No clamps to close
                //gromit.glyphTrain.glyphclamp("close");
            }*/
            //You are in front of the Box
            //Drive into the box
            gromit.glyphTrain.startGlyphMotors(1.0);
            mecanumDrive(menuFile.DriveSpeed * 0.3, -6, headingcrypto, 0); //move 50

            gromit.glyphTrain.glyphclamp("wide");   // OPEN BOTH SEROVS
            gromit.glyphTrain.glyphclampupper("open");
            sleep(500);


        } else {//Put it on top
            mecanumDrive(menuFile.DriveSpeed * 0.6, 5, menuFile.BlueFrontHeading2, 90);  //-90    strafe back to where you are
            mecanumTurn(menuFile.DriveSpeed, menuFile.BlueFrontHeading2);
            gromit.glyphTrain.startGlyphMotors(0.7);
            mecanumDriveBlock(menuFile.DriveSpeed * 0.6, 54, menuFile.BlueFrontHeading2, 0); //HEad to center
            mecanumTurn(menuFile.DriveSpeed, menuFile.BlueFrontHeading2);
            //drive back towards glyph box
            mecanumDriveBlock(menuFile.DriveSpeed * 0.6, -48, menuFile.BlueFrontHeading2, 0); //move back
            gromit.glyphTrain.startGlyphMotors(0.7);
            sleep(700);
            gromit.glyphTrain.stopGlyphMotors();

            //Grip Block
            gromit.glyphTrain.glyphclamp("close");
            sleep(500);
            gromit.glyphTrain.liftGlyphIndex(1, 0.9);
            // Put the block into the box
            //WIGGLE?
            mecanumDrive(menuFile.DriveSpeed * 0.3, 10, menuFile.BlueFrontHeading2, 180); //move 50
            //unclamp glyph
            gromit.glyphTrain.glyphclamp("open");
            sleep(200);
        }
        //unclamp glyph
        gromit.glyphTrain.glyphclamp("wide");   // OPEN BOTH SEROVS
        gromit.glyphTrain.glyphclampupper("open");
        //Do the routine to push the block back into atleast one box //ROUTINE TO PUSH BLOCK INTO ATLEST INE COLUMN
        //drive forward
        mecanumDrive(menuFile.DriveSpeed * 0.3, 6, menuFile.BlueFrontHeading2, 0);    // back up
        gromit.glyphTrain.stopGlyphMotors();
        mecanumDrive(menuFile.DriveSpeed * 0.6, 5, menuFile.BlueFrontHeading2, -90);  //-90    strafe (strafe is never as long)
        mecanumDrive(menuFile.DriveSpeed * 0.6, -11, menuFile.BlueFrontHeading2, 0);    // into box
        mecanumDrive(menuFile.DriveSpeed * 0.6, 5, menuFile.BlueFrontHeading2, 0);    // don't touch the block
        gromit.glyphTrain.stopGlyphMotors();

    }
    public void goGetAnotherGlyphBlueBack(RelicRecoveryVuMark vuMark, double headingcrypto, double driveangle) {
        //push first block in different direcion based on vumark
        double strafe = 0;
        if (vuMark == RelicRecoveryVuMark.CENTER) {
            //Strafe right
            strafe = -3;//strafe right
        } else if (vuMark == RelicRecoveryVuMark.LEFT) {
            //Strafe Right
            strafe = -3;//strafe right
        } else {///THIS IS DEFAULT CASE OF VUMARK/LEFT
            strafe = 3;//left
        }

        //Push in first block
        mecanumDrive(menuFile.DriveSpeed * 0.6, 6, headingcrypto, 0);
        mecanumDrive(menuFile.DriveSpeed * 0.6, strafe, headingcrypto, -90);  //-90    strafe (strafe is never as long)
        mecanumDrive(menuFile.DriveSpeed * 0.6, -6, headingcrypto, 0);    // forward
        mecanumDrive(menuFile.DriveSpeed * 0.6, 4, headingcrypto, 0);    // back up




        //double driveangle = -150;
//Re-align
//        double drivedistance = 60;
        double drivedistance = menuFile.BlueBackDistance4;

        /**ONE WHEEL TURNS WOULD BE GOOD HERE **/

        if (menuFile.mode == 2) {//Align to a different box to do a row
            // COnditional for where you are
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                //Strafe right
                mecanumDriveBlock(menuFile.DriveSpeed * 0.8, 0, headingcrypto, -90);   //strafe right 3//Move right (from our point of view)
            } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                //Strafe Left
                telemetry.addLine("ITS LEFT");
                telemetry.update();
                mecanumDriveBlock(menuFile.DriveSpeed * 0.8, -8, headingcrypto, -90);  //strafe left 3 //Move left if you are on the certain edge
            } else {///THIS IS DEFAULT CASE OF VUMARK
                //Strafe Right
                mecanumDriveBlock(menuFile.DriveSpeed * 0.8, 3, headingcrypto, -90);  //THe column to the left, Our and robot's roight
            }
            mecanumTurn(menuFile.DriveSpeed, driveangle);
          //drive fast without glyphtrain

            mecanumDriveBlock(menuFile.DriveSpeed * 1.5, drivedistance/2, driveangle, 0); //HEad to center
            gromit.glyphTrain.startGlyphMotors(0.7);
            mecanumDriveBlock(menuFile.DriveSpeed * 0.6, drivedistance/2, driveangle, 0); //HEad to center
            mecanumTurn(menuFile.DriveSpeed, driveangle);
            //drive back towards glyph box
            mecanumDriveBlock(menuFile.DriveSpeed * 1.5, -(drivedistance-1),driveangle, 0); //move back
            //gromit.glyphTrain.glyphclamp("close");
            mecanumTurn(menuFile.DriveSpeed, headingcrypto);
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                //Strafe right
                mecanumDriveBlock(menuFile.DriveSpeed * 0.6, -12, headingcrypto, -90);  //strafe left 3 //Move left if you are on the certain edge
            } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                //Strafe Left
                //mecanumDriveBlock(menuFile.DriveSpeed * 0.6, -17, menuFile.BlueBackHeading3, -90);  //strafe left 3 //Move left if you are on the certain edge
            } else {///THIS IS DEFAULT CASE OF VUMARK
                //Strafe Right
                //mecanumDriveBlock(menuFile.DriveSpeed * 0.6, 3, menuFile.BlueBackHeading3, 90);  //THe column to the left, Our and robot's roight
            }
            gromit.glyphTrain.startGlyphMotors(0.7);

            //Drive to separate box
            mecanumDrive(menuFile.DriveSpeed * 0.3, -7, headingcrypto, 0); //move 50

            //unclamp glyph
            gromit.glyphTrain.glyphclamp("wide");   // OPEN BOTH SEROVS
            gromit.glyphTrain.glyphclampupper("open");


        }
        //Do the routine to push the block back into atleast one box //ROUTINE TO PUSH BLOCK INTO ATLEST INE COLUMN
        //drive forward
        mecanumDrive(menuFile.DriveSpeed * 0.3, 5, headingcrypto, 0);    // back up
        gromit.glyphTrain.stopGlyphMotors();
        mecanumDrive(menuFile.DriveSpeed * 0.6, 5, headingcrypto, -90);  //-90    strafe (strafe is never as long)
        mecanumDrive(menuFile.DriveSpeed * 0.6, -3, headingcrypto, 0);    // forward
        mecanumDrive(menuFile.DriveSpeed * 0.6, 5, headingcrypto, 0);    // back up

    }

    public void goGetAnotherGlyphRedBack(RelicRecoveryVuMark vuMark, double headingcrypto, double driveangle) {
        //push first block in different direcion based on vumark
        double strafe = 0;
        if (vuMark == RelicRecoveryVuMark.CENTER) {
            //Strafe right
            strafe = 3;//strafe right
        } else if (vuMark == RelicRecoveryVuMark.LEFT) {
            //Strafe Right
            strafe = -3;//strafe right
        } else {///THIS IS DEFAULT CASE OF VUMARK/LEFT
            strafe = 3;//left
        }

        //Push in first block
        mecanumDrive(menuFile.DriveSpeed * 0.6, 6, headingcrypto, 0);
        gromit.glyphTrain.stopGlyphMotors();
        mecanumDrive(menuFile.DriveSpeed * 0.6, strafe, headingcrypto, -90);  //-90    strafe (strafe is never as long)
        mecanumDrive(menuFile.DriveSpeed * 0.6, -6, headingcrypto, 0);    // forward
        mecanumDrive(menuFile.DriveSpeed * 0.6, 4, headingcrypto, 0);    // back up

        // now try to go get some more.
        //double driveangle = -150;
//Re-align
//        double drivedistance = 70;
        double drivedistance = menuFile.RedBackDistance4;
        /**ONE WHEEL TURNS WOULD BE GOOD HERE **/

        if (menuFile.mode == 2) {//Align to a different box to do a row
            // COnditional for where you are
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                //Strafe right
                mecanumDriveBlock(menuFile.DriveSpeed * 0.7, 9, headingcrypto, -90);   //strafe right 3//Move right (from our point of view)
            } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                //Strafe Left
                //telemetry.addLine("ITS LEFT");
                //telemetry.update();
                mecanumDriveBlock(menuFile.DriveSpeed * 0.7, -3, headingcrypto, -90);  //strafe left 3 //Move left if you are on the certain edge
            } else {///THIS IS DEFAULT CASE OF VUMARK
                //Strafe Right
                mecanumDriveBlock(menuFile.DriveSpeed * 0.7, 8, headingcrypto, -90);  //THe column to the left, Our and robot's roight
            }
            mecanumTurn(menuFile.DriveSpeed, driveangle);
            //drive fast without glyphtrain

            mecanumDriveBlockClamp(menuFile.DriveSpeed * 1.5, drivedistance/2, driveangle, 0); //HEad to center
            gromit.glyphTrain.startGlyphMotors(0.7);
            mecanumDriveBlockClamp(menuFile.DriveSpeed * 0.6, drivedistance/2, driveangle, 0); //HEad to center
            mecanumTurn(menuFile.DriveSpeed, driveangle);
            //drive back towards glyph box  try going back a bit shorter.
            mecanumDriveBlockClamp(menuFile.DriveSpeed * 1.5, -(drivedistance-1),driveangle, 0); //move back
            //gromit.glyphTrain.glyphclamp("close");
            mecanumTurn(menuFile.DriveSpeed, headingcrypto);
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                //Strafe right
                //mecanumDriveBlock(menuFile.DriveSpeed * 0.6, 11, headingcrypto, -90);  //strafe left 3 //Move left if you are on the certain edge
            } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                //Strafe Left
                mecanumDriveBlockClamp(menuFile.DriveSpeed * 0.6, -1,headingcrypto, -90);  //strafe left 3 //Move left if you are on the certain edge
            } else {///THIS IS DEFAULT CASE OF VUMARK
                //Strafe Right
                //mecanumDriveBlock(menuFile.DriveSpeed * 0.6, 3, menuFile.BlueBackHeading3, 90);  //THe column to the left, Our and robot's roight
            }
            mecanumDriveBlockClamp(menuFile.DriveSpeed * 0.3, -7, headingcrypto, 0); //move 50
            gromit.glyphTrain.startGlyphMotors(0.8);
            sleep(1000);
            gromit.glyphTrain.glyphclamp("open");


            //unclamp glyph
            gromit.glyphTrain.glyphclamp("wide");   // OPEN BOTH SEROVS
            gromit.glyphTrain.glyphclampupper("open");


        }
        //Do the routine to push the block back into atleast one box //ROUTINE TO PUSH BLOCK INTO ATLEST INE COLUMN
        //drive forward
        mecanumDrive(menuFile.DriveSpeed * 0.3, 5, headingcrypto, 0);    // back up
        gromit.glyphTrain.stopGlyphMotors();
        mecanumDrive(menuFile.DriveSpeed * 0.6, 5, headingcrypto, -90);  //-90    strafe (strafe is never as long)
        mecanumDrive(menuFile.DriveSpeed * 0.6, -3, headingcrypto, 0);    // forward
        mecanumDrive(menuFile.DriveSpeed * 0.6, 5, headingcrypto, 0);    // back up

    }
    public void mecanumDriveBlock(double speed, double distance, double robot_orientation, double drive_direction) { //Orientation is to the field //Drive direction is from the robot
        double max;
        double multiplier;
        int right_start;
        int left_start;
        int moveCounts;
        boolean glyphSensed = false;
        //int drive_direction = -90;
        moveCounts = (int) (distance * gromit.driveTrain.COUNTS_PER_INCH);
        right_start = gromit.driveTrain.right_rear.getCurrentPosition();
        left_start = gromit.driveTrain.left_rear.getCurrentPosition();
        double lfpower;
        double lrpower;
        double rfpower;
        double rrpower;

        double lfbase;
        double lrbase;
        double rfbase;
        double rrbase;
        lfbase = signum(distance) * Math.cos(Math.toRadians(drive_direction + 45));
        lrbase = signum(distance) * Math.sin(Math.toRadians(drive_direction + 45));
        rfbase = signum(distance) * Math.sin(Math.toRadians(drive_direction + 45));
        rrbase = signum(distance) * Math.cos(Math.toRadians(drive_direction + 45));
        while (((abs(gromit.driveTrain.right_rear.getCurrentPosition() - right_start) + abs(gromit.driveTrain.left_rear.getCurrentPosition() - left_start)) / 2 < abs(moveCounts)) && opModeIsActive() /* ENCODERS*/) {//Should we average all four motors?
            //Determine correction
            double correction = robot_orientation - gromit.driveTrain.getheading();
            if (correction <= -180) {
                correction += 360;
            } else if (correction >= 180) {                      // correction should be +/- 180 (to the left negative, right positive)
                correction -= 360;
            }
            lrpower = lrbase; //MIGHT BE MORE EFFECIENT TO COMBINE THESE WITHT HE ADJUSTMENT PART AND SET ADJUSTMENT TO ZERO IF NOT NEEDED
            lfpower = lfbase;
            rrpower = rrbase;
            rfpower = rfbase;
            if (abs(correction) > drive_THRESHOLD) {//If you are off
                //Apply power to one side of the robot to turn the robot back to the right heading
                double right_adjustment = Range.clip((drive_COEF * correction / 45), -1, 1);
                lrpower -= right_adjustment;
                lfpower -= right_adjustment;
                rrpower = rrbase + right_adjustment;
                rfpower = rfbase + right_adjustment;

            }//Otherwise you Are at the right orientation

            //Determine largest power being applied in either direction
            max = abs(lfpower);
            if (abs(lrpower) > max) max = abs(lrpower);
            if (abs(rfpower) > max) max = abs(rfpower);
            if (abs(rrpower) > max) max = abs(rrpower);

            multiplier = speed / max; //multiplier to adjust speeds of each wheel so you can have a max power of 1 on atleast 1 wheel

            lfpower *= multiplier;
            lrpower *= multiplier;
            rfpower *= multiplier;
            rrpower *= multiplier;

            gromit.driveTrain.left_front.setPower(lfpower);
            gromit.driveTrain.left_rear.setPower(lrpower);
            gromit.driveTrain.right_front.setPower(rfpower);
            gromit.driveTrain.right_rear.setPower(rrpower);

            if (gromit.driveTrain.sharpIRSensor.getVoltage() < 1 && !glyphSensed) {     // if block is sensed set boolean
                glyphSensed = true;
            } else if (glyphSensed && gromit.driveTrain.sharpIRSensor.getVoltage() > 1) {     // if block was already sensed (sense the back end)
                glyphSensed = false;
                gromit.glyphTrain.stopGlyphMotors();
                //gromit.glyphTrain.glyphclamp("close");
            }
        }
        gromit.driveTrain.stopMotors();
    }
    public void mecanumDriveBlockClamp(double speed, double distance, double robot_orientation, double drive_direction) { //Orientation is to the field //Drive direction is from the robot
        double max;
        double multiplier;
        int right_start;
        int left_start;
        int moveCounts;
        boolean glyphSensed = false;
        boolean twoblocks =false;
        //int drive_direction = -90;
        moveCounts = (int) (distance * gromit.driveTrain.COUNTS_PER_INCH);
        right_start = gromit.driveTrain.right_rear.getCurrentPosition();
        left_start = gromit.driveTrain.left_rear.getCurrentPosition();
        double lfpower;
        double lrpower;
        double rfpower;
        double rrpower;

        double lfbase;
        double lrbase;
        double rfbase;
        double rrbase;
        lfbase = signum(distance) * Math.cos(Math.toRadians(drive_direction + 45));
        lrbase = signum(distance) * Math.sin(Math.toRadians(drive_direction + 45));
        rfbase = signum(distance) * Math.sin(Math.toRadians(drive_direction + 45));
        rrbase = signum(distance) * Math.cos(Math.toRadians(drive_direction + 45));
        while (((abs(gromit.driveTrain.right_rear.getCurrentPosition() - right_start) + abs(gromit.driveTrain.left_rear.getCurrentPosition() - left_start)) / 2 < abs(moveCounts)) && opModeIsActive() /* ENCODERS*/) {//Should we average all four motors?
            //Determine correction
            double correction = robot_orientation - gromit.driveTrain.getheading();
            if (correction <= -180) {
                correction += 360;
            } else if (correction >= 180) {                      // correction should be +/- 180 (to the left negative, right positive)
                correction -= 360;
            }
            lrpower = lrbase; //MIGHT BE MORE EFFECIENT TO COMBINE THESE WITHT HE ADJUSTMENT PART AND SET ADJUSTMENT TO ZERO IF NOT NEEDED
            lfpower = lfbase;
            rrpower = rrbase;
            rfpower = rfbase;
            if (abs(correction) > drive_THRESHOLD) {//If you are off
                //Apply power to one side of the robot to turn the robot back to the right heading
                double right_adjustment = Range.clip((drive_COEF * correction / 45), -1, 1);
                lrpower -= right_adjustment;
                lfpower -= right_adjustment;
                rrpower = rrbase + right_adjustment;
                rfpower = rfbase + right_adjustment;

            }//Otherwise you Are at the right orientation

            //Determine largest power being applied in either direction
            max = abs(lfpower);
            if (abs(lrpower) > max) max = abs(lrpower);
            if (abs(rfpower) > max) max = abs(rfpower);
            if (abs(rrpower) > max) max = abs(rrpower);

            multiplier = speed / max; //multiplier to adjust speeds of each wheel so you can have a max power of 1 on atleast 1 wheel

            lfpower *= multiplier;
            lrpower *= multiplier;
            rfpower *= multiplier;
            rrpower *= multiplier;

            gromit.driveTrain.left_front.setPower(lfpower);
            gromit.driveTrain.left_rear.setPower(lrpower);
            gromit.driveTrain.right_front.setPower(rfpower);
            gromit.driveTrain.right_rear.setPower(rrpower);

            if (gromit.driveTrain.sharpIRSensor.getVoltage() < 1 && !glyphSensed) {     // if block is sensed set boolean
                glyphSensed = true;
                if(!gromit.glyphTrain.seeMiddleBlock.getState()){//TWO BLOCKS!!!!!
                    twoblocks = true;
                }
            }
            else if(glyphSensed){//Second Edge
                if(twoblocks){
                    if((gromit.glyphTrain.seeMiddleBlock.getState() || gromit.driveTrain.sharpIRSensor.getVoltage() > 1 ) ) {
                        glyphSensed = false;
                        gromit.glyphTrain.stopGlyphMotors();
                        gromit.glyphTrain.glyphclamp("close");
                        gromit.glyphTrain.liftGlyphIndex(1,0.9);  //lower
                    }
                }
                else if (gromit.driveTrain.sharpIRSensor.getVoltage() > 1){
                    glyphSensed = false;
                    gromit.glyphTrain.stopGlyphMotors();
                    gromit.glyphTrain.glyphclamp("close");
                }
            }
        }
        gromit.driveTrain.stopMotors();
    }
    public void mecanumTurn(double speed, double target_heading) {
        if (speed > 1) speed = 1.0;
        //else if(speed <= 0) speed = 0.1;

        double correction = target_heading - gromit.driveTrain.getheading();
        if (correction <= -180) {
            correction += 360;   // correction should be +/- 180 (to the left negative, right positive)
        } else if (correction >= 180){
            correction -= 360;
        }

        while (abs(correction) >= gromit.driveTrain.turn_THRESHOLD && opModeIsActive()) { //opmode active?{
            correction = target_heading - gromit.driveTrain.getheading();
            if (abs(correction) <= gromit.driveTrain.turn_THRESHOLD) break;

            if (correction <= -180)
                correction += 360;   // correction should be +/- 180 (to the left negative, right positive)
            if (correction >= 180) correction -= 360;
            /**^^^^^^^^^^^MAYBE WE ONLY NEED TO DO THIS ONCE?????*/

            double adjustment = Range.clip((Math.signum(correction) * gromit.driveTrain.turn_MIN_SPEED + gromit.driveTrain.turn_COEF * correction / 100), -1, 1);  // adjustment is motor power: sign of correction *0.07 (base power)  + a proportional bit

            gromit.driveTrain.left_front.setPower(-adjustment * speed);
            gromit.driveTrain.left_rear.setPower(-adjustment * speed);
            gromit.driveTrain.right_front.setPower((adjustment * speed));
            gromit.driveTrain.right_rear.setPower((adjustment * speed));
        }
        gromit.driveTrain.stopMotors();
    }


    public void mecanumDrive(double speed, double distance, double robot_orientation, double drive_direction) { //Orientation is to the field //Drive direction is from the robot
        double max;
        double multiplier;
        int right_start;
        int left_start;
        int moveCounts;
        //int drive_direction = -90;
        moveCounts = (int) (distance * gromit.driveTrain.COUNTS_PER_INCH);
        right_start = gromit.driveTrain.right_rear.getCurrentPosition();
        left_start = gromit.driveTrain.left_rear.getCurrentPosition();
        double lfpower;
        double lrpower;
        double rfpower;
        double rrpower;

        double lfbase;
        double lrbase;
        double rfbase;
        double rrbase;
        lfbase = signum(distance)*Math.cos(Math.toRadians(drive_direction + 45));
        lrbase = signum(distance)*Math.sin(Math.toRadians(drive_direction + 45));
        rfbase = signum(distance)*Math.sin(Math.toRadians(drive_direction + 45));
        rrbase = signum(distance)*Math.cos(Math.toRadians(drive_direction + 45));
        while (((abs(gromit.driveTrain.right_rear.getCurrentPosition() - right_start) + abs(gromit.driveTrain.left_rear.getCurrentPosition() - left_start)) / 2 < abs(moveCounts)) && opModeIsActive() /* ENCODERS*/) {//Should we average all four motors?
            //Determine correction
            double correction = robot_orientation - gromit.driveTrain.getheading();
            if (correction <= -180){
                correction += 360; }
            else if (correction >= 180) {                      // correction should be +/- 180 (to the left negative, right positive)
                correction -= 360;
            }
            lrpower = lrbase; //MIGHT BE MORE EFFECIENT TO COMBINE THESE WITHT HE ADJUSTMENT PART AND SET ADJUSTMENT TO ZERO IF NOT NEEDED
            lfpower = lfbase;
            rrpower = rrbase;
            rfpower = rfbase;
            if (abs(correction) > drive_THRESHOLD) {//If you are off
                //Apply power to one side of the robot to turn the robot back to the right heading
                double right_adjustment = Range.clip((drive_COEF * correction / 45), -1, 1);
                lrpower -= right_adjustment;
                lfpower -= right_adjustment;
                rrpower = rrbase + right_adjustment;
                rfpower = rfbase + right_adjustment;

            }//Otherwise you Are at the right orientation

            //Determine largest power being applied in either direction
            max = abs(lfpower);
            if (abs(lrpower) > max) max = abs(lrpower);
            if (abs(rfpower) > max) max = abs(rfpower);
            if (abs(rrpower) > max) max = abs(rrpower);

            multiplier = speed / max; //multiplier to adjust speeds of each wheel so you can have a max power of 1 on atleast 1 wheel

            lfpower *= multiplier;
            lrpower *= multiplier;
            rfpower *= multiplier;
            rrpower *= multiplier;

            gromit.driveTrain.left_front.setPower(lfpower);
            gromit.driveTrain.left_rear.setPower(lrpower);
            gromit.driveTrain.right_front.setPower(rfpower);
            gromit.driveTrain.right_rear.setPower(rrpower);

//            RobotLog.ii("[GromitIR] ", Double.toString(18.7754*Math.pow(sharpIRSensor.getVoltage(),-1.51)), Integer.toString(left_front.getCurrentPosition()));

        }
        gromit.driveTrain.stopMotors();
    }




}


