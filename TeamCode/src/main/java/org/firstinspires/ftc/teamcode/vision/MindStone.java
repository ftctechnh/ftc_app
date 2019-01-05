package org.firstinspires.ftc.teamcode.vision;

import android.app.Activity;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.List;
import java.util.Locale;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.*;

//test for Slack integration

@Autonomous(name="Vision", group="Linear Opmode")
//@Disabled
public class MindStone extends LinearOpMode{
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;
    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;
    private String GoldMineralPosition;

    //Driving Motors
    private DcMotor lf = null; //"lf" stands for left front and so on.
    private DcMotor rf = null;
    private DcMotor lb = null;
    private DcMotor rb = null;
    private DcMotor elevator = null;

    //Servos
    private Servo flipper = null;
    @Override
    public void runOpMode() throws InterruptedException {
        //initialize required driving motors
        lf = hardwareMap.get(DcMotor.class, "lf");
        rf = hardwareMap.get(DcMotor.class, "rf");
        lb = hardwareMap.get(DcMotor.class, "lb");
        rb = hardwareMap.get(DcMotor.class, "rb");
        elevator = hardwareMap.get(DcMotor.class, "elevator");
        flipper = hardwareMap.get(Servo.class, "flipper");
        // get a reference to the color sensor.
//        sensorColor = hardwareMap.get(ColorSensor.class, "SCD");
//
//        // get a reference to the distance sensor that shares the same name.
//        sensorDistance = hardwareMap.get(DistanceSensor.class, "SCD");


        //declare final variables here
        //motor variables
        final int FULL_POWER_MOTOR = 1;
        final int STOP_POWER_MOTOR = 0;

        //Sensors
        float hsvValues[] = {0F, 0F, 0F};
        final float values[] = hsvValues;
        final double SCALE_FACTOR = 255;
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);


        boolean object = false;
        //true = block

        //Motor Direction settings
        lf.setDirection(DcMotor.Direction.REVERSE);
        rf.setDirection(DcMotor.Direction.FORWARD);
        lb.setDirection(DcMotor.Direction.REVERSE);
        rb.setDirection(DcMotor.Direction.FORWARD);
        elevator.setDirection(DcMotor.Direction.REVERSE);

        //we wil try to use encoders
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector())
            initTfod();

///////////////////////////////////////////////////////////////////////////////////////////////////

        waitForStart();

        DetermineGoldPosition(); //Determines the position of the gold mineral;
        telemetry.addLine(GoldMineralPosition);
        telemetry.update();
        if(GoldMineralPosition.equals("Left")) {
            Elevate(1, 5600);
            DriveForward(0.3, 250);
            StrafeLeft(0.5, 1796);
            DriveForward(1, -300);
            DriveForward(1, 300);

//        DriveForward(-0.5, -3700);
            TurnRight(0.5, -1300);
            DriveForward(1, 1794);
            flipper.setPosition(0.7);
            sleep(2500);
            flipper.setPosition(0);
            Elevate(-1, -5500);
        } else if(GoldMineralPosition.equals("Right")) {
            Elevate(1, 5600);
            DriveForward(0.3, 250);
            StrafeLeft(0.5, 1096);
            DriveForward(0.5, 1350);
            StrafeLeft(0.5, 700);
            DriveForward(1, -300);
        } else {
            Elevate(1, 5600);
            DriveForward(0.3, 250);
            StrafeLeft(0.5, 1096);
            DriveForward(0.5, 1350);
            StrafeLeft(0.5, 700);
            DriveForward(1, -300);

//        DriveForward(-0.5, -3700);
            TurnRight(0.5, -1500);
            DriveForward(1, 1794);
            flipper.setPosition(0.7);
            sleep(2500);
            flipper.setPosition(0);
            Elevate(-1, -5500);
        }
    }
    public void DetermineGoldPosition()
    {
        if (opModeIsActive()) {
            /** Activate Tensor Flow Object Detection. */
            if (tfod != null) {
                tfod.activate();
            }
            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() == 3) {
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
                                    GoldMineralPosition = "Left";
                                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                    telemetry.addData("Gold Mineral Position", "Right");
                                    GoldMineralPosition = "Right";
                                } else {
                                    telemetry.addData("Gold Mineral Position", "Center");
                                    GoldMineralPosition = "Center";

                                }
                            }
                        }
                        telemetry.update();
                    }
                }
                break;
            }
        }
        if (tfod != null) {
            tfod.shutdown();
        }
    }
    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = "AWPT3W3/////AAABmffHHV90h0b1mbesyQfiLYuNouqYpf009Cr+0RzKlUrUKFoPRlzDUM06oa3GnTWF21mVWZo49apb4/3cN47YhebKyKiXfVCYgTSAQtyx+S2cWzYAbcw5vck8DDkg/lj7GBVpr1qH/B7IwNW5L6Bn8zZQZEz/LMRQa4AGsVGlcV0zScfkqyzqIbCmqjON1JlYoSnktcEs6yXwYC1NnvIsEvp0Wpm+Hl+juuDJTtAIobfiJy1Q++LdDHt2CrnhT2dcfrmulTXNWAXdT6O9oXZGDz5B3o44sgAoBYdvH9yibcoxio6OwbV3d4OT+2921K/XWv8W/omZg1dFtTqsE+Zpkqh4N6Bz/cd1NqtYtiRDJQPe";
        parameters.cameraDirection = CameraDirection.BACK;
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
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }


    public void DriveForward(double power, int distance)//Drive Forward
    {
        //resets encoder values
        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //sets Target position
        lf.setTargetPosition(distance);
        lb.setTargetPosition(distance);
        rf.setTargetPosition(distance);
        rb.setTargetPosition(distance);

        //sets to runs to position
        lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //runs
        lf.setPower(power);
        lb.setPower(power);
        rf.setPower(power);
        rb.setPower(power);
        while (lf.isBusy()&&lb.isBusy()&&rf.isBusy()&&rb.isBusy()){                 //RED FLAG CHECK THIS THING!!!!
            //waits for all motors to stop
        }
        DF(0);  //sets power to 0

        //resets mode
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void DF(double power){           //Method with no end, DO NOT USE UNLESS NECESSARY
        lf.setPower(power);
        lb.setPower(power);
        rf.setPower(power);
        rb.setPower(power);
    }
    public void Elevate(double power, int distance){
        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setTargetPosition(distance);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevator.setPower(power);
        while (elevator.isBusy()){

        }
        elevator.setPower(0);

    }
    public void TurnRight(double power, int distance)//Drive Forward
    {

        //resets encoder values
        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //sets Target position
        lf.setTargetPosition(distance);
        lb.setTargetPosition(distance);
        rf.setTargetPosition(-distance);
        rb.setTargetPosition(-distance);

        //sets to runs to position
        lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //runs
        lf.setPower(power);
        lb.setPower(power);
        rf.setPower(-power);
        rb.setPower(-power);
        while (lf.isBusy()&&lb.isBusy()&&rf.isBusy()&&rb.isBusy()){
            //waits for all motors to stop
        }
        DF(0);  //sets power to 0 to stop robot

        //resets mode
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        //resets encoder values
//        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//        lf.setPower(-power);
//        lb.setPower(-power);
//        rf.setPower(power);
//        rb.setPower(power);
    }
    public void TurnLeft(double power)//Drive Forward
    {
        lf.setPower(power);
        lb.setPower(power);
        rf.setPower(-power);
        rb.setPower(-power);
    }
    public void StrafeRight(double power, int distance)//Drive Forward
    {
        //resets encoder values
        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //sets Target position
        lf.setTargetPosition(distance);
        lb.setTargetPosition(-distance);
        rf.setTargetPosition(-distance);
        rb.setTargetPosition(distance);

        //sets to runs to position
        lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //runs
        lf.setPower(power);
        lb.setPower(-power);
        rf.setPower(-power);
        rb.setPower(power);
        while (lf.isBusy()&&lb.isBusy()&&rf.isBusy()&&rb.isBusy()){
            //waits for all motors to stop
        }
        DF(0);  //sets power to 0 to stop robot

        //resets mode
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void StrafeLeft(double power, int distance)//Drive Forward
    {
        //resets encoder values
        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //sets Target position
        lf.setTargetPosition(-distance);
        lb.setTargetPosition(distance);
        rf.setTargetPosition(distance);
        rb.setTargetPosition(-distance);

        //sets to runs to position
        lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //runs
        lf.setPower(-power);
        lb.setPower(power);
        rf.setPower(power);
        rb.setPower(-power);
        while (lf.isBusy()&&lb.isBusy()&&rf.isBusy()&&rb.isBusy()){
            //waits for all motors to stop
        }
        DF(0);  //sets power to 0 to stop robot

        //resets mode
        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
//    public void Sensation(boolean object , double SCALE_FACTOR, float hsvValues[]){
//        telemetry.addData("Distance (cm)",
//                String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.CM)));
//        Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
//                (int) (sensorColor.green() * SCALE_FACTOR),
//                (int) (sensorColor.blue() * SCALE_FACTOR),
//                hsvValues);
//        if (hsvValues[0]>100)
//            object = false;
//        telemetry.addData("Mineral", "Silver");
//        if (hsvValues[0]<100)
//            object = true;
//        telemetry.addData("Mineral", "Gold");
//
//    }
//    public void Alldir(double power, double degrees, int distance){       //Do not worry about this
//        //resets encoder values
//        lf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        lb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rf.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        rb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//        //sets Target position
//        lf.setTargetPosition(distance);
//        lb.setTargetPosition(distance);
//        rf.setTargetPosition(distance);
//        rb.setTargetPosition(distance);
//
//        //sets to runs to position
//        lf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        lb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        rf.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        rb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        //runs
//        lf.setPower(power);
//        lb.setPower(power);
//        rf.setPower(power);
//        rb.setPower(power);
//        while (lf.isBusy()&&lb.isBusy()&&rf.isBusy()&&rb.isBusy()){
//            //waits for all motors to stop
//        }
//        DF(0);  //sets power to 0
//
//        //resets mode
//        lf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        lb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rf.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        rb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//    }
}
