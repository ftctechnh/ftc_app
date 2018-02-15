package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "DrivetrainTest", group = "7518")
public class DrivetrainTest extends LinearOpMode{

    private DcMotor  leftFront, rightFront, leftRear, rightRear, rightLift, leftLift, rightIntake, leftIntake;
    private Servo rightFlip, leftFlip, colorSensorServo;

    @Override
    public void runOpMode() throws InterruptedException
    {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Declare hardwareMap here, example below
        leftFront = hardwareMap.dcMotor.get("leftFront");
        rightFront = hardwareMap.dcMotor.get("rightFront");
        leftRear = hardwareMap.dcMotor.get("leftRear");
        rightRear = hardwareMap.dcMotor.get("rightRear");
        rightLift = hardwareMap.dcMotor.get("rightLift");
        leftLift = hardwareMap.dcMotor.get("leftLift");
        rightIntake = hardwareMap.dcMotor.get("rightIntake");
        leftIntake = hardwareMap.dcMotor.get("leftIntake");

        rightFlip = hardwareMap.servo.get("rightFlip");
        leftFlip = hardwareMap.servo.get("leftFlip");
        colorSensorServo = hardwareMap.servo.get("colorSensorServo");

        waitForStart();

        //Declare Variables Here


        //Set Motors to STOP_AND_RESET_ENCODER and RUN_TO_POSITION
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        if (opModeIsActive())
            driveForward(.5,20);





    }//end runOpMode

    public void driveForward (double speed , double inches){
        int leftFrontPos = 0, rightFrontPos = 0, leftRearPos = 0, rightRearPos = 0;
        double revolution = 1680;
        double circ = 4*Math.PI;
        double ticks = (revolution/circ)*inches;
        while (opModeIsActive()&& Math.abs(leftFrontPos)<ticks && Math.abs(rightFrontPos)<ticks && Math.abs(leftRearPos)<ticks && Math.abs(rightRearPos)<ticks){
            //leftFront
            leftFront.setPower(speed);
            leftFront.setTargetPosition((int)-ticks);
            //rightFront
            rightFront.setPower(speed);
            rightFront.setTargetPosition((int)ticks);
            //leftRear
            leftRear.setPower(speed);
            leftRear.setTargetPosition((int)-ticks);
            //rightRear
            rightRear.setPower(speed);
            rightRear.setTargetPosition((int)ticks);

            leftFrontPos = leftFront.getCurrentPosition();
            rightFrontPos = rightFront.getCurrentPosition();
            leftRearPos = leftRear.getCurrentPosition();
            rightRearPos = rightRear.getCurrentPosition();

            telemetry.addData("ticks:\t", ticks);
            telemetry.update();
        }

        //reset encoders
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }//end driveForward



    public void rotateLeft (double speed){
        int leftFrontPos = 0, rightFrontPos = 0, leftRearPos = 0, rightRearPos = 0;
        double revolution = 1680;
        double circ = 4*Math.PI;
        double ticks = (revolution/circ)*15;
        while (opModeIsActive()&& Math.abs(leftFrontPos)<ticks && Math.abs(rightFrontPos)<ticks && Math.abs(leftRearPos)<ticks && Math.abs(rightRearPos)<ticks){
            //leftFront
            leftFront.setPower(speed);
            leftFront.setTargetPosition((int)ticks);
            //rightFront
            rightFront.setPower(speed);
            rightFront.setTargetPosition((int)ticks);
            //leftRear
            leftRear.setPower(speed);
            leftRear.setTargetPosition((int)ticks);
            //rightRear
            rightRear.setPower(speed);
            rightRear.setTargetPosition((int)ticks);

            leftFrontPos = leftFront.getCurrentPosition();
            rightFrontPos = rightFront.getCurrentPosition();
            leftRearPos = leftRear.getCurrentPosition();
            rightRearPos = rightRear.getCurrentPosition();

            telemetry.addData("ticks:\t", ticks);
            telemetry.update();
        }//end while

        //reset encoders
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }//end rotateLeft

    public void rotateRight (double speed){
        int leftFrontPos = 0, rightFrontPos = 0, leftRearPos = 0, rightRearPos = 0;
        double revolution = 1680;
        double circ = 4*Math.PI;
        double ticks = (revolution/circ)*15;
        while (opModeIsActive()&& Math.abs(leftFrontPos)<ticks && Math.abs(rightFrontPos)<ticks && Math.abs(leftRearPos)<ticks && Math.abs(rightRearPos)<ticks){
            //leftFront
            leftFront.setPower(speed);
            leftFront.setTargetPosition((int)-ticks);
            //rightFront
            rightFront.setPower(speed);
            rightFront.setTargetPosition((int)-ticks);
            //leftRear
            leftRear.setPower(speed);
            leftRear.setTargetPosition((int)-ticks);
            //rightRear
            rightRear.setPower(speed);
            rightRear.setTargetPosition((int)-ticks);

            leftFrontPos = leftFront.getCurrentPosition();
            rightFrontPos = rightFront.getCurrentPosition();
            leftRearPos = leftRear.getCurrentPosition();
            rightRearPos = rightRear.getCurrentPosition();

            telemetry.addData("ticks:\t", ticks);
            telemetry.update();
        }//end while

        //reset encoders
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }//end rotateRight


}//end class
