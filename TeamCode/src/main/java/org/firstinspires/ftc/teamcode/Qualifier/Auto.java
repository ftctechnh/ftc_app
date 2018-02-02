package org.firstinspires.ftc.teamcode.Qualifier;

/**
 *
 */


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
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

import org.firstinspires.ftc.teamcode.Qualifier.AutoTransitioner;



@Autonomous(name = "TheAuto", group = "8045")  // @Autonomous(...) is the other common choice
//@Disabled
public class Auto extends LinearOpMode {

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
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565,true);
        VuforiaLocalizer.CloseableFrame frame = null;

        telemetry.addLine("Back/Start Buttons revert to code values");
        telemetry.update();
        sleep(500);
        menuFile.initArrays();
        menuFile.initializeNTransferValues( true );       // initialize variables & values

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

        //Run using encoders
        gromit.driveTrain.runUsingEncoders();
        AutoTransitioner.transitionOnStop(this, "zMoo");   // get ready for teleop at the end of auto

        // AutoTransitioner used before waitForStart()
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
            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
            if ( (gromit.jewelArm.BRRatio > 1.5)|| (gromit.jewelArm.BRRatio < 0.67) ) {
                if (blueJewelIsLeft) {
                    newScreenColor = Color.BLUE;
                } else {
                    newScreenColor = Color.RED;
                }
            }else{
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

        relicTrackables.deactivate();

        if(menuFile.mode == 3){   //  test mode
//            gromit.driveTrain.mecanumTurn (menuFile.DriveSpeed,menuFile.RedFrontTurn1);
//            gromit.driveTrain.mecanumDrive();
            RobotLog.vv("[Gromit] IR", "Begin" );

            while(runtime.milliseconds() <3000) {
                gromit.driveTrain.left_rear.setPower(0.5);
                gromit.driveTrain.left_front.setPower(0.5);
                gromit.driveTrain.right_rear.setPower(0.5);
                gromit.driveTrain.right_front.setPower(0.5);
                double sharpIRVoltage = gromit.driveTrain.sharpIRSensor.getVoltage();
                double IRdistance = 18.7754 * Math.pow(sharpIRVoltage, -1.51);
                RobotLog.vv("[Gromit] IR", Double.toString(IRdistance) );
//                Log.d("[Gromi2] IR",Double.toString(IRdistance));
                idle();
            }
            RobotLog.vv("[Gromit] IR", "End" );
            gromit.driveTrain.stopMotors();

            while (opModeIsActive()) {
                telemetry.addLine("TEST MODE 3 ");
                double sharpIRVoltage = gromit.driveTrain.sharpIRSensor.getVoltage();
                double IRdistance = 18.7754 * Math.pow(sharpIRVoltage, -1.51);
//                gromit.driveTrain.mecanumTurn(menuFile.DriveSpeed, menuFile.RedFrontTurn1);
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
        sleep(1000);
//        gromit.jewelArm.solveJewelPuzzle(menuFile.teamIsRed);
        gromit.jewelArm.solveJewelPuzzleCamera(menuFile.teamIsRed,blueJewelIsLeft);
        sleep(1000);
        gromit.jewelArm.jewelArmUp();

/**  ------------------------- Here are the vumark choices ----------------------------------------------------  */


        if ( menuFile.teamIsRed && menuFile.startPositionIsFront ){                   /** RED Front  */
            double distance1 = menuFile.RedFrontDistance1Right;
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                distance1 =  menuFile.RedFrontDistance1Center; ;
            } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                distance1 = menuFile.RedFrontDistance1Left ;
            }
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.8, distance1, menuFile.RedFrontHeading1,0);
            gromit.driveTrain.mecanumTurn (menuFile.DriveSpeed,menuFile.RedFrontTurn1);
            gromit.glyphTrain.lowerGlyph(2);

            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.5, menuFile.RedFrontDistance2, menuFile.RedFrontHeading2,0);
            gromit.glyphTrain.glyphclamp("open");
            sleep(400);
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.6, 5,  menuFile.RedFrontHeading2,0);

            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed*0.6, 5, menuFile.RedFrontHeading2, -90);  //-90    strafe (strafe is never as long)
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.6, -5,  menuFile.RedFrontHeading2,0);    // forward
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.6, 5,  menuFile.RedFrontHeading2,0);    // back up


        } else if (!menuFile.teamIsRed && menuFile.startPositionIsFront ){               /** BLUE Front  */
            double distance1 = menuFile.BlueFrontDistance1Right;
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                distance1 = menuFile.BlueFrontDistance1Center ;
            } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                distance1 = menuFile.BlueFrontDistance1Left ;
            }
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.8, distance1, menuFile.BlueFrontHeading1,0);
            gromit.driveTrain.mecanumTurn (menuFile.DriveSpeed,menuFile.BlueFrontTurn1);
            gromit.glyphTrain.lowerGlyph(2);
            sleep(400);
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed*0.5, menuFile.BlueFrontDistance2, menuFile.BlueFrontHeading2,0);
            gromit.glyphTrain.glyphclamp  ("open");
            sleep(400);
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.6, 5,  menuFile.BlueFrontHeading2,0);    // back up

            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed*0.6, 5, menuFile.BlueFrontHeading2, -90);  //-90    strafe (strafe is never as long)
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.6, -5,  menuFile.BlueFrontHeading2,0);    // forward
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.6, 5,  menuFile.BlueFrontHeading2,0);    // back up


        } else if (menuFile.teamIsRed && !menuFile.startPositionIsFront ) {                 /** RED  Back  */    //  back  4, 14, 26
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.6, menuFile.RedBackDistance1, menuFile.RedBackHeading1, 0);  //0
            double distance2 = menuFile.RedBackDistance2Right;
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                distance2 = menuFile.RedBackDistance2Center ;
            } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                distance2 = menuFile.RedBackDistance2Left ;
            }
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed, distance2, menuFile.RedBackHeading2, -90);  //-90
            gromit.glyphTrain.lowerGlyph(2);

            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed*0.5, menuFile.RedBackDistance3, menuFile.RedBackHeading3, 0);  //0
            gromit.glyphTrain.glyphclamp  ("open");
            sleep(400);
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.6, 5,  menuFile.RedBackHeading3,0);

            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed*0.6, 5, menuFile.RedBackHeading3, -90);  //-90    strafe (strafe is never as long)
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.6, -5,  menuFile.RedBackHeading3,0);    // forward
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.6, 5,  menuFile.RedBackHeading3,0);    // back up

        } else if ( !menuFile.teamIsRed && !menuFile.startPositionIsFront ){                 /** BLUE Back  */     //  back  4, 14, 26
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.8, menuFile.BlueBackDistance1,  menuFile.BlueBackHeading1,0);  // drive off stone
            double distance2 = menuFile.BlueBackDistance2Left;
            if (vuMark == RelicRecoveryVuMark.CENTER) {
                distance2 = menuFile.BlueBackDistance2Center   ;
            } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
                distance2 = menuFile.BlueBackDistance2Right ;
            }
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed*0.6, distance2, menuFile.RedBackHeading2, -90);  //-90    strafe
            sleep(400);
            gromit.driveTrain.mecanumTurn (menuFile.DriveSpeed,menuFile.BlueBackTurn3);   // about face 180.
            sleep(400);
            gromit.glyphTrain.lowerGlyph(2);

            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed*0.5, menuFile.BlueBackDistance3,  menuFile.BlueBackHeading3,0);  // 0

            gromit.glyphTrain.glyphclamp("open");
            sleep(400);
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.6, 5,  menuFile.BlueBackHeading3,0);

            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed*0.6, 5, menuFile.BlueBackHeading3, -90);  //-90    strafe (strafe is never as long)
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.6, -5,  menuFile.BlueBackHeading3,0);    // forward
            gromit.driveTrain.mecanumDrive(menuFile.DriveSpeed * 0.6, 5,  menuFile.BlueBackHeading3,0);    // back up

        }

        gromit.glyphTrain.liftGlyphIndex(0);  //lower
        //        gromit.glyphTrain.liftGlyph(0);


        gromit.driveTrain.stopMotors();
        while (opModeIsActive()) {
            telemetry.addData("", "Heading: %4.2f ", gromit.driveTrain.getheading());
            telemetry.update();
        }

    }

}


