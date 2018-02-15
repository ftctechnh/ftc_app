package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ResetLift", group = "7518")
public class ResetLift extends LinearOpMode{

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


        while(opModeIsActive())
        {
            while(gamepad1.right_trigger>0){
                rightLift.setPower(.25);
            }
            while(gamepad1.right_bumper){
                rightLift.setPower(-.25);
            }
            while(gamepad1.left_trigger>0){
                leftLift.setPower(-.25);
            }
            while(gamepad1.left_bumper){
                leftLift.setPower(.25);
            }
            rightLift.setPower(0);
            leftLift.setPower(0);

        }//end while(opModeIsActive)


    }//end runOpMode

}//end class
