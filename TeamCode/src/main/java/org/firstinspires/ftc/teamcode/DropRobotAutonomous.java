package org.firstinspires.ftc.teamcode;


import android.widget.Space;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.ObjectTracker;
import com.vuforia.TrackerManager;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.FRONT;

//@Autonomous(name = "DropRobotAutonomous")
@TeleOp(name = "DropRobotTeleop")
public class DropRobotAutonomous extends LinearOpMode {


    private static final String VUFORIA_KEY = "AXx8B3P/////AAABmUvCh01qr0NYuiZsl4XR5wxHpJiI+AQBbtiTScffb3UpHwbjqT0gTnTtblgQyH6abrP5hIOA/Y4wgs8bU+1LwD/bor01NOM30m6KKqBS0hrGh0Z8IZu+1sNQyNzgm5dZNUKFI7UzEGUTlEL0L8r1v2++74NQkE8ZZFs6WyUjEowkDBpYQQE0ANXA5qDl0g2Rd7S3Y4rk9HgRJrJaZ0ojGT0uNzHdjkO7gpPYFsEDfAPVz7Pguzw7psyDlPvRmnKajnomWiCVEortJir77e1fgPSCnLobhrXL8b9PN3vaLu8ow0GxMbmJJN1ni0m+vzguiRaNy4JhDDgevKJuN4bv5CLqIt1EDMDG9ROrxcq3OJMR";
    private static final float mmPerInch        = 25.4f;
    private static final float mmFTCFieldWidth  = (12*6) * mmPerInch;       // the width of the FTC field (from the center point to the outer panels)
    private static final float mmTargetHeight   = (6) * mmPerInch;          // the height of the center of the target image above the floor
    // Valid choices are:  BACK or FRONT
    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    private static String Blue_Rover = "Blue-Rover";
    private static String Red_Footprint = "Red-Footprint";
    private static String Front_Craters = "Front-Craters";
    private static String Back_Space = "Back-Space";
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private final int CAMERA_FORWARD_DISPLACEMENT  = 110;   // eg: Camera is 110 mm in front of robot center
    private final int CAMERA_VERTICAL_DISPLACEMENT = 200;   // eg: Camera is 200 mm above ground
    private final int CAMERA_LEFT_DISPLACEMENT     = 0;     // eg: Camera is ON the robot's center line


    private AnimatornicsRobot robot = null;
    private VuforiaLocalizer vuforia = null;
    private TFObjectDetector tfod = null;
    private boolean targetVisible = false;
    private String targetName = null;
    private ElapsedTime runtime = new ElapsedTime();
    private boolean isAll3ObjDetected = false;
    private VuforiaTrackables targetsRoverRuckus = null;
    private List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();

    @Override
    public void runOpMode() throws InterruptedException {

        //robot = new AnimatornicsRobot(hardwareMap, telemetry);

        initVuforia();
        loadNavigationTragets();

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStart();

        /** Start tracking the data sets we care about. */
        targetsRoverRuckus.activate();
        targetVisible = false;
        lookForNavigationTarget(allTrackables, 5.0);
        targetsRoverRuckus.deactivate();
        Thread.sleep(1000);

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
            telemetry.update();
        }

        if (tfod != null) {
            tfod.activate();
            checkForGoldAndSilverMinerals(15.0);

            if (isAll3ObjDetected) {
                trackGoldMineral(15.0);
            }
        } else {
            telemetry.addData("Sorry!!!", "This device is not compatible with TFOD");
            telemetry.update();
        }

        if (tfod != null) {
            tfod.shutdown();
        }
        //robot.moveLift(this, 2.0, "Down", 0.7);
        //robot.moveLift(this, 2.0, "Down", 1.0);
        //robot.moveRobot(this, 3.0, "LateralRight", -1.0, 1.0, -1.0, 1.0);
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        //VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CAMERA_CHOICE;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        //TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters();
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    private void loadNavigationTragets() {
        // Load the data sets that for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        targetsRoverRuckus = this.vuforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blueRover = targetsRoverRuckus.get(0);
        blueRover.setName(Blue_Rover);
        VuforiaTrackable redFootprint = targetsRoverRuckus.get(1);
        redFootprint.setName(Red_Footprint);
        VuforiaTrackable frontCraters = targetsRoverRuckus.get(2);
        frontCraters.setName(Front_Craters);
        VuforiaTrackable backSpace = targetsRoverRuckus.get(3);
        backSpace.setName(Back_Space);

        allTrackables.addAll(targetsRoverRuckus);
        OpenGLMatrix blueRoverLocationOnField = OpenGLMatrix
                .translation(0, mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0));
        blueRover.setLocation(blueRoverLocationOnField);
        OpenGLMatrix redFootprintLocationOnField = OpenGLMatrix
                .translation(0, -mmFTCFieldWidth, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180));
        redFootprint.setLocation(redFootprintLocationOnField);
        OpenGLMatrix frontCratersLocationOnField = OpenGLMatrix
                .translation(-mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90));
        frontCraters.setLocation(frontCratersLocationOnField);
        OpenGLMatrix backSpaceLocationOnField = OpenGLMatrix
                .translation(mmFTCFieldWidth, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90));
        backSpace.setLocation(backSpaceLocationOnField);

        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES,
                        CAMERA_CHOICE == FRONT ? 90 : -90, 0, 0));

        /**  Let all the trackable listeners know where the phone is.  */
        for (VuforiaTrackable trackable : allTrackables)
        {
            ((VuforiaTrackableDefaultListener)trackable.getListener()).setPhoneInformation(phoneLocationOnRobot, CAMERA_CHOICE);
        }
    }

    private void lookForNavigationTarget(List<VuforiaTrackable> allTrackables, double time) {
        runtime.reset();
        while (opModeIsActive() && !targetVisible && runtime.seconds() < time) {
            for (VuforiaTrackable trackable : allTrackables) {
                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                    targetVisible = true;
                    targetName = trackable.getName();
                }
            }

            if(targetVisible) {
                telemetry.addData("Visible Target", targetName);
                // TODO: Stop robot
            } else {
                telemetry.addData("Visible Target", "none");
                // TODO: turn robot to left until you see any target
            }
            telemetry.update();
        }
    }

    private void checkForGoldAndSilverMinerals(double time) {
        runtime.reset();
        while(opModeIsActive() && !isAll3ObjDetected && runtime.seconds() < time) {
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() == 3) {
                    isAll3ObjDetected = true;
                    int goldMineralX = -1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getLeft();
                        } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                        } else {
                            silverMineral2X = (int) recognition.getLeft();
                        }
                    }
                    if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                        if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Left");
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Right");
                        } else {
                            telemetry.addData("Gold Mineral Position", "Center");
                        }
                    }
                }
            }

            if(isAll3ObjDetected) {
                // TODO: Stop Robot and try moving forward to hit Gold minaral
            } else {
                // TODO: turn robot to right until you see all 3 minerals
            }
            telemetry.update();
        }
    }

    private void trackGoldMineral(double time) {
        runtime.reset();
        boolean isDone = false;
        while(opModeIsActive() && !isDone && runtime.seconds() < time) {
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            boolean hasGoldMinaralInCamera = false;
            Recognition goldRecognition = null;
            for (Recognition recognition : updatedRecognitions) {
                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                    hasGoldMinaralInCamera = true;
                    goldRecognition = recognition;
                }
            }

            if (hasGoldMinaralInCamera && goldRecognition != null) {
                float goldMineralSizeInCamera = goldRecognition.getWidth();
                double angle =  goldRecognition.estimateAngleToObject(AngleUnit.DEGREES);
                telemetry.addData("Gold Mineral Position", "width:" + goldMineralSizeInCamera +
                        ", angle:" + angle + ", IWidth:" + goldRecognition.getImageWidth());

                if(angle > 1) {
                    //TODO: trun right slowly
                } else if (angle < -1) {
                    // TODO: turn left slowly
                } else {
                    if(goldMineralSizeInCamera < goldRecognition.getImageWidth()) {
                        // TODO: Move straight
                    } else {
                        // TODO: Stop?
                        isDone = true;
                    }
                }
            } else {
                telemetry.addData("Gold Mineral Position", "Lost it what to do now?");
                //TODO: Stop?
                isDone = true;
            }
            telemetry.update();
        }
    }
}
