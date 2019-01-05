package org.firstinspires.ftc.teamcode.vision;

import android.app.Activity;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

//test for Slack integration

@Autonomous(name="ICANCLEFT", group="Linear Opmode")
//@Disabled
public class ICANCLEFT extends LinearOpMode{
    MasterVision vision;
    SampleRandomizedPositions goldPosition;

    //Driving Motors
    private DcMotor lf = null; //"lf" stands for left front and so on.
    private DcMotor rf = null;
    private DcMotor lb = null;
    private DcMotor rb = null;
    private DcMotor elevator = null;

    @Override
    public void runOpMode() throws InterruptedException {
        //initialize required driving motors
        lf = hardwareMap.get(DcMotor.class, "lf");
        rf = hardwareMap.get(DcMotor.class, "rf");
        lb = hardwareMap.get(DcMotor.class, "lb");
        rb = hardwareMap.get(DcMotor.class, "rb");
        elevator = hardwareMap.get(DcMotor.class, "elevator");

        //initialize sensors
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

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;// recommended camera direction
        parameters.vuforiaLicenseKey = "AWPT3W3/////AAABmffHHV90h0b1mbesyQfiLYuNouqYpf009Cr+0RzKlUrUKFoPRlzDUM06oa3GnTWF21mVWZo49apb4/3cN47YhebKyKiXfVCYgTSAQtyx+S2cWzYAbcw5vck8DDkg/lj7GBVpr1qH/B7IwNW5L6Bn8zZQZEz/LMRQa4AGsVGlcV0zScfkqyzqIbCmqjON1JlYoSnktcEs6yXwYC1NnvIsEvp0Wpm+Hl+juuDJTtAIobfiJy1Q++LdDHt2CrnhT2dcfrmulTXNWAXdT6O9oXZGDz5B3o44sgAoBYdvH9yibcoxio6OwbV3d4OT+2921K/XWv8W/omZg1dFtTqsE+Zpkqh4N6Bz/cd1NqtYtiRDJQPe";

        vision = new MasterVision(parameters, hardwareMap, true, MasterVision.TFLiteAlgorithm.INFER_RIGHT);
        vision.init();// enables the camera overlay. this will take a couple of seconds
        vision.enable();// enables the tracking algorithms. this might also take a little time

        waitForStart();

        vision.disable();// disables tracking algorithms. this will free up your phone's processing power for other jobs.

        goldPosition = vision.getTfLite().getLastKnownSampleOrder();

//        while(opModeIsActive()){
            telemetry.addData("goldPosition was", goldPosition);// giving feedback

            switch (goldPosition){ // using for things in the autonomous program
                case LEFT:
                    telemetry.addLine("going to the left");
                    telemetry.update();
                    Elevate(1, 5600);
                    DriveForward(0.3, 250);
                    StrafeLeft(0.5, 896);
                    DriveForward(0.5, 1050);
                    StrafeLeft(0.5, 700);
                    DriveForward(1, -300);

//        DriveForward(-0.5, -3700);
                    TurnRight(0.5, -1500);
                    DriveForward(1, 1794);
                    Elevate(-1, -5500);
                    break;
                case CENTER:
                    telemetry.addLine("going straight");
                    telemetry.update();
                    Elevate(1, 5600);
                    DriveForward(0.3, 250);
                    StrafeLeft(0.5, 896);
                    DriveForward(1, -300);
                    DriveForward(1, 300);

//        DriveForward(-0.5, -3700);
                    TurnRight(0.5, -1300);
                    DriveForward(1, 1794);
                    Elevate(-1, -5500);
                    break;
                case RIGHT:
                    telemetry.addLine("going to the right");
                    telemetry.update();
                    Elevate(1, 5600);
                    DriveForward(0.3, 250);
                    StrafeLeft(0.5, 896);
                    DriveForward(0.5, -1050);
                    StrafeLeft(0.5, 700);
                    DriveForward(1, -300);

//        DriveForward(-0.5, -3700);
                    TurnRight(0.5, -1500);
                    DriveForward(1, 1794);
                    Elevate(-1, -5500);
                    break;
                case UNKNOWN:
                    telemetry.addLine("staying put");
                    telemetry.update();
                    Elevate(1, 5600);
                    DriveForward(0.3, 250);
                    StrafeLeft(0.5, 896);
                    TurnRight(0.5, -1300);
                    DriveForward(1, 300);

//        DriveForward(-0.5, -3700);

                    DriveForward(1, 1794);
                    Elevate(-1, -5500);
                    break;
            }

            telemetry.update();
//        }

        vision.shutdown();
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
