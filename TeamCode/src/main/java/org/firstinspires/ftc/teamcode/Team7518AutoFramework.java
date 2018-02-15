package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "7518AutoFramework", group = "7518")
@Disabled
public class Team7518AutoFramework extends LinearOpMode{

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
        boolean speed = true;




    }//end runOpMode

    public void driveForward (double speed , int ticks){
        if(speed>=0&&speed<=1){
            //leftFront
            leftFront.setPower(speed);
            leftFront.setTargetPosition(ticks);
            //rightFront
            rightFront.setPower(speed);
            rightFront.setTargetPosition(-ticks);
            //leftRear
            leftRear.setPower(speed);
            leftRear.setTargetPosition(ticks);
            //rightRear
            rightRear.setPower(speed);
            rightRear.setTargetPosition(-ticks);
        }//end if
    }//end driveFoward


}//end class
