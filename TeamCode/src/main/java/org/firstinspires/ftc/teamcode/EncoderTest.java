package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "EncoderTest", group = "7518")
public class EncoderTest extends LinearOpMode{

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


        //Include any code to run only once here
        waitForStart();

        leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftLift.setPower(.25);
        leftLift.setTargetPosition(0);

        rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightLift.setPower(.25);
        rightLift.setTargetPosition(0);


        while(opModeIsActive())
        {




                        //up
                        if(gamepad1.a){
                            rightLift.setTargetPosition(-11000);
                            leftLift.setTargetPosition(-11000);
                        }//end if
                        //down
                        else if (gamepad1.b){
                            rightLift.setTargetPosition(0);
                            leftLift.setTargetPosition(0);
                        }//end else
                        telemetry.addData("rightLift:\t", rightLift.getCurrentPosition());
                        telemetry.addData("leftLift:\t", leftLift.getCurrentPosition());
                        telemetry.update();

        }//end while(opModeIsActive)


    }//end runOpMode

    public void setFlip(double position){
        rightFlip.setPosition(1-position);
        leftFlip.setPosition(position);
        sleep(250);
    }//end setFlip

    public void setLift(boolean up){
        if(up){
            rightLift.setTargetPosition(-11000);
            leftLift.setTargetPosition(-11000);
        }//end if
        else{
            rightLift.setTargetPosition(0);
            leftLift.setTargetPosition(0);
        }//end else
    }//end setLift


}//end class
